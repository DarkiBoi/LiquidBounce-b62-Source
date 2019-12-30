// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir.debug;

import java.util.Arrays;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ArrayDeque;
import java.util.IdentityHashMap;
import java.lang.reflect.Method;
import java.util.Deque;
import java.util.Map;

public final class ObjectSizeCalculator
{
    private final int arrayHeaderSize;
    private final int objectHeaderSize;
    private final int objectPadding;
    private final int referenceSize;
    private final int superclassFieldPadding;
    private final Map<Class<?>, ClassSizeInfo> classSizeInfos;
    private final Map<Object, Object> alreadyVisited;
    private final Map<Class<?>, ClassHistogramElement> histogram;
    private final Deque<Object> pending;
    private long size;
    static Class<?> managementFactory;
    static Class<?> memoryPoolMXBean;
    static Class<?> memoryUsage;
    static Method getMemoryPoolMXBeans;
    static Method getUsage;
    static Method getMax;
    
    public static long getObjectSize(final Object obj) throws UnsupportedOperationException {
        return (obj == null) ? 0L : new ObjectSizeCalculator(CurrentLayout.SPEC).calculateObjectSize(obj);
    }
    
    public ObjectSizeCalculator(final MemoryLayoutSpecification memoryLayoutSpecification) {
        this.classSizeInfos = new IdentityHashMap<Class<?>, ClassSizeInfo>();
        this.alreadyVisited = new IdentityHashMap<Object, Object>();
        this.histogram = new IdentityHashMap<Class<?>, ClassHistogramElement>();
        this.pending = new ArrayDeque<Object>(16384);
        Objects.requireNonNull(memoryLayoutSpecification);
        this.arrayHeaderSize = memoryLayoutSpecification.getArrayHeaderSize();
        this.objectHeaderSize = memoryLayoutSpecification.getObjectHeaderSize();
        this.objectPadding = memoryLayoutSpecification.getObjectPadding();
        this.referenceSize = memoryLayoutSpecification.getReferenceSize();
        this.superclassFieldPadding = memoryLayoutSpecification.getSuperclassFieldPadding();
    }
    
    public synchronized long calculateObjectSize(final Object obj) {
        this.histogram.clear();
        try {
            Object o = obj;
            while (true) {
                this.visit(o);
                if (this.pending.isEmpty()) {
                    break;
                }
                o = this.pending.removeFirst();
            }
            return this.size;
        }
        finally {
            this.alreadyVisited.clear();
            this.pending.clear();
            this.size = 0L;
        }
    }
    
    public List<ClassHistogramElement> getClassHistogram() {
        return new ArrayList<ClassHistogramElement>(this.histogram.values());
    }
    
    private ClassSizeInfo getClassSizeInfo(final Class<?> clazz) {
        ClassSizeInfo csi = this.classSizeInfos.get(clazz);
        if (csi == null) {
            csi = new ClassSizeInfo(clazz);
            this.classSizeInfos.put(clazz, csi);
        }
        return csi;
    }
    
    private void visit(final Object obj) {
        if (this.alreadyVisited.containsKey(obj)) {
            return;
        }
        final Class<?> clazz = obj.getClass();
        if (clazz == ArrayElementsVisitor.class) {
            ((ArrayElementsVisitor)obj).visit(this);
        }
        else {
            this.alreadyVisited.put(obj, obj);
            if (clazz.isArray()) {
                this.visitArray(obj);
            }
            else {
                this.getClassSizeInfo(clazz).visit(obj, this);
            }
        }
    }
    
    private void visitArray(final Object array) {
        final Class<?> arrayClass = array.getClass();
        final Class<?> componentType = arrayClass.getComponentType();
        final int length = Array.getLength(array);
        if (componentType.isPrimitive()) {
            this.increaseByArraySize(arrayClass, length, getPrimitiveFieldSize(componentType));
        }
        else {
            this.increaseByArraySize(arrayClass, length, this.referenceSize);
            switch (length) {
                case 0: {
                    break;
                }
                case 1: {
                    this.enqueue(Array.get(array, 0));
                    break;
                }
                default: {
                    this.enqueue(new ArrayElementsVisitor((Object[])array));
                    break;
                }
            }
        }
    }
    
