// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.tabs;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Collection;
import java.util.Iterator;
import com.google.gson.JsonObject;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;
import com.google.gson.JsonElement;
import java.util.Map;
import net.ccbluex.liquidbounce.utils.misc.NetworkUtils;
import com.google.gson.JsonParser;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;

@SideOnly(Side.CLIENT)
public final class HeadsTab extends CreativeTabs
{
    private final List<ItemStack> heads;
    
    public HeadsTab() {
        super("Heads");
        this.heads = new ArrayList<ItemStack>();
        this.func_78025_a("item_search.png");
        try {
            ClientUtils.getLogger().info("Loading heads...");
            final JsonElement headsConfiguration = new JsonParser().parse(NetworkUtils.readContent("https://ccbluex.github.io/FileCloud/LiquidBounce/heads.json"));
            if (!headsConfiguration.isJsonObject()) {
                return;
            }
            final JsonObject headsConf = headsConfiguration.getAsJsonObject();
            if (headsConf.get("enabled").getAsBoolean()) {
                final String url = headsConf.get("url").getAsString();
                ClientUtils.getLogger().info("Loading heads from " + url + "...");
                final JsonElement headsElement = new JsonParser().parse(NetworkUtils.readContent(url));
                if (!headsElement.isJsonObject()) {
                    ClientUtils.getLogger().error("Something is wrong, the heads json is not a JsonObject!");
                    return;
                }
                final JsonObject headsObject = headsElement.getAsJsonObject();
                for (final Map.Entry<String, JsonElement> entry : headsObject.entrySet()) {
                    final JsonObject headElement = entry.getValue().getAsJsonObject();
                    this.heads.add(ItemUtils.createItem("skull 1 3 {display:{Name:\"" + headElement.get("name").getAsString() + "\"},SkullOwner:{Id:\"" + headElement.get("uuid").getAsString() + "\",Properties:{textures:[{Value:\"" + headElement.get("value").getAsString() + "\"}]}}}"));
                }
                ClientUtils.getLogger().info("Loaded " + this.heads.size() + " heads from HeadDB.");
            }
            else {
                ClientUtils.getLogger().info("Heads are disabled.");
            }
        }
        catch (Exception e) {
            ClientUtils.getLogger().error("Error while reading heads.", (Throwable)e);
        }
    }
    
    public void func_78018_a(final List<ItemStack> itemList) {
        itemList.addAll(this.heads);
    }
    
    public Item func_78016_d() {
        return Items.field_151144_bL;
    }
    
    public String func_78024_c() {
        return "Heads";
    }
    
    public boolean hasSearchBar() {
        return true;
    }
}
