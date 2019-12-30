// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.hud.element.elements;

import net.ccbluex.liquidbounce.features.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.ui.hud.element.Facing;
import java.util.Iterator;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.util.function.Consumer;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import java.util.ArrayList;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.ui.hud.element.Element;

public class TabGUI extends Element
{
    private final IntegerValue redValue;
    private final IntegerValue greenValue;
    private final IntegerValue blueValue;
    private final IntegerValue alphaValue;
    private final BoolValue rectangleRainbow;
    private final IntegerValue backgroundRedValue;
    private final IntegerValue backgroundGreenValue;
    private final IntegerValue backgroundBlueValue;
    private final IntegerValue backgroundAlphaValue;
    private final BoolValue borderValue;
    private final IntegerValue borderStrength;
    private final IntegerValue borderRedValue;
    private final IntegerValue borderGreenValue;
    private final IntegerValue borderBlueValue;
    private final IntegerValue borderAlphaValue;
    private final BoolValue borderRainbow;
    private final BoolValue arrowsValue;
    private FontRenderer fontRenderer;
    private final BoolValue textShadow;
    private final BoolValue textFade;
    private final IntegerValue textPositionY;
    private final IntegerValue width;
    private final IntegerValue tabHeight;
    private final BoolValue upperCaseValue;
    private int guiHeight;
    private float tabY;
    private float itemY;
    private int selectedTab;
    private int selectedItem;
    private boolean mainMenu;
    private final List<Tab> tabs;
    
    public TabGUI() {
        this.redValue = new IntegerValue("Rectangle Red", 0, 0, 255);
        this.greenValue = new IntegerValue("Rectangle Green", 200, 0, 255);
        this.blueValue = new IntegerValue("Rectangle Blue", 255, 0, 255);
        this.alphaValue = new IntegerValue("Rectangle Alpha", 140, 0, 255);
        this.rectangleRainbow = new BoolValue("Rectangle Rainbow", true);
        this.backgroundRedValue = new IntegerValue("Background Red", 0, 0, 255);
        this.backgroundGreenValue = new IntegerValue("Background Green", 0, 0, 255);
        this.backgroundBlueValue = new IntegerValue("Background Blue", 0, 0, 255);
        this.backgroundAlphaValue = new IntegerValue("Background Alpha", 150, 0, 255);
        this.borderValue = new BoolValue("Border", true);
        this.borderStrength = new IntegerValue("Border Strength", 2, 1, 5);
        this.borderRedValue = new IntegerValue("Border Red", 0, 0, 255);
        this.borderGreenValue = new IntegerValue("Border Green", 0, 0, 255);
        this.borderBlueValue = new IntegerValue("Border Blue", 0, 0, 255);
        this.borderAlphaValue = new IntegerValue("Border Alpha", 150, 0, 255);
        this.borderRainbow = new BoolValue("Border Rainbow", false);
        this.arrowsValue = new BoolValue("Arrows", true);
        this.fontRenderer = Fonts.font35;
        this.textShadow = new BoolValue("TextShadow", false);
        this.textFade = new BoolValue("TextFade", true);
        this.textPositionY = new IntegerValue("TextPosition-Y", 2, 0, 5);
        this.width = new IntegerValue("Width", 60, 55, 100);
        this.tabHeight = new IntegerValue("TabHeight", 12, 10, 15);
        this.upperCaseValue = new BoolValue("UpperCase", false);
        this.mainMenu = true;
        this.tabs = new ArrayList<Tab>();
        for (final ModuleCategory category : ModuleCategory.values()) {
            final Tab tab = new Tab(category.getDisplayName());
            ModuleManager.getModules().stream().filter(module -> category.equals(module.getCategory())).forEach(tab.modules::add);
            this.tabs.add(tab);
        }
    }
    
    @Override
    public void drawElement() {
        final int delta = RenderUtils.deltaTime;
        final int xPos = this.tabHeight.asInteger() * this.selectedTab;
        if ((int)this.tabY != xPos) {
            if (xPos > this.tabY) {
                this.tabY += 0.1f * delta;
            }
            else {
                this.tabY -= 0.1f * delta;
            }
        }
        else {
            this.tabY = (float)xPos;
        }
        final int xPos2 = this.tabHeight.asInteger() * this.selectedItem;
        if ((int)this.itemY != xPos2) {
            if (xPos2 > this.itemY) {
                this.itemY += 0.1f * delta;
            }
            else {
                this.itemY -= 0.1f * delta;
            }
        }
        else {
            this.itemY = (float)xPos2;
        }
        if (this.mainMenu) {
            this.itemY = 0.0f;
        }
        if (this.textFade.asBoolean()) {
            for (int i = 0; i < this.tabs.size(); ++i) {
                final Tab tab = this.tabs.get(i);
                if (i == this.selectedTab) {
                    if (tab.textFade < 4.0f) {
                        tab.textFade += 0.05f * delta;
                    }
                    if (tab.textFade > 4.0f) {
                        tab.textFade = 4.0f;
                    }
                }
                else {
                    if (tab.textFade > 0.0f) {
                        tab.textFade -= 0.05f * delta;
                    }
                    if (tab.textFade < 0.0f) {
                        tab.textFade = 0.0f;
                    }
                }
            }
        }
        else {
            for (final Tab tab : this.tabs) {
                if (tab.textFade > 0.0f) {
                    tab.textFade -= 0.05f * delta;
                }
                if (tab.textFade < 0.0f) {
                    tab.textFade = 0.0f;
                }
            }
        }
        final int[] loc = this.getLocationFromFacing();
        this.drawGui(loc[0], loc[1]);
    }
    
