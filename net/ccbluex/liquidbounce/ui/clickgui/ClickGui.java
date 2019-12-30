// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.clickgui;

import net.ccbluex.liquidbounce.LiquidBounce;
import java.io.IOException;
import net.minecraft.client.renderer.RenderHelper;
import net.ccbluex.liquidbounce.ui.clickgui.elements.Element;
import net.minecraft.client.renderer.GlStateManager;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import java.util.Objects;
import net.ccbluex.liquidbounce.ui.hud.GuiHudDesigner;
import org.lwjgl.input.Mouse;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.ui.clickgui.elements.ButtonElement;
import java.util.Iterator;
import net.ccbluex.liquidbounce.ui.clickgui.elements.ModuleElement;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.clickgui.style.styles.SlowlyStyle;
import java.util.ArrayList;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.ui.clickgui.style.Style;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public class ClickGui extends GuiScreen
{
    public final List<Panel> panels;
    private Panel clickedPanel;
    public Style style;
    private int mouseX;
    private int mouseY;
    private final ResourceLocation hudIcon;
    
    public ClickGui() {
        this.panels = new ArrayList<Panel>();
        this.style = new SlowlyStyle();
        this.hudIcon = new ResourceLocation("LiquidBounce".toLowerCase() + "/custom_hud_icon.png");
        final int width = 100;
        final int height = 18;
        int yPos = 5;
        for (final ModuleCategory category : ModuleCategory.values()) {
            this.panels.add(new Panel(category.getDisplayName(), 100, yPos, 100, 18, false) {
                @Override
                public void setupItems() {
                    for (final Module module : ModuleManager.getModules()) {
                        if (module.getCategory() == category) {
                            this.getElements().add(new ModuleElement(module));
                        }
                    }
                }
            });
            yPos += 20;
        }
        yPos += 20;
        this.panels.add(new Panel("Targets", 100, yPos, 100, 18, false) {
            @Override
            public void setupItems() {
                this.getElements().add(new ButtonElement("Players") {
                    @Override
                    public void createButton(final String displayName) {
                        this.color = (EntityUtils.targetPlayer ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE);
                        super.createButton(displayName);
                    }
                    
                    @Override
                    public String getDisplayName() {
                        this.displayName = "Players";
                        this.color = (EntityUtils.targetPlayer ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE);
                        return super.getDisplayName();
                    }
                    
                    @Override
                    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
                        if (mouseButton == 0 && this.isHovering(mouseX, mouseY) && this.isVisible()) {
                            EntityUtils.targetPlayer = !EntityUtils.targetPlayer;
                            this.displayName = "Players";
                            this.color = (EntityUtils.targetPlayer ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE);
                            ClickGui.this.field_146297_k.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                        }
                    }
                });
                this.getElements().add(new ButtonElement("Mobs") {
                    @Override
                    public void createButton(final String displayName) {
                        this.color = (EntityUtils.targetMobs ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE);
                        super.createButton(displayName);
                    }
                    
                    @Override
                    public String getDisplayName() {
                        this.displayName = "Mobs";
                        this.color = (EntityUtils.targetMobs ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE);
                        return super.getDisplayName();
                    }
                    
                    @Override
                    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
                        if (mouseButton == 0 && this.isHovering(mouseX, mouseY) && this.isVisible()) {
                            EntityUtils.targetMobs = !EntityUtils.targetMobs;
                            this.displayName = "Mobs";
                            this.color = (EntityUtils.targetMobs ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE);
                            ClickGui.this.field_146297_k.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                        }
                    }
                });
                this.getElements().add(new ButtonElement("Animals") {
                    @Override
                    public void createButton(final String displayName) {
                        this.color = (EntityUtils.targetAnimals ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE);
                        super.createButton(displayName);
                    }
                    
                    @Override
                    public String getDisplayName() {
                        this.displayName = "Animals";
                        this.color = (EntityUtils.targetAnimals ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE);
                        return super.getDisplayName();
                    }
                    
                    @Override
                    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
                        if (mouseButton == 0 && this.isHovering(mouseX, mouseY) && this.isVisible()) {
                            EntityUtils.targetAnimals = !EntityUtils.targetAnimals;
                            this.displayName = "Animals";
                            this.color = (EntityUtils.targetAnimals ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE);
                            ClickGui.this.field_146297_k.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                        }
                    }
                });
                this.getElements().add(new ButtonElement("Invisible") {
                    @Override
                    public void createButton(final String displayName) {
                        this.color = (EntityUtils.targetInvisible ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE);
                        super.createButton(displayName);
                    }
                    
                    @Override
                    public String getDisplayName() {
                        this.displayName = "Invisible";
                        this.color = (EntityUtils.targetInvisible ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE);
                        return super.getDisplayName();
                    }
                    
                    @Override
                    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
                        if (mouseButton == 0 && this.isHovering(mouseX, mouseY) && this.isVisible()) {
                            EntityUtils.targetInvisible = !EntityUtils.targetInvisible;
                            this.displayName = "Invisible";
                            this.color = (EntityUtils.targetInvisible ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE);
                            ClickGui.this.field_146297_k.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                        }
                    }
                });
                this.getElements().add(new ButtonElement("Dead") {
                    @Override
                    public void createButton(final String displayName) {
                        this.color = (EntityUtils.targetDead ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE);
                        super.createButton(displayName);
                    }
                    
                    @Override
                    public String getDisplayName() {
                        this.displayName = "Dead";
                        this.color = (EntityUtils.targetDead ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE);
                        return super.getDisplayName();
                    }
                    
                    @Override
                    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
                        if (mouseButton == 0 && this.isHovering(mouseX, mouseY) && this.isVisible()) {
                            EntityUtils.targetDead = !EntityUtils.targetDead;
                            this.displayName = "Dead";
                            this.color = (EntityUtils.targetDead ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE);
                            ClickGui.this.field_146297_k.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                        }
                    }
                });
            }
        });
    }
    
    public void func_73863_a(int mouseX, int mouseY, final float partialTicks) {
        if (Mouse.isButtonDown(0) && mouseX >= 5 && mouseX <= 50 && mouseY <= this.field_146295_m - 5 && mouseY >= this.field_146295_m - 50) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiHudDesigner());
        }
        final double scale = Objects.requireNonNull(ModuleManager.getModule(ClickGUI.class)).scaleValue.asDouble();
        mouseX /= (int)scale;
        mouseY /= (int)scale;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.func_146276_q_();
        RenderUtils.drawFilledCircle(25, this.field_146295_m - 25, 16.0f, new Color(37, 126, 255));
        RenderUtils.drawImage(this.hudIcon, 12, this.field_146295_m - 35, 23, 23);
        GlStateManager.func_179139_a(scale, scale, scale);
        for (final Panel panel : this.panels) {
            panel.updateFade(RenderUtils.deltaTime);
            panel.drawScreen(mouseX, mouseY, partialTicks);
        }
        for (final Panel panel : this.panels) {
            for (final Element element : panel.getElements()) {
                if (element instanceof ModuleElement) {
                    final ModuleElement moduleElement = (ModuleElement)element;
                    if (mouseX == 0 || mouseY == 0 || !moduleElement.isHovering(mouseX, mouseY) || !moduleElement.isVisible() || element.getY() > panel.getY() + panel.getFade()) {
                        continue;
                    }
                    this.style.drawDescription(mouseX, mouseY, moduleElement.getModule().getDescription());
                }
            }
        }
        if (Mouse.hasWheel()) {
            final int wheel = Mouse.getDWheel();
            for (int i = this.panels.size() - 1; i >= 0; --i) {
                if (this.panels.get(i).handleScroll(mouseX, mouseY, wheel)) {
                    break;
                }
            }
        }
        GlStateManager.func_179140_f();
        RenderHelper.func_74518_a();
        GlStateManager.func_179152_a(1.0f, 1.0f, 1.0f);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    protected void func_73864_a(int mouseX, int mouseY, final int mouseButton) throws IOException {
        final double scale = Objects.requireNonNull(ModuleManager.getModule(ClickGUI.class)).scaleValue.asDouble();
        mouseX /= (int)scale;
        mouseY /= (int)scale;
        for (final Panel panel : this.panels) {
            panel.mouseClicked(mouseX, mouseY, mouseButton);
            panel.drag = false;
            if (mouseButton == 0 && panel.isHovering(mouseX, mouseY)) {
                this.clickedPanel = panel;
            }
        }
        if (this.clickedPanel != null) {
            this.clickedPanel.x2 = this.clickedPanel.x - mouseX;
            this.clickedPanel.y2 = this.clickedPanel.y - mouseY;
            this.clickedPanel.drag = true;
            this.panels.remove(this.clickedPanel);
            this.panels.add(this.clickedPanel);
            this.clickedPanel = null;
        }
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }
    
    protected void func_146286_b(int mouseX, int mouseY, final int state) {
        final double scale = Objects.requireNonNull(ModuleManager.getModule(ClickGUI.class)).scaleValue.asDouble();
        mouseX /= (int)scale;
        mouseY /= (int)scale;
        for (final Panel panel : this.panels) {
            panel.mouseReleased(mouseX, mouseY, state);
        }
        super.func_146286_b(mouseX, mouseY, state);
    }
    
    public void func_73876_c() {
        for (final Panel panel : this.panels) {
            for (final Element element : panel.getElements()) {
                if (element instanceof ButtonElement) {
                    final ButtonElement buttonElement = (ButtonElement)element;
                    if (buttonElement.isHovering(this.mouseX, this.mouseY)) {
                        if (buttonElement.hoverTime < 7) {
                            final ButtonElement buttonElement2 = buttonElement;
                            ++buttonElement2.hoverTime;
                        }
                    }
                    else if (buttonElement.hoverTime > 0) {
                        final ButtonElement buttonElement3 = buttonElement;
                        --buttonElement3.hoverTime;
                    }
                }
                if (element instanceof ModuleElement) {
                    if (((ModuleElement)element).getModule().getState()) {
                        if (((ModuleElement)element).slowlyFade < 255) {
                            final ModuleElement moduleElement = (ModuleElement)element;
                            moduleElement.slowlyFade += 20;
                        }
                    }
                    else if (((ModuleElement)element).slowlyFade > 0) {
                        final ModuleElement moduleElement2 = (ModuleElement)element;
                        moduleElement2.slowlyFade -= 20;
                    }
                    if (((ModuleElement)element).slowlyFade > 255) {
                        ((ModuleElement)element).slowlyFade = 255;
                    }
                    if (((ModuleElement)element).slowlyFade >= 0) {
                        continue;
                    }
                    ((ModuleElement)element).slowlyFade = 0;
                }
            }
        }
        super.func_73876_c();
    }
    
    public void func_146281_b() {
        LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.clickGuiConfig);
    }
    
    public boolean func_73868_f() {
        return false;
    }
}
