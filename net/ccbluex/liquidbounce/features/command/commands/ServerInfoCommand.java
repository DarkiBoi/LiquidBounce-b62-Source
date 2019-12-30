// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.minecraft.client.multiplayer.ServerData;
import java.text.MessageFormat;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventListener;
import net.ccbluex.liquidbounce.features.command.Command;

public class ServerInfoCommand extends Command implements EventListener
{
    private String ip;
    private int port;
    
    public ServerInfoCommand() {
        super("serverinfo", null);
        LiquidBounce.CLIENT.eventManager.registerListener(this);
    }
    
    @Override
    public void execute(final String[] args) {
        if (ServerInfoCommand.mc.func_147104_D() == null) {
            this.chat("This command only work on a server.");
            return;
        }
        final ServerData data = ServerInfoCommand.mc.func_147104_D();
        this.chat(MessageFormat.format("Server infos:\n§7Name: §8{0}\n§7IP: §8{1}:{2}\n§7Players: §8{3}\n§7MOTD: §8{4}\n§7ServerVersion: §8{5}\n§7ProtocolVersion: §8{6}\n§7PingToServer: §8{7}", data.field_78847_a, this.ip, this.port, data.field_78846_c, data.field_78843_d, data.field_82822_g, data.field_82821_f, data.field_78844_e));
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();
        if (packet instanceof C00Handshake) {
            final C00Handshake handshake = (C00Handshake)packet;
            this.ip = handshake.field_149598_b;
            this.port = handshake.field_149599_c;
        }
    }
    
    @Override
    public boolean handleEvents() {
        return true;
    }
}
