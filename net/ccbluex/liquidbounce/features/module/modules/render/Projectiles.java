// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import java.util.Iterator;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import org.lwjgl.util.glu.Cylinder;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.entity.Entity;
import java.util.ArrayList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemBow;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Projectiles", description = "Allows you to see where arrows will land.", category = ModuleCategory.RENDER)
public class Projectiles extends Module
{
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (Projectiles.mc.field_71439_g.func_70694_bm() == null) {
            return;
        }
        final Item item = Projectiles.mc.field_71439_g.func_70694_bm().func_77973_b();
        final RenderManager renderManager = Projectiles.mc.func_175598_ae();
        boolean isBow = false;
        float motionFactor = 1.5f;
        float motionSlowdown = 0.99f;
        float gravity;
        float size;
        if (item instanceof ItemBow) {
            isBow = true;
            gravity = 0.05f;
            size = 0.3f;
            float power = item.func_77626_a(new ItemStack(item)) / 20.0f;
            power = (power * power + power * 2.0f) / 3.0f;
            if (power < 0.1f) {
                return;
            }
            if (power > 1.0f) {
                power = 1.0f;
            }
            motionFactor = power * 3.0f;
        }
        else if (item instanceof ItemFishingRod) {
            gravity = 0.04f;
            size = 0.25f;
            motionSlowdown = 0.92f;
        }
        else if (item instanceof ItemPotion && ItemPotion.func_77831_g(Projectiles.mc.field_71439_g.func_70694_bm().func_77952_i())) {
            gravity = 0.05f;
            size = 0.25f;
            motionFactor = 0.5f;
        }
        else {
            if (!(item instanceof ItemSnowball) && !(item instanceof ItemEnderPearl) && !(item instanceof ItemEgg)) {
                return;
            }
            gravity = 0.03f;
            size = 0.25f;
        }
        final float yaw = RotationUtils.lookChanged ? RotationUtils.targetYaw : Projectiles.mc.field_71439_g.field_70177_z;
        final float pitch = RotationUtils.lookChanged ? RotationUtils.targetPitch : Projectiles.mc.field_71439_g.field_70125_A;
        double posX = renderManager.field_78725_b - MathHelper.func_76134_b(yaw / 180.0f * 3.1415927f) * 0.16f;
        double posY = renderManager.field_78726_c + Projectiles.mc.field_71439_g.func_70047_e() - 0.10000000149011612;
        double posZ = renderManager.field_78723_d - MathHelper.func_76126_a(yaw / 180.0f * 3.1415927f) * 0.16f;
        double motionX = -MathHelper.func_76126_a(yaw / 180.0f * 3.1415927f) * MathHelper.func_76134_b(pitch / 180.0f * 3.1415927f) * (isBow ? 1.0 : 0.4);
        double motionY = -MathHelper.func_76126_a((pitch + ((item instanceof ItemPotion && ItemPotion.func_77831_g(Projectiles.mc.field_71439_g.func_70694_bm().func_77952_i())) ? -20 : 0)) / 180.0f * 3.1415927f) * (isBow ? 1.0 : 0.4);
        double motionZ = MathHelper.func_76134_b(yaw / 180.0f * 3.1415927f) * MathHelper.func_76134_b(pitch / 180.0f * 3.1415927f) * (isBow ? 1.0 : 0.4);
        final float distance = MathHelper.func_76133_a(motionX * motionX + motionY * motionY + motionZ * motionZ);
        motionX /= distance;
        motionY /= distance;
        motionZ /= distance;
        motionX *= motionFactor;
        motionY *= motionFactor;
        motionZ *= motionFactor;
        MovingObjectPosition landingPosition = null;
        boolean hasLanded = false;
        boolean hitEntity = false;
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        RenderUtils.glColor(new Color(0, 160, 255, 255));
        GL11.glLineWidth(2.0f);
        GL11.glBegin(3);
        while (!hasLanded && posY > 0.0) {
            Vec3 posBefore = new Vec3(posX, posY, posZ);
            Vec3 posAfter = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
            landingPosition = Projectiles.mc.field_71441_e.func_147447_a(posBefore, posAfter, false, true, false);
            posBefore = new Vec3(posX, posY, posZ);
            posAfter = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
            if (landingPosition != null) {
                hasLanded = true;
                posAfter = new Vec3(landingPosition.field_72307_f.field_72450_a, landingPosition.field_72307_f.field_72448_b, landingPosition.field_72307_f.field_72449_c);
            }
            final AxisAlignedBB arrowBox = new AxisAlignedBB(posX - size, posY - size, posZ - size, posX + size, posY + size, posZ + size).func_72321_a(motionX, motionY, motionZ).func_72314_b(1.0, 1.0, 1.0);
            final int chunkMinX = MathHelper.func_76128_c((arrowBox.field_72340_a - 2.0) / 16.0);
            final int chunkMaxX = MathHelper.func_76128_c((arrowBox.field_72336_d + 2.0) / 16.0);
            final int chunkMinZ = MathHelper.func_76128_c((arrowBox.field_72339_c - 2.0) / 16.0);
            final int chunkMaxZ = MathHelper.func_76128_c((arrowBox.field_72334_f + 2.0) / 16.0);
            final List<Entity> collidedEntities = new ArrayList<Entity>();
            for (int x = chunkMinX; x <= chunkMaxX; ++x) {
                for (int z = chunkMinZ; z <= chunkMaxZ; ++z) {
                    Projectiles.mc.field_71441_e.func_72964_e(x, z).func_177414_a((Entity)Projectiles.mc.field_71439_g, arrowBox, (List)collidedEntities, (Predicate)null);
                }
            }
            for (final Entity possibleEntity : collidedEntities) {
                if (possibleEntity.func_70067_L() && possibleEntity != Projectiles.mc.field_71439_g) {
                    final AxisAlignedBB possibleEntityBoundingBox = possibleEntity.func_174813_aQ().func_72314_b((double)size, (double)size, (double)size);
                    final MovingObjectPosition possibleEntityLanding = possibleEntityBoundingBox.func_72327_a(posBefore, posAfter);
                    if (possibleEntityLanding == null) {
                        continue;
                    }
                    hitEntity = true;
                    hasLanded = true;
                    landingPosition = possibleEntityLanding;
                }
            }
            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            if (Projectiles.mc.field_71441_e.func_180495_p(new BlockPos(posX, posY, posZ)).func_177230_c().func_149688_o() == Material.field_151586_h) {
                motionX *= 0.6;
                motionY *= 0.6;
                motionZ *= 0.6;
            }
            else {
                motionX *= motionSlowdown;
                motionY *= motionSlowdown;
                motionZ *= motionSlowdown;
            }
            motionY -= gravity;
            GL11.glVertex3d(posX - renderManager.field_78725_b, posY - renderManager.field_78726_c, posZ - renderManager.field_78723_d);
        }
        GL11.glEnd();
        GL11.glPushMatrix();
        GL11.glTranslated(posX - renderManager.field_78725_b, posY - renderManager.field_78726_c, posZ - renderManager.field_78723_d);
        if (landingPosition != null) {
            switch (landingPosition.field_178784_b.func_176740_k().ordinal()) {
                case 0: {
                    GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
                    break;
                }
                case 2: {
                    GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
                    break;
                }
            }
            if (hitEntity) {
                RenderUtils.glColor(new Color(255, 0, 0, 150));
            }
        }
        GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
        final Cylinder cylinder = new Cylinder();
        cylinder.setDrawStyle(100011);
        cylinder.draw(0.2f, 0.0f, 0.0f, 60, 1);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDisable(2848);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
