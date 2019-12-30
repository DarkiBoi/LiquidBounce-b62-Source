// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.special;

import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.client.Minecraft;
import java.text.MessageFormat;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.handshake.client.C00Handshake;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.ccbluex.liquidbounce.event.EventListener;

@SideOnly(Side.CLIENT)
public class BungeeCordSpoof implements EventListener
{
    public static boolean enabled;
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();
        if (packet instanceof C00Handshake && BungeeCordSpoof.enabled && ((C00Handshake)packet).func_149594_c() == EnumConnectionState.LOGIN) {
            final C00Handshake handshake = (C00Handshake)packet;
            handshake.field_149598_b = handshake.field_149598_b + "\u0000" + MessageFormat.format("{0}.{1}.{2}.{3}", this.getRandomIpPart(), this.getRandomIpPart(), this.getRandomIpPart(), this.getRandomIpPart()) + "\u0000" + Minecraft.func_71410_x().func_110432_I().func_148255_b().replace("-", "");
        }
    }
    
    private String getRandomIpPart() {
        return RandomUtils.getRandom().nextInt(2) + "" + RandomUtils.getRandom().nextInt(5) + "" + RandomUtils.getRandom().nextInt(5);
    }
    
    @Override
    public boolean handleEvents() {
        return true;
    }
    
    static {
        BungeeCordSpoof.enabled = false;
    }
}
