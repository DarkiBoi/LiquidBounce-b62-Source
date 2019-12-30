// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge;

import java.util.Map;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.launch.MixinBootstrap;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class MixinLoader implements IFMLLoadingPlugin
{
    public MixinLoader() {
        System.out.println("[LiquidBounce] Injecting with IFMLLoadingPlugin.");
        MixinBootstrap.init();
        Mixins.addConfiguration("liquidbounce.forge.mixins.json");
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
    }
    
    public String[] getASMTransformerClass() {
        return new String[0];
    }
    
    public String getModContainerClass() {
        return null;
    }
    
    public String getSetupClass() {
        return null;
    }
    
    public void injectData(final Map<String, Object> data) {
    }
    
    public String getAccessTransformerClass() {
        return null;
    }
}
