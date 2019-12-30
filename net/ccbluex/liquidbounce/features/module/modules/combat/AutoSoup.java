// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.minecraft.init.Items;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AutoSoup", description = "Makes you automatically eat soup whenever your health is low.", category = ModuleCategory.COMBAT)
public class AutoSoup extends Module
{
    private final FloatValue healthValue;
    private final IntegerValue delayValue;
    private final BoolValue openInventoryValue;
    private final MSTimer msTimer;
    
    public AutoSoup() {
        this.healthValue = new FloatValue("Health", 15.0f, 0.0f, 20.0f);
        this.delayValue = new IntegerValue("Delay", 150, 0, 500);
        this.openInventoryValue = new BoolValue("OpenInv", false);
        this.msTimer = new MSTimer();
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("autosoup", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("health")) {
                    this.chatSyntax(".autosoup <health>");
                    return;
                }
                if (args.length > 2) {
                    try {
                        final float value = Float.parseFloat(args[2]);
                        AutoSoup.this.healthValue.setValue(value);
                        this.chat("§7AutoSoup health was set to §8" + value + "§7.");
                        AutoSoup$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                    }
                    catch (NumberFormatException exception) {
                        this.chatSyntaxError();
                    }
                    return;
                }
                this.chatSyntax(".autosoup health <value>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (!this.msTimer.hasTimePassed(this.delayValue.asInteger())) {
            return;
        }
        final int soupInHotbar = InventoryUtils.findItem(36, 45, Items.field_151009_A);
        if (AutoSoup.mc.field_71439_g.func_110143_aJ() <= this.healthValue.asFloat() && soupInHotbar != -1) {
            AutoSoup.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(soupInHotbar - 36));
            AutoSoup.mc.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(AutoSoup.mc.field_71439_g.field_71069_bz.func_75139_a(soupInHotbar).func_75211_c()));
            AutoSoup.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
            AutoSoup.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(AutoSoup.mc.field_71439_g.field_71071_by.field_70461_c));
            this.msTimer.reset();
            return;
        }
        final int soupInInventory = InventoryUtils.findItem(9, 36, Items.field_151009_A);
        if (soupInInventory != -1 && InventoryUtils.hasSpaceHotbar()) {
            if (this.openInventoryValue.asBoolean() && !(AutoSoup.mc.field_71462_r instanceof GuiInventory)) {
                return;
            }
            AutoSoup.mc.field_71442_b.func_78753_a(0, soupInInventory, 0, 1, (EntityPlayer)AutoSoup.mc.field_71439_g);
            this.msTimer.reset();
        }
    }
    
    @Override
    public String getTag() {
        return String.valueOf(this.healthValue.asFloat());
    }
}
