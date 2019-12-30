// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import java.util.Iterator;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.Entity;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import java.util.HashMap;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.minecraft.util.BlockPos;
import java.util.Map;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "ProphuntESP", description = "Allows you to see disguised players in PropHunt.", category = ModuleCategory.RENDER)
public class ProphuntESP extends Module
{
    public final Map<BlockPos, Long> blocks;
    private final IntegerValue colorRedValue;
    private final IntegerValue colorGreenValue;
    private final IntegerValue colorBlueValue;
    private final BoolValue colorRainbow;
    
    public ProphuntESP() {
        this.blocks = new HashMap<BlockPos, Long>();
        this.colorRedValue = new IntegerValue("R", 0, 0, 255);
        this.colorGreenValue = new IntegerValue("G", 90, 0, 255);
        this.colorBlueValue = new IntegerValue("B", 255, 0, 255);
        this.colorRainbow = new BoolValue("Rainbow", false);
    }
    
    @Override
    public void onDisable() {
        synchronized (this.blocks) {
            this.blocks.clear();
        }
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        final Color color = this.colorRainbow.asBoolean() ? ColorUtils.rainbow() : new Color(this.colorRedValue.asInteger(), this.colorGreenValue.asInteger(), this.colorBlueValue.asInteger());
        for (final Entity entity : ProphuntESP.mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityFallingBlock)) {
                continue;
            }
            RenderUtils.drawEntityBox(entity, color, true);
        }
        synchronized (this.blocks) {
            final Iterator<Map.Entry<BlockPos, Long>> iterator = this.blocks.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<BlockPos, Long> entry = iterator.next();
                if (System.currentTimeMillis() - entry.getValue() > 2000L) {
                    iterator.remove();
                }
                else {
                    RenderUtils.drawBlockBox(entry.getKey(), color, true);
                }
            }
        }
    }
}
