// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.valuesystem.types;

import net.ccbluex.liquidbounce.valuesystem.Value;

public class FloatValue extends Value
{
    private float value;
    private final float min;
    private final float max;
    
    public FloatValue(final String name, final float value) {
        super(name);
        this.value = value;
        this.min = 0.0f;
        this.max = Float.MAX_VALUE;
    }
    
    public FloatValue(final String name, final float value, final float min, final float max) {
        super(name);
        this.value = value;
        this.min = min;
        this.max = max;
    }
    
    @Override
    public void setValueSilent(final Object value) {
        this.value = Float.parseFloat(String.valueOf(value));
    }
    
    @Override
    public Object asObject() {
        return this.value;
    }
    
    @Override
    public float asFloat() {
        return this.value;
    }
    
    @Override
    public double asDouble() {
        return this.value;
    }
    
    @Override
    public int asInteger() {
        return (int)this.value;
    }
    
    public float getMaximum() {
        return this.max;
    }
    
    public float getMinimum() {
        return this.min;
    }
}
