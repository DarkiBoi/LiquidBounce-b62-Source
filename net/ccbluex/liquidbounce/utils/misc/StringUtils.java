// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.misc;

import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class StringUtils
{
    public static String toCompleteString(final String[] args, final int start) {
        if (args.length <= start) {
            return "";
        }
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = start; args.length > i; ++i) {
            stringBuilder.append(args[i]).append(" ");
        }
        return stringBuilder.toString().substring(0, stringBuilder.length() - 1);
    }
    
    public static String patchString(String s) {
        s = replace(s, "\u00e4", "ae");
        s = replace(s, "\u00fc", "ue");
        s = replace(s, "\u00f6", "oe");
        return s;
    }
    
    public static String replace(final String string, final String searchChars, String replaceChars) {
        if (string.isEmpty() || searchChars.isEmpty() || searchChars.equals(replaceChars)) {
            return string;
        }
        if (replaceChars == null) {
            replaceChars = "";
        }
        final int stringLength = string.length();
        final int searchCharsLength = searchChars.length();
        final StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < stringLength) {
            final int start = stringBuilder.indexOf(searchChars, i);
            if (start == -1) {
                if (i == 0) {
                    return string;
                }
                return stringBuilder.toString();
            }
            else {
                stringBuilder.replace(start, start + searchCharsLength, replaceChars);
                ++i;
            }
        }
        return stringBuilder.toString();
    }
    
    public static String hashString(final String text) throws NoSuchAlgorithmException {
        return Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-256").digest(text.getBytes(StandardCharsets.UTF_8)));
    }
}
