// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.clickgui.style;

import net.ccbluex.liquidbounce.ui.clickgui.elements.ModuleElement;
import net.ccbluex.liquidbounce.ui.clickgui.elements.ButtonElement;
import net.ccbluex.liquidbounce.ui.clickgui.Panel;

public interface Style
{
    void drawPanel(final int p0, final int p1, final Panel p2);
    
    void drawDescription(final int p0, final int p1, final String p2);
    
    void drawButtonElement(final int p0, final int p1, final ButtonElement p2);
    
    void drawModuleElement(final int p0, final int p1, final ModuleElement p2);
}
