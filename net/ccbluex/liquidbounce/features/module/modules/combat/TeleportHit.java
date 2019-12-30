// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.combat;

import javax.vecmath.Vector3d;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.util.Vec3;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.utils.PathUtils;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.RaycastUtils;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.minecraft.entity.EntityLivingBase;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "TeleportHit", description = "Allows to hit entities from far away.", category = ModuleCategory.COMBAT)
public class TeleportHit extends Module
{
    private EntityLivingBase targetEntity;
    private boolean shouldHit;
    
    @EventTarget
    public void onMotion(final MotionEvent event) {
        if (event.getEventState() != EventState.PRE) {
            return;
        }
        final Entity facedEntity = RaycastUtils.raycastEntity(100.0, raycastedEntity -> raycastedEntity instanceof EntityLivingBase);
        if (TeleportHit.mc.field_71474_y.field_74312_F.func_151470_d() && EntityUtils.isSelected(facedEntity, true) && facedEntity.func_70068_e((Entity)TeleportHit.mc.field_71439_g) >= 1.0) {
            this.targetEntity = (EntityLivingBase)facedEntity;
        }
        if (this.targetEntity != null) {
            if (!this.shouldHit) {
                this.shouldHit = true;
                return;
            }
            if (TeleportHit.mc.field_71439_g.field_70143_R > 0.0f) {
                final Vec3 rotationVector = RotationUtils.getVectorForRotation(0.0f, TeleportHit.mc.field_71439_g.field_70177_z);
                final double x = TeleportHit.mc.field_71439_g.field_70165_t + rotationVector.field_72450_a * (TeleportHit.mc.field_71439_g.func_70032_d((Entity)this.targetEntity) - 1.0f);
                final double z = TeleportHit.mc.field_71439_g.field_70161_v + rotationVector.field_72449_c * (TeleportHit.mc.field_71439_g.func_70032_d((Entity)this.targetEntity) - 1.0f);
                final double y = this.targetEntity.func_180425_c().func_177956_o() + 0.25;
                PathUtils.findPath(x, y + 1.0, z, 4.0).forEach(pos -> TeleportHit.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(pos.getX(), pos.getY(), pos.getZ(), false)));
                TeleportHit.mc.field_71439_g.func_71038_i();
                TeleportHit.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C02PacketUseEntity((Entity)this.targetEntity, C02PacketUseEntity.Action.ATTACK));
                TeleportHit.mc.field_71439_g.func_71009_b((Entity)this.targetEntity);
                this.shouldHit = false;
                this.targetEntity = null;
            }
            else if (TeleportHit.mc.field_71439_g.field_70122_E) {
                TeleportHit.mc.field_71439_g.func_70664_aZ();
            }
        }
        else {
            this.shouldHit = false;
        }
    }
}
