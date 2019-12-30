// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import java.util.Iterator;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import java.util.LinkedList;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Breadcrumbs", description = "Leaves a trail behind you.", category = ModuleCategory.RENDER)
public class Breadcrumbs extends Module
{
    private final LinkedList<double[]> positions;
    public final IntegerValue colorRedValue;
    public final IntegerValue colorGreenValue;
    public final IntegerValue colorBlueValue;
    public final BoolValue colorRainbow;
    
    public Breadcrumbs() {
        this.positions = new LinkedList<double[]>();
        this.colorRedValue = new IntegerValue("R", 255, 0, 255);
        this.colorGreenValue = new IntegerValue("G", 179, 0, 255);
        this.colorBlueValue = new IntegerValue("B", 72, 0, 255);
        this.colorRainbow = new BoolValue("Rainbow", false);
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        final Color color = this.colorRainbow.asBoolean() ? ColorUtils.rainbow() : new Color(this.colorRedValue.asInteger(), this.colorGreenValue.asInteger(), this.colorBlueValue.asInteger());
        synchronized (this.positions) {
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2848);
            GL11.glEnable(3042);
            GL11.glDisable(2929);
            Breadcrumbs.mc.field_71460_t.func_175072_h();
            GL11.glBegin(3);
            RenderUtils.glColor(color);
            final double renderPosX = Breadcrumbs.mc.func_175598_ae().field_78730_l;
            final double renderPosY = Breadcrumbs.mc.func_175598_ae().field_78731_m;
            final double renderPosZ = Breadcrumbs.mc.func_175598_ae().field_78728_n;
            for (final double[] pos : this.positions) {
                GL11.glVertex3d(pos[0] - renderPosX, pos[1] - renderPosY, pos[2] - renderPosZ);
            }
            GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
            GL11.glEnd();
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glPopMatrix();
        }
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        synchronized (this.positions) {
            this.positions.add(new double[] { Breadcrumbs.mc.field_71439_g.field_70165_t, Breadcrumbs.mc.field_71439_g.func_174813_aQ().field_72338_b, Breadcrumbs.mc.field_71439_g.field_70161_v });
        }
    }
    
    @Override
    public void onEnable() {
        if (Breadcrumbs.mc.field_71439_g == null) {
            return;
        }
        synchronized (this.positions) {
            this.positions.add(new double[] { Breadcrumbs.mc.field_71439_g.field_70165_t, Breadcrumbs.mc.field_71439_g.func_174813_aQ().field_72338_b + Breadcrumbs.mc.field_71439_g.func_70047_e() / 2.0f, Breadcrumbs.mc.field_71439_g.field_70161_v });
            this.positions.add(new double[] { Breadcrumbs.mc.field_71439_g.field_70165_t, Breadcrumbs.mc.field_71439_g.func_174813_aQ().field_72338_b, Breadcrumbs.mc.field_71439_g.field_70161_v });
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        synchronized (this.positions) {
            this.positions.clear();
        }
        super.onDisable();
    }
}
