// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

public abstract class DefaultPropertyAccess implements PropertyAccess
{
    @Override
    public int getInt(final Object key, final int programPoint) {
        return JSType.toInt32(this.get(key));
    }
    
    @Override
    public int getInt(final double key, final int programPoint) {
        return this.getInt(JSType.toObject(key), programPoint);
    }
    
    @Override
    public int getInt(final int key, final int programPoint) {
        return this.getInt(JSType.toObject(key), programPoint);
    }
    
    @Override
    public double getDouble(final Object key, final int programPoint) {
        return JSType.toNumber(this.get(key));
    }
    
    @Override
    public double getDouble(final double key, final int programPoint) {
        return this.getDouble(JSType.toObject(key), programPoint);
    }
    
    @Override
    public double getDouble(final int key, final int programPoint) {
        return this.getDouble(JSType.toObject(key), programPoint);
    }
    
    @Override
    public abstract Object get(final Object p0);
    
    @Override
    public Object get(final double key) {
        return this.get(JSType.toObject(key));
    }
    
    @Override
    public Object get(final int key) {
        return this.get(JSType.toObject(key));
    }
    
    @Override
    public void set(final double key, final int value, final int flags) {
        this.set(JSType.toObject(key), JSType.toObject(value), flags);
    }
    
    @Override
    public void set(final double key, final double value, final int flags) {
        this.set(JSType.toObject(key), JSType.toObject(value), flags);
    }
    
    @Override
    public void set(final double key, final Object value, final int flags) {
        this.set(JSType.toObject(key), JSType.toObject(value), flags);
    }
    
    @Override
    public void set(final int key, final int value, final int flags) {
        this.set(JSType.toObject(key), JSType.toObject(value), flags);
    }
    
    @Override
    public void set(final int key, final double value, final int flags) {
        this.set(JSType.toObject(key), JSType.toObject(value), flags);
    }
    
    @Override
    public void set(final int key, final Object value, final int flags) {
        this.set(JSType.toObject(key), value, flags);
    }
    
    @Override
    public void set(final Object key, final int value, final int flags) {
        this.set(key, JSType.toObject(value), flags);
    }
    
    @Override
    public void set(final Object key, final double value, final int flags) {
        this.set(key, JSType.toObject(value), flags);
    }
    
    @Override
    public abstract void set(final Object p0, final Object p1, final int p2);
    
    @Override
    public abstract boolean has(final Object p0);
    
    @Override
    public boolean has(final int key) {
        return this.has(JSType.toObject(key));
    }
    
    @Override
    public boolean has(final double key) {
        return this.has(JSType.toObject(key));
    }
    
    @Override
    public boolean hasOwnProperty(final int key) {
        return this.hasOwnProperty(JSType.toObject(key));
    }
    
    @Override
    public boolean hasOwnProperty(final double key) {
        return this.hasOwnProperty(JSType.toObject(key));
    }
    
    @Override
    public abstract boolean hasOwnProperty(final Object p0);
    
    @Override
    public boolean delete(final int key, final boolean strict) {
        return this.delete(JSType.toObject(key), strict);
    }
    
    @Override
    public boolean delete(final double key, final boolean strict) {
        return this.delete(JSType.toObject(key), strict);
    }
}
