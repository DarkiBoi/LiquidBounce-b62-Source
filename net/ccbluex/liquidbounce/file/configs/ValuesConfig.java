// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.file.configs;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.features.module.Module;
import java.util.Iterator;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.valuesystem.types.FontValue;
import net.ccbluex.liquidbounce.valuesystem.Value;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.ui.altmanager.sub.altgenerator.thealtening.GuiTheAltening;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.special.BungeeCordSpoof;
import net.ccbluex.liquidbounce.features.special.AntiForge;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import com.google.gson.JsonElement;
import java.util.Map;
import com.google.gson.JsonObject;
import com.google.gson.JsonNull;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import net.ccbluex.liquidbounce.utils.misc.JsonUtils;
import java.io.File;
import net.ccbluex.liquidbounce.file.FileConfig;

public class ValuesConfig extends FileConfig
{
    public ValuesConfig(final File file) {
        super(file);
    }
    
    @Override
    protected void loadConfig() throws IOException {
        final JsonElement jsonElement = JsonUtils.JSON_PARSER.parse((Reader)new BufferedReader(new FileReader(this.getFile())));
        if (jsonElement instanceof JsonNull) {
            return;
        }
        final JsonObject jsonObject = (JsonObject)jsonElement;
        for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            if (entry.getKey().equalsIgnoreCase("targets")) {
                final JsonObject jsonValue = (JsonObject)entry.getValue();
                if (jsonValue.has("TargetPlayer")) {
                    EntityUtils.targetPlayer = jsonValue.get("TargetPlayer").getAsBoolean();
                }
                if (jsonValue.has("TargetMobs")) {
                    EntityUtils.targetMobs = jsonValue.get("TargetMobs").getAsBoolean();
                }
                if (jsonValue.has("TargetAnimals")) {
                    EntityUtils.targetAnimals = jsonValue.get("TargetAnimals").getAsBoolean();
                }
                if (jsonValue.has("TargetInvisible")) {
                    EntityUtils.targetInvisible = jsonValue.get("TargetInvisible").getAsBoolean();
                }
                if (!jsonValue.has("TargetDead")) {
                    continue;
                }
                EntityUtils.targetDead = jsonValue.get("TargetDead").getAsBoolean();
            }
            else if (entry.getKey().equalsIgnoreCase("features")) {
                final JsonObject jsonValue = (JsonObject)entry.getValue();
                if (jsonValue.has("AntiForge")) {
                    AntiForge.enabled = jsonValue.get("AntiForge").getAsBoolean();
                }
                if (jsonValue.has("AntiForgeFML")) {
                    AntiForge.blockFML = jsonValue.get("AntiForgeFML").getAsBoolean();
                }
                if (jsonValue.has("AntiForgeProxy")) {
                    AntiForge.blockProxyPacket = jsonValue.get("AntiForgeProxy").getAsBoolean();
                }
                if (jsonValue.has("AntiForgePayloads")) {
                    AntiForge.blockPayloadPackets = jsonValue.get("AntiForgePayloads").getAsBoolean();
                }
                if (!jsonValue.has("BungeeSpoof")) {
                    continue;
                }
                BungeeCordSpoof.enabled = jsonValue.get("BungeeSpoof").getAsBoolean();
            }
            else if (entry.getKey().equalsIgnoreCase("mainmenu")) {
                final JsonObject jsonValue = (JsonObject)entry.getValue();
                if (!jsonValue.has("Style")) {
                    continue;
                }
                LiquidBounce.CLIENT.mainMenuStyle = jsonValue.get("Style").getAsString();
            }
            else if (entry.getKey().equalsIgnoreCase("thealtening")) {
                final JsonObject jsonValue = (JsonObject)entry.getValue();
                if (!jsonValue.has("API-Key")) {
                    continue;
                }
                GuiTheAltening.Companion.setApiKey(jsonValue.get("API-Key").getAsString());
            }
            else {
                final Module module = ModuleManager.getModule(entry.getKey());
                if (module == null) {
                    continue;
                }
                final JsonObject jsonModule = (JsonObject)entry.getValue();
                for (final Value moduleValue : module.getValues()) {
                    final JsonElement element = jsonModule.get(moduleValue.getValueName());
                    if (element != null) {
                        if (moduleValue instanceof FontValue) {
                            final JsonObject valueObject = element.getAsJsonObject();
                            final FontRenderer fontRenderer = Fonts.getFontRenderer(valueObject.get("fontName").getAsString(), valueObject.get("fontSize").getAsInt());
                            if (fontRenderer == null) {
                                continue;
                            }
                            moduleValue.setValueSilent(fontRenderer);
                        }
                        else {
                            moduleValue.setValueSilent(element.getAsString());
                        }
                    }
                }
            }
        }
    }
    
    @Override
    protected void saveConfig() throws IOException {
        final JsonObject jsonObject = new JsonObject();
        final JsonObject jsonTargets = new JsonObject();
        jsonTargets.addProperty("TargetPlayer", Boolean.valueOf(EntityUtils.targetPlayer));
        jsonTargets.addProperty("TargetMobs", Boolean.valueOf(EntityUtils.targetMobs));
        jsonTargets.addProperty("TargetAnimals", Boolean.valueOf(EntityUtils.targetAnimals));
        jsonTargets.addProperty("TargetInvisible", Boolean.valueOf(EntityUtils.targetInvisible));
        jsonTargets.addProperty("TargetDead", Boolean.valueOf(EntityUtils.targetDead));
        jsonObject.add("targets", (JsonElement)jsonTargets);
        final JsonObject jsonFeatures = new JsonObject();
        jsonFeatures.addProperty("AntiForge", Boolean.valueOf(AntiForge.enabled));
        jsonFeatures.addProperty("AntiForgeFML", Boolean.valueOf(AntiForge.blockFML));
        jsonFeatures.addProperty("AntiForgeProxy", Boolean.valueOf(AntiForge.blockProxyPacket));
        jsonFeatures.addProperty("AntiForgePayloads", Boolean.valueOf(AntiForge.blockPayloadPackets));
        jsonFeatures.addProperty("BungeeSpoof", Boolean.valueOf(BungeeCordSpoof.enabled));
        jsonObject.add("features", (JsonElement)jsonFeatures);
        final JsonObject jsonMainMenu = new JsonObject();
        jsonMainMenu.addProperty("Style", LiquidBounce.CLIENT.mainMenuStyle);
        jsonObject.add("mainmenu", (JsonElement)jsonMainMenu);
        final JsonObject theAlteningObject = new JsonObject();
        theAlteningObject.addProperty("API-Key", GuiTheAltening.Companion.getApiKey());
        jsonObject.add("thealtening", (JsonElement)theAlteningObject);
        final JsonObject jsonMod;
        final Iterator<Value> iterator;
        Value value;
        JsonObject valueObject;
        Object[] obj;
        final JsonObject jsonObject2;
        ModuleManager.getModules().stream().filter(module -> !module.getValues().isEmpty()).forEach(module -> {
            jsonMod = new JsonObject();
            module.getValues().iterator();
            while (iterator.hasNext()) {
                value = iterator.next();
                if (value instanceof FontValue) {
                    valueObject = new JsonObject();
                    if (value.asObject() == null) {
                        continue;
                    }
                    else {
                        obj = Fonts.getFontDetails((FontRenderer)value.asObject());
                        if (obj == null) {
                            continue;
                        }
                        else {
                            valueObject.addProperty("fontName", (String)obj[0]);
                            valueObject.addProperty("fontSize", (Number)(int)obj[1]);
                            jsonMod.add(value.getValueName(), (JsonElement)valueObject);
                        }
                    }
                }
                else {
                    jsonMod.addProperty(value.getValueName(), String.valueOf(value.asObject()));
                }
            }
            jsonObject2.add(module.getName(), (JsonElement)jsonMod);
            return;
        });
        final PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        printWriter.println(JsonUtils.PRETTY_GSON.toJson((JsonElement)jsonObject));
        printWriter.close();
    }
}
