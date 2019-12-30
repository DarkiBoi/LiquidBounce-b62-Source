// 
// Decompiled by Procyon v0.5.36
// 

package org.jglr.jchroma.effects;

import java.util.Arrays;
import java.util.List;
import com.sun.jna.Structure;
import org.jglr.jchroma.utils.ColorRef;

public class BreathingKeyboardEffect extends KeyboardEffect
{
    private BreathingType breathingType;
    private ColorRef firstColor;
    private ColorRef secondColor;
    
    public BreathingKeyboardEffect(final BreathingType breathingType, final ColorRef firstColor, final ColorRef secondColor) {
        this.breathingType = breathingType;
        this.firstColor = firstColor;
        this.secondColor = secondColor;
    }
    
    public BreathingType getBreathingType() {
        return this.breathingType;
    }
    
    public void setType(final BreathingType type) {
        this.breathingType = type;
    }
    
    public ColorRef getFirstColor() {
        return this.firstColor;
    }
    
    public void setFirstColor(final ColorRef firstColor) {
        this.firstColor = firstColor;
    }
    
    public ColorRef getSecondColor() {
        return this.secondColor;
    }
    
    public void setSecondColor(final ColorRef secondColor) {
        this.secondColor = secondColor;
    }
    
    @Override
    public KeyboardEffectType getType() {
        return KeyboardEffectType.BREATHING;
    }
    
    @Override
    public Structure createParameter() {
        final BreathingStructure struct = new BreathingStructure();
        struct.color1 = this.firstColor.getValue();
        struct.color2 = this.firstColor.getValue();
        struct.type = this.breathingType.ordinal() + 1;
        return struct;
    }
    
    public static class BreathingStructure extends Structure implements Structure.ByReference
    {
        public int color1;
        public int color2;
        public int type;
        
        protected List getFieldOrder() {
            return Arrays.asList("type", "color1", "color2");
        }
    }
}
