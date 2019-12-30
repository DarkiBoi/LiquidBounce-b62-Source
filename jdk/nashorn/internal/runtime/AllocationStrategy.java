// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.lang.ref.WeakReference;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.lookup.Lookup;
import java.util.Collection;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.codegen.ObjectClassGenerator;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.io.Serializable;

public final class AllocationStrategy implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static final MethodHandles.Lookup LOOKUP;
    private final int fieldCount;
    private final boolean dualFields;
    private transient String allocatorClassName;
    private transient MethodHandle allocator;
    private transient AllocatorMap lastMap;
    
    public AllocationStrategy(final int fieldCount, final boolean dualFields) {
        this.fieldCount = fieldCount;
        this.dualFields = dualFields;
    }
    
    private String getAllocatorClassName() {
        if (this.allocatorClassName == null) {
            this.allocatorClassName = Compiler.binaryName(ObjectClassGenerator.getClassName(this.fieldCount, this.dualFields)).intern();
        }
        return this.allocatorClassName;
    }
    
    synchronized PropertyMap getAllocatorMap(final ScriptObject prototype) {
        assert prototype != null;
        final PropertyMap protoMap = prototype.getMap();
        if (this.lastMap != null) {
            if (!this.lastMap.hasSharedProtoMap()) {
                if (this.lastMap.hasSamePrototype(prototype)) {
                    return this.lastMap.allocatorMap;
                }
                if (this.lastMap.hasSameProtoMap(protoMap) && this.lastMap.hasUnchangedProtoMap()) {
                    final PropertyMap allocatorMap = PropertyMap.newMap(null, this.getAllocatorClassName(), 0, this.fieldCount, 0);
                    final SharedPropertyMap sharedProtoMap = new SharedPropertyMap(protoMap);
                    allocatorMap.setSharedProtoMap(sharedProtoMap);
                    prototype.setMap(sharedProtoMap);
                    this.lastMap = new AllocatorMap(prototype, protoMap, allocatorMap);
                    return allocatorMap;
                }
            }
            if (this.lastMap.hasValidSharedProtoMap() && this.lastMap.hasSameProtoMap(protoMap)) {
                prototype.setMap(this.lastMap.getSharedProtoMap());
                return this.lastMap.allocatorMap;
            }
        }
        final PropertyMap allocatorMap = PropertyMap.newMap(null, this.getAllocatorClassName(), 0, this.fieldCount, 0);
        this.lastMap = new AllocatorMap(prototype, protoMap, allocatorMap);
        return allocatorMap;
    }
    
    ScriptObject allocate(final PropertyMap map) {
        try {
            if (this.allocator == null) {
                this.allocator = Lookup.MH.findStatic(AllocationStrategy.LOOKUP, Context.forStructureClass(this.getAllocatorClassName()), CompilerConstants.ALLOCATE.symbolName(), Lookup.MH.type(ScriptObject.class, PropertyMap.class));
            }
            return this.allocator.invokeExact(map);
        }
        catch (RuntimeException | Error ex) {
            final Throwable t2;
            final Throwable e = t2;
            throw e;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    @Override
    public String toString() {
        return "AllocationStrategy[fieldCount=" + this.fieldCount + "]";
    }
    
    static {
        LOOKUP = MethodHandles.lookup();
    }
    
    static class AllocatorMap
    {
        private final WeakReference<ScriptObject> prototype;
        private final WeakReference<PropertyMap> prototypeMap;
        private PropertyMap allocatorMap;
        
        AllocatorMap(final ScriptObject prototype, final PropertyMap protoMap, final PropertyMap allocMap) {
            this.prototype = new WeakReference<ScriptObject>(prototype);
            this.prototypeMap = new WeakReference<PropertyMap>(protoMap);
            this.allocatorMap = allocMap;
        }
        
        boolean hasSamePrototype(final ScriptObject proto) {
            return this.prototype.get() == proto;
        }
        
        boolean hasSameProtoMap(final PropertyMap protoMap) {
            return this.prototypeMap.get() == protoMap || this.allocatorMap.getSharedProtoMap() == protoMap;
        }
        
        boolean hasUnchangedProtoMap() {
            final ScriptObject proto = this.prototype.get();
            return proto != null && proto.getMap() == this.prototypeMap.get();
        }
        
        boolean hasSharedProtoMap() {
            return this.getSharedProtoMap() != null;
        }
        
        boolean hasValidSharedProtoMap() {
            return this.hasSharedProtoMap() && this.getSharedProtoMap().isValidSharedProtoMap();
        }
        
        PropertyMap getSharedProtoMap() {
            return this.allocatorMap.getSharedProtoMap();
        }
    }
}
