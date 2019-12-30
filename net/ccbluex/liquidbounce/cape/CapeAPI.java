// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.cape;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.Minecraft;
import java.io.File;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.util.ResourceLocation;
import java.util.Date;
import java.util.UUID;
import java.util.Iterator;
import com.google.gson.JsonObject;
import net.ccbluex.liquidbounce.cape.service.services.ServiceList;
import com.google.gson.JsonElement;
import java.util.Map;
import java.util.HashMap;
import net.ccbluex.liquidbounce.cape.service.services.ServiceAPI;
import net.ccbluex.liquidbounce.utils.misc.NetworkUtils;
import com.google.gson.JsonParser;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.cape.service.CapeService;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CapeAPI
{
    private static final CapeAPI CAPE_API;
    private CapeService capeService;
    
    public static CapeAPI getInstance() {
        return CapeAPI.CAPE_API;
    }
    
    public void registerCapeService() {
        try {
            ClientUtils.getLogger().info("Loading cape service...");
            final JsonObject jsonObject = new JsonParser().parse(NetworkUtils.readContent("https://ccbluex.github.io/FileCloud/LiquidBounce/cape/service.json")).getAsJsonObject();
            final String serviceType = jsonObject.get("serviceType").getAsString();
            final String lowerCase = serviceType.toLowerCase();
            switch (lowerCase) {
                case "api": {
                    final String url = jsonObject.get("api").getAsJsonObject().get("url").getAsString();
                    this.capeService = new ServiceAPI(url);
                    ClientUtils.getLogger().info("Registered " + url + " as '" + serviceType + "' service type.");
                    break;
                }
                case "list": {
                    final Map<String, String> users = new HashMap<String, String>();
                    for (final Map.Entry<String, JsonElement> entry : jsonObject.get("users").getAsJsonObject().entrySet()) {
                        users.put(entry.getKey(), entry.getValue().getAsString());
                        ClientUtils.getLogger().info("Loaded user cape for '" + entry.getKey() + "'.");
                    }
                    this.capeService = new ServiceList(users);
                    ClientUtils.getLogger().info("Registered '" + serviceType + "' service type.");
                    break;
                }
            }
            ClientUtils.getLogger().info("Loaded.");
        }
        catch (Throwable t) {
            ClientUtils.getLogger().error("Failed to register cape service", t);
        }
    }
    
    public CapeInfo loadCape(final UUID uuid) {
        final String url = this.capeService.getCape(uuid);
        if (url == null) {
            return null;
        }
        final ResourceLocation resourceLocation = new ResourceLocation(String.format("capes/%s.png", new Date().getTime()));
        final CapeInfo capeInfo = new CapeInfo(resourceLocation);
        final ThreadDownloadImageData threadDownloadImageData = new ThreadDownloadImageData((File)null, url, (ResourceLocation)null, (IImageBuffer)new IImageBuffer() {
            public BufferedImage func_78432_a(final BufferedImage image) {
                return image;
            }
            
            public void func_152634_a() {
                capeInfo.setCapeAvailable(true);
            }
        });
        Minecraft.func_71410_x().func_110434_K().func_110579_a(resourceLocation, (ITextureObject)threadDownloadImageData);
        return capeInfo;
    }
    
    public boolean hasCapeService() {
        return this.capeService != null;
    }
    
    static {
        CAPE_API = new CapeAPI();
    }
}
