// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.altmanager.sub.altgenerator.mcleaks;

import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.mcleaks.MCLeaks;
import net.mcleaks.Session;
import net.mcleaks.RedeemResponse;
import net.ccbluex.liquidbounce.utils.render.ChatColor;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiMCLeaks extends GuiScreen
{
    private String message;
    private GuiTextField tokenField;
    private final GuiScreen prevGui;
    
    public GuiMCLeaks(final GuiScreen prevGui) {
        this.prevGui = prevGui;
    }
    
    public void func_73876_c() {
        this.tokenField.func_146178_a();
    }
    
    public void func_73866_w_() {
        Keyboard.enableRepeatEvents(true);
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 150, this.field_146295_m / 4 + 96, 300, 20, "Redeem Token"));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 150, this.field_146295_m / 4 + 120, 158, 20, "Get Token"));
        this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 + 12, this.field_146295_m / 4 + 120, 138, 20, "Back"));
        (this.tokenField = new GuiTextField(0, this.field_146289_q, this.field_146294_l / 2 - 100, 110, 200, 20)).func_146195_b(true);
        this.tokenField.func_146203_f(Integer.MAX_VALUE);
    }
    
    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
    }
    
    protected void func_146284_a(final GuiButton button) {
        if (!button.field_146124_l) {
            return;
        }
        switch (button.field_146127_k) {
            case 1: {
                if (this.tokenField.func_146179_b().length() != 16) {
                    this.message = ChatColor.RED + "The token has to be 16 characters long!";
                    return;
                }
                button.field_146124_l = false;
                button.field_146126_j = "Please wait ...";
                RedeemResponse redeemResponse;
                MCLeaks.redeem(this.tokenField.func_146179_b(), o -> {
                    if (o instanceof String) {
                        this.message = ChatColor.RED + (String)o;
                        return;
                    }
                    else {
                        redeemResponse = o;
                        MCLeaks.refresh(new Session(redeemResponse.getUsername(), redeemResponse.getToken()));
                        this.message = ChatColor.GREEN + "Your token was redeemed successfully!";
                        button.field_146124_l = true;
                        button.field_146126_j = "Redeem Token";
                        return;
                    }
                });
                break;
            }
            case 2: {
                MiscUtils.showURL("https://mcleaks.net/");
                break;
            }
            case 3: {
                this.field_146297_k.func_147108_a(this.prevGui);
                break;
            }
        }
    }
    
    protected void func_73869_a(final char typedChar, final int keyCode) {
        this.tokenField.func_146201_a(typedChar, keyCode);
        switch (keyCode) {
            case 15: {
                this.tokenField.func_146195_b(!this.tokenField.func_146206_l());
                break;
            }
            case 28: {
                this.func_146284_a(this.field_146292_n.get(1));
                break;
            }
            case 156: {
                this.func_146284_a(this.field_146292_n.get(1));
                break;
            }
        }
    }
    
    protected void func_73864_a(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.func_73864_a(mouseX, mouseY, mouseButton);
        this.tokenField.func_146192_a(mouseX, mouseY, mouseButton);
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146278_c(0);
        Gui.func_73734_a(30, 30, this.field_146294_l - 30, this.field_146295_m - 30, Integer.MIN_VALUE);
        this.func_73732_a(this.field_146297_k.field_71466_p, "MCLeaks", this.field_146294_l / 2, 34, 16777215);
        this.func_73732_a(this.field_146289_q, MCLeaks.isAltActive() ? ("§aToken active. Using §9" + MCLeaks.getSession().getUsername() + "§a to login!") : "§7No Token redeemed.", this.field_146294_l / 2, this.field_146295_m / 4 + ((this.message != null) ? 74 : 84), 16777215);
        this.func_73731_b(this.field_146289_q, "Token", this.field_146294_l / 2 - 100, 98, 10526880);
        if (this.message != null) {
            this.func_73732_a(this.field_146289_q, this.message, this.field_146294_l / 2, this.field_146295_m / 4 + 84, 16777215);
        }
        this.tokenField.func_146194_f();
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
}