    @Override
    public void destroyElement() {
    }
    
    @Override
    public void handleMouseClick(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    @Override
    public void handleKey(final char c, final int keyCode) {
        switch (keyCode) {
            case 200: {
                this.parseAction(Action.UP);
                break;
            }
            case 208: {
                this.parseAction(Action.DOWN);
                break;
            }
            case 205: {
                this.parseAction((this.getFacing().getHorizontal() == Facing.Horizontal.RIGHT) ? Action.LEFT : Action.RIGHT);
                break;
            }
            case 203: {
                this.parseAction((this.getFacing().getHorizontal() == Facing.Horizontal.RIGHT) ? Action.RIGHT : Action.LEFT);
                break;
            }
            case 28: {
                this.parseAction(Action.TOGGLE);
                break;
            }
        }
    }
    
    @Override
    public boolean isMouseOverElement(final int mouseX, final int mouseY) {
        final int[] location = this.getLocationFromFacing();
        return mouseX >= location[0] && mouseY >= location[1] && mouseX <= location[0] + this.width.asInteger() && mouseY <= location[1] + this.guiHeight;
    }
    
    private void drawGui(final int posX, final int posY) {
        final Color color = this.rectangleRainbow.asBoolean() ? ColorUtils.rainbow(400000000L, this.alphaValue.asInteger()) : new Color(this.redValue.asInteger(), this.greenValue.asInteger(), this.blueValue.asInteger(), this.alphaValue.asInteger());
        final Color backgroundColor = new Color(this.backgroundRedValue.asInteger(), this.backgroundGreenValue.asInteger(), this.backgroundBlueValue.asInteger(), this.backgroundAlphaValue.asInteger());
        final Color borderColor = this.borderRainbow.asBoolean() ? ColorUtils.rainbow(400000000L, this.borderAlphaValue.asInteger()) : new Color(this.borderRedValue.asInteger(), this.borderGreenValue.asInteger(), this.borderBlueValue.asInteger(), this.borderAlphaValue.asInteger());
        this.guiHeight = this.tabs.size() * this.tabHeight.asInteger();
        if (this.borderValue.asBoolean()) {
            RenderUtils.drawBorderedRect((float)(posX - 1), (float)posY, (float)(posX + this.width.asInteger()), (float)(posY + this.guiHeight), (float)this.borderStrength.asInteger(), borderColor.getRGB(), backgroundColor.getRGB());
        }
        else {
            RenderUtils.drawRect((float)(posX - 1), (float)posY, (float)(posX + this.width.asInteger()), (float)(posY + this.guiHeight), backgroundColor.getRGB());
        }
        RenderUtils.drawRect((float)(posX - 1), posY + 1 + this.tabY - 1.0f, (float)(posX + this.width.asInteger()), posY + this.tabY + this.tabHeight.asInteger(), color);
        GlStateManager.func_179117_G();
        int yOff = posY + 1;
        for (int i = 0; i < this.tabs.size(); ++i) {
            final String tabName = this.upperCaseValue.asBoolean() ? this.tabs.get(i).tabName.toUpperCase() : this.tabs.get(i).tabName;
            this.fontRenderer.func_175065_a(tabName, (this.getFacing().getHorizontal() == Facing.Horizontal.RIGHT) ? (posX + this.width.asInteger() - this.fontRenderer.func_78256_a(tabName) - this.tabs.get(i).textFade - 3.0f) : (posX + this.tabs.get(i).textFade + 2.0f), (float)(yOff + this.textPositionY.asInteger()), (this.selectedTab == i) ? 16777215 : new Color(210, 210, 210).getRGB(), this.textShadow.asBoolean());
            if (this.arrowsValue.asBoolean()) {
                if (this.getFacing().getHorizontal() == Facing.Horizontal.RIGHT) {
                    this.fontRenderer.func_175065_a((!this.mainMenu && this.selectedTab == i) ? ">" : "<", (float)(posX + 3), (float)(yOff + 2), 16777215, this.textShadow.asBoolean());
                }
                else {
                    this.fontRenderer.func_175065_a((!this.mainMenu && this.selectedTab == i) ? "<" : ">", (float)(posX + this.width.asInteger() - 8), (float)(yOff + 2), 16777215, this.textShadow.asBoolean());
                }
            }
            if (i == this.selectedTab && !this.mainMenu) {
                this.tabs.get(i).drawTab((this.getFacing().getHorizontal() == Facing.Horizontal.RIGHT) ? (posX - 3 - this.tabs.get(i).menuWidth) : (posX + this.width.asInteger() + 3), yOff - 2, color.getRGB(), backgroundColor.getRGB(), borderColor.getRGB(), this.borderStrength.asInteger(), this.upperCaseValue.asBoolean());
            }
            yOff += this.tabHeight.asInteger();
        }
    }
    
    private void parseAction(final Action action) {
        switch (action) {
            case UP: {
                if (this.mainMenu) {
                    --this.selectedTab;
                    if (this.selectedTab < 0) {
                        this.selectedTab = this.tabs.size() - 1;
                        this.tabY = (float)(this.tabHeight.asInteger() * this.selectedTab);
                        break;
                    }
                    break;
                }
                else {
                    --this.selectedItem;
                    if (this.selectedItem < 0) {
                        this.selectedItem = this.tabs.get(this.selectedTab).modules.size() - 1;
                        this.itemY = (float)(this.tabHeight.asInteger() * this.selectedItem);
                        break;
                    }
                    break;
                }
                break;
            }
            case DOWN: {
                if (this.mainMenu) {
                    ++this.selectedTab;
                    if (this.selectedTab > this.tabs.size() - 1) {
                        this.selectedTab = 0;
                        this.tabY = (float)(this.tabHeight.asInteger() * this.selectedTab);
                        break;
                    }
                    break;
                }
                else {
                    ++this.selectedItem;
                    if (this.selectedItem > this.tabs.get(this.selectedTab).modules.size() - 1) {
                        this.selectedItem = 0;
                        this.itemY = (float)(this.tabHeight.asInteger() * this.selectedItem);
                        break;
                    }
                    break;
                }
                break;
            }
            case LEFT: {
                if (!this.mainMenu) {
                    this.mainMenu = true;
                    break;
                }
                break;
            }
            case RIGHT: {
                if (this.mainMenu) {
                    this.mainMenu = false;
                    this.selectedItem = 0;
                    break;
                }
                break;
            }
            case TOGGLE: {
                if (!this.mainMenu) {
                    final int sel = this.selectedItem;
                    this.tabs.get(this.selectedTab).modules.get(sel).toggle();
                    break;
                }
                break;
            }
        }
    }
    
    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }
    
