// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.hud.config;

import net.ccbluex.liquidbounce.ui.hud.DefaultHUD;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.ui.hud.element.Facing;
import com.google.gson.JsonNull;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Field;
import java.util.Iterator;
import com.google.gson.JsonElement;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.valuesystem.Value;
import net.ccbluex.liquidbounce.ui.hud.NameIndex;
import com.google.gson.JsonObject;
import net.ccbluex.liquidbounce.ui.hud.element.Element;
import net.ccbluex.liquidbounce.ui.hud.HUD;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Config
{
    private JsonArray jsonArray;
    
    public Config(final String config) {
        this.jsonArray = new JsonArray();
        this.jsonArray = (JsonArray)new Gson().fromJson(config, (Class)JsonArray.class);
    }
    
    public Config(final HUD hud) {
        this.jsonArray = new JsonArray();
        for (final Element element : hud.getElements()) {
            final JsonObject elementObject = new JsonObject();
            elementObject.addProperty("Type", NameIndex.getName(element.getClass()));
            elementObject.addProperty("X", (Number)element.getX());
            elementObject.addProperty("Y", (Number)element.getY());
            elementObject.addProperty("Scale", (Number)element.getScale());
            elementObject.addProperty("HorizontalFacing", element.getFacing().getHorizontal().getName());
            elementObject.addProperty("VerticalFacing", element.getFacing().getVertical().getName());
            for (final Field field : element.getClass().getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    final Object o = field.get(element);
                    if (o instanceof Value) {
                        final Value value = (Value)o;
                        elementObject.addProperty(value.getValueName(), String.valueOf(value.asObject()));
                    }
                    if (o instanceof FontRenderer) {
                        final JsonObject fontObject = new JsonObject();
                        final Object[] fontDetails = Fonts.getFontDetails((FontRenderer)o);
                        fontObject.addProperty("fontName", (String)fontDetails[0]);
                        fontObject.addProperty("fontSize", (Number)(int)fontDetails[1]);
                        elementObject.add("font", (JsonElement)fontObject);
                    }
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            this.jsonArray.add((JsonElement)elementObject);
        }
    }
    
    public String toJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)this.jsonArray);
    }
    
    public HUD toHUD() {
        final HUD hud = new HUD();
        try {
            for (final JsonElement jsonElement : this.jsonArray) {
                try {
                    if (jsonElement == null || jsonElement instanceof JsonNull || !jsonElement.isJsonObject()) {
                        continue;
                    }
                    final JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if (!jsonObject.has("Type")) {
                        continue;
                    }
                    final String type = jsonObject.get("Type").getAsString();
                    for (final Class c : NameIndex.NAME_MAP.keySet()) {
                        final String classType = NameIndex.NAME_MAP.get(c);
                        if (classType.equals(type)) {
                            final Element element = c.newInstance();
                            element.setX(jsonObject.get("X").getAsInt());
                            element.setY(jsonObject.get("Y").getAsInt());
                            element.setScale(jsonObject.get("Scale").getAsFloat());
                            element.setFacing(new Facing(Facing.Horizontal.getByName(jsonObject.get("HorizontalFacing").getAsString()), Facing.Vertical.getByName(jsonObject.get("VerticalFacing").getAsString())));
                            for (final Field field : element.getClass().getDeclaredFields()) {
                                try {
                                    field.setAccessible(true);
                                    final Object o = field.get(element);
                                    if (o instanceof Value) {
                                        final Value value = (Value)o;
                                        if (jsonObject.has(value.getValueName())) {
                                            value.setValue(jsonObject.get(value.getValueName()).getAsString());
                                        }
                                    }
                                    if (o instanceof FontRenderer && jsonObject.has("font")) {
                                        final JsonObject fontObject = jsonObject.get("font").getAsJsonObject();
                                        field.set(element, Fonts.getFontRenderer(fontObject.get("fontName").getAsString(), fontObject.get("fontSize").getAsInt()));
                                    }
                                }
                                catch (Exception e) {
                                    ClientUtils.getLogger().error("Error while loading value of custom hud element from config.", (Throwable)e);
                                }
                            }
                            hud.addElement(element);
                            break;
                        }
                    }
                }
                catch (Exception e2) {
                    ClientUtils.getLogger().error("Error while loading custom hud element from config.", (Throwable)e2);
                }
            }
        }
        catch (Exception e3) {
            ClientUtils.getLogger().error("Error while loading custom hud config.", (Throwable)e3);
            return new DefaultHUD();
        }
        return hud;
    }
}
