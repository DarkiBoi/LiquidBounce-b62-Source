// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "BugUp", description = "Automatically setbacks you after falling a certain distance.", category = ModuleCategory.MOVEMENT)
public class BugUp extends Module
{
    private final ListValue modeValue;
    private final FloatValue fallDistanceValue;
    private double prevX;
    private double prevY;
    private double prevZ;
    
    public BugUp() {
        this.modeValue = new ListValue("Mode", new String[] { "TeleportBack", "FlyFlag", "OnGroundSpoof" }, "FlyFlag");
        this.fallDistanceValue = new FloatValue("FallDistance", 2.0f, 1.0f, 5.0f);
    }
    
    @Override
    public void onDisable() {
        this.prevX = 0.0;
        this.prevY = 0.0;
        this.prevZ = 0.0;
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent e) {
        if (BugUp.mc.field_71439_g.field_70122_E) {
            this.prevX = BugUp.mc.field_71439_g.field_70165_t;
            this.prevY = BugUp.mc.field_71439_g.field_70163_u;
            this.prevZ = BugUp.mc.field_71439_g.field_70161_v;
        }
        if (BugUp.mc.field_71439_g.field_70143_R > this.fallDistanceValue.asFloat()) {
            final String mode = this.modeValue.asString();
            final String lowerCase = mode.toLowerCase();
            switch (lowerCase) {
                case "teleportback": {
                    BugUp.mc.field_71439_g.func_70634_a((double)(int)this.prevX, (double)(int)this.prevY, (double)(int)this.prevZ);
                    BugUp.mc.field_71439_g.field_70143_R = 0.0f;
                    BugUp.mc.field_71439_g.field_70181_x = 0.0;
                    break;
                }
                case "flyflag": {
                    final EntityPlayerSP field_71439_g = BugUp.mc.field_71439_g;
                    field_71439_g.field_70181_x += 0.1;
                    BugUp.mc.field_71439_g.field_70143_R = 0.0f;
                    break;
                }
                case "ongroundspoof": {
                    BugUp.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(true));
                    break;
                }
            }
        }
    }
}
