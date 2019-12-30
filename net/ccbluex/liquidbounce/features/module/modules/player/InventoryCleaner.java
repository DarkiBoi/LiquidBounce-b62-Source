// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.player;

import net.ccbluex.liquidbounce.injection.implementations.IItemStack;
import java.util.HashMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import java.util.Iterator;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;
import net.ccbluex.liquidbounce.utils.item.Armor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.minecraft.item.ItemStack;
import java.util.Map;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "InventoryCleaner", description = "Automatically throws away useless items.", category = ModuleCategory.PLAYER)
public class InventoryCleaner extends Module
{
    private final IntegerValue maxDelayValue;
    private final IntegerValue minDelayValue;
    private final BoolValue invOpenValue;
    private final BoolValue simulateInventory;
    private final BoolValue noMoveValue;
    private final BoolValue hotbarValue;
    private final BoolValue randomSlotValue;
    private final BoolValue sortValue;
    private final IntegerValue itemDelayValue;
    private final String[] items;
    private final ListValue sortSlot1Value;
    private final ListValue sortSlot2Value;
    private final ListValue sortSlot3Value;
    private final ListValue sortSlot4Value;
    private final ListValue sortSlot5Value;
    private final ListValue sortSlot6Value;
    private final ListValue sortSlot7Value;
    private final ListValue sortSlot8Value;
    private final ListValue sortSlot9Value;
    private final MSTimer clickTimer;
    private long delay;
    
