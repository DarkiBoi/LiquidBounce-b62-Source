// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.vanilla;

import net.ccbluex.liquidbounce.script.remapper.injection.transformers.AbstractJavaLinkerTransformer;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.launch.MixinBootstrap;
import net.minecraft.launchwrapper.LaunchClassLoader;
import java.util.Collection;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.launchwrapper.ITweaker;

public class TweakerMixinLoader implements ITweaker
{
    private final List<String> list;
    
    public TweakerMixinLoader() {
        this.list = new ArrayList<String>();
    }
    
    public void acceptOptions(final List<String> args, final File gameDir, final File assetsDir, final String profile) {
        this.list.addAll(args);
        if (!args.contains("--version") && profile != null) {
            this.list.add("--version");
            this.list.add(profile);
        }
        if (!args.contains("--assetDir") && assetsDir != null) {
            this.list.add("--assetDir");
            this.list.add(assetsDir.getAbsolutePath());
        }
        if (!args.contains("--gameDir") && gameDir != null) {
            this.list.add("--gameDir");
            this.list.add(gameDir.getAbsolutePath());
        }
    }
    
    public void injectIntoClassLoader(final LaunchClassLoader classLoader) {
        System.out.println("[LiquidBounce] Injecting with TweakerMixinLoader.");
        MixinBootstrap.init();
        Mixins.addConfiguration("liquidbounce.forge.mixins.json");
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
        classLoader.registerTransformer(AbstractJavaLinkerTransformer.class.getName());
    }
    
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }
    
    public String[] getLaunchArguments() {
        return this.list.toArray(new String[0]);
    }
}
