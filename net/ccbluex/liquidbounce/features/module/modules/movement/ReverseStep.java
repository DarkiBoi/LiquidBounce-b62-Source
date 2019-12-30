// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.minecraft.block.Block;
import net.ccbluex.liquidbounce.event.events.JumpEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.util.AxisAlignedBB;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.block.BlockLiquid;
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

@ModuleInfo(name = "ReverseStep", description = "Allows you to step down blocks faster.", category = ModuleCategory.MOVEMENT)
public class ReverseStep extends Module
{
    private final FloatValue motionValue;
    private boolean jumped;
    
    public ReverseStep() {
        this.motionValue = new FloatValue("Motion", 1.0f, 0.21f, 1.0f);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("reversestep", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("motion")) {
                    this.chatSyntax(".reversestep <motion>");
                    return;
                }
                if (args.length > 2) {
                    try {
                        final float motion = Float.parseFloat(args[2]);
                        ReverseStep.this.motionValue.setValue(motion);
                        this.chat("§7ReverseStep motion was set to §8" + motion + "§7.");
                        ReverseStep$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                    }
                    catch (NumberFormatException exception) {
                        this.chatSyntaxError();
                    }
                    return;
                }
                this.chatSyntax(".reversestep motion <value>");
            }
        });
    }
    
    @EventTarget(ignoreCondition = true)
    public void onUpdate(final UpdateEvent event) {
        if (ReverseStep.mc.field_71439_g.field_70122_E) {
            this.jumped = false;
        }
        if (ReverseStep.mc.field_71439_g.field_70181_x > 0.0) {
            this.jumped = true;
        }
        if (!this.getState()) {
            return;
        }
        if (BlockUtils.collideBlock(ReverseStep.mc.field_71439_g.func_174813_aQ(), block -> block instanceof BlockLiquid) || BlockUtils.collideBlock(new AxisAlignedBB(ReverseStep.mc.field_71439_g.func_174813_aQ().field_72336_d, ReverseStep.mc.field_71439_g.func_174813_aQ().field_72337_e, ReverseStep.mc.field_71439_g.func_174813_aQ().field_72334_f, ReverseStep.mc.field_71439_g.func_174813_aQ().field_72340_a, ReverseStep.mc.field_71439_g.func_174813_aQ().field_72338_b - 0.01, ReverseStep.mc.field_71439_g.func_174813_aQ().field_72339_c), block -> block instanceof BlockLiquid)) {
            return;
        }
        if (!ReverseStep.mc.field_71474_y.field_74314_A.func_151470_d() && !ReverseStep.mc.field_71439_g.field_70122_E && !ReverseStep.mc.field_71439_g.field_71158_b.field_78901_c && ReverseStep.mc.field_71439_g.field_70181_x <= 0.0 && ReverseStep.mc.field_71439_g.field_70143_R <= 1.0f && !this.jumped) {
            ReverseStep.mc.field_71439_g.field_70181_x = -this.motionValue.asFloat();
        }
    }
    
    @EventTarget(ignoreCondition = true)
    public void onJump(final JumpEvent event) {
        this.jumped = true;
    }
}
