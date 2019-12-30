// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils;

import net.minecraft.client.settings.GameSettings;
import org.apache.logging.log4j.LogManager;
import io.netty.util.concurrent.Future;
import net.minecraft.network.Packet;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import net.minecraft.network.NetworkManager;
import net.minecraft.client.Minecraft;
import java.lang.reflect.Field;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ClientUtils
{
    private static final Logger logger;
    private static Field fastRenderField;
    
    public static Logger getLogger() {
        return ClientUtils.logger;
    }
    
    public static void disableFastRender() {
        try {
            if (ClientUtils.fastRenderField != null) {
                if (!ClientUtils.fastRenderField.isAccessible()) {
                    ClientUtils.fastRenderField.setAccessible(true);
                }
                ClientUtils.fastRenderField.setBoolean(Minecraft.func_71410_x().field_71474_y, false);
            }
        }
        catch (IllegalAccessException ex) {}
    }
    
    public static void sendEncryption(final NetworkManager networkManager, final SecretKey secretKey, final PublicKey publicKey, final S01PacketEncryptionRequest encryptionRequest) {
        networkManager.func_179288_a((Packet)new C01PacketEncryptionResponse(secretKey, publicKey, encryptionRequest.func_149607_e()), p_operationComplete_1_ -> networkManager.func_150727_a(secretKey), new GenericFutureListener[0]);
    }
    
    static {
        logger = LogManager.getLogger("LiquidBounce");
        try {
            ClientUtils.fastRenderField = GameSettings.class.getDeclaredField("ofFastRender");
            if (!ClientUtils.fastRenderField.isAccessible()) {
                ClientUtils.fastRenderField.setAccessible(true);
            }
        }
        catch (NoSuchFieldException ex) {}
    }
}
