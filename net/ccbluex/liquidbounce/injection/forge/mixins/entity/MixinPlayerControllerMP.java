// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import net.ccbluex.liquidbounce.features.module.modules.player.Reach;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.InventoryMove;
import net.minecraft.item.ItemStack;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.exploit.AbortBreaking;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.event.events.AttackEvent;
import net.ccbluex.liquidbounce.LiquidBounce;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ PlayerControllerMP.class })
public class MixinPlayerControllerMP
{
    @Inject(method = { "attackEntity" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;syncCurrentPlayItem()V") })
    private void attackEntity(final EntityPlayer entityPlayer, final Entity targetEntity, final CallbackInfo callbackInfo) {
        LiquidBounce.CLIENT.eventManager.callEvent(new AttackEvent(targetEntity));
    }
    
    @Inject(method = { "getIsHittingBlock" }, at = { @At("HEAD") }, cancellable = true)
    private void getIsHittingBlock(final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (ModuleManager.getModule(AbortBreaking.class).getState()) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }
    
    @Inject(method = { "windowClick" }, at = { @At("HEAD") }, cancellable = true)
    private void windowClick(final int windowId, final int slotId, final int mouseButtonClicked, final int mode, final EntityPlayer playerIn, final CallbackInfoReturnable<ItemStack> callbackInfo) {
        final InventoryMove inventoryMove = (InventoryMove)ModuleManager.getModule(InventoryMove.class);
        if (inventoryMove.getState() && inventoryMove.noMoveClicks.asBoolean() && MovementUtils.isMoving()) {
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "getBlockReachDistance" }, at = { @At("HEAD") }, cancellable = true)
    private void getReach(final CallbackInfoReturnable<Float> returnable) {
        final Reach reach = (Reach)ModuleManager.getModule(Reach.class);
        if (reach.getState()) {
            returnable.setReturnValue(reach.reachValue.asFloat());
        }
    }
}
