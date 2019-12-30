// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.login;

import com.google.gson.JsonElement;
import java.net.URLConnection;
import java.io.Closeable;
import kotlin.io.CloseableKt;
import kotlin.Unit;
import java.io.Reader;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;
import kotlin.TypeCastException;
import java.net.URL;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004¨\u0006\u0006" }, d2 = { "Lnet/ccbluex/liquidbounce/utils/login/UniversallyUniqueIdentifierUtils;", "", "()V", "getUUID", "", "username", "LiquidBounce" })
public final class UniversallyUniqueIdentifierUtils
{
    public static final UniversallyUniqueIdentifierUtils INSTANCE;
    
    @NotNull
    public final String getUUID(@NotNull final String username) {
        Intrinsics.checkParameterIsNotNull(username, "username");
        try {
            final URLConnection openConnection = new URL("https://api.mojang.com/users/profiles/minecraft/" + username).openConnection();
            if (openConnection == null) {
                throw new TypeCastException("null cannot be cast to non-null type javax.net.ssl.HttpsURLConnection");
            }
            final HttpsURLConnection httpConnection = (HttpsURLConnection)openConnection;
            httpConnection.setConnectTimeout(2000);
            httpConnection.setReadTimeout(2000);
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            HttpURLConnection.setFollowRedirects(true);
            httpConnection.setDoOutput(true);
            if (httpConnection.getResponseCode() != 200) {
                return "";
            }
            final InputStreamReader $receiver = new InputStreamReader(httpConnection.getInputStream());
            Throwable cause = null;
            try {
                final InputStreamReader it = $receiver;
                final int n = 0;
                final JsonElement parse;
                final JsonElement jsonElement = parse = new JsonParser().parse((Reader)it);
                Intrinsics.checkExpressionValueIsNotNull(parse, "jsonElement");
                if (parse.isJsonObject()) {
                    final JsonElement value = jsonElement.getAsJsonObject().get("id");
                    Intrinsics.checkExpressionValueIsNotNull(value, "jsonElement.asJsonObject.get(\"id\")");
                    final String asString = value.getAsString();
                    Intrinsics.checkExpressionValueIsNotNull(asString, "jsonElement.asJsonObject.get(\"id\").asString");
                    return asString;
                }
                final Unit instance = Unit.INSTANCE;
            }
            catch (Throwable t) {
                cause = t;
                throw t;
            }
            finally {
                CloseableKt.closeFinally($receiver, cause);
            }
        }
        catch (Throwable t2) {}
        return "";
    }
    
    private UniversallyUniqueIdentifierUtils() {
    }
    
    static {
        INSTANCE = new UniversallyUniqueIdentifierUtils();
    }
}
