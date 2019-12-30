// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command;

import net.ccbluex.liquidbounce.utils.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class Command
{
    private final String command;
    private final String[] alias;
    protected static final Minecraft mc;
    
    public Command(final String command, final String[] alias) {
        this.command = command;
        this.alias = alias;
    }
    
    public String getCommand() {
        return this.command;
    }
    
    public String[] getAlias() {
        return this.alias;
    }
    
    public abstract void execute(final String[] p0);
    
    protected void chat(final String msg) {
        ChatUtils.displayChatMessage("§8[§9§lLiquidBounce§8] §3" + msg);
    }
    
    protected void chatSyntax(final String syntax) {
        ChatUtils.displayChatMessage("§8[§9§lLiquidBounce§8] §3Syntax: §7" + syntax);
    }
    
    protected void chatSyntax(final String[] syntaxes) {
        ChatUtils.displayChatMessage("§8[§9§lLiquidBounce§8] §3Syntax:");
        for (final String syntax : syntaxes) {
            ChatUtils.displayChatMessage("§8> §7." + this.getCommand() + " " + syntax.toLowerCase());
        }
    }
    
    protected void chatSyntaxError() {
        ChatUtils.displayChatMessage("§8[§9§lLiquidBounce§8] §3Syntax error");
    }
    
    static {
        mc = Minecraft.func_71410_x();
    }
}
