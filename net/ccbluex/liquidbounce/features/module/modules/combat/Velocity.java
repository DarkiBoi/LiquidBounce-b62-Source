// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.events.JumpEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Velocity", description = "Allows you to modify the amount of knowckback you take.", category = ModuleCategory.COMBAT)
public class Velocity extends Module
{
    private final FloatValue horizontalValue;
    private final FloatValue verticalValue;
    private final ListValue modeValue;
    private final FloatValue reverseStrengthValue;
    private final FloatValue reverse2StrenghtValue;
    private final FloatValue aacPushXZReducerValue;
    private final BoolValue aacPushYReducerValue;
    private long velocityTime;
    private boolean gotVelocity;
    private boolean gotHurt;
    
    public Velocity() {
        this.horizontalValue = new FloatValue("Horizontal", 0.0f, 0.0f, 1.0f);
        this.verticalValue = new FloatValue("Vertical", 0.0f, 0.0f, 1.0f);
        this.modeValue = new ListValue("Mode", new String[] { "Simple", "AAC", "AACPush", "AACZero", "Jump", "Reverse", "Reverse2", "Glitch" }, "Simple");
        this.reverseStrengthValue = new FloatValue("ReverseStrength", 1.0f, 0.1f, 1.0f);
        this.reverse2StrenghtValue = new FloatValue("Reverse2Strength", 0.05f, 0.02f, 0.1f);
        this.aacPushXZReducerValue = new FloatValue("AACPushXZReducer", 2.0f, 1.0f, 3.0f);
        this.aacPushYReducerValue = new BoolValue("AACPushYReducer", true);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("velocity", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("horizontal")) {
                        if (args.length > 2) {
                            try {
                                final float value = Float.parseFloat(args[2]);
                                Velocity.this.horizontalValue.setValue(value);
                                this.chat("§7Velocity horizontal was set to §8" + value + "§7.");
                                Velocity$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".velocity horizontal <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("vertical")) {
                        if (args.length > 2) {
                            try {
                                final float value = Float.parseFloat(args[2]);
                                Velocity.this.verticalValue.setValue(value);
                                this.chat("§7Velocity vertical was set to §8" + value + "§7.");
                                Velocity$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".velocity vertical <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("mode")) {
                        if (args.length > 2 && Velocity.this.modeValue.contains(args[2])) {
                            Velocity.this.modeValue.setValue(args[2].toLowerCase());
                            this.chat("§7Velocity mode was set to §8" + Velocity.this.modeValue.asString().toUpperCase() + "§7.");
                            Velocity$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        this.chatSyntax(".velocity mode §c<§8" + Strings.join(Velocity.this.modeValue.getValues(), "§7, §8") + "§c>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("ReverseStrength")) {
                        if (args.length > 2) {
                            try {
                                final float value = Float.parseFloat(args[2]);
                                Velocity.this.reverseStrengthValue.setValue(value);
                                this.chat("§7Velocity reversestrength was set to §8" + value + "§7.");
                                Velocity$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".velocity reversestrength <value>");
                        return;
                    }
                }
                this.chatSyntax(".velocity <horizontal, vertical, mode, reversestrength>");
            }
        });
    }
    
    @Override
    public void onDisable() {
        if (Velocity.mc.field_71439_g == null) {
            return;
        }
        Velocity.mc.field_71439_g.field_71102_ce = 0.02f;
        super.onDisable();
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (Velocity.mc.field_71439_g.func_70090_H()) {
            return;
        }
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "reverse": {
                if (!this.gotVelocity) {
                    break;
                }
                if (!Velocity.mc.field_71439_g.field_70122_E && !Velocity.mc.field_71439_g.func_70090_H() && !Velocity.mc.field_71439_g.func_180799_ab() && !Velocity.mc.field_71439_g.field_70134_J) {
                    MovementUtils.strafe(MovementUtils.getSpeed() * this.reverseStrengthValue.asFloat());
                    break;
                }
                if (System.currentTimeMillis() - this.velocityTime > 80L) {
                    this.gotVelocity = false;
                    break;
                }
                break;
            }
            case "aac": {
                if (this.velocityTime != 0L && System.currentTimeMillis() - this.velocityTime > 80L) {
                    final EntityPlayerSP field_71439_g = Velocity.mc.field_71439_g;
                    field_71439_g.field_70159_w *= this.horizontalValue.asFloat();
                    final EntityPlayerSP field_71439_g2 = Velocity.mc.field_71439_g;
                    field_71439_g2.field_70179_y *= this.verticalValue.asFloat();
                    this.velocityTime = 0L;
                    break;
                }
                break;
            }
            case "jump": {
                if (Velocity.mc.field_71439_g.field_70737_aN > 0 && Velocity.mc.field_71439_g.field_70122_E) {
                    Velocity.mc.field_71439_g.field_70181_x = 0.42;
                    final float f = Velocity.mc.field_71439_g.field_70177_z * 0.017453292f;
                    final EntityPlayerSP field_71439_g3 = Velocity.mc.field_71439_g;
                    field_71439_g3.field_70159_w -= MathHelper.func_76126_a(f) * 0.2f;
                    final EntityPlayerSP field_71439_g4 = Velocity.mc.field_71439_g;
                    field_71439_g4.field_70179_y += MathHelper.func_76134_b(f) * 0.2f;
                    break;
                }
                break;
            }
            case "aacpush": {
                if (Velocity.mc.field_71439_g.field_71158_b.field_78901_c) {
                    break;
                }
                if (this.velocityTime != 0L && System.currentTimeMillis() - this.velocityTime > 80L) {
                    this.velocityTime = 0L;
                }
                if (Velocity.mc.field_71439_g.field_70737_aN > 0 && Velocity.mc.field_71439_g.field_70159_w != 0.0 && Velocity.mc.field_71439_g.field_70179_y != 0.0) {
                    Velocity.mc.field_71439_g.field_70122_E = true;
                }
                if (Velocity.mc.field_71439_g.field_70172_ad > 0 && this.aacPushYReducerValue.asBoolean()) {
                    final EntityPlayerSP field_71439_g5 = Velocity.mc.field_71439_g;
                    field_71439_g5.field_70181_x -= 0.0145;
                }
                if (Velocity.mc.field_71439_g.field_70172_ad >= 19) {
                    final double reduce = this.aacPushXZReducerValue.asDouble();
                    final EntityPlayerSP field_71439_g6 = Velocity.mc.field_71439_g;
                    field_71439_g6.field_70159_w /= reduce;
                    final EntityPlayerSP field_71439_g7 = Velocity.mc.field_71439_g;
                    field_71439_g7.field_70179_y /= reduce;
                    break;
                }
                break;
            }
            case "glitch": {
                Velocity.mc.field_71439_g.field_70145_X = this.gotVelocity;
                if (Velocity.mc.field_71439_g.field_70737_aN < 8 && Velocity.mc.field_71439_g.field_70737_aN > 6) {
                    Velocity.mc.field_71439_g.field_70181_x = 0.4;
                }
                this.gotVelocity = false;
                break;
            }
            case "reverse2": {
                if (!this.gotVelocity) {
                    Velocity.mc.field_71439_g.field_71102_ce = 0.02f;
                    break;
                }
                if (Velocity.mc.field_71439_g.field_70737_aN > 0) {
                    this.gotHurt = true;
                }
                if (!Velocity.mc.field_71439_g.field_70122_E && !Velocity.mc.field_71439_g.func_70090_H() && !Velocity.mc.field_71439_g.func_180799_ab() && !Velocity.mc.field_71439_g.field_70134_J) {
                    if (this.gotHurt) {
                        Velocity.mc.field_71439_g.field_71102_ce = this.reverse2StrenghtValue.asFloat();
                        break;
                    }
                    break;
                }
                else {
                    if (System.currentTimeMillis() - this.velocityTime > 80L) {
                        this.gotVelocity = false;
                        this.gotHurt = false;
                        break;
                    }
                    break;
                }
                break;
            }
            case "aaczero": {
                if (Velocity.mc.field_71439_g.field_70737_aN <= 0) {
                    this.gotVelocity = false;
                    break;
                }
                if (!this.gotVelocity || Velocity.mc.field_71439_g.field_70122_E) {
                    break;
                }
                if (Velocity.mc.field_71439_g.field_70143_R > 2.0f) {
                    break;
                }
                Velocity.mc.field_71439_g.func_70024_g(0.0, -1.0, 0.0);
                Velocity.mc.field_71439_g.field_70122_E = true;
                break;
            }
        }
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();
        if (packet instanceof S12PacketEntityVelocity && Velocity.mc.field_71439_g != null && Velocity.mc.field_71441_e != null) {
            final S12PacketEntityVelocity packetEntityVelocity = (S12PacketEntityVelocity)packet;
            if (Velocity.mc.field_71441_e.func_73045_a(packetEntityVelocity.func_149412_c()) == Velocity.mc.field_71439_g) {
                this.velocityTime = System.currentTimeMillis();
                final String mode = this.modeValue.asString();
                final String lowerCase = mode.toLowerCase();
                switch (lowerCase) {
                    case "simple": {
                        final double horizontal = this.horizontalValue.asFloat();
                        final double vertical = this.verticalValue.asFloat();
                        if (horizontal == 0.0 && vertical == 0.0) {
                            event.setCancelled(true);
                        }
                        packetEntityVelocity.field_149415_b = (int)(packetEntityVelocity.func_149411_d() * horizontal);
                        packetEntityVelocity.field_149416_c = (int)(packetEntityVelocity.func_149410_e() * vertical);
                        packetEntityVelocity.field_149414_d = (int)(packetEntityVelocity.func_149409_f() * horizontal);
                        break;
                    }
                    case "reverse":
                    case "reverse2":
                    case "aaczero": {
                        this.gotVelocity = true;
                        break;
                    }
                    case "glitch": {
                        if (!Velocity.mc.field_71439_g.field_70122_E) {
                            break;
                        }
                        event.setCancelled(this.gotVelocity = true);
                        break;
                    }
                }
            }
        }
        if (packet instanceof S27PacketExplosion) {
            event.setCancelled(true);
        }
    }
    
    @EventTarget
    public void onJump(final JumpEvent event) {
        if (Velocity.mc.field_71439_g == null || Velocity.mc.field_71439_g.func_70090_H()) {
            return;
        }
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "aacpush":
            case "aaczero": {
                if (Velocity.mc.field_71439_g.field_70737_aN > 0) {
                    event.setCancelled(true);
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
