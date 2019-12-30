// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.misc;

import java.net.URISyntaxException;
import java.io.IOException;
import java.net.URI;
import java.awt.Desktop;
import java.awt.Component;
import javax.swing.JOptionPane;

public final class MiscUtils
{
    public static void showErrorPopup(final String title, final String message) {
        JOptionPane.showMessageDialog(null, message, title, 0);
    }
    
    public static void showURL(final String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        }
        catch (IOException | URISyntaxException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
        }
    }
}
