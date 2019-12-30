// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils;

import net.minecraft.util.Vec3;

public final class VectorUtils
{
    private final Vec3 start;
    private final Vec3 end;
    private final double offsetX;
    private final double offsetY;
    private final double offsetZ;
    
    public VectorUtils(final Vec3 vector, final float yaw, final float pitch, final double length) {
        this.start = vector;
        this.end = new Vec3(Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * length + vector.field_72450_a, Math.sin(Math.toRadians(pitch)) * length + vector.field_72448_b, Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * length + vector.field_72449_c);
        this.offsetX = this.end.field_72450_a - this.start.field_72450_a;
        this.offsetY = this.end.field_72448_b - this.start.field_72448_b;
        this.offsetZ = this.end.field_72449_c - this.start.field_72449_c;
    }
    
    public double getOffsetX() {
        return this.offsetX;
    }
    
    public double getOffsetY() {
        return this.offsetY;
    }
    
    public double getOffsetZ() {
        return this.offsetZ;
    }
    
    public Vec3 getStartVector() {
        return this.start;
    }
    
    public Vec3 getEndVector() {
        return this.end;
    }
}
