// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.entity.EntityLivingBase;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.minecraft.item.ItemBow;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "BowAimbot", description = "Automatically aims at players when using a bow.", category = ModuleCategory.COMBAT)
public class BowAimbot extends Module
{
    private final BoolValue silentValue;
    private final BoolValue predictValue;
    private final FloatValue predictSizeValue;
    private final ListValue priorityValue;
    private final BoolValue markValue;
    private Entity target;
    
    public BowAimbot() {
        this.silentValue = new BoolValue("Silent", true);
        this.predictValue = new BoolValue("Predict", true);
        this.predictSizeValue = new FloatValue("PredictSize", 2.0f, 0.1f, 5.0f);
        this.priorityValue = new ListValue("Priority", new String[] { "Health", "Distance", "Direction" }, "Direction");
        this.markValue = new BoolValue("Mark", true);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("bowaimbot", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1 && args[1].equalsIgnoreCase("silent")) {
                    BowAimbot.this.silentValue.setValue(!BowAimbot.this.silentValue.asBoolean());
                    this.chat("§7BowAimbot silent was toggled §8" + (BowAimbot.this.silentValue.asBoolean() ? "on" : "off") + "§7.");
                    BowAimbot$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                    return;
                }
                this.chatSyntax(".bowaimbot <silent>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (BowAimbot.mc.field_71439_g.func_70694_bm() != null && BowAimbot.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemBow && BowAimbot.mc.field_71439_g.func_71039_bw()) {
            final EntityLivingBase entity = EntityUtils.getEntity(true, EntityUtils.PriorityMode.fromString(this.priorityValue.asString()));
            if (entity != null) {
                RotationUtils.faceBow(this.target = (Entity)entity, this.silentValue.asBoolean(), this.predictValue.asBoolean(), this.predictSizeValue.asFloat());
            }
        }
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (this.target == null) {
            return;
        }
        if (!this.priorityValue.asString().equalsIgnoreCase("Multi") && this.markValue.asBoolean()) {
            RenderUtils.drawPlatform(this.target, new Color(37, 126, 255, 70));
        }
    }
    
    @Override
    public void onDisable() {
        this.target = null;
    }
}
