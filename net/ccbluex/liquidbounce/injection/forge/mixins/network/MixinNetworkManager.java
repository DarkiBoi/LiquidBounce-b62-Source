// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.network;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.Packet;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ NetworkManager.class })
public class MixinNetworkManager
{
    @Inject(method = { "channelRead0" }, at = { @At("HEAD") }, cancellable = true)
    private void read(final ChannelHandlerContext context, final Packet<?> packet, final CallbackInfo callback) {
        final PacketEvent event = new PacketEvent(packet);
        LiquidBounce.CLIENT.eventManager.callEvent(event);
        if (event.isCancelled()) {
            callback.cancel();
        }
    }
    
    @Inject(method = { "sendPacket(Lnet/minecraft/network/Packet;)V" }, at = { @At("HEAD") }, cancellable = true)
    private void send(final Packet<?> packet, final CallbackInfo callback) {
        final PacketEvent event = new PacketEvent(packet);
        LiquidBounce.CLIENT.eventManager.callEvent(event);
        if (event.isCancelled()) {
            callback.cancel();
        }
    }
}
