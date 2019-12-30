// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiChat;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "InventoryMove", description = "Allows you to walk while an inventory is opened.", category = ModuleCategory.MOVEMENT)
public class InventoryMove extends Module
{
    public final BoolValue aacAdditionProValue;
    public final BoolValue noMoveClicks;
    
    public InventoryMove() {
        this.aacAdditionProValue = new BoolValue("AACAdditionPro", false);
        this.noMoveClicks = new BoolValue("NoMoveClicks", false);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("inventorymove", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("aacadditionpro")) {
                        InventoryMove.this.aacAdditionProValue.setValue(!InventoryMove.this.aacAdditionProValue.asBoolean());
                        this.chat("§7InventoryMove AACAdditionPro was toggled §8" + (InventoryMove.this.aacAdditionProValue.asBoolean() ? "on" : "off") + "§7.");
                        InventoryMove$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                        return;
                    }
                    if (args[1].equalsIgnoreCase("NoMoveClicks")) {
                        InventoryMove.this.noMoveClicks.setValue(!InventoryMove.this.noMoveClicks.asBoolean());
                        this.chat("§7InventoryMove NoMoveClicks was toggled §8" + (InventoryMove.this.noMoveClicks.asBoolean() ? "on" : "off") + "§7.");
                        InventoryMove$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                        return;
                    }
                }
                this.chatSyntax(".inventorymove <aacadditionpro, nomoveclicks>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (InventoryMove.mc.field_71439_g != null && !(InventoryMove.mc.field_71462_r instanceof GuiChat) && !(InventoryMove.mc.field_71462_r instanceof GuiIngameMenu)) {
            InventoryMove.mc.field_71474_y.field_74351_w.field_74513_e = GameSettings.func_100015_a(InventoryMove.mc.field_71474_y.field_74351_w);
            InventoryMove.mc.field_71474_y.field_74368_y.field_74513_e = GameSettings.func_100015_a(InventoryMove.mc.field_71474_y.field_74368_y);
            InventoryMove.mc.field_71474_y.field_74366_z.field_74513_e = GameSettings.func_100015_a(InventoryMove.mc.field_71474_y.field_74366_z);
            InventoryMove.mc.field_71474_y.field_74370_x.field_74513_e = GameSettings.func_100015_a(InventoryMove.mc.field_71474_y.field_74370_x);
            InventoryMove.mc.field_71474_y.field_74314_A.field_74513_e = GameSettings.func_100015_a(InventoryMove.mc.field_71474_y.field_74314_A);
            InventoryMove.mc.field_71474_y.field_151444_V.field_74513_e = GameSettings.func_100015_a(InventoryMove.mc.field_71474_y.field_151444_V);
        }
    }
    
    @Override
    public void onDisable() {
        if (!GameSettings.func_100015_a(InventoryMove.mc.field_71474_y.field_74351_w) || InventoryMove.mc.field_71462_r != null) {
            InventoryMove.mc.field_71474_y.field_74351_w.field_74513_e = false;
        }
        if (!GameSettings.func_100015_a(InventoryMove.mc.field_71474_y.field_74368_y) || InventoryMove.mc.field_71462_r != null) {
            InventoryMove.mc.field_71474_y.field_74368_y.field_74513_e = false;
        }
        if (!GameSettings.func_100015_a(InventoryMove.mc.field_71474_y.field_74366_z) || InventoryMove.mc.field_71462_r != null) {
            InventoryMove.mc.field_71474_y.field_74366_z.field_74513_e = false;
        }
        if (!GameSettings.func_100015_a(InventoryMove.mc.field_71474_y.field_74370_x) || InventoryMove.mc.field_71462_r != null) {
            InventoryMove.mc.field_71474_y.field_74370_x.field_74513_e = false;
        }
        if (!GameSettings.func_100015_a(InventoryMove.mc.field_71474_y.field_74314_A) || InventoryMove.mc.field_71462_r != null) {
            InventoryMove.mc.field_71474_y.field_74314_A.field_74513_e = false;
        }
        if (!GameSettings.func_100015_a(InventoryMove.mc.field_71474_y.field_151444_V) || InventoryMove.mc.field_71462_r != null) {
            InventoryMove.mc.field_71474_y.field_151444_V.field_74513_e = false;
        }
    }
    
    @Override
    public String getTag() {
        return this.aacAdditionProValue.asBoolean() ? "AACAdditionPro" : null;
    }
}
