// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.ccbluex.liquidbounce.event.events.Render2DEvent;
import net.ccbluex.liquidbounce.utils.ChatUtils;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.event.EventListener;
import net.ccbluex.liquidbounce.features.command.Command;

public class TacoCommand extends Command implements EventListener
{
    private boolean toggle;
    private int image;
    private int running;
    private final ResourceLocation[] tacoTextures;
    
    public TacoCommand() {
        super("taco", null);
        this.tacoTextures = new ResourceLocation[] { new ResourceLocation("liquidbounce/taco/1.png"), new ResourceLocation("liquidbounce/taco/2.png"), new ResourceLocation("liquidbounce/taco/3.png"), new ResourceLocation("liquidbounce/taco/4.png"), new ResourceLocation("liquidbounce/taco/5.png"), new ResourceLocation("liquidbounce/taco/6.png"), new ResourceLocation("liquidbounce/taco/7.png"), new ResourceLocation("liquidbounce/taco/8.png"), new ResourceLocation("liquidbounce/taco/9.png"), new ResourceLocation("liquidbounce/taco/10.png"), new ResourceLocation("liquidbounce/taco/11.png"), new ResourceLocation("liquidbounce/taco/12.png") };
        LiquidBounce.CLIENT.eventManager.registerListener(this);
    }
    
    @Override
    public void execute(final String[] args) {
        this.toggle = !this.toggle;
        ChatUtils.displayChatMessage(this.toggle ? "§aTACO TACO TACO. :)" : "§cYou made the little taco sad! :(");
    }
    
    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        if (!this.toggle) {
            return;
        }
        ++this.running;
        final ScaledResolution scaledResolution = new ScaledResolution(TacoCommand.mc);
        RenderUtils.drawImage(this.tacoTextures[this.image], this.running, scaledResolution.func_78328_b() - 60, 64, 32);
        if (scaledResolution.func_78326_a() <= this.running) {
            this.running = -64;
        }
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (!this.toggle) {
            this.image = 0;
            return;
        }
        ++this.image;
        if (this.image >= this.tacoTextures.length) {
            this.image = 0;
        }
    }
    
    @Override
    public boolean handleEvents() {
        return true;
    }
}
