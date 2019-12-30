// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.hud;

import java.io.File;
import net.ccbluex.liquidbounce.ui.hud.element.elements.Image;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import java.awt.HeadlessException;
import javax.swing.JDialog;
import java.awt.Component;
import javax.swing.JFileChooser;
import net.ccbluex.liquidbounce.ui.hud.element.elements.Notifications;
import net.ccbluex.liquidbounce.ui.hud.element.elements.Effects;
import net.ccbluex.liquidbounce.ui.hud.element.elements.Armor;
import net.ccbluex.liquidbounce.ui.hud.element.elements.Model;
import net.ccbluex.liquidbounce.ui.hud.element.elements.TabGUI;
import net.ccbluex.liquidbounce.ui.hud.element.Facing;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.hud.element.elements.Text;
import java.io.IOException;
import java.util.Iterator;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.Gui;
import net.ccbluex.liquidbounce.ui.hud.element.elements.Arraylist;
import net.ccbluex.liquidbounce.LiquidBounce;
import org.lwjgl.input.Keyboard;
import net.ccbluex.liquidbounce.ui.hud.element.EditorPanel;
import net.ccbluex.liquidbounce.ui.hud.element.Element;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiHudDesigner extends GuiScreen
{
    private GuiButton createButton;
    private GuiButton resetButton;
    private GuiButton deleteButton;
    private boolean isCreating;
    private GuiButton textButton;
    private GuiButton tabGuiButton;
    private GuiButton arrayButton;
    private GuiButton modelButton;
    private GuiButton armorButton;
    private GuiButton effectsButton;
    private GuiButton notificationsButton;
    private GuiButton imageButton;
    private GuiButton closeButton;
    public Element selectedElement;
    private EditorPanel editorPanel;
    private boolean buttonAction;
    
    public GuiHudDesigner() {
        this.editorPanel = new EditorPanel(this, 2, 2);
    }
    
    public void func_73866_w_() {
        Keyboard.enableRepeatEvents(true);
        this.editorPanel = new EditorPanel(this, this.field_146294_l / 2, this.field_146295_m / 2);
        this.field_146292_n.add(this.deleteButton = new GuiButton(1, this.field_146294_l / 2 - 95, 2, 60, 20, "Delete"));
        this.field_146292_n.add(this.resetButton = new GuiButton(12, this.field_146294_l / 2 - 30, 2, 60, 20, "Reset"));
        this.field_146292_n.add(this.createButton = new GuiButton(2, this.field_146294_l / 2 + 35, 2, 60, 20, "Create"));
        this.isCreating = false;
        this.textButton = new GuiButton(3, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48, "Text");
        this.tabGuiButton = new GuiButton(4, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48 + 25, "TabGUI");
        this.arrayButton = new GuiButton(5, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48 + 50, "Array");
        this.modelButton = new GuiButton(7, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48 + 75, "Model");
        this.armorButton = new GuiButton(8, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48 + 100, "Armor");
        this.effectsButton = new GuiButton(9, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48 + 125, "Effects");
        this.notificationsButton = new GuiButton(10, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48 + 150, "Notifications");
        this.imageButton = new GuiButton(11, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48 + 175, "Image");
        this.closeButton = new GuiButton(6, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48 + 200, "Close");
        super.func_73866_w_();
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.arrayButton.field_146124_l = true;
        for (final Element element : LiquidBounce.CLIENT.hud.getElements()) {
            if (element instanceof Arraylist) {
                this.arrayButton.field_146124_l = false;
                break;
            }
        }
        Gui.func_73734_a(this.field_146294_l / 2 - 100, 0, this.field_146294_l / 2 + 100, 25, Integer.MIN_VALUE);
        LiquidBounce.CLIENT.hud.render();
        LiquidBounce.CLIENT.hud.handleMouseMove(mouseX, mouseY);
        if (!LiquidBounce.CLIENT.hud.getElements().contains(this.selectedElement)) {
            this.selectedElement = null;
        }
        final int wheel = Mouse.getDWheel();
        this.editorPanel.drawPanel(mouseX, mouseY, wheel);
        if (this.isCreating) {
            super.func_73863_a(mouseX, mouseY, partialTicks);
            this.func_73733_a(0, 0, this.field_146294_l, this.field_146295_m, -1072689136, -804253680);
            this.textButton.func_146112_a(this.field_146297_k, mouseX, mouseY);
            this.tabGuiButton.func_146112_a(this.field_146297_k, mouseX, mouseY);
            this.arrayButton.func_146112_a(this.field_146297_k, mouseX, mouseY);
            this.closeButton.func_146112_a(this.field_146297_k, mouseX, mouseY);
            this.modelButton.func_146112_a(this.field_146297_k, mouseX, mouseY);
            this.armorButton.func_146112_a(this.field_146297_k, mouseX, mouseY);
            this.effectsButton.func_146112_a(this.field_146297_k, mouseX, mouseY);
            this.notificationsButton.func_146112_a(this.field_146297_k, mouseX, mouseY);
            this.imageButton.func_146112_a(this.field_146297_k, mouseX, mouseY);
            return;
        }
        for (final Element element2 : LiquidBounce.CLIENT.hud.getElements()) {
            if (element2.isMouseOverElement((int)(mouseX / element2.getScale()), (int)(mouseY / element2.getScale()))) {
                element2.setScale(element2.getScale() + ((wheel > 0) ? 0.05f : ((wheel < 0) ? -0.05f : 0.0f)));
                break;
            }
        }
        this.deleteButton.field_146124_l = (this.selectedElement != null);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    protected void func_73864_a(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.func_73864_a(mouseX, mouseY, mouseButton);
        if (this.buttonAction) {
            this.buttonAction = false;
            return;
        }
        if (this.isCreating) {
            return;
        }
        LiquidBounce.CLIENT.hud.handleMouseClick(mouseX, mouseY, mouseButton);
        if (mouseX < this.editorPanel.getX() || mouseX > this.editorPanel.getX() + this.editorPanel.getWidth() || mouseY < this.editorPanel.getY() || mouseY > this.editorPanel.getY() + ((this.editorPanel.getRealHeight() > 200) ? 200 : this.editorPanel.getRealHeight())) {
            this.selectedElement = null;
        }
        for (final Element element : LiquidBounce.CLIENT.hud.getElements()) {
            if (mouseButton == 0 && element.isMouseOverElement((int)(mouseX / element.getScale()), (int)(mouseY / element.getScale()))) {
                this.selectedElement = element;
                break;
            }
        }
    }
    
    protected void func_146286_b(final int mouseX, final int mouseY, final int state) {
        super.func_146286_b(mouseX, mouseY, state);
        if (this.isCreating) {
            return;
        }
        LiquidBounce.CLIENT.hud.handleMouseReleased();
    }
    
    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
        LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.hudConfig);
        super.func_146281_b();
    }
    
    protected void func_146284_a(final GuiButton button) throws IOException {
        this.buttonAction = true;
        switch (button.field_146127_k) {
            case 1: {
                if (this.selectedElement != null) {
                    LiquidBounce.CLIENT.hud.removeElement(this.selectedElement);
                    break;
                }
                break;
            }
            case 2: {
                this.field_146292_n.add(this.textButton);
                this.field_146292_n.add(this.tabGuiButton);
                this.field_146292_n.add(this.arrayButton);
                this.field_146292_n.add(this.modelButton);
                this.field_146292_n.add(this.armorButton);
                this.field_146292_n.add(this.closeButton);
                this.field_146292_n.add(this.effectsButton);
                this.field_146292_n.add(this.notificationsButton);
                this.field_146292_n.add(this.imageButton);
                this.createButton.field_146124_l = false;
                this.resetButton.field_146124_l = false;
                this.deleteButton.field_146124_l = false;
                this.isCreating = true;
                break;
            }
            case 3: {
                LiquidBounce.CLIENT.hud.addElement(new Text().setText("").setShadow(true).setRainbow(false).setFontRenderer(Fonts.font40).setX(2).setY(2).setScale(1.0f).setFacing(new Facing(Facing.Horizontal.LEFT, Facing.Vertical.UP)));
                break;
            }
            case 4: {
                LiquidBounce.CLIENT.hud.addElement(new TabGUI().setFontRenderer(Fonts.font35).setX(2).setY(25).setFacing(new Facing(Facing.Horizontal.LEFT, Facing.Vertical.UP)));
                break;
            }
            case 5: {
                LiquidBounce.CLIENT.hud.addElement(new Arraylist().setFontRenderer(Fonts.font40).setX(0).setY(2).setFacing(new Facing(Facing.Horizontal.RIGHT, Facing.Vertical.UP)));
                break;
            }
            case 6: {
                this.field_146292_n.remove(this.textButton);
                this.field_146292_n.remove(this.tabGuiButton);
                this.field_146292_n.remove(this.arrayButton);
                this.field_146292_n.remove(this.modelButton);
                this.field_146292_n.remove(this.armorButton);
                this.field_146292_n.remove(this.closeButton);
                this.field_146292_n.remove(this.effectsButton);
                this.field_146292_n.remove(this.notificationsButton);
                this.field_146292_n.remove(this.imageButton);
                this.createButton.field_146124_l = true;
                this.resetButton.field_146124_l = true;
                this.deleteButton.field_146124_l = true;
                this.isCreating = false;
                break;
            }
            case 7: {
                LiquidBounce.CLIENT.hud.addElement(new Model().setX(40).setY(100).setFacing(new Facing(Facing.Horizontal.LEFT, Facing.Vertical.UP)));
                break;
            }
            case 8: {
                LiquidBounce.CLIENT.hud.addElement(new Armor().setX(8).setY(55).setFacing(new Facing(Facing.Horizontal.MIDDLE, Facing.Vertical.DOWN)));
                break;
            }
            case 9: {
                LiquidBounce.CLIENT.hud.addElement(new Effects().setFontRenderer(Fonts.font35).setShadow(true).setX(2).setY(10).setFacing(new Facing(Facing.Horizontal.RIGHT, Facing.Vertical.DOWN)));
                break;
            }
            case 10: {
                LiquidBounce.CLIENT.hud.addElement(new Notifications().setX(0).setY(40).setFacing(new Facing(Facing.Horizontal.RIGHT, Facing.Vertical.DOWN)));
                break;
            }
            case 11: {
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
                if (!selectedFile.exists()) {
                    MiscUtils.showErrorPopup("Error", "The file does not exist.");
                    return;
                }
                if (selectedFile.isDirectory()) {
                    MiscUtils.showErrorPopup("Error", "The file is a directory.");
                    return;
                }
                LiquidBounce.CLIENT.hud.addElement(new Image().setImage(selectedFile).setX(2).setY(2).setFacing(new Facing(Facing.Horizontal.LEFT, Facing.Vertical.UP)));
                break;
            }
            case 12: {
                this.selectedElement = null;
                LiquidBounce.CLIENT.hud.clearElements();
                LiquidBounce.CLIENT.hud = new DefaultHUD();
                this.selectedElement = null;
                break;
            }
        }
        super.func_146284_a(button);
    }
    
    protected void func_73869_a(final char typedChar, final int keyCode) throws IOException {
        if (this.isCreating) {
            super.func_73869_a(typedChar, keyCode);
            return;
        }
        if (211 == keyCode && this.selectedElement != null) {
            LiquidBounce.CLIENT.hud.removeElement(this.selectedElement);
        }
        if (1 != keyCode) {
            LiquidBounce.CLIENT.hud.handleKey(typedChar, keyCode);
        }
        super.func_73869_a(typedChar, keyCode);
    }
}
