// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.world;

import net.minecraft.util.MovingObjectPosition;
import java.util.function.Function;
import java.util.Map;
import java.util.Comparator;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoTool;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.util.Vec3;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.block.Block;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.BlockValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Fucker", description = "Destroys selected blocks around you. (aka.  IDNuker)", category = ModuleCategory.WORLD)
public class Fucker extends Module
{
    private final BlockValue blockValue;
    private final BoolValue instantValue;
    private final ListValue throughWallsValue;
    private final BoolValue swingValue;
    private final BoolValue rotationsValue;
    private final BoolValue surroundingsValue;
    private final ListValue actionValue;
    private final BoolValue noHitValue;
    private BlockPos pos;
    private BlockPos oldPos;
    static float currentDamage;
    private int blockHitDelay;
    private final MSTimer switchTimer;
    
    public Fucker() {
        this.blockValue = new BlockValue("id", 26);
        this.instantValue = new BoolValue("Instant", false);
        this.throughWallsValue = new ListValue("ThroughWalls", new String[] { "None", "Raycast", "Around" }, "None");
        this.swingValue = new BoolValue("Swing", true);
        this.rotationsValue = new BoolValue("Rotations", true);
        this.surroundingsValue = new BoolValue("Surroundings", true);
        this.actionValue = new ListValue("Action", new String[] { "Destroy", "Use" }, "Destroy");
        this.noHitValue = new BoolValue("NoHit", false);
        this.switchTimer = new MSTimer();
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("fucker", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("select")) {
                        if (args.length > 2) {
                            int id;
                            try {
                                id = Integer.parseInt(args[2]);
                            }
                            catch (NumberFormatException exception) {
                                id = Block.func_149682_b(Block.func_149684_b(args[2]));
                            }
                            if (id != 0) {
                                Fucker.this.blockValue.setValue(id);
                                this.chat("§aThe block was set to " + BlockUtils.getBlockName(id) + ".");
                                Fucker$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            else {
                                this.chat("Block was not found or the block is air.");
                            }
                            return;
                        }
                        this.chat("§7Current: §8" + BlockUtils.getBlockName(Fucker.this.blockValue.asInteger()));
                        this.chatSyntax(".fucker select <name/id>");
                        return;
                    }
                    else {
                        if (args[1].equalsIgnoreCase("instant")) {
                            Fucker.this.instantValue.setValue(!Fucker.this.instantValue.asBoolean());
                            this.chat("§7Fucker instant was toggled §8" + (Fucker.this.instantValue.asBoolean() ? "on" : "off") + "§7.");
                            Fucker$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        if (args[1].equalsIgnoreCase("throughwalls")) {
                            if (args.length > 2 && Fucker.this.throughWallsValue.contains(args[2])) {
                                Fucker.this.throughWallsValue.setValue(args[2].toLowerCase());
                                this.chat("§7Fucker throughwalls was set to §8" + Fucker.this.throughWallsValue.asString().toUpperCase() + "§7.");
                                Fucker$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                return;
                            }
                            this.chatSyntax(".fucker throughwalls §c<§8" + Strings.join(Fucker.this.throughWallsValue.getValues(), "§7, §8") + "§c>");
                            return;
                        }
                        else {
                            if (args[1].equalsIgnoreCase("Surroundings")) {
                                Fucker.this.surroundingsValue.setValue(!Fucker.this.surroundingsValue.asBoolean());
                                this.chat("§7Fucker Surroundings was toggled §8" + (Fucker.this.surroundingsValue.asBoolean() ? "on" : "off") + "§7.");
                                Fucker$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                return;
                            }
                            if (args[1].equalsIgnoreCase("nohit")) {
                                Fucker.this.noHitValue.setValue(!Fucker.this.noHitValue.asBoolean());
                                this.chat("§7Fucker NoHit was toggled §8" + (Fucker.this.noHitValue.asBoolean() ? "on" : "off") + "§7.");
                                Fucker$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                return;
                            }
                        }
                    }
                }
                this.chatSyntax(".fucker <select/instant/throughwalls/surroundings/nohit>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (this.noHitValue.asBoolean()) {
            final KillAura killAura = (KillAura)ModuleManager.getModule(KillAura.class);
            if (killAura.getState() && killAura.target != null) {
                return;
            }
        }
        final int targetId = this.blockValue.asInteger();
        if (this.pos == null || Block.func_149682_b(BlockUtils.getBlock(this.pos)) != targetId || Fucker.mc.field_71439_g.func_174818_b(this.pos) >= 22.399999618530273) {
            this.pos = this.find(targetId);
        }
        boolean surroundings = false;
        if (this.pos != null && this.surroundingsValue.asBoolean()) {
            final double diffX = this.pos.func_177958_n() + 0.5 - Fucker.mc.field_71439_g.field_70165_t;
            final double diffY = this.pos.func_177956_o() + 0.5 - (Fucker.mc.field_71439_g.func_174813_aQ().field_72338_b + Fucker.mc.field_71439_g.func_70047_e());
            final double diffZ = this.pos.func_177952_p() + 0.5 - Fucker.mc.field_71439_g.field_70161_v;
            final double dist = MathHelper.func_76133_a(diffX * diffX + diffZ * diffZ);
            final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
            final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
            final float f = MathHelper.func_76134_b(-yaw * 0.017453292f - 3.1415927f);
            final float f2 = MathHelper.func_76126_a(-yaw * 0.017453292f - 3.1415927f);
            final float f3 = -MathHelper.func_76134_b(-pitch * 0.017453292f);
            final float f4 = MathHelper.func_76126_a(-pitch * 0.017453292f);
            final Vec3 vec31 = new Vec3((double)(f2 * f3), (double)f4, (double)(f * f3));
            final Vec3 vec32 = Fucker.mc.field_71439_g.func_174824_e(1.0f);
            final Vec3 vec33 = vec32.func_72441_c(vec31.field_72450_a * Fucker.mc.field_71442_b.func_78757_d(), vec31.field_72448_b * Fucker.mc.field_71442_b.func_78757_d(), vec31.field_72449_c * Fucker.mc.field_71442_b.func_78757_d());
            final BlockPos blockPos = Fucker.mc.field_71441_e.func_147447_a(vec32, vec33, false, false, true).func_178782_a();
            if (blockPos != null) {
                if (this.pos.func_177958_n() != blockPos.func_177958_n() || this.pos.func_177956_o() != blockPos.func_177956_o() || this.pos.func_177952_p() != blockPos.func_177952_p()) {
                    surroundings = true;
                }
                this.pos = blockPos;
            }
        }
        if (this.pos == null) {
            Fucker.currentDamage = 0.0f;
            return;
        }
        if (this.oldPos != null && !this.oldPos.equals((Object)this.pos)) {
            this.switchTimer.reset();
        }
        this.oldPos = this.pos;
        if (this.blockHitDelay > 0) {
            --this.blockHitDelay;
            return;
        }
        if (!this.switchTimer.hasTimePassed(250L)) {
            return;
        }
        if (this.rotationsValue.asBoolean()) {
            RotationUtils.faceBlockPacket(this.pos);
        }
        final String s = surroundings ? "destroy" : this.actionValue.asString().toLowerCase();
        switch (s) {
            case "destroy": {
                final AutoTool autoTool = (AutoTool)ModuleManager.getModule(AutoTool.class);
                if (autoTool != null && autoTool.getState()) {
                    autoTool.switchSlot(this.pos);
                }
                if (this.instantValue.asBoolean()) {
                    Fucker.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.pos, EnumFacing.DOWN));
                    Fucker.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.pos, EnumFacing.DOWN));
                    Fucker.currentDamage = 0.0f;
                    this.pos = null;
                    break;
                }
                if (Fucker.currentDamage == 0.0f) {
                    Fucker.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.pos, EnumFacing.DOWN));
                    if (Fucker.mc.field_71439_g.field_71075_bZ.field_75098_d || BlockUtils.getBlock(this.pos).func_180647_a((EntityPlayer)Fucker.mc.field_71439_g, (World)Fucker.mc.field_71441_e, this.pos) >= 1.0f) {
                        Fucker.currentDamage = 0.0f;
                        if (this.swingValue.asBoolean()) {
                            Fucker.mc.field_71439_g.func_71038_i();
                        }
                        Fucker.mc.field_71442_b.func_178888_a(this.pos, EnumFacing.DOWN);
                        this.pos = null;
                        return;
                    }
                }
                if (this.swingValue.asBoolean()) {
                    Fucker.mc.field_71439_g.func_71038_i();
                }
                Fucker.currentDamage += BlockUtils.getBlock(this.pos).func_180647_a((EntityPlayer)Fucker.mc.field_71439_g, (World)Fucker.mc.field_71441_e, this.pos);
                Fucker.mc.field_71441_e.func_175715_c(Fucker.mc.field_71439_g.func_145782_y(), this.pos, (int)(Fucker.currentDamage * 10.0f) - 1);
                if (Fucker.currentDamage >= 1.0f) {
                    Fucker.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.pos, EnumFacing.DOWN));
                    Fucker.mc.field_71442_b.func_178888_a(this.pos, EnumFacing.DOWN);
                    this.blockHitDelay = 4;
                    Fucker.currentDamage = 0.0f;
                    this.pos = null;
                    break;
                }
                break;
            }
            case "use": {
                if (Fucker.mc.field_71442_b.func_178890_a(Fucker.mc.field_71439_g, Fucker.mc.field_71441_e, Fucker.mc.field_71439_g.func_70694_bm(), this.pos, EnumFacing.DOWN, new Vec3((double)this.pos.func_177958_n(), (double)this.pos.func_177956_o(), (double)this.pos.func_177952_p()))) {
                    if (this.swingValue.asBoolean()) {
                        Fucker.mc.field_71439_g.func_71038_i();
                    }
                    this.blockHitDelay = 4;
                    Fucker.currentDamage = 0.0f;
                    this.pos = null;
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (this.pos != null) {
            RenderUtils.drawBlockBox(this.pos, Color.RED, true);
        }
    }
    
    private BlockPos find(final int targetID) {
        return BlockUtils.searchBlocks(4).entrySet().stream().filter(entry -> Block.func_149682_b((Block)entry.getValue()) == targetID && Fucker.mc.field_71439_g.func_174818_b((BlockPos)entry.getKey()) < 22.3 && (this.isHitable((BlockPos)entry.getKey()) || this.surroundingsValue.asBoolean())).min(Comparator.comparingDouble(value -> BlockUtils.getCenterDistance(value.getKey()))).map((Function<? super Object, ? extends BlockPos>)Map.Entry::getKey).orElse(null);
    }
    
    @Override
    public String getTag() {
        return BlockUtils.getBlockName(this.blockValue.asInteger());
    }
    
    private boolean isHitable(final BlockPos blockPos) {
        final String lowerCase = this.throughWallsValue.asString().toLowerCase();
        switch (lowerCase) {
            case "none": {
                return true;
            }
            case "raycast": {
                final Vec3 eyesPos = RotationUtils.getEyesPos();
                final MovingObjectPosition movingObjectPosition = Fucker.mc.field_71441_e.func_147447_a(eyesPos, new Vec3(blockPos.func_177958_n() + 0.5, blockPos.func_177956_o() + 0.5, blockPos.func_177952_p() + 0.5), false, true, false);
                return movingObjectPosition != null && movingObjectPosition.func_178782_a().equals((Object)blockPos);
            }
            case "around": {
                return !BlockUtils.isFullBlock(blockPos.func_177977_b()) || !BlockUtils.isFullBlock(blockPos.func_177984_a()) || !BlockUtils.isFullBlock(blockPos.func_177978_c()) || !BlockUtils.isFullBlock(blockPos.func_177974_f()) || !BlockUtils.isFullBlock(blockPos.func_177968_d()) || !BlockUtils.isFullBlock(blockPos.func_177976_e());
            }
            default: {
                return true;
            }
        }
    }
}
