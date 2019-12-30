// 
// Decompiled by Procyon v0.5.36
// 

package org.jglr.jchroma.effects;

import org.jglr.jchroma.utils.ColorRef;
import com.sun.jna.Structure;

public class CombinedCustomKeyboardEffect extends CustomKeyboardEffect
{
    private final CustomKeyboardEffect firstEffect;
    private final CustomKeyboardEffect secondEffect;
    
    public CombinedCustomKeyboardEffect(final CustomKeyboardEffect firstEffect, final CustomKeyboardEffect secondEffect) {
        this.firstEffect = firstEffect;
        this.secondEffect = secondEffect;
    }
    
    @Override
    public Structure createParameter() {
        for (int j = 0; j < 22; ++j) {
            for (int i = 0; i < 6; ++i) {
                final ColorRef firstColor = this.firstEffect.getColor(i, j);
                final ColorRef secondColor = this.secondEffect.getColor(i, j);
                ColorRef toSet = firstColor;
                if (firstColor.equals(ColorRef.NULL)) {
                    toSet = secondColor;
                }
                this.setColor(i, j, toSet);
            }
        }
        return super.createParameter();
    }
}
