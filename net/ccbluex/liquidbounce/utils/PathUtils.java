// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils;

import net.minecraft.util.MathHelper;
import java.util.ArrayList;
import javax.vecmath.Vector3d;
import java.util.List;
import net.minecraft.client.Minecraft;

public final class PathUtils
{
    private static final Minecraft mc;
    
    public static List<Vector3d> findBlinkPath(final double tpX, final double tpY, final double tpZ) {
        final List<Vector3d> positions = new ArrayList<Vector3d>();
        double curX = PathUtils.mc.field_71439_g.field_70165_t;
        double curY = PathUtils.mc.field_71439_g.field_70163_u;
        double curZ = PathUtils.mc.field_71439_g.field_70161_v;
        double distance = Math.abs(curX - tpX) + Math.abs(curY - tpY) + Math.abs(curZ - tpZ);
        int count = 0;
        while (distance > 0.0) {
            distance = Math.abs(curX - tpX) + Math.abs(curY - tpY) + Math.abs(curZ - tpZ);
            final double diffX = curX - tpX;
            final double diffY = curY - tpY;
            final double diffZ = curZ - tpZ;
            final double offset = ((count & 0x1) == 0x0) ? 0.4 : 0.1;
            if (diffX < 0.0) {
                if (Math.abs(diffX) > offset) {
                    curX += offset;
                }
                else {
                    curX += Math.abs(diffX);
                }
            }
            if (diffX > 0.0) {
                if (Math.abs(diffX) > offset) {
                    curX -= offset;
                }
                else {
                    curX -= Math.abs(diffX);
                }
            }
            if (diffY < 0.0) {
                if (Math.abs(diffY) > 0.25) {
                    curY += 0.25;
                }
                else {
                    curY += Math.abs(diffY);
                }
            }
            if (diffY > 0.0) {
                if (Math.abs(diffY) > 0.25) {
                    curY -= 0.25;
                }
                else {
                    curY -= Math.abs(diffY);
                }
            }
            if (diffZ < 0.0) {
                if (Math.abs(diffZ) > offset) {
                    curZ += offset;
                }
                else {
                    curZ += Math.abs(diffZ);
                }
            }
            if (diffZ > 0.0) {
                if (Math.abs(diffZ) > offset) {
                    curZ -= offset;
                }
                else {
                    curZ -= Math.abs(diffZ);
                }
            }
            positions.add(new Vector3d(curX, curY, curZ));
            ++count;
        }
        return positions;
    }
    
    public static List<Vector3d> findPath(final double tpX, final double tpY, final double tpZ, final double offset) {
        final List<Vector3d> positions = new ArrayList<Vector3d>();
        final float yaw = (float)(Math.atan2(tpZ - PathUtils.mc.field_71439_g.field_70161_v, tpX - PathUtils.mc.field_71439_g.field_70165_t) * 180.0 / 3.141592653589793 - 90.0);
        final double steps = getDistance(PathUtils.mc.field_71439_g.field_70165_t, PathUtils.mc.field_71439_g.field_70163_u, PathUtils.mc.field_71439_g.field_70161_v, tpX, tpY, tpZ) / offset;
        double curY = PathUtils.mc.field_71439_g.field_70163_u;
        for (double d = offset; d < getDistance(PathUtils.mc.field_71439_g.field_70165_t, PathUtils.mc.field_71439_g.field_70163_u, PathUtils.mc.field_71439_g.field_70161_v, tpX, tpY, tpZ); d += offset) {
            curY -= (PathUtils.mc.field_71439_g.field_70163_u - tpY) / steps;
            positions.add(new Vector3d(PathUtils.mc.field_71439_g.field_70165_t - Math.sin(Math.toRadians(yaw)) * d, curY, PathUtils.mc.field_71439_g.field_70161_v + Math.cos(Math.toRadians(yaw)) * d));
        }
        positions.add(new Vector3d(tpX, tpY, tpZ));
        return positions;
    }
    
    private static double getDistance(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        final double xDiff = x1 - x2;
        final double yDiff = y1 - y2;
        final double zDiff = z1 - z2;
        return MathHelper.func_76133_a(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }
    
    static {
        mc = Minecraft.func_71410_x();
    }
}
