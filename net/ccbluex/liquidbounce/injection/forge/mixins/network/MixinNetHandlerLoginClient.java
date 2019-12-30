// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.network;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.mcleaks.Session;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.math.BigInteger;
import net.minecraft.util.CryptManager;
import net.mcleaks.MCLeaks;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.network.NetworkManager;
import net.minecraft.client.network.NetHandlerLoginClient;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ NetHandlerLoginClient.class })
public class MixinNetHandlerLoginClient
{
    @Shadow
    @Final
    private NetworkManager field_147393_d;
    
    @Inject(method = { "handleEncryptionRequest" }, at = { @At("HEAD") }, cancellable = true)
    private void handleEncryptionRequest(final S01PacketEncryptionRequest packetIn, final CallbackInfo callbackInfo) {
        if (MCLeaks.isAltActive()) {
            final SecretKey secretkey = CryptManager.func_75890_a();
            final String s = packetIn.func_149609_c();
            final PublicKey publickey = packetIn.func_149608_d();
            final String s2 = new BigInteger(CryptManager.func_75895_a(s, publickey, secretkey)).toString(16);
            final Session session = MCLeaks.getSession();
            final String server = ((InetSocketAddress)this.field_147393_d.func_74430_c()).getHostName() + ":" + ((InetSocketAddress)this.field_147393_d.func_74430_c()).getPort();
            try {
                final String jsonBody = "{\"session\":\"" + session.getToken() + "\",\"mcname\":\"" + session.getUsername() + "\",\"serverhash\":\"" + s2 + "\",\"server\":\"" + server + "\"}";
                final HttpURLConnection connection = (HttpURLConnection)new URL("https://auth.mcleaks.net/v1/joinserver").openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                connection.setDoOutput(true);
                final DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.write(jsonBody.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                final StringBuilder outputBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    outputBuilder.append(line);
                }
                reader.close();
                final JsonElement jsonElement = (JsonElement)new Gson().fromJson(outputBuilder.toString(), (Class)JsonElement.class);
                if (!jsonElement.isJsonObject() || !jsonElement.getAsJsonObject().has("success")) {
                    this.field_147393_d.func_150718_a((IChatComponent)new ChatComponentText("Invalid response from MCLeaks API"));
                    callbackInfo.cancel();
                    return;
                }
                if (!jsonElement.getAsJsonObject().get("success").getAsBoolean()) {
                    String errorMessage = "Received success=false from MCLeaks API";
                    if (jsonElement.getAsJsonObject().has("errorMessage")) {
                        errorMessage = jsonElement.getAsJsonObject().get("errorMessage").getAsString();
                    }
                    this.field_147393_d.func_150718_a((IChatComponent)new ChatComponentText(errorMessage));
                    callbackInfo.cancel();
                    return;
                }
            }
            catch (Exception e) {
                this.field_147393_d.func_150718_a((IChatComponent)new ChatComponentText("Error whilst contacting MCLeaks API: " + e.toString()));
                callbackInfo.cancel();
                return;
            }
            ClientUtils.sendEncryption(this.field_147393_d, secretkey, publickey, packetIn);
            callbackInfo.cancel();
        }
    }
}