    private void increaseByArraySize(final Class<?> clazz, final int length, final long elementSize) {
        this.increaseSize(clazz, roundTo(this.arrayHeaderSize + length * elementSize, this.objectPadding));
    }
    
    void enqueue(final Object obj) {
        if (obj != null) {
            this.pending.addLast(obj);
        }
    }
    
    void increaseSize(final Class<?> clazz, final long objectSize) {
        ClassHistogramElement he = this.histogram.get(clazz);
        if (he == null) {
            he = new ClassHistogramElement(clazz);
            this.histogram.put(clazz, he);
        }
        he.addInstance(objectSize);
        this.size += objectSize;
    }
    
    static long roundTo(final long x, final int multiple) {
        return (x + multiple - 1L) / multiple * multiple;
    }
    
    private static long getPrimitiveFieldSize(final Class<?> type) {
        if (type == Boolean.TYPE || type == Byte.TYPE) {
            return 1L;
        }
        if (type == Character.TYPE || type == Short.TYPE) {
            return 2L;
        }
        if (type == Integer.TYPE || type == Float.TYPE) {
            return 4L;
        }
        if (type == Long.TYPE || type == Double.TYPE) {
            return 8L;
        }
        throw new AssertionError((Object)("Encountered unexpected primitive type " + type.getName()));
    }
    
    public static MemoryLayoutSpecification getEffectiveMemoryLayoutSpecification() {
        final String vmName = System.getProperty("java.vm.name");
        if (vmName == null || !vmName.startsWith("Java HotSpot(TM) ")) {
            throw new UnsupportedOperationException("ObjectSizeCalculator only supported on HotSpot VM");
        }
        final String dataModel = System.getProperty("sun.arch.data.model");
        if ("32".equals(dataModel)) {
            return new MemoryLayoutSpecification() {
                @Override
                public int getArrayHeaderSize() {
                    return 12;
                }
                
                @Override
                public int getObjectHeaderSize() {
                    return 8;
                }
                
                @Override
                public int getObjectPadding() {
                    return 8;
                }
                
                @Override
                public int getReferenceSize() {
                    return 4;
                }
                
                @Override
                public int getSuperclassFieldPadding() {
                    return 4;
                }
            };
        }
        if (!"64".equals(dataModel)) {
            throw new UnsupportedOperationException("Unrecognized value '" + dataModel + "' of sun.arch.data.model system property");
        }
        final String strVmVersion = System.getProperty("java.vm.version");
        final int vmVersion = Integer.parseInt(strVmVersion.substring(0, strVmVersion.indexOf(46)));
        if (vmVersion >= 17) {
            long maxMemory = 0L;
            if (ObjectSizeCalculator.getMemoryPoolMXBeans == null) {
                throw new AssertionError((Object)"java.lang.management not available in compact 1");
            }
            try {
                final List<?> memoryPoolMXBeans = (List<?>)ObjectSizeCalculator.getMemoryPoolMXBeans.invoke(ObjectSizeCalculator.managementFactory, new Object[0]);
                for (final Object mp : memoryPoolMXBeans) {
                    final Object usage = ObjectSizeCalculator.getUsage.invoke(mp, new Object[0]);
                    final Object max = ObjectSizeCalculator.getMax.invoke(usage, new Object[0]);
                    maxMemory += (long)max;
                }
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex3) {
                final Exception ex2;
                final Exception ex = ex2;
                throw new AssertionError((Object)"java.lang.management not available in compact 1");
            }
            if (maxMemory < 32212254720L) {
                return new MemoryLayoutSpecification() {
                    @Override
                    public int getArrayHeaderSize() {
                        return 16;
                    }
                    
                    @Override
                    public int getObjectHeaderSize() {
                        return 12;
                    }
                    
                    @Override
                    public int getObjectPadding() {
                        return 8;
                    }
                    
                    @Override
                    public int getReferenceSize() {
                        return 4;
                    }
                    
                    @Override
                    public int getSuperclassFieldPadding() {
                        return 4;
                    }
                };
            }
        }
        return new MemoryLayoutSpecification() {
            @Override
            public int getArrayHeaderSize() {
                return 24;
            }
            
            @Override
            public int getObjectHeaderSize() {
                return 16;
            }
            
            @Override
            public int getObjectPadding() {
                return 8;
            }
            
            @Override
            public int getReferenceSize() {
                return 8;
            }
            
            @Override
            public int getSuperclassFieldPadding() {
                return 8;
            }
        };
    }
    
