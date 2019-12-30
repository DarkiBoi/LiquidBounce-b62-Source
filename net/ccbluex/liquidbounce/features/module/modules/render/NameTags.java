// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import net.minecraft.util.Timer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.utils.render.ChatColor;
import net.minecraft.entity.EntityLivingBase;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.valuesystem.types.FontValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "NameTags", description = "Changes the scale of the nametags so you can always read them.", category = ModuleCategory.RENDER)
public class NameTags extends Module
{
    private final BoolValue healthValue;
    private final BoolValue pingValue;
    private final BoolValue distanceValue;
    private final BoolValue armorValue;
    private final BoolValue clearNamesValue;
    private final FontValue fontValue;
    
    public NameTags() {
        this.healthValue = new BoolValue("Health", true);
        this.pingValue = new BoolValue("Ping", true);
        this.distanceValue = new BoolValue("Distance", false);
        this.armorValue = new BoolValue("Armor", true);
        this.clearNamesValue = new BoolValue("ClearNames", false);
        this.fontValue = new FontValue("Font", Fonts.font40);
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        NameTags.mc.field_71441_e.field_72996_f.stream().filter(entity -> entity != NameTags.mc.field_71439_g && EntityUtils.isSelected(entity, false)).forEach(entity -> this.renderNameTag(entity, this.clearNamesValue.asBoolean() ? ChatColor.stripColor(((Entity)entity).func_145748_c_().func_150260_c()) : ((Entity)entity).func_145748_c_().func_150260_c()));
    }
    
    private void renderNameTag(final EntityLivingBase entity, String tag) {
        final FontRenderer fontRenderer = (FontRenderer)this.fontValue.asObject();
        final boolean bot = AntiBot.isBot(entity);
        final ChatColor nameColor = bot ? ChatColor.DARK_BLUE : (entity.func_82150_aj() ? ChatColor.GOLD : (entity.func_70093_af() ? ChatColor.DARK_RED : ChatColor.GRAY));
        final int ping = (entity instanceof EntityPlayer) ? EntityUtils.getPing((EntityPlayer)entity) : 0;
        tag = (this.distanceValue.asBoolean() ? ("§7" + Math.round(NameTags.mc.field_71439_g.func_70032_d((Entity)entity)) + "m ") : "") + ((this.pingValue.asBoolean() && entity instanceof EntityPlayer) ? (((ping > 200) ? "§c" : ((ping > 100) ? "§e" : "§a")) + ping + "ms §7") : "") + nameColor + tag + (this.healthValue.asBoolean() ? ("§7§c " + (int)entity.func_110143_aJ() + "\u2764") : ("" + (bot ? " §c§lBot" : "")));
        GL11.glPushMatrix();
        final RenderManager renderManager = NameTags.mc.func_175598_ae();
        final Timer timer = NameTags.mc.field_71428_T;
        GL11.glTranslated(entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * timer.field_74281_c - renderManager.field_78725_b, entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * timer.field_74281_c - renderManager.field_78726_c + entity.func_70047_e() + 0.550000011920929, entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * timer.field_74281_c - renderManager.field_78723_d);
        GL11.glRotatef(-NameTags.mc.func_175598_ae().field_78735_i, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(NameTags.mc.func_175598_ae().field_78732_j, 1.0f, 0.0f, 0.0f);
        float distance = NameTags.mc.field_71439_g.func_70032_d((Entity)entity) / 4.0f;
        if (distance < 1.0f) {
            distance = 1.0f;
        }
        final float scale = distance / 100.0f;
        GL11.glScalef(-scale, -scale, scale);
        RenderUtils.disableGlCap(2896, 2929);
        RenderUtils.enableGlCap(3042);
        GL11.glBlendFunc(770, 771);
        final int width = fontRenderer.func_78256_a(tag) / 2;
        RenderUtils.drawBorderedRect((float)(-width - 2), -2.0f, (float)(width + 2), (float)(fontRenderer.field_78288_b + 2), 2.0f, new Color(255, 255, 255, 90).getRGB(), Integer.MIN_VALUE);
        fontRenderer.func_175065_a(tag, (float)(-width), (fontRenderer == Fonts.minecraftFont) ? 0.0f : 1.5f, 16777215, true);
        if (this.armorValue.asBoolean() && entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)entity;
            for (int index = 0; index < 5; ++index) {
                if (player.func_71124_b(index) != null) {
                    NameTags.mc.func_175599_af().field_77023_b = -147.0f;
                    NameTags.mc.func_175599_af().func_180450_b(player.func_71124_b(index), -50 + index * 20, -22);
                }
            }
            GlStateManager.func_179141_d();
            GlStateManager.func_179084_k();
            GlStateManager.func_179098_w();
        }
        RenderUtils.resetCaps();
        GlStateManager.func_179117_G();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
}
