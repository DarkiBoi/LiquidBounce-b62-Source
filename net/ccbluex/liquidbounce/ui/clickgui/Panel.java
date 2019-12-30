// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.clickgui;

import net.minecraft.util.StringUtils;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import java.util.Iterator;
import net.ccbluex.liquidbounce.LiquidBounce;
import java.util.Objects;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import java.util.ArrayList;
import net.ccbluex.liquidbounce.ui.clickgui.elements.Element;
import java.util.List;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class Panel
{
    private final String name;
    public int x;
    public int y;
    public int x2;
    public int y2;
    private final int width;
    private final int height;
    private int scroll;
    private int dragged;
    private boolean open;
    public boolean drag;
    private boolean scrollbar;
    private final List<Element> elements;
    private boolean visible;
    private float elementsHeight;
    private float fade;
    
    public Panel(final String name, final int x, final int y, final int width, final int height, final boolean open) {
        this.name = name;
        this.elements = new ArrayList<Element>();
        this.scrollbar = false;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.open = open;
        this.visible = true;
        this.setupItems();
    }
    
    public abstract void setupItems();
    
    public void drawScreen(final int mouseX, final int mouseY, final float button) {
        if (!this.visible) {
            return;
        }
        final int maxElements = Objects.requireNonNull(ModuleManager.getModule(ClickGUI.class)).maxElementsValue.asInteger();
        if (this.drag) {
            final int nx = this.x2 + mouseX;
            final int ny = this.y2 + mouseY;
            if (nx > -1) {
                this.x = nx;
            }
            if (ny > -1) {
                this.y = ny;
            }
        }
        this.elementsHeight = (float)(this.getElementsHeight() - 1);
        final boolean scrollbar = this.elements.size() >= maxElements;
        if (this.scrollbar != scrollbar) {
            this.scrollbar = scrollbar;
        }
        LiquidBounce.CLIENT.clickGui.style.drawPanel(mouseX, mouseY, this);
        int y = this.y + this.height - 2;
        int count = 0;
        for (final Element element : this.elements) {
            if (++count > this.scroll && count < this.scroll + (maxElements + 1) && this.scroll < this.elements.size()) {
                element.setLocation(this.x, y);
                element.setWidth(this.getWidth());
                if (y <= this.getY() + this.fade) {
                    element.drawScreen(mouseX, mouseY, button);
                }
                y += element.getHeight() + 1;
                element.setVisible(true);
            }
            else {
                element.setVisible(false);
            }
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (!this.visible) {
            return;
        }
        if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            this.open = !this.open;
            Minecraft.func_71410_x().func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.bow"), 1.0f));
            return;
        }
        for (final Element element : this.elements) {
            if (element.getY() <= this.getY() + this.fade) {
                element.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (!this.visible) {
            return;
        }
        this.drag = false;
        if (!this.open) {
            return;
        }
        for (final Element element : this.elements) {
            element.mouseReleased(mouseX, mouseY, state);
        }
    }
    
    public boolean handleScroll(final int mouseX, final int mouseY, final int wheel) {
        final int maxElements = Objects.requireNonNull(ModuleManager.getModule(ClickGUI.class)).maxElementsValue.asInteger();
        if (mouseX >= this.getX() && mouseX <= this.getX() + 100 && mouseY >= this.getY() && mouseY <= this.getY() + 19 + this.elementsHeight) {
            if (wheel < 0 && this.scroll < this.elements.size() - maxElements) {
                ++this.scroll;
                if (this.scroll < 0) {
                    this.scroll = 0;
                }
            }
            else if (wheel > 0) {
                --this.scroll;
                if (this.scroll < 0) {
                    this.scroll = 0;
                }
            }
            if (wheel < 0) {
                if (this.dragged < this.elements.size() - maxElements) {
                    ++this.dragged;
                }
            }
            else if (wheel > 0 && this.dragged >= 1) {
                --this.dragged;
            }
            return true;
        }
        return false;
    }
    
    void updateFade(final int delta) {
        if (this.open) {
            if (this.fade < this.elementsHeight) {
                this.fade += 0.4f * delta;
            }
            if (this.fade > this.elementsHeight) {
                this.fade = (float)(int)this.elementsHeight;
            }
        }
        else {
            if (this.fade > 0.0f) {
                this.fade -= 0.4f * delta;
            }
            if (this.fade < 0.0f) {
                this.fade = 0.0f;
            }
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setX(final int dragX) {
        this.x = dragX;
    }
    
    public void setY(final int dragY) {
        this.y = dragY;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public boolean getScrollbar() {
        return this.scrollbar;
    }
    
    public void setOpen(final boolean open) {
        this.open = open;
    }
    
    public boolean getOpen() {
        return this.open;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public List<Element> getElements() {
        return this.elements;
    }
    
    public int getFade() {
        return (int)this.fade;
    }
    
    public int getDragged() {
        return this.dragged;
    }
    
    private int getElementsHeight() {
        int height = 0;
        int count = 0;
        for (final Element element : this.elements) {
            if (count >= Objects.requireNonNull(ModuleManager.getModule(ClickGUI.class)).maxElementsValue.asInteger()) {
                continue;
            }
            height += element.getHeight() + 1;
            ++count;
        }
        return height;
    }
    
    boolean isHovering(final int mouseX, final int mouseY) {
        final float textWidth = Minecraft.func_71410_x().field_71466_p.func_78256_a(StringUtils.func_76338_a(this.name)) - 100.0f;
        return mouseX >= this.x - textWidth / 2.0f - 19.0f && mouseX <= this.x - textWidth / 2.0f + Minecraft.func_71410_x().field_71466_p.func_78256_a(StringUtils.func_76338_a(this.name)) + 19.0f && mouseY >= this.y && mouseY <= this.y + this.height - (this.open ? 2 : 0);
    }
}
