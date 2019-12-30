// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.misc;

public final class MouseUtils
{
    public static int translateButton(final String name) {
        final String lowerCase = name.toLowerCase();
        switch (lowerCase) {
            case "left": {
                return 0;
            }
            case "right": {
                return 1;
            }
            case "middle": {
                return 2;
            }
            default: {
                return -1;
            }
        }
    }
}
