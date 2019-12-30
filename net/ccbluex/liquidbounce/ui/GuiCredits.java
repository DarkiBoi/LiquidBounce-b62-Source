// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import java.io.IOException;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import com.google.gson.JsonElement;
import java.util.Map;
import com.google.gson.JsonObject;
import com.google.gson.JsonNull;
import net.ccbluex.liquidbounce.utils.misc.NetworkUtils;
import com.google.gson.JsonParser;
import java.util.Iterator;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.FontRenderer;
import java.awt.Color;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public class GuiCredits extends GuiScreen
{
    private final GuiScreen prevGui;
    private GuiList list;
    private final List<Credit> credits;
    
    public GuiCredits(final GuiScreen prevGui) {
        this.credits = new ArrayList<Credit>();
        this.prevGui = prevGui;
    }
    
    public void func_73866_w_() {
        (this.list = new GuiList(this)).func_148134_d(7, 8);
        this.list.func_148144_a(-1, false, 0, 0);
        new Thread(this::loadCredits).start();
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m - 30, "Back"));
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146278_c(0);
        this.list.func_148128_a(mouseX, mouseY, partialTicks);
        Gui.func_73734_a(this.field_146294_l / 4, 40, this.field_146294_l, this.field_146295_m - 40, Integer.MIN_VALUE);
        if (this.list.getSelectedSlot() != -1) {
            final Credit credit = this.credits.get(this.list.getSelectedSlot());
            int y = 45;
            Fonts.font40.func_175065_a("Name: " + credit.getName(), (float)(this.field_146294_l / 4 + 5), (float)y, Color.WHITE.getRGB(), true);
            if (credit.getTwitterName() != null) {
                y += Fonts.font40.field_78288_b;
                Fonts.font40.func_175065_a("Twitter: " + credit.getTwitterName(), (float)(this.field_146294_l / 4 + 5), (float)y, Color.WHITE.getRGB(), true);
            }
            if (credit.getYoutubeName() != null) {
                y += Fonts.font40.field_78288_b;
                Fonts.font40.func_175065_a("YouTube: " + credit.getYoutubeName(), (float)(this.field_146294_l / 4 + 5), (float)y, Color.WHITE.getRGB(), true);
            }
            y += Fonts.font40.field_78288_b;
            for (final String s : credit.getCredits()) {
                y += Fonts.font40.field_78288_b;
                Fonts.font40.func_175065_a(s, (float)(this.field_146294_l / 4 + 5), (float)y, Color.WHITE.getRGB(), true);
            }
        }
        Fonts.font40.drawCenteredString("Credits", this.field_146294_l / 2, 6, 16777215);
        if (this.credits.isEmpty()) {
            this.func_73732_a((FontRenderer)Fonts.font40, "Loading...", this.field_146294_l / 8, this.field_146295_m / 2, Color.WHITE.getRGB());
            RenderUtils.drawLoadingCircle((float)(this.field_146294_l / 8), (float)(this.field_146295_m / 2 - 40));
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    private void loadCredits() {
        this.credits.clear();
        try {
            final JsonElement jsonElement = new JsonParser().parse(NetworkUtils.readContent("https://ccbluex.github.io/FileCloud/LiquidBounce/credits.json"));
            if (jsonElement instanceof JsonNull) {
                return;
            }
            final JsonObject jsonObject = (JsonObject)jsonElement;
            for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                final JsonObject creditJson = entry.getValue().getAsJsonObject();
                final List<String> userCredits = new ArrayList<String>();
                creditJson.get("Credits").getAsJsonObject().entrySet().forEach(stringJsonElementEntry -> userCredits.add(stringJsonElementEntry.getValue().getAsString()));
                this.credits.add(new Credit(this.getInfoFromJson(creditJson, "Name"), this.getInfoFromJson(creditJson, "TwitterName"), this.getInfoFromJson(creditJson, "YouTubeName"), userCredits));
            }
        }
        catch (Exception e) {
            ClientUtils.getLogger().error("Failed to load credits.", (Throwable)e);
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
    
    public void func_146274_d() throws IOException {
        super.func_146274_d();
        this.list.func_178039_p();
    }
    
    private String getInfoFromJson(final JsonObject jsonObject, final String key) {
        return jsonObject.has(key) ? jsonObject.get(key).getAsString() : null;
    }
    
    class Credit
    {
        private final String name;
        private final String twitterName;
        private final String youtubeName;
        private final List<String> credits;
        
        Credit(final String name, final String twitterName, final String youtubeName, final List<String> credits) {
            this.name = name;
            this.twitterName = twitterName;
            this.youtubeName = youtubeName;
            this.credits = credits;
        }
        
        String getName() {
            return this.name;
        }
        
        String getTwitterName() {
            return this.twitterName;
        }
        
        String getYoutubeName() {
            return this.youtubeName;
        }
        
        List<String> getCredits() {
            return this.credits;
        }
    }
    
    private class GuiList extends GuiSlot
    {
        private int selectedSlot;
        
        GuiList(final GuiScreen prevGui) {
            super(Minecraft.func_71410_x(), prevGui.field_146294_l / 4, prevGui.field_146295_m, 40, prevGui.field_146295_m - 40, 15);
        }
        
        protected boolean func_148131_a(final int id) {
            return this.selectedSlot == id;
        }
        
        protected int func_148127_b() {
            return GuiCredits.this.credits.size();
        }
        
        int getSelectedSlot() {
            if (this.selectedSlot > GuiCredits.this.credits.size()) {
                this.selectedSlot = -1;
            }
            return this.selectedSlot;
        }
        
        protected void func_148144_a(final int index, final boolean doubleClick, final int var3, final int var4) {
            this.selectedSlot = index;
        }
        
        protected void func_180791_a(final int entryID, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int mouseXIn, final int mouseYIn) {
            final Credit credit = GuiCredits.this.credits.get(entryID);
            Fonts.font40.drawCenteredString(credit.getName(), this.field_148155_a / 2, p_180791_3_ + 2, Color.WHITE.getRGB(), true);
        }
        
        protected void func_148123_a() {
        }
    }
}
