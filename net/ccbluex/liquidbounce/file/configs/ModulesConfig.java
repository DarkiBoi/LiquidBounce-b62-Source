// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.file.configs;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import net.ccbluex.liquidbounce.features.module.Module;
import java.util.Iterator;
import com.google.gson.JsonObject;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import com.google.gson.JsonElement;
import java.util.Map;
import com.google.gson.JsonNull;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import net.ccbluex.liquidbounce.utils.misc.JsonUtils;
import java.io.File;
import net.ccbluex.liquidbounce.file.FileConfig;

public class ModulesConfig extends FileConfig
{
    public ModulesConfig(final File file) {
        super(file);
    }
    
    @Override
    protected void loadConfig() throws IOException {
        final JsonElement jsonElement = JsonUtils.JSON_PARSER.parse((Reader)new BufferedReader(new FileReader(this.getFile())));
        if (jsonElement instanceof JsonNull) {
            return;
        }
        for (final Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
            final Module module = ModuleManager.getModule(entry.getKey());
            if (module != null) {
                final JsonObject jsonModule = (JsonObject)entry.getValue();
                module.setState(jsonModule.get("State").getAsBoolean());
                module.setKeyBind(jsonModule.get("KeyBind").getAsInt());
            }
        }
    }
    
    @Override
    protected void saveConfig() throws IOException {
        final JsonObject jsonObject = new JsonObject();
        for (final Module module : ModuleManager.getModules()) {
            final JsonObject jsonMod = new JsonObject();
            jsonMod.addProperty("State", Boolean.valueOf(module.getState()));
            jsonMod.addProperty("KeyBind", (Number)module.getKeyBind());
            jsonObject.add(module.getName(), (JsonElement)jsonMod);
        }
        final PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        printWriter.println(JsonUtils.PRETTY_GSON.toJson((JsonElement)jsonObject));
        printWriter.close();
    }
}
