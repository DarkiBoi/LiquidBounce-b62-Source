// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.hud;

import net.ccbluex.liquidbounce.ui.hud.element.elements.Effects;
import net.ccbluex.liquidbounce.ui.hud.element.elements.Notifications;
import net.ccbluex.liquidbounce.ui.hud.element.elements.Armor;
import net.ccbluex.liquidbounce.ui.hud.element.elements.Arraylist;
import net.ccbluex.liquidbounce.ui.hud.element.elements.TabGUI;
import net.ccbluex.liquidbounce.ui.hud.element.Facing;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import java.awt.Color;
import net.ccbluex.liquidbounce.ui.hud.element.elements.Text;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DefaultHUD extends HUD
{
    public DefaultHUD() {
        this.addElement(new Text().setText("%clientName%").setShadow(true).setRainbow(false).setColor(new Color(0, 111, 255)).setFontRenderer(Fonts.font40).setX(2).setY(2).setScale(2.0f).setFacing(new Facing(Facing.Horizontal.LEFT, Facing.Vertical.UP)));
        this.addElement(new TabGUI().setFontRenderer(Fonts.font35).setRainbow(false).setColor(new Color(0, 148, 255, 140)).setX(5).setY(25).setFacing(new Facing(Facing.Horizontal.LEFT, Facing.Vertical.UP)));
        this.addElement(new Arraylist().setFontRenderer(Fonts.font40).setX(1).setY(2).setFacing(new Facing(Facing.Horizontal.RIGHT, Facing.Vertical.UP)));
        this.addElement(new Armor().setX(8).setY(57).setFacing(new Facing(Facing.Horizontal.MIDDLE, Facing.Vertical.DOWN)));
        this.addElement(new Notifications().setX(0).setY(30).setFacing(new Facing(Facing.Horizontal.RIGHT, Facing.Vertical.DOWN)));
        this.addElement(new Effects().setFontRenderer(Fonts.font35).setShadow(true).setX(2).setY(10).setFacing(new Facing(Facing.Horizontal.RIGHT, Facing.Vertical.DOWN)));
    }
}
