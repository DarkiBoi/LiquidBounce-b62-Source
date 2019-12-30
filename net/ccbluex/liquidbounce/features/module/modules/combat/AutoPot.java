// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AutoPot", description = "Automatically throws healing potions.", category = ModuleCategory.COMBAT)
public class AutoPot extends Module
{
    private final MSTimer msTimer;
    private final FloatValue healthValue;
    private final IntegerValue delayValue;
    private final BoolValue openInventoryValue;
    private final BoolValue noAirValue;
    private final ListValue modeValue;
    
    public AutoPot() {
        this.msTimer = new MSTimer();
        this.healthValue = new FloatValue("Health", 15.0f, 1.0f, 20.0f);
        this.delayValue = new IntegerValue("Delay", 150, 0, 500);
        this.openInventoryValue = new BoolValue("OpenInv", false);
        this.noAirValue = new BoolValue("NoAir", false);
        this.modeValue = new ListValue("Mode", new String[] { "Normal", "Jump", "Port" }, "Normal");
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("autopot", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("health")) {
                    this.chatSyntax(".autopot <health>");
                    return;
                }
                if (args.length > 2) {
                    try {
                        final float value = Float.parseFloat(args[2]);
                        AutoPot.this.healthValue.setValue(value);
                        this.chat("§7AutoPot health was set to §8" + value + "§7.");
                        AutoPot$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                    }
                    catch (NumberFormatException exception) {
                        this.chatSyntaxError();
                    }
                    return;
                }
                this.chatSyntax(".autopot health <value>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (!this.msTimer.hasTimePassed(this.delayValue.asInteger()) || AutoPot.mc.field_71442_b.func_78758_h()) {
            return;
        }
        if (this.noAirValue.asBoolean() && !AutoPot.mc.field_71439_g.field_70122_E) {
            return;
        }
        final int potionInHotbar = this.findPotion(36, 45);
        if (AutoPot.mc.field_71439_g.func_110143_aJ() <= this.healthValue.asFloat() && potionInHotbar != -1) {
            if (AutoPot.mc.field_71439_g.field_70122_E) {
                final String lowerCase = this.modeValue.asString().toLowerCase();
                switch (lowerCase) {
                    case "jump": {
                        AutoPot.mc.field_71439_g.func_70664_aZ();
                        break;
                    }
                    case "port": {
                        AutoPot.mc.field_71439_g.func_70091_d(0.0, 0.42, 0.0);
                        break;
                    }
                }
            }
            this.throwPot(potionInHotbar);
            this.msTimer.reset();
            return;
        }
        final int potionInInventory = this.findPotion(9, 36);
        if (potionInInventory != -1 && InventoryUtils.hasSpaceHotbar()) {
            if (this.openInventoryValue.asBoolean() && !(AutoPot.mc.field_71462_r instanceof GuiInventory)) {
                return;
            }
            AutoPot.mc.field_71442_b.func_78753_a(0, potionInInventory, 0, 1, (EntityPlayer)AutoPot.mc.field_71439_g);
            this.msTimer.reset();
        }
    }
    
    private void throwPot(final int slot) {
        RotationUtils.keepRotation = true;
        AutoPot.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(AutoPot.mc.field_71439_g.field_70177_z, 90.0f, AutoPot.mc.field_71439_g.field_70122_E));
        AutoPot.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(slot - 36));
        AutoPot.mc.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(AutoPot.mc.field_71439_g.field_71069_bz.func_75139_a(slot).func_75211_c()));
        AutoPot.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(AutoPot.mc.field_71439_g.field_71071_by.field_70461_c));
        RotationUtils.keepRotation = false;
        AutoPot.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(AutoPot.mc.field_71439_g.field_70177_z, AutoPot.mc.field_71439_g.field_70125_A, AutoPot.mc.field_71439_g.field_70122_E));
    }
    
    private int findPotion(final int startSlot, final int endSlot) {
        for (int i = startSlot; i < endSlot; ++i) {
            final ItemStack stack = AutoPot.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (stack != null && stack.func_77973_b() instanceof ItemPotion) {
                if (ItemPotion.func_77831_g(stack.func_77952_i())) {
                    final ItemPotion itemPotion = (ItemPotion)stack.func_77973_b();
                    for (final PotionEffect potionEffect : itemPotion.func_77832_l(stack)) {
                        if (potionEffect.func_76456_a() == Potion.field_76432_h.field_76415_H) {
                            return i;
                        }
                    }
                    if (!AutoPot.mc.field_71439_g.func_70644_a(Potion.field_76428_l)) {
                        for (final PotionEffect potionEffect : itemPotion.func_77832_l(stack)) {
                            if (potionEffect.func_76456_a() == Potion.field_76428_l.field_76415_H) {
                                return i;
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }
    
    @Override
    public String getTag() {
        return String.valueOf(this.healthValue.asFloat());
    }
}
