// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.script;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Enumeration;
import java.net.URL;
import java.awt.Desktop;
import net.ccbluex.liquidbounce.script.Script;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.io.File;
import java.util.ArrayList;
import java.util.zip.ZipFile;
import net.ccbluex.liquidbounce.ui.clickgui.ClickGui;
import net.ccbluex.liquidbounce.LiquidBounce;
import java.awt.HeadlessException;
import javax.swing.JDialog;
import java.awt.Component;
import javax.swing.JFileChooser;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.GuiScreen;

public class GuiScripts extends GuiScreen
{
    private final GuiScreen prevGui;
    private GuiList list;
    
    public GuiScripts(final GuiScreen gui) {
        this.prevGui = gui;
    }
    
    public void func_73866_w_() {
        (this.list = new GuiList(this)).func_148134_d(7, 8);
        this.list.func_148144_a(-1, false, 0, 0);
        final int j = 22;
        final ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k);
        this.field_146292_n.add(new GuiButton(0, scaledResolution.func_78326_a() - 80, scaledResolution.func_78328_b() - 65, 70, 20, "Back"));
        this.field_146292_n.add(new GuiButton(1, scaledResolution.func_78326_a() - 80, j + 24, 70, 20, "Import"));
        this.field_146292_n.add(new GuiButton(2, scaledResolution.func_78326_a() - 80, j + 48, 70, 20, "Delete"));
        this.field_146292_n.add(new GuiButton(3, scaledResolution.func_78326_a() - 80, j + 72, 70, 20, "Reload"));
        this.field_146292_n.add(new GuiButton(4, scaledResolution.func_78326_a() - 80, j + 96, 70, 20, "Folder"));
        this.field_146292_n.add(new GuiButton(5, scaledResolution.func_78326_a() - 80, j + 120, 70, 20, "Wiki"));
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146278_c(0);
        this.list.func_148128_a(mouseX, mouseY, partialTicks);
        this.func_73732_a((FontRenderer)Fonts.font40, "§9§lScripts", this.field_146294_l / 2, 28, 16777215);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    protected void func_146284_a(final GuiButton button) {
        switch (button.field_146127_k) {
            case 0: {
                this.field_146297_k.func_147108_a(this.prevGui);
                break;
            }
            case 1: {
                try {
                    final JFileChooser fileChooser = new JFileChooser() {
                        @Override
                        protected JDialog createDialog(final Component parent) throws HeadlessException {
                            final JDialog jDialog = super.createDialog(parent);
                            jDialog.setAlwaysOnTop(true);
                            return jDialog;
                        }
                    };
                    fileChooser.setFileSelectionMode(0);
                    final int returnValue = fileChooser.showOpenDialog(null);
                    if (returnValue == 0) {
                        final File file = fileChooser.getSelectedFile();
                        final String fileName = file.getName();
                        if (fileName.endsWith(".js")) {
                            LiquidBounce.CLIENT.scriptManager.importScript(file);
                            LiquidBounce.CLIENT.moduleManager.sortModules();
                            LiquidBounce.CLIENT.clickGui = new ClickGui();
                            LiquidBounce.CLIENT.fileManager.loadConfig(LiquidBounce.CLIENT.fileManager.clickGuiConfig);
                        }
                        else if (fileName.endsWith(".zip")) {
                            final ZipFile zipFile = new ZipFile(file);
                            final Enumeration<? extends ZipEntry> entries = zipFile.entries();
                            final List<File> scriptFiles = new ArrayList<File>();
                            while (entries.hasMoreElements()) {
                                final ZipEntry entry = (ZipEntry)entries.nextElement();
                                final String entryName = entry.getName();
                                final File entryFile = new File(LiquidBounce.CLIENT.scriptManager.getScriptsFolder(), entryName);
                                if (entry.isDirectory()) {
                                    entryFile.mkdir();
                                }
                                else {
                                    final InputStream fileStream = zipFile.getInputStream(entry);
                                    final FileOutputStream fileOutputStream = new FileOutputStream(entryFile);
                                    IOUtils.copy(fileStream, (OutputStream)fileOutputStream);
                                    fileOutputStream.close();
                                    fileStream.close();
                                    if (entryName.contains("/")) {
                                        continue;
                                    }
                                    scriptFiles.add(entryFile);
                                }
                            }
                            scriptFiles.forEach(scriptFile -> LiquidBounce.CLIENT.scriptManager.loadScript(scriptFile));
                            LiquidBounce.CLIENT.moduleManager.sortModules();
                            LiquidBounce.CLIENT.clickGui = new ClickGui();
                            LiquidBounce.CLIENT.fileManager.loadConfig(LiquidBounce.CLIENT.fileManager.clickGuiConfig);
                            LiquidBounce.CLIENT.fileManager.loadConfig(LiquidBounce.CLIENT.fileManager.hudConfig);
                        }
                        else {
                            MiscUtils.showErrorPopup("Wrong file extension.", "The file extension has to be .js or .zip");
                        }
                    }
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while importing a script.", t);
                    MiscUtils.showErrorPopup(t.getClass().getName(), t.getMessage());
                }
                break;
            }
            case 2: {
                try {
                    if (this.list.getSelectedSlot() != -1) {
                        final Script script = LiquidBounce.CLIENT.scriptManager.getScripts().get(this.list.getSelectedSlot());
                        LiquidBounce.CLIENT.scriptManager.deleteScript(script);
                        LiquidBounce.CLIENT.moduleManager.sortModules();
                        LiquidBounce.CLIENT.clickGui = new ClickGui();
                        LiquidBounce.CLIENT.fileManager.loadConfig(LiquidBounce.CLIENT.fileManager.clickGuiConfig);
                        LiquidBounce.CLIENT.fileManager.loadConfig(LiquidBounce.CLIENT.fileManager.hudConfig);
                    }
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while deleting a script.", t);
                    MiscUtils.showErrorPopup(t.getClass().getName(), t.getMessage());
                }
                break;
            }
            case 3: {
                try {
                    LiquidBounce.CLIENT.scriptManager.reloadScripts();
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while reloading all scripts.", t);
                    MiscUtils.showErrorPopup(t.getClass().getName(), t.getMessage());
                }
                break;
            }
            case 4: {
                try {
                    Desktop.getDesktop().open(LiquidBounce.CLIENT.scriptManager.getScriptsFolder());
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while trying to open your scripts folder.", t);
                    MiscUtils.showErrorPopup(t.getClass().getName(), t.getMessage());
                }
                break;
            }
            case 5: {
                try {
                    Desktop.getDesktop().browse(new URL("https://github.com/CCBlueX/LiquidBounce-ScriptAPI/wiki").toURI());
                }
                catch (Exception ex) {}
                break;
            }
        }
    }
    
