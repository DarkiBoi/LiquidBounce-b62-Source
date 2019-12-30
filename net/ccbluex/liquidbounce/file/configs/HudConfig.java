// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.file.configs;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import net.ccbluex.liquidbounce.ui.hud.config.Config;
import org.apache.commons.io.FileUtils;
import net.ccbluex.liquidbounce.LiquidBounce;
import java.io.File;
import net.ccbluex.liquidbounce.file.FileConfig;

public class HudConfig extends FileConfig
{
    public HudConfig(final File file) {
        super(file);
    }
    
    @Override
    protected void loadConfig() throws IOException {
        LiquidBounce.CLIENT.hud.clearElements();
        LiquidBounce.CLIENT.hud = new Config(FileUtils.readFileToString(this.getFile())).toHUD();
    }
    
    @Override
    protected void saveConfig() throws IOException {
        final PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        printWriter.println(new Config(LiquidBounce.CLIENT.hud).toJson());
        printWriter.close();
    }
}
