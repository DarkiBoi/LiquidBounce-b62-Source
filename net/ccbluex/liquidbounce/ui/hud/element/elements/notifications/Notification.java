// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.hud.element.elements.notifications;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.hud.element.elements.Notifications;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Notification
{
    private final String message;
    public float x;
    public int textLength;
    private float stay;
    public FadeState fadeState;
    
    public Notification(final String message) {
        this.fadeState = FadeState.IN;
        this.message = message;
    }
    
    public void draw(final Notifications notifications) {
        final int[] location = notifications.getLocationFromFacing();
        this.textLength = Fonts.font35.func_78256_a(this.message);
        Gui.func_73734_a(location[0] - (int)this.x, location[1], location[0] - (int)this.x + 8 + this.textLength, location[1] - 20, Color.BLACK.getRGB());
        Gui.func_73734_a(location[0] - (int)this.x - 5, location[1], location[0] - (int)this.x, location[1] - 20, new Color(0, 160, 255).getRGB());
        Fonts.font35.drawString(this.message, location[0] - this.x + 4.0f, (float)(location[1] - 14), Integer.MAX_VALUE);
        GlStateManager.func_179117_G();
        final int delta = RenderUtils.deltaTime;
        switch (this.fadeState) {
            case IN: {
                if (this.x < this.textLength + 8) {
                    this.x += 0.2f * delta;
                }
                else {
                    this.fadeState = FadeState.STAY;
                }
                this.stay = 60.0f;
                if (this.x > this.textLength + 8) {
                    this.x = (float)(this.textLength + 8);
                    break;
                }
                break;
            }
            case STAY: {
                if (this.stay > 0.0f) {
                    this.stay -= 0.2f * delta;
                    break;
                }
                this.fadeState = FadeState.OUT;
                break;
            }
            case OUT: {
                if (this.x > 0.0f) {
                    this.x -= 0.2f * delta;
                    break;
                }
                this.fadeState = FadeState.END;
                break;
            }
            case END: {
                LiquidBounce.CLIENT.hud.removeNotification(this);
                break;
            }
        }
    }
}
