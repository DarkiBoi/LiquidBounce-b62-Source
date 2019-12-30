// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.block.BlockSlab;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.network.NetworkPlayerInfo;
import java.util.Iterator;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.Comparator;
import java.util.ArrayList;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityAnimal;
import net.ccbluex.liquidbounce.utils.render.ChatColor;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams;
import java.util.Objects;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.combat.NoFriends;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class EntityUtils
{
    private static final Minecraft mc;
    public static boolean targetInvisible;
    public static boolean targetPlayer;
    public static boolean targetMobs;
    public static boolean targetAnimals;
    public static boolean targetDead;
    
    public static boolean isSelected(final Entity entity, final boolean canAttackCheck) {
        if (!(entity instanceof EntityLivingBase) || (!EntityUtils.targetDead && !entity.func_70089_S()) || entity == EntityUtils.mc.field_71439_g || (!EntityUtils.targetInvisible && entity.func_82150_aj())) {
            return false;
        }
        if (!EntityUtils.targetPlayer || !(entity instanceof EntityPlayer)) {
            return (EntityUtils.targetMobs && isMob(entity)) || (EntityUtils.targetAnimals && isAnimal(entity));
        }
        final EntityPlayer entityPlayer = (EntityPlayer)entity;
        if (!canAttackCheck) {
            return true;
        }
        if (AntiBot.isBot((EntityLivingBase)entityPlayer)) {
            return false;
        }
        if (isFriend((Entity)entityPlayer) && !Objects.requireNonNull(ModuleManager.getModule(NoFriends.class)).getState()) {
            return false;
        }
        if (entityPlayer.func_175149_v()) {
            return false;
        }
        final Teams teams = (Teams)ModuleManager.getModule(Teams.class);
        return !teams.getState() || !teams.isInYourTeam((EntityLivingBase)entityPlayer);
    }
    
    public static boolean isFriend(final Entity entity) {
        return entity instanceof EntityPlayer && entity.func_70005_c_() != null && LiquidBounce.CLIENT.fileManager.friendsConfig.isFriend(ChatColor.stripColor(entity.func_70005_c_()));
    }
    
    public static boolean isAnimal(final Entity entity) {
        return entity instanceof EntityAnimal || entity instanceof EntitySquid || entity instanceof EntityGolem || entity instanceof EntityBat;
    }
    
    public static boolean isMob(final Entity entity) {
        return entity instanceof EntityMob || entity instanceof EntityVillager || entity instanceof EntitySlime || entity instanceof EntityGhast || entity instanceof EntityDragon;
    }
    
    public static EntityLivingBase getEntity(final boolean see, final PriorityMode priorityMode) {
        if (priorityMode == null) {
            return null;
        }
        final List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
        for (final Entity entity : EntityUtils.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityLivingBase && isSelected(entity, true) && (!see || EntityUtils.mc.field_71439_g.func_70685_l(entity))) {
                targets.add((EntityLivingBase)entity);
            }
        }
        if (targets.isEmpty()) {
            return null;
        }
        switch (priorityMode) {
            case DISTANCE: {
                targets.sort(Comparator.comparingDouble(value -> EntityUtils.mc.field_71439_g.func_70032_d(value)));
                break;
            }
            case DIRECTION: {
                targets.sort(Comparator.comparingDouble((ToDoubleFunction<? super EntityLivingBase>)RotationUtils::getRotationDifference));
                break;
            }
            case HEALTH: {
                targets.sort(Comparator.comparingDouble(EntityLivingBase::func_110143_aJ));
                break;
            }
        }
        return targets.get(0);
    }
    
    public static String getName(final NetworkPlayerInfo networkPlayerInfoIn) {
        return (networkPlayerInfoIn.func_178854_k() != null) ? networkPlayerInfoIn.func_178854_k().func_150254_d() : ScorePlayerTeam.func_96667_a((Team)networkPlayerInfoIn.func_178850_i(), networkPlayerInfoIn.func_178845_a().getName());
    }
    
    public static boolean isNearBlock() {
        final EntityPlayerSP thePlayer = EntityUtils.mc.field_71439_g;
        final WorldClient theWorld = EntityUtils.mc.field_71441_e;
        final List<BlockPos> blocks = new ArrayList<BlockPos>();
        blocks.add(new BlockPos(thePlayer.field_70165_t, thePlayer.field_70163_u + 1.0, thePlayer.field_70161_v - 0.7));
        blocks.add(new BlockPos(thePlayer.field_70165_t + 0.7, thePlayer.field_70163_u + 1.0, thePlayer.field_70161_v));
        blocks.add(new BlockPos(thePlayer.field_70165_t, thePlayer.field_70163_u + 1.0, thePlayer.field_70161_v + 0.7));
        blocks.add(new BlockPos(thePlayer.field_70165_t - 0.7, thePlayer.field_70163_u + 1.0, thePlayer.field_70161_v));
        for (final BlockPos blockPos : blocks) {
            if ((theWorld.func_180495_p(blockPos).func_177230_c().func_149669_A() == theWorld.func_180495_p(blockPos).func_177230_c().func_149665_z() + 1.0 && !theWorld.func_180495_p(blockPos).func_177230_c().func_149751_l() && theWorld.func_180495_p(blockPos).func_177230_c() != Blocks.field_150355_j && !(theWorld.func_180495_p(blockPos).func_177230_c() instanceof BlockSlab)) || theWorld.func_180495_p(blockPos).func_177230_c() == Blocks.field_180401_cv) {
                return true;
            }
        }
        return false;
    }
    
    public static int getPing(final EntityPlayer entityPlayer) {
        if (entityPlayer == null) {
            return 0;
        }
        final NetworkPlayerInfo networkPlayerInfo = EntityUtils.mc.func_147114_u().func_175102_a(entityPlayer.func_110124_au());
        return (networkPlayerInfo == null) ? 0 : networkPlayerInfo.func_178853_c();
    }
    
    static {
        mc = Minecraft.func_71410_x();
        EntityUtils.targetInvisible = false;
        EntityUtils.targetPlayer = true;
        EntityUtils.targetMobs = true;
        EntityUtils.targetAnimals = false;
        EntityUtils.targetDead = false;
    }
    
    public enum PriorityMode
    {
        DISTANCE("Distance"), 
        DIRECTION("Direction"), 
        HEALTH("Health");
        
        private final String name;
        
        private PriorityMode(final String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static PriorityMode fromString(final String name) {
            for (final PriorityMode priorityMode : values()) {
                if (priorityMode.getName().equalsIgnoreCase(name)) {
                    return priorityMode;
                }
            }
            return null;
        }
    }
}
