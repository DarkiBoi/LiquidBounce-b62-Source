// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.player;

import java.util.Iterator;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.Breadcrumbs;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import java.util.ArrayList;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import java.util.LinkedList;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import java.util.List;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Blink", description = "Suspends all movement packets.", category = ModuleCategory.PLAYER)
public class Blink extends Module
{
    private final List<Packet> packets;
    private EntityOtherPlayerMP fakePlayer;
    private int count;
    private boolean disableLogger;
    private final LinkedList<double[]> positions;
    private final BoolValue pulseValue;
    private final IntegerValue pulseDelayValue;
    private final MSTimer pulseTimer;
    
    public Blink() {
        this.packets = new ArrayList<Packet>();
        this.fakePlayer = null;
        this.positions = new LinkedList<double[]>();
        this.pulseValue = new BoolValue("Pulse", false);
        this.pulseDelayValue = new IntegerValue("PulseDelay", 1000, 500, 5000);
        this.pulseTimer = new MSTimer();
    }
    
    @Override
    public void onEnable() {
        if (Blink.mc.field_71439_g == null) {
            return;
        }
        (this.fakePlayer = new EntityOtherPlayerMP((World)Blink.mc.field_71441_e, Blink.mc.field_71439_g.func_146103_bH())).func_71049_a((EntityPlayer)Blink.mc.field_71439_g, true);
        this.fakePlayer.func_82149_j((Entity)Blink.mc.field_71439_g);
        this.fakePlayer.field_70759_as = Blink.mc.field_71439_g.field_70759_as;
        Blink.mc.field_71441_e.func_73027_a(-9100, (Entity)this.fakePlayer);
        synchronized (this.positions) {
            this.positions.add(new double[] { Blink.mc.field_71439_g.field_70165_t, Blink.mc.field_71439_g.func_174813_aQ().field_72338_b + Blink.mc.field_71439_g.func_70047_e() / 2.0f, Blink.mc.field_71439_g.field_70161_v });
            this.positions.add(new double[] { Blink.mc.field_71439_g.field_70165_t, Blink.mc.field_71439_g.func_174813_aQ().field_72338_b, Blink.mc.field_71439_g.field_70161_v });
        }
        this.pulseTimer.reset();
    }
    
    @Override
    public void onDisable() {
        if (Blink.mc.field_71439_g == null || this.fakePlayer == null) {
            return;
        }
        this.blink();
        Blink.mc.field_71441_e.func_73028_b(this.fakePlayer.func_145782_y());
        this.fakePlayer = null;
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();
        if (Blink.mc.field_71439_g == null || !(packet instanceof C03PacketPlayer) || this.disableLogger) {
            return;
        }
        event.setCancelled(true);
        if (!(packet instanceof C03PacketPlayer.C04PacketPlayerPosition) && !(packet instanceof C03PacketPlayer.C06PacketPlayerPosLook)) {
            return;
        }
        this.packets.add(packet);
        ++this.count;
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        synchronized (this.positions) {
            this.positions.add(new double[] { Blink.mc.field_71439_g.field_70165_t, Blink.mc.field_71439_g.func_174813_aQ().field_72338_b, Blink.mc.field_71439_g.field_70161_v });
        }
        if (this.pulseValue.asBoolean() && this.pulseTimer.hasTimePassed(this.pulseDelayValue.asInteger())) {
            this.blink();
            this.pulseTimer.reset();
        }
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        final Breadcrumbs breadcrumbs = (Breadcrumbs)ModuleManager.getModule(Breadcrumbs.class);
        final Color color = breadcrumbs.colorRainbow.asBoolean() ? ColorUtils.rainbow() : new Color((int)breadcrumbs.colorRedValue.asObject(), (int)breadcrumbs.colorGreenValue.asObject(), (int)breadcrumbs.colorBlueValue.asObject());
        synchronized (this.positions) {
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2848);
            GL11.glEnable(3042);
            GL11.glDisable(2929);
            Blink.mc.field_71460_t.func_175072_h();
            GL11.glBegin(3);
            RenderUtils.glColor(color);
            final double renderPosX = Blink.mc.func_175598_ae().field_78730_l;
            final double renderPosY = Blink.mc.func_175598_ae().field_78731_m;
            final double renderPosZ = Blink.mc.func_175598_ae().field_78728_n;
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
    
    @Override
    public String getTag() {
        return String.valueOf(this.count);
    }
    
    private void blink() {
        try {
            this.disableLogger = true;
            final Iterator<Packet> packetIterator = this.packets.iterator();
            while (packetIterator.hasNext()) {
                Blink.mc.func_147114_u().func_147297_a((Packet)packetIterator.next());
                packetIterator.remove();
            }
            this.count = 0;
            this.disableLogger = false;
        }
        catch (Exception e) {
            e.printStackTrace();
            this.disableLogger = false;
        }
        synchronized (this.positions) {
            this.positions.clear();
        }
    }
}
