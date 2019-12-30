// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.item;

import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;

public final class Armor
{
    private static final int[] HELMET;
    private static final int[] CHESTPLATE;
    private static final int[] LEGGINGS;
    private static final int[] BOOTS;
    
    public static int[] getArmorArray(final int type) {
        switch (type) {
            case 0: {
                return Armor.BOOTS;
            }
            case 1: {
                return Armor.LEGGINGS;
            }
            case 2: {
                return Armor.CHESTPLATE;
            }
            case 3: {
                return Armor.HELMET;
            }
            default: {
                return null;
            }
        }
    }
    
    public static int[] getArmorArray(final ItemArmor itemArmor) {
        switch (itemArmor.field_77881_a) {
            case 3: {
                return Armor.BOOTS;
            }
            case 2: {
                return Armor.LEGGINGS;
            }
            case 1: {
                return Armor.CHESTPLATE;
            }
            case 0: {
                return Armor.HELMET;
            }
            default: {
                return null;
            }
        }
    }
    
    public static int getArmorSlot(final int type) {
        switch (type) {
            case 0: {
                return 8;
            }
            case 1: {
                return 7;
            }
            case 2: {
                return 6;
            }
            case 3: {
                return 5;
            }
            default: {
                return -1;
            }
        }
    }
    
    static {
        HELMET = new int[] { Item.func_150891_b((Item)Items.field_151161_ac), Item.func_150891_b((Item)Items.field_151028_Y), Item.func_150891_b((Item)Items.field_151020_U), Item.func_150891_b((Item)Items.field_151169_ag), Item.func_150891_b((Item)Items.field_151024_Q) };
        CHESTPLATE = new int[] { Item.func_150891_b((Item)Items.field_151163_ad), Item.func_150891_b((Item)Items.field_151030_Z), Item.func_150891_b((Item)Items.field_151023_V), Item.func_150891_b((Item)Items.field_151171_ah), Item.func_150891_b((Item)Items.field_151027_R) };
        LEGGINGS = new int[] { Item.func_150891_b((Item)Items.field_151173_ae), Item.func_150891_b((Item)Items.field_151165_aa), Item.func_150891_b((Item)Items.field_151022_W), Item.func_150891_b((Item)Items.field_151149_ai), Item.func_150891_b((Item)Items.field_151026_S) };
        BOOTS = new int[] { Item.func_150891_b((Item)Items.field_151175_af), Item.func_150891_b((Item)Items.field_151167_ab), Item.func_150891_b((Item)Items.field_151029_X), Item.func_150891_b((Item)Items.field_151151_aj), Item.func_150891_b((Item)Items.field_151021_T) };
    }
}
