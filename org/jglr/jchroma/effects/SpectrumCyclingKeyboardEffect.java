// 
// Decompiled by Procyon v0.5.36
// 

package org.jglr.jchroma.effects;

import com.sun.jna.Structure;

public class SpectrumCyclingKeyboardEffect extends KeyboardEffect
{
    @Override
    public KeyboardEffectType getType() {
        return KeyboardEffectType.SPECTRUMCYCLING;
    }
    
    @Override
    public Structure createParameter() {
        return null;
    }
}
