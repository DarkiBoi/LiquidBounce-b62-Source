// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.valuesystem.types;

import net.ccbluex.liquidbounce.valuesystem.Value;

public class ListValue extends Value
{
    private String value;
    private final String[] values;
    public boolean open;
    
    public ListValue(final String name, final String[] values, final String value) {
        super(name);
        this.values = values;
        this.value = value;
    }
    
    public String[] getValues() {
        return this.values;
    }
    
    public boolean contains(final String string) {
        for (final String s : this.values) {
            if (s.equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void setValueSilent(final Object value) {
        for (final String element : this.values) {
            if (element.equalsIgnoreCase(String.valueOf(value))) {
                this.value = element;
                break;
            }
        }
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
