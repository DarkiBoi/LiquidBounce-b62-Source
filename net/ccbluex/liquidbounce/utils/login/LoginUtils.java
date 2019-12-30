// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.login;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import net.minecraft.util.Session;
import net.minecraft.client.Minecraft;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class LoginUtils
{
    public static LoginResult login(final String username, final String password) {
        final YggdrasilUserAuthentication userAuthentication = (YggdrasilUserAuthentication)new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
        userAuthentication.setUsername(username);
        userAuthentication.setPassword(password);
        try {
            userAuthentication.logIn();
            Minecraft.func_71410_x().field_71449_j = new Session(userAuthentication.getSelectedProfile().getName(), userAuthentication.getSelectedProfile().getId().toString(), userAuthentication.getAuthenticatedToken(), "mojang");
            return LoginResult.LOGGED;
        }
        catch (AuthenticationUnavailableException exception2) {
            return LoginResult.NO_CONTACT;
        }
        catch (AuthenticationException exception) {
            if (exception.getMessage().contains("Invalid username or password.")) {
                return LoginResult.INVALID_ACCOUNT_DATA;
            }
            if (exception.getMessage().toLowerCase().contains("account migrated")) {
                return LoginResult.MIGRATED;
            }
            return LoginResult.NO_CONTACT;
        }
        catch (NullPointerException exception3) {
            return LoginResult.WRONG_PASSWORD;
        }
    }
    
    public static void loginCracked(final String username) {
        Minecraft.func_71410_x().field_71449_j = new Session(username, UniversallyUniqueIdentifierUtils.INSTANCE.getUUID(username), "-", "legacy");
    }
    
    public enum LoginResult
    {
        WRONG_PASSWORD, 
        NO_CONTACT, 
        INVALID_ACCOUNT_DATA, 
        MIGRATED, 
        LOGGED;
    }
}
