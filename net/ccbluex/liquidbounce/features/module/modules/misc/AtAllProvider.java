// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.misc;

import java.util.Iterator;
import java.util.Arrays;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import java.util.Collection;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import java.util.ArrayList;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AtAllProvider", description = "Automatically mentions everyone on the server when using '@a' in your message.", category = ModuleCategory.MISC)
public class AtAllProvider extends Module
{
    private final IntegerValue maxDelayValue;
    private final IntegerValue minDelayValue;
    private final BoolValue retryValue;
    private final LinkedBlockingQueue<String> sendQueue;
    private final List<String> retryQueue;
    private final MSTimer msTimer;
    private long delay;
    
    public AtAllProvider() {
        this.maxDelayValue = new IntegerValue("MaxDelay", 1000, 0, 20000) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int i = AtAllProvider.this.minDelayValue.asInteger();
                if (i > Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(i);
                }
            }
        };
        this.minDelayValue = new IntegerValue("MinDelay", 500, 0, 20000) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int i = AtAllProvider.this.maxDelayValue.asInteger();
                if (i < Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(i);
                }
            }
        };
        this.retryValue = new BoolValue("Retry", false);
        this.sendQueue = new LinkedBlockingQueue<String>();
        this.retryQueue = new ArrayList<String>();
        this.msTimer = new MSTimer();
        this.delay = TimeUtils.randomDelay(this.minDelayValue.asInteger(), this.maxDelayValue.asInteger());
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("atallprovider", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("maxdelay")) {
                        if (args.length > 2) {
                            try {
                                final int value = Integer.parseInt(args[2]);
                                if (AtAllProvider.this.minDelayValue.asInteger() > value) {
                                    this.chat("MinDelay can't higher as MaxDelay!");
                                    return;
                                }
                                AtAllProvider.this.maxDelayValue.setValue(value);
                                AtAllProvider.this.delay = TimeUtils.randomDelay(AtAllProvider.this.minDelayValue.asInteger(), AtAllProvider.this.maxDelayValue.asInteger());
                                this.chat("§7AtAllProvider maxdelay was set to §8" + value + "§7.");
                                AtAllProvider$3.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".atallprovider maxdelay <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("mindelay")) {
                        if (args.length > 2) {
                            try {
                                final int value = Integer.parseInt(args[2]);
                                if (AtAllProvider.this.maxDelayValue.asInteger() < value) {
                                    this.chat("MinDelay can't higher as MaxDelay!");
                                    return;
                                }
                                AtAllProvider.this.minDelayValue.setValue(value);
                                AtAllProvider.this.delay = TimeUtils.randomDelay(AtAllProvider.this.minDelayValue.asInteger(), AtAllProvider.this.maxDelayValue.asInteger());
                                this.chat("§7AtAllProvider mindelay was set to §8" + value + "§7.");
                                AtAllProvider$3.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".atallprovider mindelay <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("retry")) {
                        AtAllProvider.this.retryValue.setValue(!AtAllProvider.this.retryValue.asBoolean());
                        this.chat("§7AtAllProvider TakeRandomized was toggled §8" + (AtAllProvider.this.retryValue.asBoolean() ? "on" : "off") + "§7.");
                        AtAllProvider$3.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                        return;
                    }
                }
                this.chatSyntax(".atallprovider <maxdelay, mindelay, retry>");
            }
        });
    }
    
    @Override
    public void onDisable() {
        synchronized (this.sendQueue) {
            this.sendQueue.clear();
        }
        synchronized (this.retryQueue) {
            this.retryQueue.clear();
        }
        super.onDisable();
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (!this.msTimer.hasTimePassed(this.delay)) {
            return;
        }
        try {
            synchronized (this.sendQueue) {
                if (this.sendQueue.isEmpty()) {
                    if (!this.retryValue.asBoolean() || this.retryQueue.isEmpty()) {
                        return;
                    }
                    this.sendQueue.addAll((Collection<?>)this.retryQueue);
                }
                AtAllProvider.mc.field_71439_g.func_71165_d((String)this.sendQueue.take());
                this.msTimer.reset();
                this.delay = TimeUtils.randomDelay(this.minDelayValue.asInteger(), this.maxDelayValue.asInteger());
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        if (event.getPacket() instanceof C01PacketChatMessage) {
            final C01PacketChatMessage packetChatMessage = (C01PacketChatMessage)event.getPacket();
            final String message = packetChatMessage.func_149439_c();
            if (message.contains("@a")) {
                synchronized (this.sendQueue) {
                    for (final NetworkPlayerInfo playerInfo : AtAllProvider.mc.func_147114_u().func_175106_d()) {
                        final String playerName = playerInfo.func_178845_a().getName();
                        if (playerName.equals(AtAllProvider.mc.field_71439_g.func_70005_c_())) {
                            continue;
                        }
                        this.sendQueue.add(message.replace("@a", playerName));
                    }
                    if (this.retryValue.asBoolean()) {
                        synchronized (this.retryQueue) {
                            this.retryQueue.clear();
                            this.retryQueue.addAll((Collection<? extends String>)Arrays.asList((Object[])this.sendQueue.toArray((T[])new String[0])));
                        }
                    }
                }
                event.setCancelled(true);
            }
        }
    }
}
