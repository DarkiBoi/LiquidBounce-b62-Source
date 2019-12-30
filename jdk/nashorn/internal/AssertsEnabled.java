// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal;

public final class AssertsEnabled
{
    private static boolean assertsEnabled;
    
    public static boolean assertsEnabled() {
        return AssertsEnabled.assertsEnabled;
    }
    
    static {
        AssertsEnabled.assertsEnabled = false;
        assert AssertsEnabled.assertsEnabled = true;
    }
}
