// 
// Decompiled by Procyon v0.5.36
// 

package org.jglr.jchroma.effects;

import com.sun.jna.Structure;

public abstract class KeyboardEffect
{
    public abstract KeyboardEffectType getType();
    
    public abstract Structure createParameter();
}
