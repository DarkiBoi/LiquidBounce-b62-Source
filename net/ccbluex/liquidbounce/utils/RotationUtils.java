// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.TickEvent;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.ccbluex.liquidbounce.event.EventListener;

@SideOnly(Side.CLIENT)
public final class RotationUtils implements EventListener
{
    private static Minecraft mc;
    public static boolean lookChanged;
    public static float targetYaw;
    public static float targetPitch;
    private static int keepLength;
    public static boolean keepRotation;
    public static float[] lastLook;
    private static double x;
    private static double y;
    private static double z;
    
    public static void faceBlockPacket(final BlockPos blockPos) {
        if (blockPos == null) {
            return;
        }
        final double diffX = blockPos.func_177958_n() + 0.5 - RotationUtils.mc.field_71439_g.field_70165_t;
        final double diffY = blockPos.func_177956_o() + 0.5 - (RotationUtils.mc.field_71439_g.func_174813_aQ().field_72338_b + RotationUtils.mc.field_71439_g.func_70047_e());
        final double diffZ = blockPos.func_177952_p() + 0.5 - RotationUtils.mc.field_71439_g.field_70161_v;
        final double sqrt = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, sqrt) * 180.0 / 3.141592653589793));
        setTargetRotation(RotationUtils.mc.field_71439_g.field_70177_z + MathHelper.func_76142_g(yaw - RotationUtils.mc.field_71439_g.field_70177_z), RotationUtils.mc.field_71439_g.field_70125_A + MathHelper.func_76142_g(pitch - RotationUtils.mc.field_71439_g.field_70125_A));
    }
    
    public static void faceBow(final Entity target, final boolean silent, final boolean predict, final float predictSize) {
        final EntityPlayerSP player = RotationUtils.mc.field_71439_g;
        final double posX = target.field_70165_t + (predict ? ((target.field_70165_t - target.field_70169_q) * predictSize) : 0.0) - (player.field_70165_t + (predict ? (player.field_70165_t - player.field_70169_q) : 0.0));
        final double posY = target.func_174813_aQ().field_72338_b + (predict ? ((target.func_174813_aQ().field_72338_b - target.field_70167_r) * predictSize) : 0.0) + target.func_70047_e() - 0.15 - (player.func_174813_aQ().field_72338_b + (predict ? (player.field_70163_u - player.field_70167_r) : 0.0)) - player.func_70047_e();
        final double posZ = target.field_70161_v + (predict ? ((target.field_70161_v - target.field_70166_s) * predictSize) : 0.0) - (player.field_70161_v + (predict ? (player.field_70161_v - player.field_70166_s) : 0.0));
        final double sqrt = Math.sqrt(posX * posX + posZ * posZ);
        float velocity = player.func_71052_bv() / 20.0f;
        velocity = (velocity * velocity + velocity * 2.0f) / 3.0f;
        if (velocity > 1.0f) {
            velocity = 1.0f;
        }
        float yaw = (float)(Math.atan2(posZ, posX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan((velocity * velocity - Math.sqrt(velocity * velocity * velocity * velocity - 0.006000000052154064 * (0.006000000052154064 * (sqrt * sqrt) + 2.0 * posY * (velocity * velocity)))) / (0.006000000052154064 * sqrt))));
        if (velocity < 0.1f) {
            final float[] rotations = getNeededRotations(getCenter(target.func_174813_aQ()), true);
            yaw = rotations[0];
            pitch = rotations[1];
        }
        if (silent) {
            setTargetRotation(yaw, pitch);
        }
        else {
            final float[] rotations = limitAngleChange(new float[] { player.field_70177_z, player.field_70125_A }, new float[] { yaw, pitch }, (float)(10 + RandomUtils.getRandom().nextInt(6)));
            if (rotations == null) {
                return;
            }
            player.field_70177_z = rotations[0];
            player.field_70125_A = rotations[1];
        }
    }
    
    public static float[] getTargetRotation(final Entity entity) {
        if (entity == null || RotationUtils.mc.field_71439_g == null) {
            return null;
        }
        return getNeededRotations(getRandomCenter(entity.func_174813_aQ(), false), true);
    }
    
    public static float[] getNeededRotations(final Vec3 vec, final boolean predict) {
        final Vec3 eyesPos = getEyesPos();
        if (predict) {
            eyesPos.func_72441_c(RotationUtils.mc.field_71439_g.field_70159_w, RotationUtils.mc.field_71439_g.field_70181_x, RotationUtils.mc.field_71439_g.field_70179_y);
        }
        final double diffX = vec.field_72450_a - eyesPos.field_72450_a;
        final double diffY = vec.field_72448_b - eyesPos.field_72448_b;
        final double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { MathHelper.func_76142_g(yaw), MathHelper.func_76142_g(pitch) };
    }
    
    public static Vec3 getCenter(final AxisAlignedBB bb) {
        return new Vec3(bb.field_72340_a + (bb.field_72336_d - bb.field_72340_a) * 0.5, bb.field_72338_b + (bb.field_72337_e - bb.field_72338_b) * 0.5, bb.field_72339_c + (bb.field_72334_f - bb.field_72339_c) * 0.5);
    }
    
    public static Vec3 getRandomCenter(final AxisAlignedBB bb, final boolean outborder) {
        if (outborder) {
            return new Vec3(bb.field_72340_a + (bb.field_72336_d - bb.field_72340_a) * (RotationUtils.x * 0.3 + 1.0), bb.field_72338_b + (bb.field_72337_e - bb.field_72338_b) * (RotationUtils.y * 0.3 + 1.0), bb.field_72339_c + (bb.field_72334_f - bb.field_72339_c) * (RotationUtils.z * 0.3 + 1.0));
        }
        return new Vec3(bb.field_72340_a + (bb.field_72336_d - bb.field_72340_a) * RotationUtils.x * 0.8, bb.field_72338_b + (bb.field_72337_e - bb.field_72338_b) * RotationUtils.y * 0.8, bb.field_72339_c + (bb.field_72334_f - bb.field_72339_c) * RotationUtils.z * 0.8);
    }
    
    public static double getRotationDifference(final Entity entity) {
        final float[] rotations = getTargetRotation(entity);
        if (rotations == null) {
            return 0.0;
        }
        return getRotationDifference(rotations[0], rotations[1]);
    }
    
    public static double getRotationDifference(final float yaw, final float pitch) {
        return Math.sqrt(Math.pow(Math.abs(angleDifference(RotationUtils.lastLook[0] % 360.0f, yaw)), 2.0) + Math.pow(Math.abs(angleDifference(RotationUtils.lastLook[1], pitch)), 2.0));
    }
    
    public static float[] limitAngleChange(final float[] current, final float[] target, final float turnSpeed) {
        final double yawDifference = angleDifference(target[0], current[0]);
        final double pitchDifference = angleDifference(target[1], current[1]);
        final int n = 0;
        current[n] += (float)((yawDifference > turnSpeed) ? turnSpeed : ((yawDifference < -turnSpeed) ? (-turnSpeed) : yawDifference));
        final int n2 = 1;
        current[n2] += (float)((pitchDifference > turnSpeed) ? turnSpeed : ((pitchDifference < -turnSpeed) ? (-turnSpeed) : pitchDifference));
        return current;
    }
    
    private static double angleDifference(final double a, final double b) {
        return ((a - b) % 360.0 + 540.0) % 360.0 - 180.0;
    }
    
    public static Vec3 getEyesPos() {
        return new Vec3(RotationUtils.mc.field_71439_g.field_70165_t, RotationUtils.mc.field_71439_g.func_174813_aQ().field_72338_b + RotationUtils.mc.field_71439_g.func_70047_e(), RotationUtils.mc.field_71439_g.field_70161_v);
    }
    
    public static boolean isFaced(final Entity targetEntity, final double blockReachDistance) {
        return RaycastUtils.raycastEntities(blockReachDistance).contains(targetEntity);
    }
    
    @EventTarget
    public void onTick(final TickEvent event) {
        if (RotationUtils.lookChanged) {
            ++RotationUtils.keepLength;
            if (RotationUtils.keepLength > 15) {
                reset();
            }
        }
        if (RandomUtils.getRandom().nextGaussian() * 100.0 > 80.0) {
            RotationUtils.x = Math.random();
        }
        if (RandomUtils.getRandom().nextGaussian() * 100.0 > 80.0) {
            RotationUtils.y = Math.random();
        }
        if (RandomUtils.getRandom().nextGaussian() * 100.0 > 80.0) {
            RotationUtils.z = Math.random();
        }
    }
    
    public static Vec3 getVectorForRotation(final float pitch, final float yaw) {
        final float f = MathHelper.func_76134_b(-yaw * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.func_76126_a(-yaw * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.func_76134_b(-pitch * 0.017453292f);
        final float f4 = MathHelper.func_76126_a(-pitch * 0.017453292f);
        return new Vec3((double)(f2 * f3), (double)f4, (double)(f * f3));
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();
        if (packet instanceof C03PacketPlayer) {
            final C03PacketPlayer packetPlayer = (C03PacketPlayer)packet;
            if (RotationUtils.lookChanged && !RotationUtils.keepRotation && (RotationUtils.targetYaw != RotationUtils.lastLook[0] || RotationUtils.targetPitch != RotationUtils.lastLook[1])) {
                packetPlayer.field_149476_e = RotationUtils.targetYaw;
                packetPlayer.field_149473_f = RotationUtils.targetPitch;
                packetPlayer.field_149481_i = true;
            }
            if (packetPlayer.field_149481_i) {
                RotationUtils.lastLook = new float[] { packetPlayer.field_149476_e, packetPlayer.field_149473_f };
            }
        }
    }
    
    public static void setTargetRotation(final float yaw, final float pitch) {
        if (Double.isNaN(yaw) || Double.isNaN(pitch)) {
            return;
        }
        RotationUtils.targetYaw = yaw;
        RotationUtils.targetPitch = pitch;
        RotationUtils.lookChanged = true;
        RotationUtils.keepLength = 0;
    }
    
    public static void reset() {
        RotationUtils.lookChanged = false;
        RotationUtils.keepLength = 0;
        RotationUtils.targetYaw = 0.0f;
        RotationUtils.targetPitch = 0.0f;
    }
    
    @Override
    public boolean handleEvents() {
        return true;
    }
    
    static {
        RotationUtils.mc = Minecraft.func_71410_x();
        RotationUtils.keepRotation = false;
        RotationUtils.lastLook = new float[] { 0.0f, 0.0f };
        RotationUtils.x = Math.random();
        RotationUtils.y = Math.random();
        RotationUtils.z = Math.random();
    }
}
