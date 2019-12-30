// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.mainmenu;

import java.awt.image.BufferedImage;
import java.io.File;
import net.ccbluex.liquidbounce.ui.GuiCredits;
import net.ccbluex.liquidbounce.ui.GuiModsMenu;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.io.FileOutputStream;
import java.awt.HeadlessException;
import javax.swing.JDialog;
import java.awt.Component;
import javax.swing.JFileChooser;
import net.ccbluex.liquidbounce.LiquidBounce;
import org.lwjgl.input.Keyboard;
import net.ccbluex.liquidbounce.ui.GuiServerStatus;
import net.ccbluex.liquidbounce.ui.altmanager.GuiAltManager;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.GuiIconButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.GuiScreen;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback
{
    public void func_73866_w_() {
        final int j = this.field_146295_m - 80;
        this.field_146292_n.add(new GuiIconButton(1, this.field_146294_l / 2 - 15 - 240, j + 24, 30, 30, I18n.func_135052_a("menu.singleplayer", new Object[0]), new ResourceLocation("LiquidBounce".toLowerCase() + "/singleplayer.png")));
        this.field_146292_n.add(new GuiIconButton(2, this.field_146294_l / 2 - 15 - 180, j + 24, 30, 30, I18n.func_135052_a("menu.multiplayer", new Object[0]), new ResourceLocation("LiquidBounce".toLowerCase() + "/multiplayer.png")));
        this.field_146292_n.add(new GuiIconButton(100, this.field_146294_l / 2 - 15 - 120, j + 24, 30, 30, "AltManager", new ResourceLocation("LiquidBounce".toLowerCase() + "/altmanager.png")));
        this.field_146292_n.add(new GuiIconButton(101, this.field_146294_l / 2 - 15 - 60, j + 24, 30, 30, "Server Status", new ResourceLocation("LiquidBounce".toLowerCase() + "/server_status.png")));
        this.field_146292_n.add(new GuiIconButton(103, this.field_146294_l / 2 - 15, j + 24, 30, 30, "Mods", new ResourceLocation("LiquidBounce".toLowerCase() + "/mods.png")));
        this.field_146292_n.add(new GuiIconButton(102, this.field_146294_l / 2 - 15 + 60, j + 24, 30, 30, "Change Wallpaper", new ResourceLocation("LiquidBounce".toLowerCase() + "/change_wallpaper.png")));
        this.field_146292_n.add(new GuiIconButton(108, this.field_146294_l / 2 - 15 + 120, j + 24, 30, 30, "Credits", new ResourceLocation("LiquidBounce".toLowerCase() + "/credits.png")));
        this.field_146292_n.add(new GuiIconButton(0, this.field_146294_l / 2 - 15 + 180, j + 24, 30, 30, I18n.func_135052_a("menu.options", new Object[0]), new ResourceLocation("LiquidBounce".toLowerCase() + "/options.png")));
        this.field_146292_n.add(new GuiIconButton(4, this.field_146294_l / 2 - 15 + 240, j + 24, 30, 30, I18n.func_135052_a("menu.quit", new Object[0]), new ResourceLocation("LiquidBounce".toLowerCase() + "/exit.png")));
        super.func_73866_w_();
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146278_c(0);
        Fonts.fontBold180.drawCenteredString("LiquidBounce", (int)(this.field_146294_l / 2.0f), (int)(this.field_146295_m / 4.0f + 50.0f), Color.WHITE.getRGB(), true);
        Fonts.font35.drawCenteredString("b" + String.valueOf(62), (int)(this.field_146294_l / 2.0f + 148.0f), (int)(this.field_146295_m / 4.0f + 50.0f + Fonts.font35.field_78288_b - 10.0f), 16777215, true);
        Gui.func_73734_a(0, this.field_146295_m - 70, this.field_146294_l, this.field_146295_m, new Color(30, 30, 30).getRGB());
        for (int i = 0; i < this.field_146294_l; ++i) {
            Gui.func_73734_a(i, this.field_146295_m - 75, i + 1, this.field_146295_m - 70, ColorUtils.rainbow(4000000L * i).getRGB());
        }
        this.func_73731_b((FontRenderer)Fonts.font40, "Made by CCBlueX", 2, this.field_146295_m - 10, 16777215);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    protected void func_146284_a(final GuiButton button) {
        switch (button.field_146127_k) {
            case 0: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiOptions((GuiScreen)this, this.field_146297_k.field_71474_y));
                break;
            }
            case 1: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiSelectWorld((GuiScreen)this));
                break;
            }
            case 2: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiMultiplayer((GuiScreen)this));
                break;
            }
            case 4: {
                this.field_146297_k.func_71400_g();
                break;
            }
            case 100: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiAltManager(this));
                break;
            }
            case 101: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiServerStatus(this));
                break;
            }
            case 102: {
                if (Keyboard.isKeyDown(42)) {
                    LiquidBounce.CLIENT.background = null;
                    LiquidBounce.CLIENT.fileManager.backgroundFile.delete();
                    return;
                }
                final JFileChooser jFileChooser = new JFileChooser() {
                    @Override
                    protected JDialog createDialog(final Component parent) throws HeadlessException {
                        final JDialog jDialog = super.createDialog(parent);
                        jDialog.setAlwaysOnTop(true);
                        return jDialog;
                    }
                };
                jFileChooser.setFileSelectionMode(0);
                final int returnValue = jFileChooser.showOpenDialog(null);
                if (returnValue != 0) {
                    break;
                }
                final File selectedFile = jFileChooser.getSelectedFile();
                if (selectedFile.isDirectory()) {
                    return;
                }
                try {
                    Files.copy(selectedFile.toPath(), new FileOutputStream(LiquidBounce.CLIENT.fileManager.backgroundFile));
                    final BufferedImage image = ImageIO.read(new FileInputStream(LiquidBounce.CLIENT.fileManager.backgroundFile));
                    LiquidBounce.CLIENT.background = new ResourceLocation("LiquidBounce".toLowerCase() + "/background.png");
                    this.field_146297_k.func_110434_K().func_110579_a(LiquidBounce.CLIENT.background, (ITextureObject)new DynamicTexture(image));
                }
                catch (Exception e) {
                    e.printStackTrace();
                    MiscUtils.showErrorPopup("Error", "Exception class: " + e.getClass().getName() + "\nMessage: " + e.getMessage());
                    LiquidBounce.CLIENT.fileManager.backgroundFile.delete();
                }
                break;
            }
            case 103: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiModsMenu(this));
                break;
            }
            case 108: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiCredits(this));
                break;
            }
        }
    }
    
    protected void func_73869_a(final char typedChar, final int keyCode) {
    }
}
