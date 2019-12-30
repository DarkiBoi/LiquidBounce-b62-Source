// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.tabs;

import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;

public final class BlocksTab extends CreativeTabs
{
    public BlocksTab() {
        super("Special blocks");
        this.func_78025_a("item_search.png");
    }
    
    public void func_78018_a(final List<ItemStack> itemList) {
        itemList.add(new ItemStack(Blocks.field_150483_bI));
        itemList.add(new ItemStack(Items.field_151095_cc));
        itemList.add(new ItemStack(Blocks.field_180401_cv));
        itemList.add(new ItemStack(Blocks.field_150380_bt));
        itemList.add(new ItemStack(Blocks.field_150420_aW));
        itemList.add(new ItemStack(Blocks.field_150419_aX));
        itemList.add(new ItemStack(Blocks.field_150458_ak));
        itemList.add(new ItemStack(Blocks.field_150474_ac));
        itemList.add(new ItemStack(Blocks.field_150470_am));
    }
    
    public Item func_78016_d() {
        return new ItemStack(Blocks.field_150483_bI).func_77973_b();
    }
    
    public String func_78024_c() {
        return "Special blocks";
    }
    
    public boolean hasSearchBar() {
        return true;
    }
}
