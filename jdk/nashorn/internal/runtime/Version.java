// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class Version
{
    private static final String VERSION_RB_NAME = "jdk.nashorn.internal.runtime.resources.version";
    private static ResourceBundle versionRB;
    
    private Version() {
    }
    
    public static String version() {
        return version("release");
    }
    
    public static String fullVersion() {
        return version("full");
    }
    
    private static String version(final String key) {
        if (Version.versionRB == null) {
            try {
                Version.versionRB = ResourceBundle.getBundle("jdk.nashorn.internal.runtime.resources.version");
            }
            catch (MissingResourceException e) {
                return "version not available";
            }
        }
        try {
            return Version.versionRB.getString(key);
        }
        catch (MissingResourceException e) {
            return "version not available";
        }
    }
}
