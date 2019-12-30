// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge;

import java.util.Map;
import net.ccbluex.liquidbounce.script.remapper.injection.transformers.AbstractJavaLinkerTransformer;
import net.ccbluex.liquidbounce.injection.transformers.ForgeNetworkDispatcherTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class TransformerLoader implements IFMLLoadingPlugin
{
    public String[] getASMTransformerClass() {
        return new String[] { ForgeNetworkDispatcherTransformer.class.getName(), AbstractJavaLinkerTransformer.class.getName() };
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
