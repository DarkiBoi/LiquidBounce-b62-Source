// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.valuesystem.types;

import net.ccbluex.liquidbounce.valuesystem.Value;

public class TextValue extends Value
{
    private String value;
    
    public TextValue(final String name, final String value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public void setValueSilent(final Object value) {
        this.value = String.valueOf(value);
    }
    
    @Override
    public Object asObject() {
        return this.value;
    }
    
    @Override
    public String asString() {
        return this.value;
    }
}
