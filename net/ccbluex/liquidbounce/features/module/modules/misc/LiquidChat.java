// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.misc;

import java.util.Enumeration;
import java.net.NetworkInterface;
import java.io.IOException;
import java.net.SocketException;
import net.ccbluex.liquidbounce.chat.packet.packets.AuthenticatePacket;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.chat.packet.packets.PrivateMessagePacket;
import net.ccbluex.liquidbounce.utils.ChatUtils;
import net.ccbluex.liquidbounce.chat.packet.Packet;
import net.ccbluex.liquidbounce.chat.packet.packets.MessagePacket;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import net.ccbluex.liquidbounce.chat.packet.PacketHandler;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "LiquidChat", description = "Allows you to chat with other LiquidBounce users.", category = ModuleCategory.MISC)
public class LiquidChat extends Module
{
    private final PacketHandler packetHandler;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private String username;
    
    public LiquidChat() {
        (this.packetHandler = new PacketHandler()).registerPackets();
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("LiquidChat", new String[] { "lc" }) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1) {
                    this.chatSyntax(".liquidchat <message...>");
                    return;
                }
                if (!LiquidChat.this.getState()) {
                    this.chat("§c§lError: §aLiquidChat is disabled");
                    return;
                }
                if (LiquidChat.this.socket == null || LiquidChat.this.socket.isClosed()) {
                    this.chat("§c§lError: §aNo connection to the LiquidChat server.");
                    return;
                }
                try {
                    LiquidChat.this.sendPacket(new MessagePacket(StringUtils.toCompleteString(args, 1)));
                }
                catch (Exception e) {
                    e.printStackTrace();
                    if (LiquidChat$1.mc.field_71439_g == null) {
                        return;
                    }
                    ChatUtils.displayChatMessage("§c§lError: §7" + e.getClass().getName() + ": " + e.getMessage());
                }
            }
        });
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("liquidchatpn", new String[] { "lcpn" }) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 2) {
                    this.chatSyntax(".liquidchatpn <username> <message...>");
                    return;
                }
                if (!LiquidChat.this.getState()) {
                    this.chat("§c§lError: §aLiquidChat is disabled");
                    return;
                }
                if (LiquidChat.this.socket == null || LiquidChat.this.socket.isClosed()) {
                    this.chat("§c§lError: §aNo connection to the LiquidChat server.");
                    return;
                }
                try {
                    LiquidChat.this.sendPacket(new PrivateMessagePacket(args[1], StringUtils.toCompleteString(args, 2)));
                }
                catch (Exception e) {
                    e.printStackTrace();
                    if (LiquidChat$2.mc.field_71439_g == null) {
                        return;
                    }
                    ChatUtils.displayChatMessage("§c§lError: §7" + e.getClass().getName() + ": " + e.getMessage());
                }
            }
        });
    }
    
    @Override
    public void onStarted() {
        if (!this.getState()) {
            return;
        }
        this.connect(LiquidChat.mc.func_110432_I().func_111285_a());
    }
    
    @Override
    public void onEnable() {
        if (LiquidBounce.CLIENT.isStarting) {
            return;
        }
        this.connect(LiquidChat.mc.func_110432_I().func_111285_a());
        if (this.socket != null && this.socket.isConnected()) {
            ChatUtils.displayChatMessage("====================================");
            ChatUtils.displayChatMessage("§c>> §lLiquidChat");
            ChatUtils.displayChatMessage("§7Write message: §a.lc <message>");
            ChatUtils.displayChatMessage("§7Write private message: §a.lcpn <user>");
            ChatUtils.displayChatMessage("====================================");
        }
    }
    
    @Override
    public void onDisable() {
        this.disconnect();
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (this.socket == null || this.socket.isClosed() || !this.socket.isConnected()) {
            this.setState(false);
            return;
        }
        if (this.socket != null && this.username != null && !this.username.equals(LiquidChat.mc.func_110432_I().func_111285_a())) {
            this.disconnect();
            this.connect(LiquidChat.mc.func_110432_I().func_111285_a());
        }
    }
    
    private void connect(final String username) {
        try {
            this.socket = new Socket("chat.liquidbounce.net", 1337);
            this.username = username;
            this.input = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
            this.output = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
            this.sendPacket(new AuthenticatePacket(username, LiquidChat.mc.func_110432_I().func_148256_e().getId(), StringUtils.hashString(this.getHWID().replace(" ", "")), LiquidChat.mc.func_110432_I().func_148254_d().equals("-") || LiquidChat.mc.func_110432_I().func_148254_d().isEmpty() || LiquidChat.mc.func_110432_I().func_152428_f().name().equals("LEGACY") || LiquidChat.mc.func_110432_I().func_148254_d().equals("0")));
        }
        catch (SocketException e) {
            if (LiquidChat.mc.field_71439_g == null) {
                return;
            }
            ChatUtils.displayChatMessage("§c§lError: §7" + e.getClass().getName() + ": " + e.getMessage());
            this.setState(false);
            this.disconnect();
            return;
        }
        catch (Exception e2) {
            e2.printStackTrace();
            if (LiquidChat.mc.field_71439_g == null) {
                return;
            }
            ChatUtils.displayChatMessage("§c§lError: §7" + e2.getClass().getName() + ": " + e2.getMessage());
            this.setState(false);
            this.disconnect();
            return;
        }
        byte[] buffer;
        Packet packet;
        MessagePacket messagePacket;
        final Thread handlerThread = new Thread(() -> {
            try {
                buffer = new byte[1024];
                while (this.input != null && this.input.read(buffer) != -1) {
                    try {
                        packet = this.packetHandler.read(buffer);
                        if (packet instanceof MessagePacket) {
                            messagePacket = (MessagePacket)packet;
                            if (LiquidChat.mc.field_71439_g == null) {
                                continue;
                            }
                            else {
                                ChatUtils.displayChatMessage("§7[§a§lLiquidChat§7] " + messagePacket.getMessage());
                            }
                        }
                        else {
                            continue;
                        }
                    }
                    catch (Exception e3) {
                        e3.printStackTrace();
                        if (LiquidChat.mc.field_71439_g == null) {
                            continue;
                        }
                        else {
                            ChatUtils.displayChatMessage("§c§lError: §7" + e3.getClass().getName() + ": " + e3.getMessage());
                        }
                    }
                }
            }
            catch (SocketException e4) {
                if (LiquidChat.mc.field_71439_g != null) {
                    if (this.socket.isClosed() && !this.socket.isConnected() && this.socket.isInputShutdown() && this.socket.isOutputShutdown()) {
                        this.setState(false);
                    }
                    else {
                        ChatUtils.displayChatMessage("§c§lError: §7" + e4.getClass().getName() + ": " + e4.getMessage());
                        this.setState(false);
                        this.disconnect();
                    }
                }
            }
            catch (Exception e5) {
                e5.printStackTrace();
                if (LiquidChat.mc.field_71439_g != null) {
                    ChatUtils.displayChatMessage("§c§lError: §7" + e5.getClass().getName() + ": " + e5.getMessage());
                    this.setState(false);
                    this.disconnect();
                }
            }
            return;
        });
        handlerThread.start();
    }
    
    private void disconnect() {
        if (this.socket == null) {
            return;
        }
        try {
            if (!this.socket.isInputShutdown()) {
                this.socket.shutdownInput();
            }
            if (!this.socket.isOutputShutdown()) {
                this.socket.shutdownOutput();
            }
            if (!this.socket.isClosed()) {
                this.socket.close();
            }
            this.socket = null;
            this.input = null;
            this.output = null;
            this.username = null;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void sendPacket(final Packet packet) throws Exception {
        if (packet == null || this.output == null) {
            return;
        }
        this.output.write(this.packetHandler.write(packet));
        this.output.flush();
    }
    
    private String getHWID() throws SocketException {
        final StringBuilder hwid = new StringBuilder(System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name").trim());
        final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            final NetworkInterface networkInterface = networkInterfaces.nextElement();
            final byte[] mac = networkInterface.getHardwareAddress();
            if (mac == null) {
                continue;
            }
            final StringBuilder builder = new StringBuilder();
            for (int i = 0; i < mac.length; ++i) {
                builder.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            hwid.append(builder.toString());
        }
        return hwid.toString();
    }
}
