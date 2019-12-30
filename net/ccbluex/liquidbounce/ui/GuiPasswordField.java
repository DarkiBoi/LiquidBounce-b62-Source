// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiPasswordField extends GuiTextField
{
    public GuiPasswordField(final int componentId, final FontRenderer fontrendererObj, final int x, final int y, final int par5Width, final int par6Height) {
        super(componentId, fontrendererObj, x, y, par5Width, par6Height);
    }
    
    public void func_146194_f() {
        final String text = this.func_146179_b();
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < text.length(); ++i) {
            stringBuilder.append('*');
        }
        this.func_146180_a(stringBuilder.toString());
        super.func_146194_f();
        this.func_146180_a(text);
    }
}
