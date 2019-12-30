// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.script.api;

import kotlin.jvm.JvmStatic;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007¨\u0006\u0007" }, d2 = { "Lnet/ccbluex/liquidbounce/script/api/Item;", "", "()V", "createItem", "Lnet/minecraft/item/ItemStack;", "itemArguments", "", "LiquidBounce" })
public final class Item
{
    public static final Item INSTANCE;
    
    @JvmStatic
    @NotNull
    public static final ItemStack createItem(@NotNull final String itemArguments) {
        Intrinsics.checkParameterIsNotNull(itemArguments, "itemArguments");
        final ItemStack item = ItemUtils.createItem(itemArguments);
        Intrinsics.checkExpressionValueIsNotNull(item, "ItemUtils.createItem(itemArguments)");
        return item;
    }
    
    private Item() {
    }
    
    static {
        INSTANCE = new Item();
    }
}
