// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.file;

import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.io.FileInputStream;
import net.ccbluex.liquidbounce.LiquidBounce;
import java.lang.reflect.Field;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.file.configs.HudConfig;
import net.ccbluex.liquidbounce.file.configs.XRayConfig;
import net.ccbluex.liquidbounce.file.configs.ClickGuiConfig;
import net.ccbluex.liquidbounce.file.configs.ValuesConfig;
import net.ccbluex.liquidbounce.file.configs.ModulesConfig;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.file.configs.FriendsConfig;
import net.ccbluex.liquidbounce.file.configs.AccountsConfig;
import java.io.File;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FileManager
{
    public final File dir;
    public final File fontsDir;
    public final File settingsDir;
    public final FileConfig modulesConfig;
    public final FileConfig valuesConfig;
    public final FileConfig clickGuiConfig;
    public final AccountsConfig accountsConfig;
    public final FriendsConfig friendsConfig;
    public final FileConfig xrayConfig;
    public final FileConfig hudConfig;
    public final File backgroundFile;
    
    public FileManager() {
        this.dir = new File(Minecraft.func_71410_x().field_71412_D, "LiquidBounce-1.8");
        this.fontsDir = new File(this.dir, "fonts");
        this.settingsDir = new File(this.dir, "settings");
        this.modulesConfig = new ModulesConfig(new File(this.dir, "modules.json"));
        this.valuesConfig = new ValuesConfig(new File(this.dir, "values.json"));
        this.clickGuiConfig = new ClickGuiConfig(new File(this.dir, "clickgui.json"));
        this.accountsConfig = new AccountsConfig(new File(this.dir, "accounts.json"));
        this.friendsConfig = new FriendsConfig(new File(this.dir, "friends.json"));
        this.xrayConfig = new XRayConfig(new File(this.dir, "xray-blocks.json"));
        this.hudConfig = new HudConfig(new File(this.dir, "hud.json"));
        this.backgroundFile = new File(this.dir, "userbackground.png");
        this.setupFolder();
        this.loadBackground();
    }
    
    public void setupFolder() {
        if (!this.dir.exists()) {
            this.dir.mkdir();
        }
        if (!this.fontsDir.exists()) {
            this.fontsDir.mkdir();
        }
        if (!this.settingsDir.exists()) {
            this.settingsDir.mkdir();
        }
    }
    
    public void loadAllConfigs() {
        for (final Field field : this.getClass().getDeclaredFields()) {
            if (field.getType() == FileConfig.class) {
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    final FileConfig fileConfig = (FileConfig)field.get(this);
                    this.loadConfig(fileConfig);
                }
                catch (IllegalAccessException e) {
                    ClientUtils.getLogger().error("Failed to load config file of field " + field.getName() + ".", (Throwable)e);
                }
            }
        }
    }
    
    public void loadConfigs(final FileConfig... configs) {
        for (final FileConfig fileConfig : configs) {
            this.loadConfig(fileConfig);
        }
    }
    
    public void loadConfig(final FileConfig config) {
        if (!config.hasConfig()) {
            ClientUtils.getLogger().info("[FileManager] Skipped loading config: " + config.getFile().getName() + ".");
            this.saveConfig(config, true);
            return;
        }
        try {
            ClientUtils.getLogger().info("[FileManager] Loading config: " + config.getFile().getName() + "...");
            config.loadConfig();
            ClientUtils.getLogger().info("[FileManager] Loaded config: " + config.getFile().getName() + ".");
        }
        catch (Throwable t) {
            ClientUtils.getLogger().error("[FileManager] Failed to load config file: " + config.getFile().getName() + ".", t);
        }
    }
    
    public void saveAllConfigs() {
        for (final Field field : this.getClass().getDeclaredFields()) {
            if (field.getType() == FileConfig.class) {
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    final FileConfig fileConfig = (FileConfig)field.get(this);
                    this.saveConfig(fileConfig);
                }
                catch (IllegalAccessException e) {
                    ClientUtils.getLogger().error("[FileManager] Failed to save config file of field " + field.getName() + ".", (Throwable)e);
                }
            }
        }
    }
    
    public void saveConfigs(final FileConfig... configs) {
        for (final FileConfig fileConfig : configs) {
            this.saveConfig(fileConfig);
        }
    }
    
    public void saveConfig(final FileConfig config) {
        this.saveConfig(config, false);
    }
    
    private void saveConfig(final FileConfig config, final boolean ignoreStarting) {
        if (!ignoreStarting && LiquidBounce.CLIENT.isStarting) {
            return;
        }
        try {
            ClientUtils.getLogger().info("[FileManager] Saving config: " + config.getFile().getName() + "...");
            if (!config.hasConfig()) {
                config.createConfig();
            }
            config.saveConfig();
            ClientUtils.getLogger().info("[FileManager] Saved config: " + config.getFile().getName() + ".");
        }
        catch (Throwable t) {
            ClientUtils.getLogger().error("[FileManager] Failed to save config file: " + config.getFile().getName() + ".", t);
        }
    }
    
    public void loadBackground() {
        if (this.backgroundFile.exists()) {
            try {
                ClientUtils.getLogger().info("[FileManager] Loading background...");
                final BufferedImage bufferedImage = ImageIO.read(new FileInputStream(this.backgroundFile));
                if (bufferedImage == null) {
                    return;
                }
                LiquidBounce.CLIENT.background = new ResourceLocation("LiquidBounce".toLowerCase() + "/background.png");
                Minecraft.func_71410_x().func_110434_K().func_110579_a(LiquidBounce.CLIENT.background, (ITextureObject)new DynamicTexture(bufferedImage));
                ClientUtils.getLogger().info("[FileManager] Loaded background.");
            }
            catch (Exception e) {
                ClientUtils.getLogger().error("[FileManager] Failed to load background.", (Throwable)e);
            }
        }
    }
}
