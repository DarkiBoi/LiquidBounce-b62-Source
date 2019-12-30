// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.features.module.modules.misc.Spammer;
import net.ccbluex.liquidbounce.features.module.modules.misc.NameProtect;
import net.ccbluex.liquidbounce.valuesystem.Value;
import net.ccbluex.liquidbounce.features.module.Module;
import java.util.Iterator;
import net.ccbluex.liquidbounce.LiquidBounce;
import org.lwjgl.input.Keyboard;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import net.ccbluex.liquidbounce.utils.render.ChatColor;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import java.util.List;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class SettingsUtils
{
    public static void executeScript(final List<String> script) {
        for (final String scriptLine : script) {
            final String[] split = scriptLine.split(" ");
            if (split.length > 1) {
                final String s = split[0];
                switch (s) {
                    case "chat": {
                        ChatUtils.displayChatMessage("§7[§3§lAutoSettings§7] §e" + ChatColor.translateAlternateColorCodes(StringUtils.toCompleteString(split, 1)));
                        continue;
                    }
                    case "load": {
                        final String urlRaw = StringUtils.toCompleteString(split, 1);
                        final String url = urlRaw.startsWith("http") ? urlRaw : ("https://ccbluex.github.io/FileCloud/LiquidBounce/autosettings/" + urlRaw.toLowerCase());
                        try {
                            ChatUtils.displayChatMessage("§7[§3§lAutoSettings§7] §7Loading settings from §a§l" + url + "§7...");
                            final List<String> nextScript = new ArrayList<String>();
                            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                if (!line.startsWith("#") && !line.isEmpty()) {
                                    nextScript.add(line);
                                }
                            }
                            bufferedReader.close();
                            executeScript(nextScript);
                            ChatUtils.displayChatMessage("§7[§3§lAutoSettings§7] §7Loaded settings from §a§l" + url + "§7.");
                        }
                        catch (Exception e2) {
                            ChatUtils.displayChatMessage("§7[§3§lAutoSettings§7] §7Failed to load settings from §a§l" + url + "§7.");
                        }
                        continue;
                    }
                    case "targetPlayer": {
                        EntityUtils.targetPlayer = split[1].equalsIgnoreCase("true");
                        ChatUtils.displayChatMessage("§7[§3§lAutoSettings§7] §a§l" + split[0] + "§7 set to §c§l" + EntityUtils.targetPlayer + "§7.");
                        continue;
                    }
                    case "targetMobs": {
                        EntityUtils.targetMobs = split[1].equalsIgnoreCase("true");
                        ChatUtils.displayChatMessage("§7[§3§lAutoSettings§7] §a§l" + split[0] + "§7 set to §c§l" + EntityUtils.targetMobs + "§7.");
                        continue;
                    }
                    case "targetAnimals": {
                        EntityUtils.targetAnimals = split[1].equalsIgnoreCase("true");
                        ChatUtils.displayChatMessage("§7[§3§lAutoSettings§7] §a§l" + split[0] + "§7 set to §c§l" + EntityUtils.targetAnimals + "§7.");
                        continue;
                    }
                    case "targetInvisible": {
                        EntityUtils.targetInvisible = split[1].equalsIgnoreCase("true");
                        ChatUtils.displayChatMessage("§7[§3§lAutoSettings§7] §a§l" + split[0] + "§7 set to §c§l" + EntityUtils.targetInvisible + "§7.");
                        continue;
                    }
                    case "targetDead": {
                        EntityUtils.targetDead = split[1].equalsIgnoreCase("true");
                        ChatUtils.displayChatMessage("§7[§3§lAutoSettings§7] §a§l" + split[0] + "§7 set to §c§l" + EntityUtils.targetDead + "§7.");
                        continue;
                    }
                    default: {
                        if (split.length == 3) {
                            final String moduleName = split[0];
                            final String valueName = split[1];
                            final String value = split[2];
                            final Module module = ModuleManager.getModule(moduleName);
                            if (module != null) {
                                if (module.getCategory() != ModuleCategory.RENDER) {
                                    if (valueName.equalsIgnoreCase("toggle")) {
                                        module.setState(value.equalsIgnoreCase("true"));
                                        ChatUtils.displayChatMessage("§7[§3§lAutoSettings§7] §a§l" + module.getName() + " §7was toggled §c§l" + (module.getState() ? "on" : "off") + "§7.");
                                    }
                                    else if (valueName.equalsIgnoreCase("bind")) {
                                        module.setKeyBind(Keyboard.getKeyIndex(value));
                                        ChatUtils.displayChatMessage("§7[§3§lAutoSettings§7] §a§l" + module.getName() + " §7was bound to §c§l" + Keyboard.getKeyName(module.getKeyBind()) + "§7.");
                                    }
                                    else {
                                        final Value moduleValue = module.getValue(valueName);
                                        if (moduleValue != null) {
                                            try {
                                                moduleValue.setValueSilent(value);
                                                ChatUtils.displayChatMessage("§7[§3§lAutoSettings§7] §a§l" + module.getName() + "§7 value §8§l" + moduleValue.getValueName() + "§7 set to §c§l" + value + "§7.");
                                            }
                                            catch (Exception e) {
                                                ChatUtils.displayChatMessage("§7[§3§lAutoSettings§7] §a§l" + e.getClass().getName() + "§7(" + e.getMessage() + ") §cexception while set §a§l" + value + "§c to §a§l" + moduleValue.getValueName() + "§c in §a§l" + module.getName() + "§c.");
                                            }
                                        }
                                        else {
                                            ChatUtils.displayChatMessage("§7[§3§lAutoSettings§7] §cValue §a§l" + valueName + "§c don't found in module §a§l" + moduleName + "§c.");
                                        }
                                    }
                                }
                                else {
                                    ChatUtils.displayChatMessage("§7[§3§lAutoSettings§7] §cModule §a§l" + moduleName + "§c is a render module!");
                                }
                            }
                            else {
                                ChatUtils.displayChatMessage("§7[§3§lAutoSettings§7] §cModule §a§l" + moduleName + "§c was not found!");
                            }
                            continue;
                        }
                        ChatUtils.displayChatMessage("§7[§3§lAutoSettings§7] §cSyntax error in setting script.\n§8§lLine: §7" + scriptLine);
                        continue;
                    }
                }
            }
        }
        LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.valuesConfig);
    }
    
    public static String generateScript(final boolean values, final boolean binds, final boolean states) {
        final StringBuilder stringBuilder = new StringBuilder();
        final Iterator<Value> iterator;
        Value value;
        final StringBuilder sb;
        ModuleManager.getModules().stream().filter(module -> module.getCategory() != ModuleCategory.RENDER && !(module instanceof NameProtect) && !(module instanceof Spammer)).forEach(module -> {
            if (values) {
                module.getValues().iterator();
                while (iterator.hasNext()) {
                    value = iterator.next();
                    sb.append(module.getName()).append(" ").append(value.getValueName()).append(" ").append(value.asObject()).append("\n");
                }
            }
            if (states) {
                sb.append(module.getName()).append(" toggle ").append(module.getState()).append("\n");
            }
            if (binds) {
                sb.append(module.getName()).append(" bind ").append(Keyboard.getKeyName(module.getKeyBind())).append("\n");
            }
            return;
        });
        return stringBuilder.toString();
    }
}
