// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.misc;

import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public final class NetworkUtils
{
    public static String readContent(final String stringURL) throws IOException {
        final HttpURLConnection httpConnection = stringURL.toLowerCase().startsWith("https://") ? ((HttpsURLConnection)new URL(stringURL).openConnection()) : ((HttpURLConnection)new URL(stringURL).openConnection());
        httpConnection.setConnectTimeout(10000);
        httpConnection.setReadTimeout(10000);
        httpConnection.setRequestMethod("GET");
        httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        HttpURLConnection.setFollowRedirects(true);
        httpConnection.setDoOutput(true);
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
        final StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }
}
