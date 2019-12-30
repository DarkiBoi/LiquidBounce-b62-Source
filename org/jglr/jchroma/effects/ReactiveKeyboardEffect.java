// 
// Decompiled by Procyon v0.5.36
// 

package org.jglr.jchroma.effects;

import java.util.Arrays;
import java.util.List;
import com.sun.jna.Structure;
import org.jglr.jchroma.utils.ColorRef;

public class ReactiveKeyboardEffect extends KeyboardEffect
{
    private final EffectDuration duration;
    private final ColorRef color;
    
    public ReactiveKeyboardEffect(final EffectDuration duration, final ColorRef color) {
        this.duration = duration;
        this.color = color;
    }
    
    @Override
    public KeyboardEffectType getType() {
        return KeyboardEffectType.REACTIVE;
    }
    
    @Override
    public Structure createParameter() {
        final ReactiveStructure struct = new ReactiveStructure();
        struct.duration = this.duration.ordinal();
        struct.color = this.color.getValue();
        return struct;
    }
    
    public static class ReactiveStructure extends Structure implements Structure.ByReference
    {
        public int duration;
        public int color;
        
        protected List getFieldOrder() {
            return Arrays.asList("duration", "color");
        }
    }
}
