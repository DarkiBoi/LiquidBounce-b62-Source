// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.misc;

import net.ccbluex.liquidbounce.event.EventTarget;
import java.util.Iterator;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.utils.render.ChatColor;
import net.ccbluex.liquidbounce.file.configs.FriendsConfig;
import net.ccbluex.liquidbounce.event.events.TextEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.TextValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "NameProtect", description = "Changes playernames clientside.", category = ModuleCategory.MISC)
public class NameProtect extends Module
{
    private final TextValue fakeNameValue;
    public final BoolValue allPlayersValue;
    public final BoolValue skinProtectValue;
    
    public NameProtect() {
        this.fakeNameValue = new TextValue("FakeName", "&cMe");
        this.allPlayersValue = new BoolValue("AllPlayers", false);
        this.skinProtectValue = new BoolValue("SkinProtect", true);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("nameprotect", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("fakeName")) {
                    this.chatSyntax(".nameprotect <fakeName>");
                    return;
                }
                if (args.length > 2) {
                    final String name = args[2];
                    NameProtect.this.fakeNameValue.setValue(name);
                    this.chat("§7NameProtect fakename was set to §8" + name + "§7.");
                    NameProtect$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                    return;
                }
                this.chatSyntax(".nameprotect fakeName <value>");
            }
        });
    }
    
    @EventTarget(ignoreCondition = true)
    public void onText(final TextEvent event) {
        if (NameProtect.mc.field_71439_g == null || event.getText().contains("§8[§9§lLiquidBounce§8] §3")) {
            return;
        }
        for (final FriendsConfig.Friend friend : LiquidBounce.CLIENT.fileManager.friendsConfig.getFriends()) {
            event.setText(StringUtils.replace(event.getText(), friend.getPlayerName(), ChatColor.translateAlternateColorCodes(friend.getAlias()) + "§f"));
        }
        if (!this.getState()) {
            return;
        }
        event.setText(StringUtils.replace(event.getText(), NameProtect.mc.field_71439_g.func_70005_c_(), ChatColor.translateAlternateColorCodes(this.fakeNameValue.asString()) + "§f"));
        if (this.allPlayersValue.asBoolean()) {
            for (final NetworkPlayerInfo playerInfo : NameProtect.mc.func_147114_u().func_175106_d()) {
                event.setText(StringUtils.replace(event.getText(), playerInfo.func_178845_a().getName(), "Protected User"));
            }
        }
    }
}
