// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils;

import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.AxisAlignedBB;
import java.util.Iterator;
import java.util.List;
import com.google.common.base.Predicates;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.Vec3;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;

public final class RaycastUtils
{
    private static final Minecraft mc;
    
    public static Entity raycastEntity(final double range) {
        return raycastEntity(range, entity -> true);
    }
    
    public static Entity raycastEntity(final double range, final IEntityFilter entityFilter) {
        return raycastEntity(range, RotationUtils.lastLook[0], RotationUtils.lastLook[1], entityFilter);
    }
    
    public static Entity raycastEntity(final double range, final float yaw, final float pitch, final IEntityFilter entityFilter) {
        final Entity renderViewEntity = RaycastUtils.mc.func_175606_aa();
        if (renderViewEntity != null && RaycastUtils.mc.field_71441_e != null) {
            double blockReachDistance = range;
            final Vec3 eyePosition = renderViewEntity.func_174824_e(1.0f);
            final float yawCos = MathHelper.func_76134_b(-yaw * 0.017453292f - 3.1415927f);
            final float yawSin = MathHelper.func_76126_a(-yaw * 0.017453292f - 3.1415927f);
            final float pitchCos = -MathHelper.func_76134_b(-pitch * 0.017453292f);
            final float pitchSin = MathHelper.func_76126_a(-pitch * 0.017453292f);
            final Vec3 entityLook = new Vec3((double)(yawSin * pitchCos), (double)pitchSin, (double)(yawCos * pitchCos));
            final Vec3 vector = eyePosition.func_72441_c(entityLook.field_72450_a * blockReachDistance, entityLook.field_72448_b * blockReachDistance, entityLook.field_72449_c * blockReachDistance);
            final List<Entity> entityList = (List<Entity>)RaycastUtils.mc.field_71441_e.func_175674_a(renderViewEntity, renderViewEntity.func_174813_aQ().func_72321_a(entityLook.field_72450_a * blockReachDistance, entityLook.field_72448_b * blockReachDistance, entityLook.field_72449_c * blockReachDistance).func_72314_b(1.0, 1.0, 1.0), Predicates.and(EntitySelectors.field_180132_d, Entity::func_70067_L));
            Entity pointedEntity = null;
            for (final Entity entity : entityList) {
                if (!entityFilter.canRaycast(entity)) {
                    continue;
                }
                final float collisionBorderSize = entity.func_70111_Y();
                final AxisAlignedBB axisalignedbb = entity.func_174813_aQ().func_72314_b((double)collisionBorderSize, (double)collisionBorderSize, (double)collisionBorderSize);
                final MovingObjectPosition movingObjectPosition = axisalignedbb.func_72327_a(eyePosition, vector);
                if (axisalignedbb.func_72318_a(eyePosition)) {
                    if (blockReachDistance < 0.0) {
                        continue;
                    }
                    pointedEntity = entity;
                    blockReachDistance = 0.0;
                }
                else {
                    if (movingObjectPosition == null) {
                        continue;
                    }
                    final double d3 = eyePosition.func_72438_d(movingObjectPosition.field_72307_f);
                    if (d3 >= blockReachDistance && blockReachDistance != 0.0) {
                        continue;
                    }
                    if (entity == renderViewEntity.field_70154_o && !renderViewEntity.canRiderInteract()) {
                        if (blockReachDistance != 0.0) {
                            continue;
                        }
                        pointedEntity = entity;
                    }
                    else {
                        pointedEntity = entity;
                        blockReachDistance = d3;
                    }
                }
            }
            return pointedEntity;
        }
        return null;
    }
    
    public static Collection<Entity> raycastEntities(final double range) {
        final Collection<Entity> entities = new ArrayList<Entity>();
        final Entity renderViewEntity = RaycastUtils.mc.func_175606_aa();
        if (renderViewEntity != null && RaycastUtils.mc.field_71441_e != null) {
            final Vec3 eyePosition = renderViewEntity.func_174824_e(1.0f);
            final float yaw = RotationUtils.lastLook[0];
            final float pitch = RotationUtils.lastLook[1];
            final float yawCos = MathHelper.func_76134_b(-yaw * 0.017453292f - 3.1415927f);
            final float yawSin = MathHelper.func_76126_a(-yaw * 0.017453292f - 3.1415927f);
            final float pitchCos = -MathHelper.func_76134_b(-pitch * 0.017453292f);
            final float pitchSin = MathHelper.func_76126_a(-pitch * 0.017453292f);
            final Vec3 entityLook = new Vec3((double)(yawSin * pitchCos), (double)pitchSin, (double)(yawCos * pitchCos));
            final Vec3 vector = eyePosition.func_72441_c(entityLook.field_72450_a * range, entityLook.field_72448_b * range, entityLook.field_72449_c * range);
            final List<Entity> entityList = (List<Entity>)RaycastUtils.mc.field_71441_e.func_175674_a(renderViewEntity, renderViewEntity.func_174813_aQ().func_72321_a(entityLook.field_72450_a * range, entityLook.field_72448_b * range, entityLook.field_72449_c * range).func_72314_b(1.0, 1.0, 1.0), Predicates.and(EntitySelectors.field_180132_d, Entity::func_70067_L));
            for (final Entity entity : entityList) {
                final float collisionBorderSize = entity.func_70111_Y();
                final AxisAlignedBB axisalignedbb = entity.func_174813_aQ().func_72314_b((double)collisionBorderSize, (double)collisionBorderSize, (double)collisionBorderSize);
                final MovingObjectPosition movingObjectPosition = axisalignedbb.func_72327_a(eyePosition, vector);
                if (axisalignedbb.func_72318_a(eyePosition)) {
                    if (range < 0.0) {
                        continue;
                    }
                    entities.add(entity);
                }
                else {
                    if (movingObjectPosition == null) {
                        continue;
                    }
                    final double d3 = eyePosition.func_72438_d(movingObjectPosition.field_72307_f);
                    if (d3 >= range && range != 0.0) {
                        continue;
                    }
                    if (entity == renderViewEntity.field_70154_o && !renderViewEntity.canRiderInteract()) {
                        if (range != 0.0) {
                            continue;
                        }
                        entities.add(entity);
                    }
                    else {
                        entities.add(entity);
                    }
                }
            }
        }
        return entities;
    }
    
    static {
        mc = Minecraft.func_71410_x();
    }
    
    public interface IEntityFilter
    {
        boolean canRaycast(final Entity p0);
    }
}
