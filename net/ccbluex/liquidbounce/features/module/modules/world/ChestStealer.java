// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.event.EventTarget;
import java.util.List;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.minecraft.item.ItemBlock;
import net.minecraft.inventory.Slot;
import java.util.ArrayList;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.client.gui.inventory.GuiChest;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "ChestStealer", description = "Automatically steals all items from a chest.", category = ModuleCategory.WORLD)
public class ChestStealer extends Module
{
    private final IntegerValue maxDelayValue;
    private final IntegerValue minDelayValue;
    private final BoolValue takeRandomizedValue;
    private final BoolValue onlyItemsValue;
    private final BoolValue noCompassValue;
    private final BoolValue autoCloseValue;
    private final IntegerValue autoCloseMaxDelayValue;
    private final IntegerValue autoCloseMinDelayValue;
    private final BoolValue closeOnFullValue;
    private final BoolValue chestTitleValue;
    private final MSTimer msTimer;
    private long delay;
    private final MSTimer autoCloseTimer;
    private long autoCloseDelay;
    
    public ChestStealer() {
        this.maxDelayValue = new IntegerValue("MaxDelay", 200, 0, 400) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int i = ChestStealer.this.minDelayValue.asInteger();
                if (i > Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(i);
                }
                ChestStealer.this.delay = TimeUtils.randomDelay(ChestStealer.this.minDelayValue.asInteger(), ChestStealer.this.maxDelayValue.asInteger());
            }
        };
        this.minDelayValue = new IntegerValue("MinDelay", 150, 0, 400) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int i = ChestStealer.this.maxDelayValue.asInteger();
                if (i < Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(i);
                }
                ChestStealer.this.delay = TimeUtils.randomDelay(ChestStealer.this.minDelayValue.asInteger(), ChestStealer.this.maxDelayValue.asInteger());
            }
        };
        this.takeRandomizedValue = new BoolValue("TakeRandomized", false);
        this.onlyItemsValue = new BoolValue("OnlyItems", false);
        this.noCompassValue = new BoolValue("NoCompass", false);
        this.autoCloseValue = new BoolValue("AutoClose", true);
        this.autoCloseMaxDelayValue = new IntegerValue("AutoCloseMaxDelay", 0, 0, 400) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int i = ChestStealer.this.autoCloseMinDelayValue.asInteger();
                if (i > Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(i);
                }
                ChestStealer.this.autoCloseDelay = TimeUtils.randomDelay(ChestStealer.this.autoCloseMinDelayValue.asInteger(), ChestStealer.this.autoCloseMaxDelayValue.asInteger());
            }
        };
        this.autoCloseMinDelayValue = new IntegerValue("AutoCloseMinDelay", 0, 0, 400) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int i = ChestStealer.this.autoCloseMaxDelayValue.asInteger();
                if (i < Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(i);
                }
                ChestStealer.this.autoCloseDelay = TimeUtils.randomDelay(ChestStealer.this.autoCloseMinDelayValue.asInteger(), ChestStealer.this.autoCloseMaxDelayValue.asInteger());
            }
        };
        this.closeOnFullValue = new BoolValue("CloseOnFull", true);
        this.chestTitleValue = new BoolValue("ChestTitle", false);
        this.msTimer = new MSTimer();
        this.delay = TimeUtils.randomDelay(this.minDelayValue.asInteger(), this.maxDelayValue.asInteger());
        this.autoCloseTimer = new MSTimer();
        this.autoCloseDelay = TimeUtils.randomDelay(this.autoCloseMinDelayValue.asInteger(), this.autoCloseMaxDelayValue.asInteger());
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("cheststealer", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("maxdelay")) {
                        if (args.length > 2) {
                            try {
                                final int value = Integer.parseInt(args[2]);
                                if (ChestStealer.this.minDelayValue.asInteger() > value) {
                                    this.chat("MinDelay can't higher as MaxDelay!");
                                    return;
                                }
                                ChestStealer.this.maxDelayValue.setValue(value);
                                ChestStealer.this.delay = TimeUtils.randomDelay(ChestStealer.this.minDelayValue.asInteger(), ChestStealer.this.maxDelayValue.asInteger());
                                this.chat("§7ChestStealer maxdelay was set to §8" + value + "§7.");
                                ChestStealer$5.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".cheststealer maxdelay <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("mindelay")) {
                        if (args.length > 2) {
                            try {
                                final int value = Integer.parseInt(args[2]);
                                if (ChestStealer.this.maxDelayValue.asInteger() < value) {
                                    this.chat("MinDelay can't higher as MaxDelay!");
                                    return;
                                }
                                ChestStealer.this.minDelayValue.setValue(value);
                                ChestStealer.this.delay = TimeUtils.randomDelay(ChestStealer.this.minDelayValue.asInteger(), ChestStealer.this.maxDelayValue.asInteger());
                                this.chat("§7ChestStealer mindelay was set to §8" + value + "§7.");
                                ChestStealer$5.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".cheststealer mindelay <value>");
                        return;
                    }
                    else {
                        if (args[1].equalsIgnoreCase("takerandomized")) {
                            ChestStealer.this.takeRandomizedValue.setValue(!ChestStealer.this.takeRandomizedValue.asBoolean());
                            this.chat("§7ChestStealer TakeRandomized was toggled §8" + (ChestStealer.this.takeRandomizedValue.asBoolean() ? "on" : "off") + "§7.");
                            ChestStealer$5.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        if (args[1].equalsIgnoreCase("onlyitems")) {
                            ChestStealer.this.onlyItemsValue.setValue(!ChestStealer.this.onlyItemsValue.asBoolean());
                            this.chat("§7ChestStealer OnlyItems was toggled §8" + (ChestStealer.this.onlyItemsValue.asBoolean() ? "on" : "off") + "§7.");
                            ChestStealer$5.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        if (args[1].equalsIgnoreCase("NoCompass")) {
                            ChestStealer.this.noCompassValue.setValue(!ChestStealer.this.noCompassValue.asBoolean());
                            this.chat("§7ChestStealer NoCompass was toggled §8" + (ChestStealer.this.noCompassValue.asBoolean() ? "on" : "off") + "§7.");
                            ChestStealer$5.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        if (args[1].equalsIgnoreCase("ChestTitle")) {
                            ChestStealer.this.chestTitleValue.setValue(!ChestStealer.this.chestTitleValue.asBoolean());
                            this.chat("§7ChestStealer ChestTitle was toggled §8" + (ChestStealer.this.noCompassValue.asBoolean() ? "on" : "off") + "§7.");
                            ChestStealer$5.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                    }
                }
                this.chatSyntax(".cheststealer <maxdelay, mindelay, takerandomized, onlyItems, nocompass, chesttitle>");
            }
        });
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (ChestStealer.mc.field_71462_r instanceof GuiChest && this.msTimer.hasTimePassed(this.delay) && (!(boolean)this.noCompassValue.asObject() || ChestStealer.mc.field_71439_g.field_71071_by.func_70448_g() == null || !ChestStealer.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b().func_77658_a().equals("item.compass"))) {
            final GuiChest guiChest = (GuiChest)ChestStealer.mc.field_71462_r;
            if (this.chestTitleValue.asBoolean() && (guiChest.field_147015_w == null || !guiChest.field_147015_w.func_70005_c_().contains(new ItemStack((Item)Item.field_150901_e.func_82594_a((Object)new ResourceLocation("minecraft:chest"))).func_82833_r()))) {
                return;
            }
            final InventoryCleaner inventoryCleaner = (InventoryCleaner)ModuleManager.getModule(InventoryCleaner.class);
            final boolean takeRandomized = (boolean)this.takeRandomizedValue.asObject();
            if (!this.isEmpty(guiChest) && (!this.closeOnFullValue.asBoolean() || !this.isInventoryFull())) {
                this.autoCloseTimer.reset();
                if (takeRandomized) {
                    final List<Slot> items = new ArrayList<Slot>();
                    for (int i = 0; i < guiChest.field_147018_x * 9; ++i) {
                        final Slot slot = guiChest.field_147002_h.field_75151_b.get(i);
                        if (slot.func_75211_c() != null && (!(boolean)this.onlyItemsValue.asObject() || !(slot.func_75211_c().func_77973_b() instanceof ItemBlock)) && (!inventoryCleaner.getState() || inventoryCleaner.isUseful(slot.func_75211_c()))) {
                            items.add(slot);
                        }
                    }
                    final int randomSlot = RandomUtils.getRandom().nextInt(items.size());
                    final Slot slot = items.get(randomSlot);
                    guiChest.func_146984_a(slot, slot.field_75222_d, 0, 1);
                    this.msTimer.reset();
                    this.delay = TimeUtils.randomDelay(this.minDelayValue.asInteger(), this.maxDelayValue.asInteger());
                }
                else {
                    for (int j = 0; j < guiChest.field_147018_x * 9; ++j) {
                        final Slot slot2 = guiChest.field_147002_h.field_75151_b.get(j);
                        if (this.msTimer.hasTimePassed(this.delay) && slot2.func_75211_c() != null && (!(boolean)this.onlyItemsValue.asObject() || !(slot2.func_75211_c().func_77973_b() instanceof ItemBlock)) && (!inventoryCleaner.getState() || inventoryCleaner.isUseful(slot2.func_75211_c()))) {
                            guiChest.func_146984_a(slot2, slot2.field_75222_d, 0, 1);
                            this.msTimer.reset();
                            this.delay = TimeUtils.randomDelay(this.minDelayValue.asInteger(), this.maxDelayValue.asInteger());
                        }
                    }
                }
            }
            else if (this.autoCloseValue.asBoolean() && this.autoCloseTimer.hasTimePassed(this.autoCloseDelay)) {
                ChestStealer.mc.field_71439_g.func_71053_j();
                this.autoCloseDelay = TimeUtils.randomDelay(this.autoCloseMinDelayValue.asInteger(), this.autoCloseMaxDelayValue.asInteger());
            }
        }
        else {
            this.autoCloseTimer.reset();
        }
    }
    
    private boolean isEmpty(final GuiChest guiChest) {
        final InventoryCleaner inventoryCleaner = (InventoryCleaner)ModuleManager.getModule(InventoryCleaner.class);
        for (int i = 0; i < guiChest.field_147018_x * 9; ++i) {
            final Slot slot = guiChest.field_147002_h.field_75151_b.get(i);
            if (slot.func_75211_c() != null && (!this.onlyItemsValue.asBoolean() || !(slot.func_75211_c().func_77973_b() instanceof ItemBlock)) && (!inventoryCleaner.getState() || inventoryCleaner.isUseful(slot.func_75211_c()))) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isInventoryFull() {
        for (int i = 0; i < ChestStealer.mc.field_71439_g.field_71071_by.field_70462_a.length; ++i) {
            if (ChestStealer.mc.field_71439_g.field_71071_by.field_70462_a[i] == null) {
                return false;
            }
        }
        return true;
    }
}
