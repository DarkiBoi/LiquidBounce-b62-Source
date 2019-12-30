// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.utils.ChatUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class MineplexGround extends SpeedMode
{
    private boolean spoofSlot;
    private float speed;
    
    public MineplexGround() {
        super("MineplexGround");
        this.speed = 0.0f;
    }
    
    @Override
    public void onMotion() {
        if (!MovementUtils.isMoving() || !MineplexGround.mc.field_71439_g.field_70122_E || MineplexGround.mc.field_71439_g.field_71071_by.func_70448_g() == null || MineplexGround.mc.field_71439_g.func_71039_bw()) {
            return;
        }
        this.spoofSlot = false;
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = MineplexGround.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (itemStack == null) {
                MineplexGround.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(i - 36));
                this.spoofSlot = true;
                break;
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (!MovementUtils.isMoving() || !MineplexGround.mc.field_71439_g.field_70122_E || MineplexGround.mc.field_71439_g.func_71039_bw()) {
            this.speed = 0.0f;
            return;
        }
        if (!this.spoofSlot && MineplexGround.mc.field_71439_g.field_71071_by.func_70448_g() != null) {
            ChatUtils.displayChatMessage("§8[§c§lMineplex§aSpeed§8] §cYou need one empty slot.");
            return;
        }
        final BlockPos blockPos = new BlockPos(MineplexGround.mc.field_71439_g.field_70165_t, MineplexGround.mc.field_71439_g.func_174813_aQ().field_72338_b - 1.0, MineplexGround.mc.field_71439_g.field_70161_v);
        final Vec3 vec = new Vec3((Vec3i)blockPos).func_72441_c(0.4000000059604645, 0.4000000059604645, 0.4000000059604645).func_178787_e(new Vec3(EnumFacing.UP.func_176730_m()));
        MineplexGround.mc.field_71442_b.func_178890_a(MineplexGround.mc.field_71439_g, MineplexGround.mc.field_71441_e, (ItemStack)null, blockPos, EnumFacing.UP, new Vec3(vec.field_72450_a * 0.4000000059604645, vec.field_72448_b * 0.4000000059604645, vec.field_72449_c * 0.4000000059604645));
        final float targetSpeed = ((Speed)ModuleManager.getModule(Speed.class)).mineplexGroundSpeedValue.asFloat();
        if (targetSpeed > this.speed) {
            this.speed += targetSpeed / 8.0f;
        }
        if (this.speed >= targetSpeed) {
            this.speed = targetSpeed;
        }
        MovementUtils.strafe(this.speed);
        if (!this.spoofSlot) {
            MineplexGround.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(MineplexGround.mc.field_71439_g.field_71071_by.field_70461_c));
        }
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
    
    @Override
    public void onDisable() {
        this.speed = 0.0f;
        MineplexGround.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(MineplexGround.mc.field_71439_g.field_71071_by.field_70461_c));
    }
}
