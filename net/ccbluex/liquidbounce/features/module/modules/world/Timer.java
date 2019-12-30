// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.event.events.WorldEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Timer", description = "Changes the speed of the entire game.", category = ModuleCategory.WORLD)
public class Timer extends Module
{
    private final FloatValue speedValue;
    
    public Timer() {
        this.speedValue = new FloatValue("Speed", 2.0f, 0.1f, 10.0f);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("timer", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("speed")) {
                    this.chatSyntax(".timer <speed>");
                    return;
                }
                if (args.length > 2) {
                    try {
                        final float speed = Float.parseFloat(args[2]);
                        if (speed <= 0.0f) {
                            this.chat("The Speed must be higher than 0.");
                            return;
                        }
                        Timer.this.speedValue.setValue(speed);
                        this.chat("§7Timer speed was set to §8" + speed + "§7.");
                        Timer$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                    }
                    catch (NumberFormatException exception) {
                        this.chatSyntaxError();
                    }
                    return;
                }
                this.chatSyntax(".timer speed <value>");
            }
        });
    }
    
    @Override
    public void onDisable() {
        if (Timer.mc.field_71439_g == null) {
            return;
        }
        Timer.mc.field_71428_T.field_74278_d = 1.0f;
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        Timer.mc.field_71428_T.field_74278_d = this.speedValue.asFloat();
    }
    
    @EventTarget
    public void onWorld(final WorldEvent event) {
        if (event.getWorldClient() != null) {
            return;
        }
        this.setState(false);
    }
}
