// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.file.configs;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import com.google.gson.JsonArray;
import net.minecraft.block.Block;
import com.google.gson.JsonElement;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import net.ccbluex.liquidbounce.utils.misc.JsonUtils;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.XRay;
import java.io.File;
import net.ccbluex.liquidbounce.file.FileConfig;

public class XRayConfig extends FileConfig
{
    public XRayConfig(final File file) {
        super(file);
    }
    
    @Override
    protected void loadConfig() throws IOException {
        final XRay xRay = (XRay)ModuleManager.getModule(XRay.class);
        if (xRay == null) {
            ClientUtils.getLogger().error("[FileManager] Failed to find xray module.");
            return;
        }
        final JsonArray jsonArray = JsonUtils.JSON_PARSER.parse((Reader)new BufferedReader(new FileReader(this.getFile()))).getAsJsonArray();
        xRay.xrayBlocks.clear();
        for (final JsonElement jsonElement : jsonArray) {
            try {
                final Block block = Block.func_149684_b(jsonElement.getAsString());
                if (xRay.xrayBlocks.contains(block)) {
                    ClientUtils.getLogger().error("[FileManager] Skipped xray block '" + block.getRegistryName() + "' because the block is already added.");
                }
                else {
                    xRay.xrayBlocks.add(block);
                }
            }
            catch (Throwable throwable) {
                ClientUtils.getLogger().error("[FileManager] Failed to add block to xray.", throwable);
            }
        }
    }
    
    @Override
    protected void saveConfig() throws IOException {
        final XRay xRay = (XRay)ModuleManager.getModule(XRay.class);
        if (xRay == null) {
            ClientUtils.getLogger().error("[FileManager] Failed to find xray module.");
            return;
        }
        final JsonArray jsonArray = new JsonArray();
        for (final Block block : xRay.xrayBlocks) {
            jsonArray.add(JsonUtils.PRETTY_GSON.toJsonTree((Object)Block.func_149682_b(block)));
        }
        final PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        printWriter.println(JsonUtils.PRETTY_GSON.toJson((JsonElement)jsonArray));
        printWriter.close();
    }
}
