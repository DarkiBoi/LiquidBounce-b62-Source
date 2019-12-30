// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import net.ccbluex.liquidbounce.ui.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.utils.misc.NetworkUtils;
import net.ccbluex.liquidbounce.utils.misc.JsonUtils;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.ui.hud.DefaultHUD;
import net.ccbluex.liquidbounce.cape.CapeAPI;
import net.ccbluex.liquidbounce.tabs.HeadsTab;
import net.ccbluex.liquidbounce.tabs.ExploitsTab;
import net.ccbluex.liquidbounce.tabs.BlocksTab;
import net.ccbluex.liquidbounce.utils.ClassUtils;
import net.ccbluex.liquidbounce.file.FileConfig;
import net.ccbluex.liquidbounce.script.remapper.Remapper;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.features.special.BungeeCordSpoof;
import net.ccbluex.liquidbounce.features.special.AntiForge;
import net.ccbluex.liquidbounce.event.EventListener;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.ui.clickgui.ClickGui;
import net.ccbluex.liquidbounce.ui.hud.HUD;
import net.ccbluex.liquidbounce.discord.LiquidDiscordRPC;
import net.ccbluex.liquidbounce.script.ScriptManager;
import net.ccbluex.liquidbounce.file.FileManager;
import net.ccbluex.liquidbounce.event.EventManager;
import net.ccbluex.liquidbounce.features.command.CommandManager;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LiquidBounce
{
    public static final LiquidBounce CLIENT;
    public static final String CLIENT_NAME = "LiquidBounce";
    public static final int CLIENT_VERSION = 62;
    public static final String CLIENT_CREATOR = "CCBlueX";
    public static final String MINECRAFT_VERSION = "1.8.9";
    public boolean isStarting;
    public ModuleManager moduleManager;
    public CommandManager commandManager;
    public EventManager eventManager;
    public FileManager fileManager;
    public ScriptManager scriptManager;
    private LiquidDiscordRPC liquidDiscordRPC;
    public HUD hud;
    public ClickGui clickGui;
    public int latestVersion;
    public boolean firstStart;
    public ResourceLocation background;
    public String mainMenuStyle;
    
    public LiquidBounce() {
        this.mainMenuStyle = "New";
    }
    
    public void startClient() {
        this.isStarting = true;
        ClientUtils.getLogger().info("Starting LiquidBounce b62, by CCBlueX");
        this.fileManager = new FileManager();
        (this.eventManager = new EventManager()).registerListener(new RotationUtils());
        this.eventManager.registerListener(new AntiForge());
        this.eventManager.registerListener(new BungeeCordSpoof());
        this.commandManager = new CommandManager();
        Fonts.loadFonts();
        (this.moduleManager = new ModuleManager()).registerModules();
        try {
            Remapper.INSTANCE.loadSrg();
            (this.scriptManager = new ScriptManager()).loadScripts();
            this.scriptManager.enableScripts();
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("Failed to load scripts.", throwable);
        }
        this.commandManager.registerCommands();
        if (!this.fileManager.dir.exists()) {
            this.firstStart = true;
        }
        this.fileManager.loadConfigs(this.fileManager.modulesConfig, this.fileManager.valuesConfig, this.fileManager.accountsConfig, this.fileManager.friendsConfig, this.fileManager.xrayConfig);
        this.moduleManager.sortModules();
        this.clickGui = new ClickGui();
        this.fileManager.loadConfig(this.fileManager.clickGuiConfig);
        if (ClassUtils.hasClass("net.minecraftforge.common.MinecraftForge")) {
            new BlocksTab();
            new ExploitsTab();
            new HeadsTab();
        }
        CapeAPI.getInstance().registerCapeService();
        try {
            (this.liquidDiscordRPC = new LiquidDiscordRPC()).setup();
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("Failed to setup Discord RPC.", throwable);
        }
        this.hud = new DefaultHUD();
        this.fileManager.loadConfig(this.fileManager.hudConfig);
        try {
            ModuleManager.getModules().forEach(Module::onStarted);
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("Failed to call started to modules.", throwable);
        }
        ClientUtils.disableFastRender();
        try {
            final JsonElement jsonElement = JsonUtils.JSON_PARSER.parse(NetworkUtils.readContent("https://ccbluex.github.io/FileCloud/LiquidBounce/versions.json"));
            if (jsonElement.isJsonObject()) {
                final JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (jsonObject.has("1.8.9")) {
                    this.latestVersion = jsonObject.get("1.8.9").getAsInt();
                }
            }
        }
        catch (Throwable exception) {
            ClientUtils.getLogger().error("Failed to check for updates.", exception);
        }
        GuiAltManager.loadGenerators();
        this.isStarting = false;
    }
    
    public void stopClient() {
        if (this.fileManager != null) {
            this.fileManager.saveAllConfigs();
        }
        if (this.liquidDiscordRPC != null) {
            this.liquidDiscordRPC.shutdown();
        }
    }
    
    static {
        CLIENT = new LiquidBounce();
    }
}
