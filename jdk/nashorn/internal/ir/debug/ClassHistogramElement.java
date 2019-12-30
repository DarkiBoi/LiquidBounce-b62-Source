// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir.debug;

import java.util.Comparator;

public final class ClassHistogramElement
{
    public static final Comparator<ClassHistogramElement> COMPARE_INSTANCES;
    public static final Comparator<ClassHistogramElement> COMPARE_BYTES;
    public static final Comparator<ClassHistogramElement> COMPARE_CLASSNAMES;
    private final Class<?> clazz;
    private long instances;
    private long bytes;
    
    public ClassHistogramElement(final Class<?> clazz) {
        this.clazz = clazz;
    }
    
    public void addInstance(final long sizeInBytes) {
        ++this.instances;
        this.bytes += sizeInBytes;
    }
    
    public long getBytes() {
        return this.bytes;
    }
    
    public Class<?> getClazz() {
        return this.clazz;
    }
    
    public long getInstances() {
        return this.instances;
    }
    
    @Override
    public String toString() {
        return "ClassHistogramElement[class=" + this.clazz.getCanonicalName() + ", instances=" + this.instances + ", bytes=" + this.bytes + "]";
    }
    
    static {
        COMPARE_INSTANCES = new Comparator<ClassHistogramElement>() {
            @Override
            public int compare(final ClassHistogramElement o1, final ClassHistogramElement o2) {
                return (int)Math.abs(o1.instances - o2.instances);
            }
        };
        COMPARE_BYTES = new Comparator<ClassHistogramElement>() {
            @Override
            public int compare(final ClassHistogramElement o1, final ClassHistogramElement o2) {
                return (int)Math.abs(o1.bytes - o2.bytes);
            }
        };
        COMPARE_CLASSNAMES = new Comparator<ClassHistogramElement>() {
            @Override
            public int compare(final ClassHistogramElement o1, final ClassHistogramElement o2) {
                return o1.clazz.getCanonicalName().compareTo(o2.clazz.getCanonicalName());
            }
        };
    }
}
