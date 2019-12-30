// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui;

import java.io.IOException;
import com.google.gson.internal.LinkedTreeMap;
import net.ccbluex.liquidbounce.utils.misc.NetworkUtils;
import com.google.gson.Gson;
import java.util.List;
import java.util.Iterator;
import net.minecraft.client.gui.FontRenderer;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.GuiButton;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.GuiScreen;

public class GuiServerStatus extends GuiScreen
{
    private final GuiScreen prevGui;
    private final Map<String, String> status;
    
    public GuiServerStatus(final GuiScreen prevGui) {
        this.status = new HashMap<String, String>();
        this.prevGui = prevGui;
    }
    
    public void func_73866_w_() {
        new Thread(this::loadInformations).start();
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 145, "Back"));
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146278_c(0);
        int i = this.field_146295_m / 4 + 40;
        Gui.func_73734_a(this.field_146294_l / 2 - 115, i - 5, this.field_146294_l / 2 + 115, this.field_146295_m / 4 + 43 + (this.status.keySet().isEmpty() ? 10 : (this.status.keySet().size() * Fonts.font40.field_78288_b)), Integer.MIN_VALUE);
        if (this.status.isEmpty()) {
            this.func_73732_a((FontRenderer)Fonts.font40, "Loading...", this.field_146294_l / 2, this.field_146295_m / 4 + 40, Color.WHITE.getRGB());
        }
        else {
            for (final String server : this.status.keySet()) {
                final String color = this.status.get(server);
                this.func_73732_a((FontRenderer)Fonts.font40, "§c§l" + server + ": " + (color.equalsIgnoreCase("red") ? "§c" : (color.equalsIgnoreCase("yellow") ? "§e" : "§a")) + (color.equalsIgnoreCase("red") ? "Offline" : (color.equalsIgnoreCase("yellow") ? "Slow" : "Online")), this.field_146294_l / 2, i, Color.WHITE.getRGB());
                i += Fonts.font40.field_78288_b;
            }
        }
        Fonts.fontBold180.drawCenteredString("Server Status", (int)(this.field_146294_l / 2.0f), (int)(this.field_146295_m / 8.0f + 5.0f), 4673984, true);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    private void loadInformations() {
        this.status.clear();
        try {
            final List<LinkedTreeMap> linkedTreeMaps = (List<LinkedTreeMap>)new Gson().fromJson(NetworkUtils.readContent("https://status.mojang.com/check"), (Class)List.class);
            for (final LinkedTreeMap linkedTreeMap : linkedTreeMaps) {
                for (final Map.Entry<String, String> entry : linkedTreeMap.entrySet()) {
                    this.status.put(entry.getKey(), entry.getValue());
                }
            }
        }
        catch (IOException e) {
            this.status.put("status.mojang.com/check", "red");
        }
    }
    
    protected void func_146284_a(final GuiButton button) {
        switch (button.field_146127_k) {
            case 1: {
                this.field_146297_k.func_147108_a(this.prevGui);
                break;
            }
        }
    }
    
    protected void func_73869_a(final char typedChar, final int keyCode) throws IOException {
        if (1 == keyCode) {
            this.field_146297_k.func_147108_a(this.prevGui);
            return;
        }
        super.func_73869_a(typedChar, keyCode);
    }
}
