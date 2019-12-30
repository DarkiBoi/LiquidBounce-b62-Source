// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.item.ItemStack;
import java.util.Iterator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.ItemSword;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "TNTBlock", description = "Automatically blocks with your sword when TNT around you explodes.", category = ModuleCategory.COMBAT)
public class TNTBlock extends Module
{
    private final IntegerValue fuseValue;
    private final BoolValue autoSwordValue;
    private boolean blocked;
    
    public TNTBlock() {
        this.fuseValue = new IntegerValue("Fuse", 10, 0, 20);
        this.autoSwordValue = new BoolValue("AutoSword", true);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("tntblock", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("fuse")) {
                    this.chatSyntax(".tntblock <fuse>");
                    return;
                }
                if (args.length > 2) {
                    try {
                        final int value = Integer.parseInt(args[2]);
                        TNTBlock.this.fuseValue.setValue(value);
                        this.chat("§7TNTBlock fuse was set to §8" + value + "§7.");
                        TNTBlock$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                    }
                    catch (NumberFormatException exception) {
                        this.chatSyntaxError();
                    }
                    return;
                }
                this.chatSyntax(".tntblock fuse <value>");
            }
        });
    }
    
    @EventTarget
    public void onMotionUpdate(final MotionEvent event) {
        for (final Entity entity : TNTBlock.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityTNTPrimed && TNTBlock.mc.field_71439_g.func_70032_d(entity) <= 9.0) {
                final EntityTNTPrimed tntPrimed = (EntityTNTPrimed)entity;
                if (tntPrimed.field_70516_a <= this.fuseValue.asInteger()) {
                    if (this.autoSwordValue.asBoolean()) {
                        int slot = -1;
                        float bestDamage = 1.0f;
                        for (int i = 0; i < 9; ++i) {
                            final ItemStack itemStack = TNTBlock.mc.field_71439_g.field_71071_by.func_70301_a(i);
                            if (itemStack != null && itemStack.func_77973_b() instanceof ItemSword) {
                                final float itemDamage = ((ItemSword)itemStack.func_77973_b()).func_150931_i() + 4.0f;
                                if (itemDamage > bestDamage) {
                                    bestDamage = itemDamage;
                                    slot = i;
                                }
                            }
                        }
                        if (slot != -1 && slot != TNTBlock.mc.field_71439_g.field_71071_by.field_70461_c) {
                            TNTBlock.mc.field_71439_g.field_71071_by.field_70461_c = slot;
                            TNTBlock.mc.field_71442_b.func_78765_e();
                        }
                    }
                    if (TNTBlock.mc.field_71439_g.func_70694_bm() != null && TNTBlock.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemSword) {
                        TNTBlock.mc.field_71474_y.field_74313_G.field_74513_e = true;
                        this.blocked = true;
                    }
                    return;
                }
                continue;
            }
        }
        if (this.blocked && !GameSettings.func_100015_a(TNTBlock.mc.field_71474_y.field_74313_G)) {
            TNTBlock.mc.field_71474_y.field_74313_G.field_74513_e = false;
            this.blocked = false;
        }
    }
}
