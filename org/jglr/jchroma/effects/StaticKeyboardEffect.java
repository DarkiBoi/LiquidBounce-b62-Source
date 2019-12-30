// 
// Decompiled by Procyon v0.5.36
// 

package org.jglr.jchroma.effects;

import java.util.Collections;
import java.util.List;
import com.sun.jna.Structure;
import org.jglr.jchroma.utils.ColorRef;

public class StaticKeyboardEffect extends KeyboardEffect
{
    private ColorRef color;
    
    public StaticKeyboardEffect(final ColorRef color) {
        this.color = color;
    }
    
    public ColorRef getColor() {
        return this.color;
    }
    
    public void setColor(final ColorRef color) {
        this.color = color;
    }
    
    @Override
    public KeyboardEffectType getType() {
        return KeyboardEffectType.STATIC;
    }
    
    @Override
    public Structure createParameter() {
        return new StaticEffectStructure(this.color.getValue());
    }
    
    public static class StaticEffectStructure extends Structure implements Structure.ByReference
    {
        public int color;
        
        public StaticEffectStructure(final int color) {
            this.color = color;
        }
        
        protected List getFieldOrder() {
            return Collections.singletonList("color");
        }
    }
}
