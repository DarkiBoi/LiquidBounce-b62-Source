// 
// Decompiled by Procyon v0.5.36
// 

package org.jglr.jchroma.effects;

import java.util.Collections;
import java.util.List;
import com.sun.jna.Structure;

public class WaveKeyboardEffect extends KeyboardEffect
{
    private WaveDirection direction;
    
    public WaveKeyboardEffect(final WaveDirection direction) {
        this.direction = direction;
    }
    
    public WaveDirection getDirection() {
        return this.direction;
    }
    
    public void setDirection(final WaveDirection direction) {
        this.direction = direction;
    }
    
    @Override
    public KeyboardEffectType getType() {
        return KeyboardEffectType.WAVE;
    }
    
    @Override
    public Structure createParameter() {
        return new WaveEffectStructure(this.direction.ordinal());
    }
    
    public static class WaveEffectStructure extends Structure implements Structure.ByReference
    {
        public int direction;
        
        public WaveEffectStructure(final int direction) {
            this.direction = direction;
        }
        
        protected List getFieldOrder() {
            return Collections.singletonList("direction");
        }
    }
}
