// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.discord;

import java.io.IOException;
import java.util.Iterator;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import net.ccbluex.liquidbounce.utils.misc.NetworkUtils;
import com.google.gson.JsonParser;
import net.minecraft.client.multiplayer.ServerData;
import com.jagrosh.discordipc.entities.pipe.PipeStatus;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.minecraft.client.Minecraft;
import com.jagrosh.discordipc.entities.RichPresence;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import com.jagrosh.discordipc.entities.DiscordBuild;
import org.json.JSONObject;
import com.jagrosh.discordipc.IPCListener;
import java.util.HashMap;
import java.time.OffsetDateTime;
import java.util.Map;
import com.jagrosh.discordipc.IPCClient;

public class LiquidDiscordRPC
{
    private IPCClient ipcClient;
    private long appID;
    private final Map<String, String> assets;
    private final OffsetDateTime timestamp;
    private boolean running;
    
    public LiquidDiscordRPC() {
        this.assets = new HashMap<String, String>();
        this.timestamp = OffsetDateTime.now();
    }
    
    public void setup() {
        try {
            this.running = true;
            this.loadConfiguration();
            (this.ipcClient = new IPCClient(this.appID)).setListener(new IPCListener() {
                @Override
                public void onReady(final IPCClient client) {
                    new Thread(() -> {
                        while (LiquidDiscordRPC.this.running) {
                            LiquidDiscordRPC.this.update();
                            try {
                                Thread.sleep(1000L);
                            }
                            catch (InterruptedException ex) {}
                        }
                    }).start();
                }
                
                @Override
                public void onClose(final IPCClient client, final JSONObject json) {
                    LiquidDiscordRPC.this.running = false;
                }
            });
            this.ipcClient.connect(new DiscordBuild[0]);
        }
        catch (Throwable e) {
            ClientUtils.getLogger().error("Failed to setup Discord RPC.", e);
        }
    }
    
    public void update() {
        final RichPresence.Builder builder = new RichPresence.Builder();
        builder.setStartTimestamp(this.timestamp);
        if (this.assets.containsKey("logo")) {
            builder.setLargeImage(this.assets.get("logo"));
        }
        final Minecraft mc = Minecraft.func_71410_x();
        if (mc.field_71439_g != null) {
            final ServerData serverData = mc.func_147104_D();
            builder.setDetails("Server: " + ((mc.func_71387_A() || serverData == null) ? "Singleplayer" : serverData.field_78845_b));
            builder.setState("Enabled " + ModuleManager.getModules().stream().filter(Module::getState).count() + " of " + ModuleManager.getModules().size() + " modules");
        }
        if (this.ipcClient.getStatus() == PipeStatus.CONNECTED) {
            this.ipcClient.sendRichPresence(builder.build());
        }
    }
    
    public void shutdown() {
        if (this.ipcClient == null) {
            return;
        }
        try {
            this.ipcClient.close();
        }
        catch (Throwable e) {
            ClientUtils.getLogger().error("Failed to close Discord RPC.", e);
        }
    }
    
    private void loadConfiguration() throws IOException {
        final JsonElement jsonElement = new JsonParser().parse(NetworkUtils.readContent("https://ccbluex.github.io/FileCloud/LiquidBounce/discord-rpc.json"));
        if (!jsonElement.isJsonObject()) {
            return;
        }
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (jsonObject.has("appID")) {
            this.appID = jsonObject.get("appID").getAsLong();
        }
        for (final Map.Entry<String, JsonElement> entry : jsonObject.get("assets").getAsJsonObject().entrySet()) {
            this.assets.put(entry.getKey(), entry.getValue().getAsString());
        }
    }
}
