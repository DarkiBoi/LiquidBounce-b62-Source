// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils;

import net.minecraft.client.gui.GuiTextField;

public final class TabUtils
{
    public static void tab(final GuiTextField... textFields) {
        for (int i = 0; i < textFields.length; ++i) {
            final GuiTextField textField = textFields[i];
            if (textField.func_146206_l()) {
                textField.func_146195_b(false);
                if (++i >= textFields.length) {
                    i = 0;
                }
                textFields[i].func_146195_b(true);
                break;
            }
        }
    }
}
