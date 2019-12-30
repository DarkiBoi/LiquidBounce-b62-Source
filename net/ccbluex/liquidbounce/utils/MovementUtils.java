// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils;

import net.minecraft.client.Minecraft;

public final class MovementUtils
{
    private static final Minecraft mc;
    
    public static float getSpeed() {
        return (float)Math.sqrt(MovementUtils.mc.field_71439_g.field_70159_w * MovementUtils.mc.field_71439_g.field_70159_w + MovementUtils.mc.field_71439_g.field_70179_y * MovementUtils.mc.field_71439_g.field_70179_y);
    }
    
    public static void strafe() {
        strafe(getSpeed());
    }
    
    public static boolean isMoving() {
        return MovementUtils.mc.field_71439_g != null && (MovementUtils.mc.field_71439_g.field_71158_b.field_78900_b != 0.0f || MovementUtils.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f);
    }
    
    public static boolean hasMotion() {
        return MovementUtils.mc.field_71439_g.field_70159_w != 0.0 && MovementUtils.mc.field_71439_g.field_70179_y != 0.0 && MovementUtils.mc.field_71439_g.field_70181_x != 0.0;
    }
    
    public static void strafe(final float speed) {
        if (!isMoving()) {
            return;
        }
        final double yaw = getDirection();
        MovementUtils.mc.field_71439_g.field_70159_w = -Math.sin(yaw) * speed;
        MovementUtils.mc.field_71439_g.field_70179_y = Math.cos(yaw) * speed;
    }
    
    public static void forward(final double length) {
        final double yaw = Math.toRadians(MovementUtils.mc.field_71439_g.field_70177_z);
        MovementUtils.mc.field_71439_g.func_70107_b(MovementUtils.mc.field_71439_g.field_70165_t + -Math.sin(yaw) * length, MovementUtils.mc.field_71439_g.field_70163_u, MovementUtils.mc.field_71439_g.field_70161_v + Math.cos(yaw) * length);
    }
    
    public static double getDirection() {
        float rotationYaw = MovementUtils.mc.field_71439_g.field_70177_z;
        if (MovementUtils.mc.field_71439_g.field_70701_bs < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (MovementUtils.mc.field_71439_g.field_70701_bs < 0.0f) {
            forward = -0.5f;
        }
        else if (MovementUtils.mc.field_71439_g.field_70701_bs > 0.0f) {
            forward = 0.5f;
        }
        if (MovementUtils.mc.field_71439_g.field_70702_br > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (MovementUtils.mc.field_71439_g.field_70702_br < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }
    
    static {
        mc = Minecraft.func_71410_x();
    }
}
