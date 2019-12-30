// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.client.Minecraft;
import java.net.UnknownHostException;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.client.network.NetHandlerLoginClient;
import java.net.InetAddress;
import net.minecraft.network.Packet;
import net.minecraft.network.login.client.C00PacketLoginStart;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import net.mcleaks.MCLeaks;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.utils.ServerUtils;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.concurrent.atomic.AtomicInteger;
import org.spongepowered.asm.mixin.Final;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.network.NetworkManager;
import net.minecraft.client.multiplayer.GuiConnecting;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;

@SideOnly(Side.CLIENT)
@Mixin({ GuiConnecting.class })
public abstract class MixinGuiConnecting extends GuiScreen
{
    @Shadow
    private NetworkManager field_146371_g;
    @Shadow
    @Final
    private static Logger field_146370_f;
    @Shadow
    private boolean field_146373_h;
    @Shadow
    @Final
    private GuiScreen field_146374_i;
    @Shadow
    @Final
    private static AtomicInteger field_146372_a;
    
    @Inject(method = { "connect" }, at = { @At("HEAD") })
    private void headConnect(final String ip, final int port, final CallbackInfo callbackInfo) {
        ServerUtils.serverData = new ServerData("", ip + ":" + port, false);
    }
    
    @Inject(method = { "connect" }, at = { @At(value = "NEW", target = "net/minecraft/network/login/client/C00PacketLoginStart") }, cancellable = true)
    private void mcLeaks(final CallbackInfo callbackInfo) {
        if (MCLeaks.isAltActive()) {
            this.field_146371_g.func_179290_a((Packet)new C00PacketLoginStart(new GameProfile((UUID)null, MCLeaks.getSession().getUsername())));
            callbackInfo.cancel();
        }
    }
    
    @Overwrite
    private void func_146367_a(final String ip, final int port) {
        MixinGuiConnecting.field_146370_f.info("Connecting to " + ip + ", " + port);
        InetAddress inetaddress;
        NetworkManager field_146371_g;
        GameProfile func_148256_e = null;
        final Packet packet;
        Minecraft field_146297_k;
        GuiScreen field_146374_i;
        final ChatComponentTranslation chatComponentTranslation;
        final GuiScreen guiScreen;
        final String s3;
        String s;
        String s2;
        Minecraft field_146297_k2;
        GuiScreen field_146374_i2;
        final ChatComponentTranslation chatComponentTranslation2;
        final GuiScreen guiScreen2;
        final String s4;
        new Thread(() -> {
            inetaddress = null;
            try {
                if (!this.field_146373_h) {
                    inetaddress = InetAddress.getByName(ip);
                    (this.field_146371_g = NetworkManager.func_181124_a(inetaddress, port, this.field_146297_k.field_71474_y.func_181148_f())).func_150719_a((INetHandler)new NetHandlerLoginClient(this.field_146371_g, this.field_146297_k, this.field_146374_i));
                    this.field_146371_g.func_179290_a((Packet)new C00Handshake(47, ip, port, EnumConnectionState.LOGIN, true));
                    field_146371_g = this.field_146371_g;
                    // new(net.minecraft.network.login.client.C00PacketLoginStart.class)
                    if (MCLeaks.isAltActive()) {
                        // new(com.mojang.authlib.GameProfile.class)
                        new GameProfile((UUID)null, MCLeaks.getSession().getUsername());
                    }
                    else {
                        func_148256_e = this.field_146297_k.func_110432_I().func_148256_e();
                    }
                    new C00PacketLoginStart(func_148256_e);
                    field_146371_g.func_179290_a(packet);
                }
            }
            catch (UnknownHostException unknownhostexception) {
                if (!this.field_146373_h) {
                    MixinGuiConnecting.field_146370_f.error("Couldn't connect to server", (Throwable)unknownhostexception);
                    field_146297_k = this.field_146297_k;
                    // new(net.minecraft.client.gui.GuiDisconnected.class)
                    field_146374_i = this.field_146374_i;
                    new ChatComponentTranslation("disconnect.genericReason", new Object[] { "Unknown host" });
                    new GuiDisconnected(field_146374_i, s3, (IChatComponent)chatComponentTranslation);
                    field_146297_k.func_147108_a(guiScreen);
                }
            }
            catch (Exception exception) {
                if (!this.field_146373_h) {
                    MixinGuiConnecting.field_146370_f.error("Couldn't connect to server", (Throwable)exception);
                    s = exception.toString();
                    if (inetaddress != null) {
                        s2 = inetaddress.toString() + ":" + port;
                        s = s.replaceAll(s2, "");
                    }
                    field_146297_k2 = this.field_146297_k;
                    // new(net.minecraft.client.gui.GuiDisconnected.class)
                    field_146374_i2 = this.field_146374_i;
                    new ChatComponentTranslation("disconnect.genericReason", new Object[] { s });
                    new GuiDisconnected(field_146374_i2, s4, (IChatComponent)chatComponentTranslation2);
                    field_146297_k2.func_147108_a(guiScreen2);
                }
            }
        }, "Server Connector #" + MixinGuiConnecting.field_146372_a.incrementAndGet()).start();
    }
    
    @Overwrite
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        this.func_146276_q_();
        RenderUtils.drawLoadingCircle((float)(scaledResolution.func_78326_a() / 2), (float)(scaledResolution.func_78328_b() / 4 + 70));
        String ip = "Unknown";
        final ServerData serverData = this.field_146297_k.func_147104_D();
        if (serverData != null) {
            ip = serverData.field_78845_b;
        }
        Fonts.font40.drawCenteredString("Connecting to", scaledResolution.func_78326_a() / 2, scaledResolution.func_78328_b() / 4 + 110, 16777215, true);
        Fonts.font35.drawCenteredString(ip, scaledResolution.func_78326_a() / 2, scaledResolution.func_78328_b() / 4 + 120, 5407227, true);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
}
