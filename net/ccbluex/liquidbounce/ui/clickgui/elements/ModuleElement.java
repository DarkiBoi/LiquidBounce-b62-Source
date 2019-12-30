// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.clickgui.elements;

import org.lwjgl.input.Mouse;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModuleElement extends ButtonElement
{
    private final Module module;
    private boolean showSettings;
    private float settingsWidth;
    private boolean wasPressed;
    public int slowlySettingsYPos;
    public int slowlyFade;
    
    public ModuleElement(final Module module) {
        super(null);
        this.settingsWidth = 0.0f;
        this.displayName = module.getName();
        this.module = module;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float button) {
        LiquidBounce.CLIENT.clickGui.style.drawModuleElement(mouseX, mouseY, this);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY) && this.isVisible()) {
            this.module.toggle();
            Minecraft.func_71410_x().func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
        }
        if (mouseButton == 1 && this.isHovering(mouseX, mouseY) && this.isVisible()) {
            this.showSettings = !this.showSettings;
            Minecraft.func_71410_x().func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
        }
    }
    
    public Module getModule() {
        return this.module;
    }
    
    public boolean isShowSettings() {
        return this.showSettings;
    }
    
    public void setShowSettings(final boolean showSettings) {
        this.showSettings = showSettings;
    }
    
    public boolean isntPressed() {
        return !this.wasPressed;
    }
    
    public void updatePressed() {
        this.wasPressed = Mouse.isButtonDown(0);
    }
    
    public float getSettingsWidth() {
        return this.settingsWidth;
    }
    
    public void setSettingsWidth(final float settingsWidth) {
        this.settingsWidth = settingsWidth;
    }
}
