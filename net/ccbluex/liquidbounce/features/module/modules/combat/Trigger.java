// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Trigger", description = "Automatically attacks the entity you are looking at.", category = ModuleCategory.COMBAT)
public class Trigger extends Module
{
    private final IntegerValue maxCPS;
    private final IntegerValue minCPS;
    private long delay;
    private long lastSwing;
    
    public Trigger() {
        this.maxCPS = new IntegerValue("MaxCPS", 8, 1, 20) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int i = Trigger.this.minCPS.asInteger();
                if (i > Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(i);
                }
                Trigger.this.delay = TimeUtils.randomClickDelay(Trigger.this.minCPS.asInteger(), Trigger.this.maxCPS.asInteger());
            }
        };
        this.minCPS = new IntegerValue("MinCPS", 5, 1, 20) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int i = Trigger.this.maxCPS.asInteger();
                if (i < Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(i);
                }
                Trigger.this.delay = TimeUtils.randomClickDelay(Trigger.this.minCPS.asInteger(), Trigger.this.maxCPS.asInteger());
            }
        };
        this.delay = TimeUtils.randomClickDelay((int)this.minCPS.asObject(), (int)this.maxCPS.asObject());
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("trigger", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("maxcps")) {
                        if (args.length > 2) {
                            try {
                                final int value = Integer.parseInt(args[2]);
                                if (value <= 20) {
                                    if (value < 1) {
                                        this.chat("CPS can't lower than 1.");
                                        return;
                                    }
                                    if ((int)Trigger.this.minCPS.asObject() > value) {
                                        this.chat("MinCPS can't higher as MaxCPS!");
                                        return;
                                    }
                                    Trigger.this.maxCPS.setValue(value);
                                    this.chat("§7Trigger maxCPS was set to §8" + value + "§7.");
                                    Trigger$3.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                }
                                else {
                                    this.chat("CPS can't higher as 20.");
                                }
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".trigger maxcps <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("mincps")) {
                        if (args.length > 2) {
                            try {
                                final int value = Integer.parseInt(args[2]);
                                if (value <= 20) {
                                    if (value < 1) {
                                        this.chat("CPS can't lower than 1.");
                                        return;
                                    }
                                    if ((int)Trigger.this.maxCPS.asObject() < value) {
                                        this.chat("MinCPS can't higher as MaxCPS!");
                                        return;
                                    }
                                    Trigger.this.minCPS.setValue(value);
                                    this.chat("§7Trigger MinCPS was set to §8" + value + "§7.");
                                    Trigger$3.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                }
                                else {
                                    this.chat("CPS can't higher as 20.");
                                }
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".trigger mincps <value>");
                        return;
                    }
                }
                this.chatSyntax(".trigger <mincps, maxcps>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (Trigger.mc.field_71476_x != null && System.currentTimeMillis() - this.lastSwing >= this.delay && EntityUtils.isSelected(Trigger.mc.field_71476_x.field_72308_g, true)) {
            Trigger.mc.func_147116_af();
            this.lastSwing = System.currentTimeMillis();
            this.delay = TimeUtils.randomClickDelay((int)this.minCPS.asObject(), (int)this.maxCPS.asObject());
        }
    }
}
