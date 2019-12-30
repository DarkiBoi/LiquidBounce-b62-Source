// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "FastStairs", description = "Allows you to climb up stairs faster.", category = ModuleCategory.MOVEMENT)
public class FastStairs extends Module
{
    private final ListValue modeValue;
    private final BoolValue longJumpValue;
    private boolean canJump;
    
    public FastStairs() {
        this.modeValue = new ListValue("Mode", new String[] { "NCP", "AAC", "LAAC" }, "NCP");
        this.longJumpValue = new BoolValue("LongJump", false);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("faststairs", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("mode")) {
                    this.chatSyntax(".faststairs <mode>");
                    return;
                }
                if (args.length > 2 && FastStairs.this.modeValue.contains(args[2])) {
                    FastStairs.this.modeValue.setValue(args[2].toLowerCase());
                    this.chat("§7FastStairs mode was set to §8" + FastStairs.this.modeValue.asString().toUpperCase() + "§7.");
                    FastStairs$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                    return;
                }
                this.chatSyntax(".faststairs mode §c<§8" + Strings.join(FastStairs.this.modeValue.getValues(), "§7, §8") + "§c>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (FastStairs.mc.field_71439_g == null || ModuleManager.getModule(Speed.class).getState()) {
            return;
        }
        final BlockPos blockPos = new BlockPos(FastStairs.mc.field_71439_g.field_70165_t, FastStairs.mc.field_71439_g.func_174813_aQ().field_72338_b, FastStairs.mc.field_71439_g.field_70161_v);
        if (FastStairs.mc.field_71439_g.field_70122_E && FastStairs.mc.field_71439_g.field_71158_b.field_78900_b > 0.0) {
            final String mode = this.modeValue.asString();
            if (BlockUtils.getBlock(blockPos) instanceof BlockStairs) {
                FastStairs.mc.field_71439_g.func_70107_b(FastStairs.mc.field_71439_g.field_70165_t, FastStairs.mc.field_71439_g.field_70163_u + 0.5, FastStairs.mc.field_71439_g.field_70161_v);
                final EntityPlayerSP field_71439_g = FastStairs.mc.field_71439_g;
                field_71439_g.field_70159_w *= (mode.equalsIgnoreCase("NCP") ? 1.4 : (mode.equalsIgnoreCase("AAC") ? 1.5 : (mode.equalsIgnoreCase("AAC") ? 1.499 : 1.0)));
                final EntityPlayerSP field_71439_g2 = FastStairs.mc.field_71439_g;
                field_71439_g2.field_70179_y *= (mode.equalsIgnoreCase("NCP") ? 1.4 : (mode.equalsIgnoreCase("AAC") ? 1.5 : (mode.equalsIgnoreCase("AAC") ? 1.499 : 1.0)));
            }
            if (BlockUtils.getBlock(blockPos.func_177977_b()) instanceof BlockStairs) {
                final EntityPlayerSP field_71439_g3 = FastStairs.mc.field_71439_g;
                field_71439_g3.field_70159_w *= 1.3;
                final EntityPlayerSP field_71439_g4 = FastStairs.mc.field_71439_g;
                field_71439_g4.field_70179_y *= 1.3;
                if (mode.equalsIgnoreCase("LAAC")) {
                    final EntityPlayerSP field_71439_g5 = FastStairs.mc.field_71439_g;
                    field_71439_g5.field_70159_w *= 1.18;
                    final EntityPlayerSP field_71439_g6 = FastStairs.mc.field_71439_g;
                    field_71439_g6.field_70179_y *= 1.18;
                }
                this.canJump = true;
            }
            else if ((mode.equalsIgnoreCase("LAAC") || mode.equalsIgnoreCase("AAC")) && FastStairs.mc.field_71439_g.field_70122_E && this.canJump) {
                if (this.longJumpValue.asBoolean()) {
                    FastStairs.mc.field_71439_g.func_70664_aZ();
                    final EntityPlayerSP field_71439_g7 = FastStairs.mc.field_71439_g;
                    field_71439_g7.field_70159_w *= 1.35;
                    final EntityPlayerSP field_71439_g8 = FastStairs.mc.field_71439_g;
                    field_71439_g8.field_70179_y *= 1.35;
                }
                this.canJump = false;
            }
        }
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
