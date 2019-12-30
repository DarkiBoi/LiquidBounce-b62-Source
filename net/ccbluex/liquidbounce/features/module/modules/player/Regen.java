// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.player;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Regen", description = "Regenerates you health much faster.", category = ModuleCategory.PLAYER)
public class Regen extends Module
{
    private final ListValue modeValue;
    private final IntegerValue healthValue;
    private final IntegerValue foodValue;
    private final IntegerValue speedValue;
    private final BoolValue noAirValue;
    private boolean resetTimer;
    
    public Regen() {
        this.modeValue = new ListValue("Mode", new String[] { "Vanilla", "Spartan" }, "Vanilla");
        this.healthValue = new IntegerValue("Health", 18, 0, 20);
        this.foodValue = new IntegerValue("Food", 18, 0, 20);
        this.speedValue = new IntegerValue("Speed", 100, 1, 100);
        this.noAirValue = new BoolValue("NoAir", false);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("regen", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("health")) {
                        if (args.length > 2) {
                            try {
                                final int i = Integer.parseInt(args[2]);
                                Regen.this.healthValue.setValue(i);
                                this.chat("§7Regen health was set to §8" + i + "§7.");
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".regen health <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("food")) {
                        if (args.length > 2) {
                            try {
                                final int i = Integer.parseInt(args[2]);
                                Regen.this.foodValue.setValue(i);
                                this.chat("§7Regen food was set to §8" + i + "§7.");
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".regen food <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("speed")) {
                        if (args.length > 2) {
                            try {
                                final int speed = Integer.parseInt(args[2]);
                                if (speed <= 100) {
                                    if (speed >= 1) {
                                        Regen.this.speedValue.setValue(speed);
                                        this.chat("§7Regen speed was set to §8" + speed + "§7.");
                                    }
                                    else {
                                        this.chat("You cant use a speed under 1.");
                                    }
                                }
                                else {
                                    this.chat("You reached the max speed. Use a speed under 100.");
                                }
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".regen speed <value>");
                        return;
                    }
                }
                this.chatSyntax(".regen <health/food/speed>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        final int speed = this.speedValue.asInteger();
        if (this.resetTimer) {
            Regen.mc.field_71428_T.field_74278_d = 1.0f;
        }
        if ((!this.noAirValue.asBoolean() || Regen.mc.field_71439_g.field_70122_E) && !Regen.mc.field_71439_g.field_71075_bZ.field_75098_d && Regen.mc.field_71439_g.func_71024_bL().func_75116_a() > this.foodValue.asInteger() && Regen.mc.field_71439_g.func_70089_S() && Regen.mc.field_71439_g.func_110143_aJ() < this.healthValue.asInteger()) {
            final String lowerCase = this.modeValue.asString().toLowerCase();
            switch (lowerCase) {
                case "vanilla": {
                    for (int i = 0; i < speed; ++i) {
                        Regen.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer());
                    }
                    break;
                }
                case "spartan": {
                    if (MovementUtils.isMoving()) {
                        break;
                    }
                    if (!Regen.mc.field_71439_g.field_70122_E) {
                        break;
                    }
                    for (int i = 0; i < 9; ++i) {
                        Regen.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer());
                    }
                    Regen.mc.field_71428_T.field_74278_d = 0.45f;
                    this.resetTimer = true;
                    break;
                }
            }
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
}
