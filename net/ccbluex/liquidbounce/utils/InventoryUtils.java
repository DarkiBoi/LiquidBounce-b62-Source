// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;

public final class InventoryUtils
{
    public static int findItem(final int startSlot, final int endSlot, final Item item) {
        for (int i = startSlot; i < endSlot; ++i) {
            final ItemStack stack = Minecraft.func_71410_x().field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (stack != null && stack.func_77973_b() == item) {
                return i;
            }
        }
        return -1;
    }
    
    public static boolean hasSpaceHotbar() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = Minecraft.func_71410_x().field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (itemStack == null) {
                return true;
            }
        }
        return false;
    }
}
