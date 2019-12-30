// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.misc;

import java.util.Random;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.TextValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Spammer", description = "Spams the chat with a given message.", category = ModuleCategory.MISC)
public class Spammer extends Module
{
    private final IntegerValue maxDelayValue;
    private final IntegerValue minDelayValue;
    private final TextValue messageValue;
    private final BoolValue customValue;
    private final MSTimer msTimer;
    private long delay;
    
    public Spammer() {
        this.maxDelayValue = new IntegerValue("MaxDelay", 1000, 0, 5000) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int minDelayValueObject = Spammer.this.minDelayValue.asInteger();
                if (minDelayValueObject > Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(minDelayValueObject);
                }
                Spammer.this.delay = TimeUtils.randomDelay(Spammer.this.minDelayValue.asInteger(), (int)Spammer.this.maxDelayValue.asObject());
            }
        };
        this.minDelayValue = new IntegerValue("MinDelay", 500, 0, 5000) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int maxDelayValue = Spammer.this.maxDelayValue.asInteger();
                if (maxDelayValue < Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(maxDelayValue);
                }
                Spammer.this.delay = TimeUtils.randomDelay(Spammer.this.minDelayValue.asInteger(), Spammer.this.maxDelayValue.asInteger());
            }
        };
        this.messageValue = new TextValue("Message", "LiquidBounce Client | liquidbounce(.net) | CCBlueX on yt");
        this.customValue = new BoolValue("Custom", false);
        this.msTimer = new MSTimer();
        this.delay = TimeUtils.randomDelay(this.minDelayValue.asInteger(), this.maxDelayValue.asInteger());
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("spammer", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("message")) {
                        if (args.length > 2) {
                            final String message = StringUtils.toCompleteString(args, 2);
                            Spammer.this.messageValue.setValue(message);
                            this.chat("§7Spammer message was set to '§8" + message + "§7'.");
                            Spammer$3.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        this.chatSyntax(".spammer message <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("maxdelay")) {
                        if (args.length > 2) {
                            try {
                                final int value = Integer.parseInt(args[2]);
                                if (Spammer.this.minDelayValue.asInteger() > value) {
                                    this.chat("MinDelay can't higher as MaxDelay!");
                                    return;
                                }
                                Spammer.this.maxDelayValue.setValue(value);
                                Spammer.this.delay = TimeUtils.randomDelay(Spammer.this.minDelayValue.asInteger(), Spammer.this.maxDelayValue.asInteger());
                                this.chat("§7Spammer maxdelay was set to §8" + value + "§7.");
                                Spammer$3.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".spammer maxdelay <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("mindelay")) {
                        if (args.length > 2) {
                            try {
                                final int value = Integer.parseInt(args[2]);
                                if (Spammer.this.maxDelayValue.asInteger() < value) {
                                    this.chat("MinDelay can't higher as MaxDelay!");
                                    return;
                                }
                                Spammer.this.minDelayValue.setValue(value);
                                Spammer.this.delay = TimeUtils.randomDelay(Spammer.this.minDelayValue.asInteger(), Spammer.this.maxDelayValue.asInteger());
                                this.chat("§7Spammer mindelay was set to §8" + value + "§7.");
                                Spammer$3.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".spammer mindelay <value>");
                        return;
                    }
                }
                this.chatSyntax(".spammer <message, maxdelay, mindelay>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (this.msTimer.hasTimePassed(this.delay)) {
            Spammer.mc.field_71439_g.func_71165_d(this.customValue.asBoolean() ? this.replace((String)this.messageValue.asObject()) : (this.messageValue.asObject() + " >" + RandomUtils.randomString(5 + RandomUtils.getRandom().nextInt(5)) + "<"));
            this.msTimer.reset();
            this.delay = TimeUtils.randomDelay((int)this.minDelayValue.asObject(), (int)this.maxDelayValue.asObject());
        }
    }
    
    private String replace(String object) {
        Random r;
        for (r = new Random(); object.contains("%f"); object = object.substring(0, object.indexOf("%f")) + r.nextFloat() + object.substring(object.indexOf("%f") + "%f".length())) {}
        while (object.contains("%i")) {
            object = object.substring(0, object.indexOf("%i")) + r.nextInt(10000) + object.substring(object.indexOf("%i") + "%i".length());
        }
        while (object.contains("%s")) {
            object = object.substring(0, object.indexOf("%s")) + RandomUtils.randomString(r.nextInt(8) + 1) + object.substring(object.indexOf("%s") + "%s".length());
        }
        while (object.contains("%ss")) {
            object = object.substring(0, object.indexOf("%ss")) + RandomUtils.randomString(r.nextInt(4) + 1) + object.substring(object.indexOf("%ss") + "%ss".length());
        }
        while (object.contains("%ls")) {
            object = object.substring(0, object.indexOf("%ls")) + RandomUtils.randomString(r.nextInt(15) + 1) + object.substring(object.indexOf("%ls") + "%ls".length());
        }
        return object;
    }
}