    public InventoryCleaner() {
        this.maxDelayValue = new IntegerValue("MaxDelay", 600, 0, 1000) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int minCPS = InventoryCleaner.this.minDelayValue.asInteger();
                if (minCPS > Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(minCPS);
                }
            }
        };
        this.minDelayValue = new IntegerValue("MinDelay", 400, 0, 1000) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int maxDelay = InventoryCleaner.this.maxDelayValue.asInteger();
                if (maxDelay < Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(maxDelay);
                }
            }
        };
        this.invOpenValue = new BoolValue("InvOpen", false);
        this.simulateInventory = new BoolValue("SimulateInventory", true);
        this.noMoveValue = new BoolValue("NoMove", false);
        this.hotbarValue = new BoolValue("Hotbar", true);
        this.randomSlotValue = new BoolValue("RandomSlot", false);
        this.sortValue = new BoolValue("Sort", true);
        this.itemDelayValue = new IntegerValue("ItemDelay", 0, 0, 5000);
        this.items = new String[] { "None", "Ignore", "Sword", "Bow", "Pickaxe", "Axe", "Food", "Block", "Water" };
        this.sortSlot1Value = new ListValue("SortSlot-1", this.items, "Sword");
        this.sortSlot2Value = new ListValue("SortSlot-2", this.items, "Bow");
        this.sortSlot3Value = new ListValue("SortSlot-3", this.items, "Pickaxe");
        this.sortSlot4Value = new ListValue("SortSlot-4", this.items, "Axe");
        this.sortSlot5Value = new ListValue("SortSlot-5", this.items, "None");
        this.sortSlot6Value = new ListValue("SortSlot-6", this.items, "None");
        this.sortSlot7Value = new ListValue("SortSlot-7", this.items, "Food");
        this.sortSlot8Value = new ListValue("SortSlot-8", this.items, "Block");
        this.sortSlot9Value = new ListValue("SortSlot-9", this.items, "Block");
        this.clickTimer = new MSTimer();
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (!this.clickTimer.hasTimePassed(this.delay) || (!(InventoryCleaner.mc.field_71462_r instanceof GuiInventory) && (boolean)this.invOpenValue.asObject()) || (this.noMoveValue.asBoolean() && MovementUtils.isMoving()) || (InventoryCleaner.mc.field_71439_g.field_71070_bA != null && InventoryCleaner.mc.field_71439_g.field_71070_bA.field_75152_c != 0)) {
            return;
        }
        final Map.Entry<Integer, ItemStack>[] entries = this.getItems(9, this.hotbarValue.asBoolean() ? 45 : 36).entrySet().stream().filter(stackEntry -> !this.isUseful(stackEntry.getValue())).sorted((o1, o2) -> this.randomSlotValue.asBoolean() ? (RandomUtils.getRandom().nextBoolean() ? 1 : -1) : 1).toArray(Map.Entry[]::new);
        if (entries.length == 0) {
            this.sortInventory();
            return;
        }
        for (final Map.Entry<Integer, ItemStack> stackEntry2 : entries) {
            final int slot = stackEntry2.getKey();
            final ItemStack itemStack = stackEntry2.getValue();
            if (!this.isUseful(itemStack)) {
                final boolean openInventory = !(InventoryCleaner.mc.field_71462_r instanceof GuiInventory) && this.simulateInventory.asBoolean();
                if (openInventory) {
                    InventoryCleaner.mc.func_147114_u().func_147297_a((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                }
                InventoryCleaner.mc.field_71442_b.func_78753_a(InventoryCleaner.mc.field_71439_g.field_71070_bA.field_75152_c, slot, 4, 4, (EntityPlayer)InventoryCleaner.mc.field_71439_g);
                if (openInventory) {
                    InventoryCleaner.mc.func_147114_u().func_147297_a((Packet)new C0DPacketCloseWindow());
                }
                this.clickTimer.reset();
                this.delay = TimeUtils.randomDelay(this.minDelayValue.asInteger(), this.maxDelayValue.asInteger());
                break;
            }
        }
    }
    
    public boolean isUseful(final ItemStack itemStack) {
        try {
            final Item item = itemStack.func_77973_b();
            if (item instanceof ItemSword || item instanceof ItemTool) {
                for (final AttributeModifier attributeModifier : itemStack.func_111283_C().get((Object)"generic.attackDamage")) {
                    final double damage = attributeModifier.func_111164_d() + 1.25 * ItemUtils.getEnchantment(itemStack, Enchantment.field_180314_l);
                    for (final ItemStack anotherStack : this.getItems(0, 45).values()) {
                        if (!itemStack.equals(anotherStack)) {
                            if (item.getClass() != anotherStack.func_77973_b().getClass()) {
                                continue;
                            }
                            for (final AttributeModifier anotherAttributeModifier : anotherStack.func_111283_C().get((Object)"generic.attackDamage")) {
                                if (damage <= anotherAttributeModifier.func_111164_d() + 1.25 * ItemUtils.getEnchantment(anotherStack, Enchantment.field_180314_l)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                return true;
            }
            if (item instanceof ItemBow) {
                final int bowPower = ItemUtils.getEnchantment(itemStack, Enchantment.field_77345_t);
                for (final ItemStack anotherStack2 : this.getItems(0, 45).values()) {
                    if (!itemStack.equals(anotherStack2)) {
                        if (!(anotherStack2.func_77973_b() instanceof ItemBow)) {
                            continue;
                        }
                        if (ItemUtils.getEnchantment(anotherStack2, Enchantment.field_77345_t) >= bowPower) {
                            return false;
                        }
                        continue;
                    }
                }
                return true;
            }
            if (item instanceof ItemArmor) {
                final ItemArmor itemArmor = (ItemArmor)item;
                final int itemID = Item.func_150891_b(item);
                final int[] array = Armor.getArmorArray(itemArmor);
                if (array == null) {
                    return true;
                }
                final List<Integer> armorArray = Arrays.stream(array).boxed().collect((Collector<? super Integer, ?, List<Integer>>)Collectors.toList());
                for (final ItemStack anotherStack : this.getItems(0, 45).values()) {
                    if (anotherStack.func_77973_b() instanceof ItemArmor && !itemStack.equals(anotherStack)) {
                        if (itemArmor.field_77881_a != ((ItemArmor)anotherStack.func_77973_b()).field_77881_a) {
                            continue;
                        }
                        if (armorArray.indexOf(itemID) >= armorArray.indexOf(Item.func_150891_b(anotherStack.func_77973_b())) && ItemUtils.getEnchantment(itemStack, Enchantment.field_180310_c) <= ItemUtils.getEnchantment(anotherStack, Enchantment.field_180310_c)) {
                            return false;
                        }
                        continue;
                    }
                }
                return true;
            }
            else {
                if (itemStack.func_77977_a().equals("item.compass")) {
                    for (final ItemStack anotherStack3 : this.getItems(0, 45).values()) {
                        if (!itemStack.equals(anotherStack3) && anotherStack3.func_77977_a().equals("item.compass")) {
                            return false;
                        }
                    }
                    return true;
                }
                return item instanceof ItemFood || itemStack.func_77977_a().equals("item.arrow") || (item instanceof ItemBlock && !itemStack.func_77977_a().contains("flower")) || item instanceof ItemBed || itemStack.func_77977_a().equals("item.diamond") || itemStack.func_77977_a().equals("item.ingotIron") || item instanceof ItemPotion || item instanceof ItemEnderPearl || item instanceof ItemEnchantedBook || item instanceof ItemBucket || itemStack.func_77977_a().equals("item.stick");
            }
        }
        catch (Throwable t) {
            ClientUtils.getLogger().error("(InventoryCleaner) Failed to check item: " + itemStack.func_77977_a() + ".", t);
            return true;
        }
    }
    
    private void sortInventory() {
        if (!this.sortValue.asBoolean()) {
            return;
        }
        for (int targetSlot = 36; targetSlot < 45; ++targetSlot) {
            final ItemStack slotStack = InventoryCleaner.mc.field_71439_g.field_71071_by.func_70301_a(targetSlot - 36);
            final SortCallback sortCallback = this.searchSortItem(targetSlot, slotStack);
            if (sortCallback != null && sortCallback.getItemSlot() != targetSlot && (sortCallback.isReplaceCurrentItem() || slotStack == null)) {
                InventoryCleaner.mc.field_71442_b.func_78753_a(0, sortCallback.getItemSlot(), targetSlot - 36, 2, (EntityPlayer)InventoryCleaner.mc.field_71439_g);
                this.clickTimer.reset();
                this.delay = TimeUtils.randomDelay(this.minDelayValue.asInteger(), this.maxDelayValue.asInteger());
                break;
            }
        }
    }
    
    private String getType(final int targetSlot) {
        switch (targetSlot) {
            case 36: {
                return this.sortSlot1Value.asString();
            }
            case 37: {
                return this.sortSlot2Value.asString();
            }
            case 38: {
                return this.sortSlot3Value.asString();
            }
            case 39: {
                return this.sortSlot4Value.asString();
            }
            case 40: {
                return this.sortSlot5Value.asString();
            }
            case 41: {
                return this.sortSlot6Value.asString();
            }
            case 42: {
                return this.sortSlot7Value.asString();
            }
            case 43: {
                return this.sortSlot8Value.asString();
            }
            case 44: {
                return this.sortSlot9Value.asString();
            }
            default: {
                return "";
            }
        }
    }
    
    private SortCallback searchSortItem(final int targetSlot, final ItemStack slotStack) {
        final String targetType = this.getType(targetSlot);
        final String lowerCase = targetType.toLowerCase();
        switch (lowerCase) {
            case "sword":
            case "pickaxe":
            case "axe": {
                int bestWeapon = (slotStack != null && slotStack.func_77973_b().getClass() == (targetType.equalsIgnoreCase("Sword") ? ItemSword.class : (targetType.equalsIgnoreCase("Pickaxe") ? ItemPickaxe.class : (targetType.equalsIgnoreCase("Axe") ? ItemAxe.class : null)))) ? targetSlot : -1;
                for (final Map.Entry<Integer, ItemStack> stackEntry : this.getItems(9, 45).entrySet()) {
                    final int sourceSlot = stackEntry.getKey();
                    final ItemStack itemStack = stackEntry.getValue();
                    if (itemStack.func_77973_b().getClass() == (targetType.equalsIgnoreCase("Sword") ? ItemSword.class : (targetType.equalsIgnoreCase("Pickaxe") ? ItemPickaxe.class : (targetType.equalsIgnoreCase("Axe") ? ItemAxe.class : null))) && !this.getType(sourceSlot).equalsIgnoreCase(targetType)) {
                        if (bestWeapon == -1) {
                            bestWeapon = sourceSlot;
                        }
                        else {
                            for (final AttributeModifier attributeModifier : itemStack.func_111283_C().get((Object)"generic.attackDamage")) {
                                final double damage = attributeModifier.func_111164_d() + 1.25 * ItemUtils.getEnchantment(itemStack, Enchantment.field_180314_l);
                                final ItemStack anotherStack = InventoryCleaner.mc.field_71439_g.field_71071_by.func_70301_a(bestWeapon);
                                if (anotherStack == null) {
                                    continue;
                                }
                                for (final AttributeModifier anotherAttributeModifier : anotherStack.func_111283_C().get((Object)"generic.attackDamage")) {
                                    if (damage <= anotherAttributeModifier.func_111164_d() + 1.25 * ItemUtils.getEnchantment(anotherStack, Enchantment.field_180314_l)) {
                                        bestWeapon = sourceSlot;
                                    }
                                }
                            }
                        }
                    }
                }
                if (bestWeapon == -1) {
                    return null;
                }
                return new SortCallback(bestWeapon, true);
            }
            case "bow": {
                int bestWeapon = -1;
                for (final Map.Entry<Integer, ItemStack> stackEntry : this.getItems(9, 45).entrySet()) {
                    final int sourceSlot = stackEntry.getKey();
                    final ItemStack itemStack = stackEntry.getValue();
                    if (itemStack.func_77973_b() instanceof ItemBow && !this.getType(sourceSlot).equalsIgnoreCase(targetType)) {
                        if (bestWeapon == -1) {
                            bestWeapon = sourceSlot;
                        }
                        else {
                            final ItemStack anotherStack2 = InventoryCleaner.mc.field_71439_g.field_71071_by.func_70301_a(bestWeapon);
                            if (ItemUtils.getEnchantment(itemStack, Enchantment.field_77345_t) < ItemUtils.getEnchantment(anotherStack2, Enchantment.field_77345_t)) {
                                continue;
                            }
                            bestWeapon = sourceSlot;
                        }
                    }
                }
                if (bestWeapon == -1) {
                    return null;
                }
                return new SortCallback(bestWeapon, true);
            }
            case "food": {
                for (final Map.Entry<Integer, ItemStack> stackEntry2 : this.getItems(9, 45).entrySet()) {
                    final int sourceSlot2 = stackEntry2.getKey();
                    final ItemStack itemStack2 = stackEntry2.getValue();
                    if (itemStack2.func_77973_b() instanceof ItemFood && !this.getType(sourceSlot2).equalsIgnoreCase("Food")) {
                        return new SortCallback(sourceSlot2, slotStack == null || !(slotStack.func_77973_b() instanceof ItemFood));
                    }
                }
                break;
            }
            case "block": {
                for (final Map.Entry<Integer, ItemStack> stackEntry2 : this.getItems(9, 45).entrySet()) {
                    final int sourceSlot2 = stackEntry2.getKey();
                    final ItemStack itemStack2 = stackEntry2.getValue();
                    if (itemStack2.func_77973_b() instanceof ItemBlock && !this.getType(sourceSlot2).equalsIgnoreCase("Block")) {
                        return new SortCallback(sourceSlot2, slotStack == null || !(slotStack.func_77973_b() instanceof ItemBlock));
                    }
                }
                break;
            }
            case "water": {
                for (final Map.Entry<Integer, ItemStack> stackEntry2 : this.getItems(9, 45).entrySet()) {
                    final int sourceSlot2 = stackEntry2.getKey();
                    final ItemStack itemStack2 = stackEntry2.getValue();
                    if (itemStack2.func_77973_b() instanceof ItemBucket && ((ItemBucket)itemStack2.func_77973_b()).field_77876_a == Blocks.field_150358_i && !this.getType(sourceSlot2).equalsIgnoreCase("Water")) {
                        return new SortCallback(sourceSlot2, slotStack == null || !(slotStack.func_77973_b() instanceof ItemBucket) || ((ItemBucket)slotStack.func_77973_b()).field_77876_a != Blocks.field_150355_j);
                    }
                }
                break;
            }
        }
        return null;
    }
    
    private Map<Integer, ItemStack> getItems(final int start, final int end) {
        final Map<Integer, ItemStack> itemsMap = new HashMap<Integer, ItemStack>();
        for (int i = end - 1; i >= start; --i) {
            final ItemStack itemStack = InventoryCleaner.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (i < 36 || i > 44 || !this.getType(i).equalsIgnoreCase("Ignore")) {
                if (itemStack != null && itemStack.func_77973_b() != null && System.currentTimeMillis() - ((IItemStack)itemStack).getItemDelay() >= this.itemDelayValue.asInteger()) {
                    itemsMap.put(i, itemStack);
                }
            }
        }
        return itemsMap;
    }
    
    public class SortCallback
    {
        private final int itemSlot;
        private final boolean replaceCurrentItem;
        
        public SortCallback(final int itemSlot, final boolean replaceCurrentItem) {
            this.itemSlot = itemSlot;
            this.replaceCurrentItem = replaceCurrentItem;
        }
        
        public int getItemSlot() {
            return this.itemSlot;
        }
        
        public boolean isReplaceCurrentItem() {
            return this.replaceCurrentItem;
        }
    }
}
