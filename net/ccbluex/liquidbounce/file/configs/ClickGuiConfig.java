// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.file.configs;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import com.google.gson.JsonElement;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.ui.clickgui.elements.ModuleElement;
import net.ccbluex.liquidbounce.ui.clickgui.elements.Element;
import net.ccbluex.liquidbounce.ui.clickgui.Panel;
import net.ccbluex.liquidbounce.LiquidBounce;
import com.google.gson.JsonObject;
import com.google.gson.JsonNull;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import net.ccbluex.liquidbounce.utils.misc.JsonUtils;
import java.io.File;
import net.ccbluex.liquidbounce.file.FileConfig;

public class ClickGuiConfig extends FileConfig
{
    public ClickGuiConfig(final File file) {
        super(file);
    }
    
    @Override
    protected void loadConfig() throws IOException {
        final JsonElement jsonElement = JsonUtils.JSON_PARSER.parse((Reader)new BufferedReader(new FileReader(this.getFile())));
        if (jsonElement instanceof JsonNull) {
            return;
        }
        final JsonObject jsonObject = (JsonObject)jsonElement;
        for (final Panel panel : LiquidBounce.CLIENT.clickGui.panels) {
            if (!jsonObject.has(panel.getName())) {
                continue;
            }
            try {
                final JsonObject panelObject = jsonObject.getAsJsonObject(panel.getName());
                panel.setOpen(panelObject.get("open").getAsBoolean());
                panel.setVisible(panelObject.get("visible").getAsBoolean());
                panel.setX(panelObject.get("posX").getAsInt());
                panel.setY(panelObject.get("posY").getAsInt());
                for (final Element element : panel.getElements()) {
                    if (!(element instanceof ModuleElement)) {
                        continue;
                    }
                    final ModuleElement moduleElement = (ModuleElement)element;
                    if (!panelObject.has(moduleElement.getModule().getName())) {
                        continue;
                    }
                    try {
                        final JsonObject elementObject = panelObject.getAsJsonObject(moduleElement.getModule().getName());
                        moduleElement.setShowSettings(elementObject.get("Settings").getAsBoolean());
                    }
                    catch (Exception e) {
                        ClientUtils.getLogger().error("Error while loading clickgui module element with the name '" + moduleElement.getModule().getName() + "' (Panel Name: " + panel.getName() + ").", (Throwable)e);
                    }
                }
            }
            catch (Exception e2) {
                ClientUtils.getLogger().error("Error while loading clickgui panel with the name '" + panel.getName() + "'.", (Throwable)e2);
            }
        }
    }
    
    @Override
    protected void saveConfig() throws IOException {
        final JsonObject jsonObject = new JsonObject();
        for (final Panel panel : LiquidBounce.CLIENT.clickGui.panels) {
            final JsonObject panelObject = new JsonObject();
            panelObject.addProperty("open", Boolean.valueOf(panel.getOpen()));
            panelObject.addProperty("visible", Boolean.valueOf(panel.isVisible()));
            panelObject.addProperty("posX", (Number)panel.getX());
            panelObject.addProperty("posY", (Number)panel.getY());
            for (final Element element : panel.getElements()) {
                if (!(element instanceof ModuleElement)) {
                    continue;
                }
                final ModuleElement moduleElement = (ModuleElement)element;
                final JsonObject elementObject = new JsonObject();
                elementObject.addProperty("Settings", Boolean.valueOf(moduleElement.isShowSettings()));
                panelObject.add(moduleElement.getModule().getName(), (JsonElement)elementObject);
            }
            jsonObject.add(panel.getName(), (JsonElement)panelObject);
        }
        final PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        printWriter.println(JsonUtils.PRETTY_GSON.toJson((JsonElement)jsonObject));
        printWriter.close();
    }
}