    protected void func_73869_a(final char typedChar, final int keyCode) throws IOException {
        if (1 == keyCode) {
            this.field_146297_k.func_147108_a(this.prevGui);
            return;
        }
        super.func_73869_a(typedChar, keyCode);
    }
    
    public void func_146274_d() throws IOException {
        super.func_146274_d();
        this.list.func_178039_p();
    }
    
    private class GuiList extends GuiSlot
    {
        private int selectedSlot;
        
        GuiList(final GuiScreen prevGui) {
            super(Minecraft.func_71410_x(), prevGui.field_146294_l, prevGui.field_146295_m, 40, prevGui.field_146295_m - 40, 30);
        }
        
        protected boolean func_148131_a(final int id) {
            return this.selectedSlot == id;
        }
        
        int getSelectedSlot() {
            if (this.selectedSlot > LiquidBounce.CLIENT.scriptManager.getScripts().size()) {
                this.selectedSlot = -1;
            }
            return this.selectedSlot;
        }
        
        protected int func_148127_b() {
            return LiquidBounce.CLIENT.scriptManager.getScripts().size();
        }
        
        protected void func_148144_a(final int id, final boolean doubleClick, final int var3, final int var4) {
            this.selectedSlot = id;
        }
        
        protected void func_180791_a(final int id, final int x, final int y, final int var4, final int var5, final int var6) {
            final Script script = LiquidBounce.CLIENT.scriptManager.getScripts().get(id);
            GuiScripts.this.func_73732_a((FontRenderer)Fonts.font40, "§9" + script.getScriptName() + " §7v" + script.getScriptVersion(), this.field_148155_a / 2, y + 2, Color.LIGHT_GRAY.getRGB());
            GuiScripts.this.func_73732_a((FontRenderer)Fonts.font40, "by §c" + script.getScriptAuthor(), this.field_148155_a / 2, y + 15, Color.LIGHT_GRAY.getRGB());
        }
        
        protected void func_148123_a() {
        }
    }
}
