// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.tools;

import net.ccbluex.liquidbounce.utils.TabUtils;
import java.io.IOException;
import java.io.File;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import javax.swing.JOptionPane;
import java.io.FileWriter;
import java.awt.HeadlessException;
import javax.swing.JDialog;
import java.awt.Component;
import javax.swing.JFileChooser;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Iterator;
import java.awt.Color;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import org.lwjgl.input.Keyboard;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiPortScanner extends GuiScreen
{
    private final GuiScreen prevGui;
    private GuiTextField hostField;
    private GuiTextField minPortField;
    private GuiTextField maxPortField;
    private GuiTextField threadsField;
    private GuiButton buttonToggle;
    private boolean running;
    private String status;
    private String host;
    private int currentPort;
    private int maxPort;
    private int minPort;
    private int checkedPort;
    private final List<Integer> ports;
    
    public GuiPortScanner(final GuiScreen prevGui) {
        this.status = "§7Wating...";
        this.ports = new ArrayList<Integer>();
        this.prevGui = prevGui;
    }
    
    public void func_73866_w_() {
        Keyboard.enableRepeatEvents(true);
        (this.hostField = new GuiTextField(0, (FontRenderer)Fonts.font40, this.field_146294_l / 2 - 100, 60, 200, 20)).func_146195_b(true);
        this.hostField.func_146203_f(Integer.MAX_VALUE);
        this.hostField.func_146180_a("localhost");
        (this.minPortField = new GuiTextField(1, (FontRenderer)Fonts.font40, this.field_146294_l / 2 - 100, 90, 90, 20)).func_146203_f(5);
        this.minPortField.func_146180_a(String.valueOf(1));
        (this.maxPortField = new GuiTextField(2, (FontRenderer)Fonts.font40, this.field_146294_l / 2 + 10, 90, 90, 20)).func_146203_f(5);
        this.maxPortField.func_146180_a(String.valueOf(65535));
        (this.threadsField = new GuiTextField(3, (FontRenderer)Fonts.font40, this.field_146294_l / 2 - 100, 120, 200, 20)).func_146203_f(Integer.MAX_VALUE);
        this.threadsField.func_146180_a(String.valueOf(500));
        this.field_146292_n.add(this.buttonToggle = new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 95, this.running ? "Stop" : "Start"));
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 120, "Back"));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 155, "Export"));
        super.func_73866_w_();
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146278_c(0);
        this.func_73732_a((FontRenderer)Fonts.font40, "Port Scanner", this.field_146294_l / 2, 34, 16777215);
        this.func_73732_a((FontRenderer)Fonts.font35, this.running ? ("§7" + this.checkedPort + " §8/ §7" + this.maxPort) : ((this.status == null) ? "" : this.status), this.field_146294_l / 2, this.field_146295_m / 4 + 80, 16777215);
        this.buttonToggle.field_146126_j = (this.running ? "Stop" : "Start");
        this.hostField.func_146194_f();
        this.minPortField.func_146194_f();
        this.maxPortField.func_146194_f();
        this.threadsField.func_146194_f();
        this.func_73731_b((FontRenderer)Fonts.font40, "§c§lPorts:", 2, 2, Color.WHITE.hashCode());
        synchronized (this.ports) {
            int i = 12;
            for (final Integer integer : this.ports) {
                this.func_73731_b((FontRenderer)Fonts.font35, String.valueOf(integer), 2, i, Color.WHITE.hashCode());
                i += Fonts.font35.field_78288_b;
            }
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
                if (this.running) {
                    this.running = false;
                }
                else {
                    this.host = this.hostField.func_146179_b();
                    if (this.host.isEmpty()) {
                        this.status = "§cInvalid host";
                        return;
                    }
                    try {
                        this.minPort = Integer.parseInt(this.minPortField.func_146179_b());
                    }
                    catch (NumberFormatException e3) {
                        this.status = "§cInvalid min port";
                        return;
                    }
                    try {
                        this.maxPort = Integer.parseInt(this.maxPortField.func_146179_b());
                    }
                    catch (NumberFormatException e3) {
                        this.status = "§cInvalid max port";
                        return;
                    }
                    int threads;
                    try {
                        threads = Integer.parseInt(this.threadsField.func_146179_b());
                    }
                    catch (NumberFormatException e4) {
                        this.status = "§cInvalid threads";
                        return;
                    }
                    this.ports.clear();
                    this.currentPort = this.minPort - 1;
                    this.checkedPort = this.minPort;
                    for (int i = 0; i < threads; ++i) {
                        int port;
                        Socket socket;
                        new Thread(() -> {
                            try {
                                while (this.running && this.currentPort < this.maxPort) {
                                    ++this.currentPort;
                                    port = this.currentPort;
                                    try {
                                        socket = new Socket();
                                        socket.connect(new InetSocketAddress(this.host, port), 500);
                                        socket.close();
                                        synchronized (this.ports) {
                                            if (!this.ports.contains(port)) {
                                                this.ports.add(port);
                                            }
                                        }
                                    }
                                    catch (Exception ex) {}
                                    if (this.checkedPort < port) {
                                        this.checkedPort = port;
                                    }
                                }
                                this.running = false;
                                this.buttonToggle.field_146126_j = "Start";
                            }
                            catch (Exception e) {
                                this.status = "§a§l" + e.getClass().getSimpleName() + ": §c" + e.getMessage();
                            }
                            return;
                        }).start();
                    }
                    this.running = true;
                }
                this.buttonToggle.field_146126_j = (this.running ? "Stop" : "Start");
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
                final int returnValue = jFileChooser.showSaveDialog(null);
                if (returnValue != 0) {
                    break;
                }
                final File selectedFile = jFileChooser.getSelectedFile();
                if (selectedFile.isDirectory()) {
                    return;
                }
                try {
                    if (!selectedFile.exists()) {
                        selectedFile.createNewFile();
                    }
                    final FileWriter fileWriter = new FileWriter(selectedFile);
                    fileWriter.write("Portscan\r\n");
                    fileWriter.write("Host: " + this.host + "\r\n\r\n");
                    fileWriter.write("Ports (" + this.minPort + " - " + this.maxPort + "):\r\n");
                    for (final Integer integer : this.ports) {
                        fileWriter.write(String.valueOf(integer) + "\r\n");
                    }
                    fileWriter.flush();
                    fileWriter.close();
                    JOptionPane.showMessageDialog(null, "Exported successful!", "Port Scanner", 1);
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                    MiscUtils.showErrorPopup("Error", "Exception class: " + e2.getClass().getName() + "\nMessage: " + e2.getMessage());
                }
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
            TabUtils.tab(this.hostField, this.minPortField, this.maxPortField);
        }
        if (this.running) {
            return;
        }
        if (this.hostField.func_146206_l()) {
            this.hostField.func_146201_a(typedChar, keyCode);
        }
        if (this.minPortField.func_146206_l() && !Character.isLetter(typedChar)) {
            this.minPortField.func_146201_a(typedChar, keyCode);
        }
        if (this.maxPortField.func_146206_l() && !Character.isLetter(typedChar)) {
            this.maxPortField.func_146201_a(typedChar, keyCode);
        }
        if (this.threadsField.func_146206_l() && !Character.isLetter(typedChar)) {
            this.threadsField.func_146201_a(typedChar, keyCode);
        }
        super.func_73869_a(typedChar, keyCode);
    }
    
    protected void func_73864_a(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.hostField.func_146192_a(mouseX, mouseY, mouseButton);
        this.minPortField.func_146192_a(mouseX, mouseY, mouseButton);
        this.maxPortField.func_146192_a(mouseX, mouseY, mouseButton);
        this.threadsField.func_146192_a(mouseX, mouseY, mouseButton);
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }
    
    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
        this.running = false;
        super.func_146281_b();
    }
}
