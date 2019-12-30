// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.hud;

import net.ccbluex.liquidbounce.ui.hud.element.elements.Image;
import net.ccbluex.liquidbounce.ui.hud.element.elements.Text;
import net.ccbluex.liquidbounce.ui.hud.element.elements.TabGUI;
import net.ccbluex.liquidbounce.ui.hud.element.elements.Notifications;
import net.ccbluex.liquidbounce.ui.hud.element.elements.Model;
import net.ccbluex.liquidbounce.ui.hud.element.elements.Effects;
import net.ccbluex.liquidbounce.ui.hud.element.elements.Arraylist;
import net.ccbluex.liquidbounce.ui.hud.element.elements.Armor;
import java.util.HashMap;
import net.ccbluex.liquidbounce.ui.hud.element.Element;
import java.util.Map;

public class NameIndex
{
    public static final Map<Class<? extends Element>, String> NAME_MAP;
    
    public static String getName(final Class<? extends Element> aClass) {
        return NameIndex.NAME_MAP.getOrDefault(aClass, "Unknown Element");
    }
    
    static {
        (NAME_MAP = new HashMap<Class<? extends Element>, String>()).put(Armor.class, "Armor");
        NameIndex.NAME_MAP.put(Arraylist.class, "Arraylist");
        NameIndex.NAME_MAP.put(Effects.class, "Effects");
        NameIndex.NAME_MAP.put(Model.class, "Model");
        NameIndex.NAME_MAP.put(Notifications.class, "Notifications");
        NameIndex.NAME_MAP.put(TabGUI.class, "TabGUI");
        NameIndex.NAME_MAP.put(Text.class, "Text");
        NameIndex.NAME_MAP.put(Image.class, "Image");
    }
}
