// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.valuesystem.types;

import net.ccbluex.liquidbounce.valuesystem.Value;

public class BoolValue extends Value
{
    private boolean value;
    
    public BoolValue(final String name, final boolean value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public void setValueSilent(final Object value) {
        this.value = String.valueOf(value).equalsIgnoreCase("true");
    }
    
    @Override
    public Object asObject() {
        return this.value;
    }
    
    @Override
    public boolean asBoolean() {
        return this.value;
    }
    
    @Override
    public int asInteger() {
        return this.value ? 1 : 0;
    }
    
    @Override
    public float asFloat() {
        return (float)this.asInteger();
    }
    
    @Override
    public double asDouble() {
        return this.asInteger();
    }
}
