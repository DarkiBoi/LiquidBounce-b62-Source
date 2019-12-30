// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.valuesystem.types;

import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.valuesystem.Value;

public class FontValue extends Value
{
    private FontRenderer fontRenderer;
    
    public FontValue(final String valueName, final FontRenderer fontRenderer) {
        super(valueName);
        this.fontRenderer = fontRenderer;
    }
    
    @Override
    public void setValueSilent(final Object value) {
        this.fontRenderer = (FontRenderer)value;
    }
    
    @Override
    public Object asObject() {
        return this.fontRenderer;
    }
}