    static {
        ObjectSizeCalculator.managementFactory = null;
        ObjectSizeCalculator.memoryPoolMXBean = null;
        ObjectSizeCalculator.memoryUsage = null;
        ObjectSizeCalculator.getMemoryPoolMXBeans = null;
        ObjectSizeCalculator.getUsage = null;
        ObjectSizeCalculator.getMax = null;
        try {
            ObjectSizeCalculator.managementFactory = Class.forName("java.lang.management.ManagementFactory");
            ObjectSizeCalculator.memoryPoolMXBean = Class.forName("java.lang.management.MemoryPoolMXBean");
            ObjectSizeCalculator.memoryUsage = Class.forName("java.lang.management.MemoryUsage");
            ObjectSizeCalculator.getMemoryPoolMXBeans = ObjectSizeCalculator.managementFactory.getMethod("getMemoryPoolMXBeans", (Class<?>[])new Class[0]);
            ObjectSizeCalculator.getUsage = ObjectSizeCalculator.memoryPoolMXBean.getMethod("getUsage", (Class<?>[])new Class[0]);
            ObjectSizeCalculator.getMax = ObjectSizeCalculator.memoryUsage.getMethod("getMax", (Class<?>[])new Class[0]);
        }
        catch (ClassNotFoundException ex) {}
        catch (NoSuchMethodException ex2) {}
        catch (SecurityException ex3) {}
    }
    
    private static class CurrentLayout
    {
        private static final MemoryLayoutSpecification SPEC;
        
        static {
            SPEC = ObjectSizeCalculator.getEffectiveMemoryLayoutSpecification();
        }
    }
    
    private static class ArrayElementsVisitor
    {
        private final Object[] array;
        
        ArrayElementsVisitor(final Object[] array) {
            this.array = array;
        }
        
        public void visit(final ObjectSizeCalculator calc) {
            for (final Object elem : this.array) {
                if (elem != null) {
                    calc.visit(elem);
                }
            }
        }
    }
    
    private class ClassSizeInfo
    {
        private final long objectSize;
        private final long fieldsSize;
        private final Field[] referenceFields;
        
        public ClassSizeInfo(final Class<?> clazz) {
            long newFieldsSize = 0L;
            final List<Field> newReferenceFields = new LinkedList<Field>();
            for (final Field f : clazz.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    final Class<?> type = f.getType();
                    if (type.isPrimitive()) {
                        newFieldsSize += getPrimitiveFieldSize(type);
                    }
                    else {
                        f.setAccessible(true);
                        newReferenceFields.add(f);
                        newFieldsSize += ObjectSizeCalculator.this.referenceSize;
                    }
                }
            }
            final Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                final ClassSizeInfo superClassInfo = ObjectSizeCalculator.this.getClassSizeInfo(superClass);
                newFieldsSize += ObjectSizeCalculator.roundTo(superClassInfo.fieldsSize, ObjectSizeCalculator.this.superclassFieldPadding);
                newReferenceFields.addAll(Arrays.asList(superClassInfo.referenceFields));
            }
            this.fieldsSize = newFieldsSize;
            this.objectSize = ObjectSizeCalculator.roundTo(ObjectSizeCalculator.this.objectHeaderSize + newFieldsSize, ObjectSizeCalculator.this.objectPadding);
            this.referenceFields = newReferenceFields.toArray(new Field[newReferenceFields.size()]);
        }
        
        void visit(final Object obj, final ObjectSizeCalculator calc) {
            calc.increaseSize(obj.getClass(), this.objectSize);
            this.enqueueReferencedObjects(obj, calc);
        }
        
        public void enqueueReferencedObjects(final Object obj, final ObjectSizeCalculator calc) {
            for (final Field f : this.referenceFields) {
                try {
                    calc.enqueue(f.get(obj));
                }
                catch (IllegalAccessException e) {
                    final AssertionError ae = new AssertionError((Object)("Unexpected denial of access to " + f));
                    ae.initCause(e);
                    throw ae;
                }
            }
        }
    }
    
    public interface MemoryLayoutSpecification
    {
        int getArrayHeaderSize();
        
        int getObjectHeaderSize();
        
        int getObjectPadding();
        
        int getReferenceSize();
        
        int getSuperclassFieldPadding();
    }
}
