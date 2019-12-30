// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.altmanager.sub;

import net.ccbluex.liquidbounce.utils.TabUtils;
import java.io.IOException;
import net.ccbluex.liquidbounce.file.FileConfig;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.ccbluex.liquidbounce.utils.login.MinecraftAccount;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import net.ccbluex.liquidbounce.ui.GuiPasswordField;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiAdd extends GuiScreen
{
    private final GuiScreen prevGui;
    private GuiTextField username;
    private GuiPasswordField password;
    private GuiTextField up;
    private String status;
    
    public GuiAdd(final GuiScreen gui) {
        this.status = "§7Idle...";
        this.prevGui = gui;
    }
    
    public void func_73866_w_() {
        Keyboard.enableRepeatEvents(true);
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 96, "Add"));
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 120, "Back"));
        (this.username = new GuiTextField(2, (FontRenderer)Fonts.font40, this.field_146294_l / 2 - 100, 60, 200, 20)).func_146195_b(true);
        this.username.func_146203_f(Integer.MAX_VALUE);
        (this.password = new GuiPasswordField(3, Fonts.font40, this.field_146294_l / 2 - 100, 85, 200, 20)).func_146203_f(Integer.MAX_VALUE);
        (this.up = new GuiTextField(4, (FontRenderer)Fonts.font40, this.field_146294_l / 2 - 100, 110, 200, 20)).func_146203_f(Integer.MAX_VALUE);
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146278_c(0);
        Gui.func_73734_a(30, 30, this.field_146294_l - 30, this.field_146295_m - 30, Integer.MIN_VALUE);
        this.func_73732_a((FontRenderer)Fonts.font40, "Add Account", this.field_146294_l / 2, 34, 16777215);
        this.func_73732_a((FontRenderer)Fonts.font35, (this.status == null) ? "" : this.status, this.field_146294_l / 2, this.field_146295_m / 4 + 86, 16777215);
        this.username.func_146194_f();
        this.password.func_146194_f();
        this.up.func_146194_f();
        if (this.username.func_146179_b().isEmpty()) {
            this.func_73732_a(this.field_146289_q, "§7Username / E-Mail", this.field_146294_l / 2 - 50, 65, 16777215);
        }
        if (this.password.func_146179_b().isEmpty()) {
            this.func_73732_a(this.field_146289_q, "§7Password", this.field_146294_l / 2 - 70, 90, 16777215);
        }
        if (this.up.func_146179_b().isEmpty()) {
            this.func_73732_a(this.field_146289_q, "§7E-Mail:Password", this.field_146294_l / 2 - 55, 115, 16777215);
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    protected void func_146284_a(final GuiButton button) throws IOException {
        switch (button.field_146127_k) {
            case 0: {
                this.field_146297_k.func_147108_a(this.prevGui);
                break;
            }
            case 1: {
                if (this.username.func_146179_b().isEmpty() && this.up.func_146179_b().isEmpty()) {
                    this.status = "§cYou must fill out the username/email box.";
                    return;
                }
                if (LiquidBounce.CLIENT.fileManager.accountsConfig.altManagerMinecraftAccounts.stream().anyMatch(account -> account.getName().equalsIgnoreCase(this.username.func_146179_b()))) {
                    this.status = "§cThis minecraftAccount is already in the list.";
                    break;
                }
                MinecraftAccount minecraftAccount;
                if (!this.up.func_146179_b().isEmpty()) {
                    final String[] accountString = this.up.func_146179_b().split(":");
                    if (accountString.length < 2) {
                        this.status = "§cSyntax error";
                        return;
                    }
                    minecraftAccount = new MinecraftAccount(accountString[0], accountString[1]);
                }
                else if (this.password.func_146179_b().isEmpty()) {
                    minecraftAccount = new MinecraftAccount(this.username.func_146179_b());
                }
                else {
                    minecraftAccount = new MinecraftAccount(this.username.func_146179_b(), this.password.func_146179_b());
                }
                boolean isWorking;
                if (!minecraftAccount.isCracked()) {
                    final YggdrasilUserAuthentication userAuthentication = (YggdrasilUserAuthentication)new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
                    userAuthentication.setUsername(minecraftAccount.getName());
                    userAuthentication.setPassword(minecraftAccount.getPassword());
                    try {
                        userAuthentication.logIn();
                        minecraftAccount.setAccountName(userAuthentication.getSelectedProfile().getName());
                        isWorking = true;
                    }
                    catch (NullPointerException | AuthenticationException ex2) {
                        final Exception ex;
                        final Exception exception = ex;
                        isWorking = false;
                    }
                }
                else {
                    isWorking = true;
                }
                if (isWorking) {
                    LiquidBounce.CLIENT.fileManager.accountsConfig.altManagerMinecraftAccounts.add(minecraftAccount);
                    LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.accountsConfig);
                    this.status = "§aAccount was added.";
                    this.field_146297_k.func_147108_a(this.prevGui);
                    break;
                }
                this.status = "§cAccount is not working.";
                break;
            }
        }
        super.func_146284_a(button);
    }
    
    protected void func_73869_a(final char typedChar, final int keyCode) throws IOException {
        if (1 == keyCode) {
            this.field_146297_k.func_147108_a(this.prevGui);
            return;
        }
        if (15 == keyCode) {
            TabUtils.tab(this.username, this.password, this.up);
        }
        if (this.username.func_146206_l()) {
            this.username.func_146201_a(typedChar, keyCode);
        }
        if (this.password.func_146206_l()) {
            this.password.func_146201_a(typedChar, keyCode);
        }
        if (this.up.func_146206_l()) {
            this.up.func_146201_a(typedChar, keyCode);
        }
        super.func_73869_a(typedChar, keyCode);
    }
    
    protected void func_73864_a(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.username.func_146192_a(mouseX, mouseY, mouseButton);
        this.password.func_146192_a(mouseX, mouseY, mouseButton);
        this.up.func_146192_a(mouseX, mouseY, mouseButton);
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }
    
    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
        super.func_146281_b();
    }
}
