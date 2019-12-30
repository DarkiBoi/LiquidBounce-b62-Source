// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui;

import java.io.IOException;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.special.AntiForge;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiAntiForge extends GuiScreen
{
    private final GuiScreen prevGui;
    private GuiButton enabledButton;
    private GuiButton fmlButton;
    private GuiButton proxyButton;
    private GuiButton payloadButton;
    
    public GuiAntiForge(final GuiScreen prevGui) {
        this.prevGui = prevGui;
    }
    
    public void func_73866_w_() {
        this.field_146292_n.add(this.enabledButton = new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 35, "Enabled (" + (AntiForge.enabled ? "On" : "Off") + ")"));
        this.field_146292_n.add(this.fmlButton = new GuiButton(2, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 50 + 25, "Block FML (" + (AntiForge.blockFML ? "On" : "Off") + ")"));
        this.field_146292_n.add(this.proxyButton = new GuiButton(3, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 50 + 50, "Block FML Proxy Packet (" + (AntiForge.blockProxyPacket ? "On" : "Off") + ")"));
        this.field_146292_n.add(this.payloadButton = new GuiButton(4, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 50 + 75, "Block Payload Packets (" + (AntiForge.blockPayloadPackets ? "On" : "Off") + ")"));
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 55 + 100 + 5, "Back"));
    }
    
    protected void func_146284_a(final GuiButton button) {
        switch (button.field_146127_k) {
            case 1: {
                AntiForge.enabled = !AntiForge.enabled;
                this.enabledButton.field_146126_j = "Enabled (" + (AntiForge.enabled ? "On" : "Off") + ")";
                LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.valuesConfig);
                break;
            }
            case 2: {
                AntiForge.blockFML = !AntiForge.blockFML;
                this.fmlButton.field_146126_j = "Block FML (" + (AntiForge.blockFML ? "On" : "Off") + ")";
                LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.valuesConfig);
                break;
            }
            case 3: {
                AntiForge.blockProxyPacket = !AntiForge.blockProxyPacket;
                this.proxyButton.field_146126_j = "Block FML Proxy Packet (" + (AntiForge.blockProxyPacket ? "On" : "Off") + ")";
                LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.valuesConfig);
                break;
            }
            case 4: {
                AntiForge.blockPayloadPackets = !AntiForge.blockPayloadPackets;
                this.payloadButton.field_146126_j = "Block Payload Packets (" + (AntiForge.blockPayloadPackets ? "On" : "Off") + ")";
                LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.valuesConfig);
                break;
            }
            case 0: {
                this.field_146297_k.func_147108_a(this.prevGui);
                break;
            }
        }
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146278_c(0);
        Fonts.fontBold180.drawCenteredString("AntiForge", (int)(this.field_146294_l / 2.0f), (int)(this.field_146295_m / 8.0f + 5.0f), 4673984, true);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    protected void func_73869_a(final char typedChar, final int keyCode) throws IOException {
        if (1 == keyCode) {
            this.field_146297_k.func_147108_a(this.prevGui);
            return;
        }
        super.func_73869_a(typedChar, keyCode);
    }
}