    public TabGUI setFontRenderer(final FontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
        return this;
    }
    
    public TabGUI setRainbow(final boolean rainbow) {
        this.rectangleRainbow.setValue(rainbow);
        return this;
    }
    
    public TabGUI setColor(final Color c) {
        this.redValue.setValue(c.getRed());
        this.greenValue.setValue(c.getGreen());
        this.blueValue.setValue(c.getBlue());
        this.alphaValue.setValue(c.getAlpha());
        return this;
    }
    
    private class Tab
    {
        private final String tabName;
        private final ArrayList<Module> modules;
        private int menuHeight;
        private int menuWidth;
        private float textFade;
        
        private Tab(final String name) {
            this.modules = new ArrayList<Module>();
            this.tabName = name;
        }
        
        private void drawTab(int x, int y, final int color, final int backgroundColor, final int borderColor, final int borderStrength, final boolean upperCase) {
            int maxWidth = 0;
            for (final Module module : this.modules) {
                if (TabGUI.this.fontRenderer.func_78256_a(upperCase ? module.getName().toUpperCase() : module.getName()) + 4 > maxWidth) {
                    maxWidth = (int)(TabGUI.this.fontRenderer.func_78256_a(upperCase ? module.getName().toUpperCase() : module.getName()) + 7.0f);
                }
            }
            this.menuWidth = maxWidth;
            this.menuHeight = this.modules.size() * TabGUI.this.tabHeight.asInteger();
            x += 2;
            y += 2;
            if (TabGUI.this.borderValue.asBoolean()) {
                RenderUtils.drawBorderedRect((float)(x - 1), (float)(y - 1), (float)(x + this.menuWidth - 2), (float)(y + this.menuHeight - 1), (float)borderStrength, borderColor, backgroundColor);
            }
            else {
                RenderUtils.drawRect((float)(x - 1), (float)(y - 1), (float)(x + this.menuWidth - 2), (float)(y + this.menuHeight - 1), backgroundColor);
            }
            RenderUtils.drawRect((float)(x - 1), y + TabGUI.this.itemY - 1.0f, (float)(x + this.menuWidth - 2), y + TabGUI.this.itemY + TabGUI.this.tabHeight.asInteger() - 1.0f, color);
            GlStateManager.func_179117_G();
            for (int i = 0; i < this.modules.size(); ++i) {
                TabGUI.this.fontRenderer.func_175065_a(upperCase ? this.modules.get(i).getName().toUpperCase() : this.modules.get(i).getName(), (float)(x + 2), (float)(y + TabGUI.this.tabHeight.asInteger() * i + TabGUI.this.textPositionY.asInteger()), this.modules.get(i).getState() ? 16777215 : new Color(205, 205, 205).getRGB(), TabGUI.this.textShadow.asBoolean());
            }
        }
    }
    
    public enum Action
    {
        UP, 
        DOWN, 
        LEFT, 
        RIGHT, 
        TOGGLE;
    }
}
