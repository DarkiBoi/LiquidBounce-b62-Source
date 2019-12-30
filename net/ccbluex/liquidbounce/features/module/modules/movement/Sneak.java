// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.minecraft.client.settings.GameSettings;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Sneak", description = "Automatically sneaks all the time.", category = ModuleCategory.MOVEMENT)
public class Sneak extends Module
{
    public final ListValue modeValue;
    public final BoolValue stopMoveValue;
    private boolean sneaked;
    
    public Sneak() {
        this.modeValue = new ListValue("Mode", new String[] { "Legit", "Vanilla", "Switch", "MineSecure" }, "MineSecure");
        this.stopMoveValue = new BoolValue("StopMove", false);
    }
    
    @Override
    public void onEnable() {
        if (Sneak.mc.field_71439_g == null) {
            return;
        }
        if ("vanilla".equalsIgnoreCase(this.modeValue.asString())) {
            Sneak.mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Sneak.mc.field_71439_g, C0BPacketEntityAction.Action.START_SNEAKING));
        }
    }
    
    @EventTarget
    public void onMotion(final MotionEvent event) {
        if (this.stopMoveValue.asBoolean() && MovementUtils.isMoving()) {
            if (this.sneaked) {
                this.onDisable();
                this.sneaked = false;
            }
            return;
        }
        this.sneaked = true;
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "legit": {
                Sneak.mc.field_71474_y.field_74311_E.field_74513_e = true;
                break;
            }
            case "switch": {
                switch (event.getEventState()) {
                    case PRE: {
                        Sneak.mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Sneak.mc.field_71439_g, C0BPacketEntityAction.Action.START_SNEAKING));
                        Sneak.mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Sneak.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SNEAKING));
                        break;
                    }
                    case POST: {
                        Sneak.mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Sneak.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SNEAKING));
                        Sneak.mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Sneak.mc.field_71439_g, C0BPacketEntityAction.Action.START_SNEAKING));
                        break;
                    }
                }
                break;
            }
            case "minesecure": {
                if (event.getEventState() == EventState.PRE) {
                    break;
                }
                Sneak.mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Sneak.mc.field_71439_g, C0BPacketEntityAction.Action.START_SNEAKING));
                break;
            }
        }
    }
    
    @Override
    public void onDisable() {
        if (Sneak.mc.field_71439_g == null) {
            return;
        }
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "legit": {
                if (!GameSettings.func_100015_a(Sneak.mc.field_71474_y.field_74311_E)) {
                    Sneak.mc.field_71474_y.field_74311_E.field_74513_e = false;
                    break;
                }
                break;
            }
            case "vanilla":
            case "switch":
            case "minesecure": {
                Sneak.mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Sneak.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SNEAKING));
                break;
            }
        }
        super.onDisable();
    }
}
