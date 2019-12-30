// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.world;

import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.ScaledResolution;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.BlockOverlay;
import net.minecraft.client.renderer.GlStateManager;
import net.ccbluex.liquidbounce.event.events.Render2DEvent;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.item.ItemBlock;
import net.ccbluex.liquidbounce.utils.block.PlaceInfo;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.client.settings.GameSettings;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.util.Vec3;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Scaffold", description = "Automatically places blocks beneath your feet.", category = ModuleCategory.WORLD, keyBind = 23)
public class Scaffold extends Module
{
    public final ListValue modeValue;
    private final IntegerValue maxDelayValue;
    private final IntegerValue minDelayValue;
    private final BoolValue autoBlockValue;
    private final BoolValue stayAutoBlock;
    public final BoolValue sprintValue;
    private final BoolValue swingValue;
    private final BoolValue searchValue;
    private final BoolValue eagleValue;
    private final BoolValue placeableDelay;
    private final IntegerValue blocksToEagleValue;
    private final IntegerValue expandLengthValue;
    private final BoolValue rotationsValue;
    private final BoolValue keepRotation;
    private final BoolValue zitterValue;
    private final ListValue zitterModeValue;
    private final FloatValue zitterSpeed;
    private final FloatValue zitterStrength;
    private final FloatValue timerValue;
    private final FloatValue speedModifierValue;
    private final BoolValue sameYValue;
    private final BoolValue airSafeValue;
    private final BoolValue counterDisplayValue;
    private final BoolValue markValue;
    private BlockPos targetBlock;
    private EnumFacing targetFacing;
    private Vec3 targetVec;
    private int launchY;
    private float yaw;
    private float pitch;
    private int slot;
    private boolean switchAACAD;
    private final MSTimer delayTimer;
    private int placedBlocksWithoutEagle;
    private final MSTimer zitterTimer;
    private long delay;
    
    public Scaffold() {
        this.modeValue = new ListValue("Mode", new String[] { "Normal", "Rewinside", "Expand" }, "Normal");
        this.maxDelayValue = new IntegerValue("MaxDelay", 0, 0, 1000) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int i = Scaffold.this.minDelayValue.asInteger();
                if (i > Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(i);
                }
            }
        };
        this.minDelayValue = new IntegerValue("MinDelay", 0, 0, 1000) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int i = Scaffold.this.maxDelayValue.asInteger();
                if (i < Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(i);
                }
            }
        };
        this.autoBlockValue = new BoolValue("AutoBlock", true);
        this.stayAutoBlock = new BoolValue("StayAutoBlock", false);
        this.sprintValue = new BoolValue("Sprint", true);
        this.swingValue = new BoolValue("Swing", true);
        this.searchValue = new BoolValue("Search", true);
        this.eagleValue = new BoolValue("Eagle", false);
        this.placeableDelay = new BoolValue("PlaceableDelay", false);
        this.blocksToEagleValue = new IntegerValue("BlocksToEagle", 0, 0, 10);
        this.expandLengthValue = new IntegerValue("ExpandLength", 5, 1, 6);
        this.rotationsValue = new BoolValue("Rotations", true);
        this.keepRotation = new BoolValue("KeepRotation", false);
        this.zitterValue = new BoolValue("Zitter", false);
        this.zitterModeValue = new ListValue("ZitterMode", new String[] { "Teleport", "Smooth" }, "Teleport");
        this.zitterSpeed = new FloatValue("ZitterSpeed", 0.13f, 0.1f, 0.3f);
        this.zitterStrength = new FloatValue("ZitterStrength", 0.072f, 0.05f, 0.2f);
        this.timerValue = new FloatValue("Timer", 1.0f, 0.1f, 10.0f);
        this.speedModifierValue = new FloatValue("SpeedModifier", 1.0f, 0.0f, 2.0f);
        this.sameYValue = new BoolValue("SameY", false);
        this.airSafeValue = new BoolValue("AirSafe", false);
        this.counterDisplayValue = new BoolValue("Counter", true);
        this.markValue = new BoolValue("Mark", false);
        this.yaw = Float.NaN;
        this.pitch = Float.NaN;
        this.delayTimer = new MSTimer();
        this.placedBlocksWithoutEagle = 0;
        this.zitterTimer = new MSTimer();
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("scaffold", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("mode")) {
                        if (args.length > 2 && Scaffold.this.modeValue.contains(args[2])) {
                            Scaffold.this.modeValue.setValue(args[2].toLowerCase());
                            this.chat("§7Scaffold mode was set to §8" + Scaffold.this.modeValue.asString().toUpperCase() + "§7.");
                            Scaffold$3.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        this.chatSyntax(".scaffold mode §c<§8" + Strings.join(Scaffold.this.modeValue.getValues(), "§7, §8") + "§c>");
                        return;
                    }
                    else {
                        if (args[1].equalsIgnoreCase("autoblock")) {
                            Scaffold.this.autoBlockValue.setValue(!Scaffold.this.autoBlockValue.asBoolean());
                            this.chat("§7Scaffold autoblock was toggled §8" + (Scaffold.this.autoBlockValue.asBoolean() ? "on" : "off") + "§7.");
                            Scaffold$3.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        if (args[1].equalsIgnoreCase("swing")) {
                            Scaffold.this.swingValue.setValue(!Scaffold.this.swingValue.asBoolean());
                            this.chat("§7Scaffold swing was toggled §8" + (Scaffold.this.swingValue.asBoolean() ? "on" : "off") + "§7.");
                            Scaffold$3.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        if (args[1].equalsIgnoreCase("expandlength")) {
                            if (args.length > 2) {
                                try {
                                    final int value = Integer.parseInt(args[2]);
                                    Scaffold.this.expandLengthValue.setValue(value);
                                    this.chat("§7Scaffold ExpandLength was set to §8" + value + "§7.");
                                    Scaffold$3.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                }
                                catch (NumberFormatException exception) {
                                    this.chatSyntaxError();
                                }
                                return;
                            }
                            this.chatSyntax(".scaffold expandlength <Length>");
                            return;
                        }
                        else if (args[1].equalsIgnoreCase("maxdelay")) {
                            if (args.length > 2) {
                                try {
                                    final int value = Integer.parseInt(args[2]);
                                    if (Scaffold.this.minDelayValue.asInteger() > value) {
                                        this.chat("MinDelay can't higher as MaxDelay!");
                                        return;
                                    }
                                    Scaffold.this.maxDelayValue.setValue(value);
                                    Scaffold.this.delay = TimeUtils.randomDelay(Scaffold.this.minDelayValue.asInteger(), Scaffold.this.maxDelayValue.asInteger());
                                    this.chat("§7Scaffold maxdelay was set to §8" + value + "§7.");
                                    Scaffold$3.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                }
                                catch (NumberFormatException exception) {
                                    this.chatSyntaxError();
                                }
                                return;
                            }
                            this.chatSyntax(".scaffold maxdelay <value>");
                            return;
                        }
                        else if (args[1].equalsIgnoreCase("mindelay")) {
                            if (args.length > 2) {
                                try {
                                    final int value = Integer.parseInt(args[2]);
                                    if (Scaffold.this.maxDelayValue.asInteger() < value) {
                                        this.chat("MinDelay can't higher as MaxDelay!");
                                        return;
                                    }
                                    Scaffold.this.minDelayValue.setValue(value);
                                    Scaffold.this.delay = TimeUtils.randomDelay(Scaffold.this.minDelayValue.asInteger(), Scaffold.this.maxDelayValue.asInteger());
                                    this.chat("§7Scaffold mindelay was set to §8" + value + "§7.");
                                    Scaffold$3.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                }
                                catch (NumberFormatException exception) {
                                    this.chatSyntaxError();
                                }
                                return;
                            }
                            this.chatSyntax(".scaffold mindelay <value>");
                            return;
                        }
                        else if (args[1].equalsIgnoreCase("timer")) {
                            if (args.length > 2) {
                                try {
                                    final float value2 = Float.parseFloat(args[2]);
                                    Scaffold.this.timerValue.setValue(value2);
                                    this.chat("§7Scaffold Timer was set to §8" + value2 + "§7.");
                                    Scaffold$3.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                }
                                catch (NumberFormatException exception) {
                                    this.chatSyntaxError();
                                }
                                return;
                            }
                            this.chatSyntax(".scaffold timer <value>");
                            return;
                        }
                        else if (args[1].equalsIgnoreCase("speedmodifier")) {
                            if (args.length > 2) {
                                try {
                                    final float value2 = Float.parseFloat(args[2]);
                                    Scaffold.this.speedModifierValue.setValue(value2);
                                    this.chat("§7Scaffold SpeedModifier was set to §8" + value2 + "§7.");
                                    Scaffold$3.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                }
                                catch (NumberFormatException exception) {
                                    this.chatSyntaxError();
                                }
                                return;
                            }
                            this.chatSyntax(".scaffold speedmodifier <value>");
                            return;
                        }
                    }
                }
                this.chatSyntax(".scaffold <mode, mindelay, maxdelay, autoblock, swing, expandlength, timer, speedmodifier>");
            }
        });
    }
    
    @Override
    public void onEnable() {
        this.launchY = (int)Scaffold.mc.field_71439_g.field_70163_u;
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        Scaffold.mc.field_71428_T.field_74278_d = this.timerValue.asFloat();
        if (Scaffold.mc.field_71439_g.field_70122_E) {
            if (!GameSettings.func_100015_a(Scaffold.mc.field_71474_y.field_74366_z)) {
                Scaffold.mc.field_71474_y.field_74366_z.field_74513_e = false;
            }
            if (!GameSettings.func_100015_a(Scaffold.mc.field_71474_y.field_74370_x)) {
                Scaffold.mc.field_71474_y.field_74370_x.field_74513_e = false;
            }
            final String value = this.modeValue.asString();
            if (value.equalsIgnoreCase("Rewinside")) {
                MovementUtils.strafe(0.2f);
                Scaffold.mc.field_71439_g.field_70181_x = 0.0;
            }
            if (this.zitterValue.asBoolean() && this.zitterModeValue.asString().equalsIgnoreCase("smooth")) {
                if (this.zitterTimer.hasTimePassed(100L)) {
                    this.switchAACAD = !this.switchAACAD;
                    this.zitterTimer.reset();
                }
                if (this.switchAACAD) {
                    Scaffold.mc.field_71474_y.field_74366_z.field_74513_e = true;
                    Scaffold.mc.field_71474_y.field_74370_x.field_74513_e = false;
                }
                else {
                    Scaffold.mc.field_71474_y.field_74366_z.field_74513_e = false;
                    Scaffold.mc.field_71474_y.field_74370_x.field_74513_e = true;
                }
            }
        }
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        if (Scaffold.mc.field_71439_g == null) {
            return;
        }
        final Packet packet = event.getPacket();
        if (packet instanceof C09PacketHeldItemChange) {
            final C09PacketHeldItemChange packetHeldItemChange = (C09PacketHeldItemChange)packet;
            this.slot = packetHeldItemChange.func_149614_c();
        }
    }
    
    @EventTarget
    public void onMotion(final MotionEvent event) {
        if (this.rotationsValue.asBoolean() && this.keepRotation.asBoolean() && !Float.isNaN(this.yaw) && !Float.isNaN(this.pitch)) {
            RotationUtils.setTargetRotation(this.yaw, this.pitch);
        }
        final String mode = this.modeValue.asString();
        switch (event.getEventState()) {
            case PRE: {
                if (this.eagleValue.asBoolean()) {
                    if (this.placedBlocksWithoutEagle >= this.blocksToEagleValue.asInteger()) {
                        Scaffold.mc.field_71474_y.field_74311_E.field_74513_e = (Scaffold.mc.field_71441_e.func_180495_p(new BlockPos(Scaffold.mc.field_71439_g.field_70165_t, Scaffold.mc.field_71439_g.field_70163_u - 1.0, Scaffold.mc.field_71439_g.field_70161_v)).func_177230_c() == Blocks.field_150350_a);
                        this.placedBlocksWithoutEagle = 0;
                    }
                    else {
                        ++this.placedBlocksWithoutEagle;
                    }
                }
                if (this.zitterValue.asBoolean() && this.zitterModeValue.asString().equalsIgnoreCase("teleport")) {
                    final EntityPlayerSP player = Scaffold.mc.field_71439_g;
                    double yaw = player.field_70177_z;
                    MovementUtils.strafe(this.zitterSpeed.asFloat());
                    if (this.switchAACAD) {
                        yaw += 90.0;
                    }
                    else {
                        yaw -= 90.0;
                    }
                    yaw = Math.toRadians(yaw);
                    final EntityPlayerSP entityPlayerSP = player;
                    entityPlayerSP.field_70159_w -= Math.sin(yaw) * this.zitterStrength.asFloat();
                    final EntityPlayerSP entityPlayerSP2 = player;
                    entityPlayerSP2.field_70179_y += Math.cos(yaw) * this.zitterStrength.asFloat();
                    this.switchAACAD = !this.switchAACAD;
                }
                final String lowerCase = mode.toLowerCase();
                switch (lowerCase) {
                    default: {
                        final BlockPos blockPosition = (Scaffold.mc.field_71439_g.field_70163_u == (int)Scaffold.mc.field_71439_g.field_70163_u + 0.5) ? new BlockPos((Entity)Scaffold.mc.field_71439_g) : new BlockPos(Scaffold.mc.field_71439_g.field_70165_t, Scaffold.mc.field_71439_g.field_70163_u, Scaffold.mc.field_71439_g.field_70161_v).func_177977_b();
                        if (!BlockUtils.isReplaceable(blockPosition) || this.search(blockPosition)) {
                            return;
                        }
                        if (this.searchValue.asBoolean()) {
                            for (int x = -1; x <= 1; ++x) {
                                for (int z = -1; z <= 1; ++z) {
                                    if (this.search(blockPosition.func_177982_a(x, 0, z))) {
                                        return;
                                    }
                                }
                            }
                            break;
                        }
                        break;
                    }
                    case "expand": {
                        int i = 0;
                        while (i < this.expandLengthValue.asInteger()) {
                            final BlockPos blockPos = new BlockPos(Scaffold.mc.field_71439_g.field_70165_t + ((Scaffold.mc.field_71439_g.func_174811_aO() == EnumFacing.WEST) ? (-i) : ((Scaffold.mc.field_71439_g.func_174811_aO() == EnumFacing.EAST) ? i : 0)), Scaffold.mc.field_71439_g.field_70163_u - ((Scaffold.mc.field_71439_g.field_70163_u == (int)Scaffold.mc.field_71439_g.field_70163_u + 0.5) ? 0.0 : 1.0), Scaffold.mc.field_71439_g.field_70161_v + ((Scaffold.mc.field_71439_g.func_174811_aO() == EnumFacing.NORTH) ? (-i) : ((Scaffold.mc.field_71439_g.func_174811_aO() == EnumFacing.SOUTH) ? i : 0)));
                            final PlaceInfo placeInfo = PlaceInfo.get(blockPos);
                            if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                                this.targetBlock = placeInfo.getBlockPos();
                                this.targetFacing = placeInfo.getEnumFacing();
                                this.targetVec = placeInfo.getVec3();
                                if (this.rotationsValue.asBoolean()) {
                                    RotationUtils.faceBlockPacket(blockPos);
                                    break;
                                }
                                break;
                            }
                            else {
                                ++i;
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case POST: {
                if (this.targetBlock == null || this.targetFacing == null || this.targetVec == null) {
                    if (this.placeableDelay.asBoolean()) {
                        this.delayTimer.reset();
                    }
                    return;
                }
                if (!this.delayTimer.hasTimePassed(this.delay) || (this.sameYValue.asBoolean() && this.launchY != (int)Scaffold.mc.field_71439_g.field_70163_u)) {
                    return;
                }
                int blockSlot = Integer.MAX_VALUE;
                ItemStack itemStack = Scaffold.mc.field_71439_g.func_70694_bm();
                if (Scaffold.mc.field_71439_g.func_70694_bm() == null || !(Scaffold.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemBlock)) {
                    if (!this.autoBlockValue.asBoolean()) {
                        return;
                    }
                    blockSlot = this.findBlock();
                    if (blockSlot == -1) {
                        return;
                    }
                    Scaffold.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(blockSlot - 36));
                    itemStack = Scaffold.mc.field_71439_g.field_71069_bz.func_75139_a(blockSlot).func_75211_c();
                }
                if (Scaffold.mc.field_71442_b.func_178890_a(Scaffold.mc.field_71439_g, Scaffold.mc.field_71441_e, itemStack, this.targetBlock, this.targetFacing, this.targetVec)) {
                    this.delayTimer.reset();
                    this.delay = TimeUtils.randomDelay(this.minDelayValue.asInteger(), this.maxDelayValue.asInteger());
                    if (Scaffold.mc.field_71439_g.field_70122_E) {
                        final EntityPlayerSP field_71439_g = Scaffold.mc.field_71439_g;
                        field_71439_g.field_70159_w *= this.speedModifierValue.asFloat();
                        final EntityPlayerSP field_71439_g2 = Scaffold.mc.field_71439_g;
                        field_71439_g2.field_70179_y *= this.speedModifierValue.asFloat();
                    }
                    if (this.swingValue.asBoolean()) {
                        Scaffold.mc.field_71439_g.func_71038_i();
                    }
                    else {
                        Scaffold.mc.func_147114_u().func_147297_a((Packet)new C0APacketAnimation());
                    }
                }
                if (!this.stayAutoBlock.asBoolean() && blockSlot != Integer.MAX_VALUE) {
                    Scaffold.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(Scaffold.mc.field_71439_g.field_71071_by.field_70461_c));
                }
                this.targetBlock = null;
                this.targetFacing = null;
                this.targetVec = null;
                break;
            }
        }
    }
    
    @Override
    public void onDisable() {
        if (Scaffold.mc.field_71439_g == null) {
            return;
        }
        if (!GameSettings.func_100015_a(Scaffold.mc.field_71474_y.field_74311_E)) {
            Scaffold.mc.field_71474_y.field_74311_E.field_74513_e = false;
        }
        if (!GameSettings.func_100015_a(Scaffold.mc.field_71474_y.field_74366_z)) {
            Scaffold.mc.field_71474_y.field_74366_z.field_74513_e = false;
        }
        if (!GameSettings.func_100015_a(Scaffold.mc.field_71474_y.field_74370_x)) {
            Scaffold.mc.field_71474_y.field_74370_x.field_74513_e = false;
        }
        this.yaw = Float.NaN;
        this.pitch = Float.NaN;
        Scaffold.mc.field_71428_T.field_74278_d = 1.0f;
        if (this.slot != Scaffold.mc.field_71439_g.field_71071_by.field_70461_c) {
            Scaffold.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(Scaffold.mc.field_71439_g.field_71071_by.field_70461_c));
        }
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
        if (this.airSafeValue.asBoolean() || Scaffold.mc.field_71439_g.field_70122_E) {
            event.setSafeWalk(true);
        }
    }
    
    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        if (this.counterDisplayValue.asBoolean()) {
            GlStateManager.func_179094_E();
            final BlockOverlay blockOverlay = (BlockOverlay)ModuleManager.getModule(BlockOverlay.class);
            if (blockOverlay.getState() && blockOverlay.infoValue.asBoolean() && Scaffold.mc.field_71476_x != null && Scaffold.mc.field_71476_x.func_178782_a() != null && Scaffold.mc.field_71441_e.func_180495_p(Scaffold.mc.field_71476_x.func_178782_a()).func_177230_c() != null && BlockUtils.canBeClicked(Scaffold.mc.field_71476_x.func_178782_a()) && Scaffold.mc.field_71441_e.func_175723_af().func_177746_a(Scaffold.mc.field_71476_x.func_178782_a())) {
                GlStateManager.func_179109_b(0.0f, 15.0f, 0.0f);
            }
            final String info = "Blocks: §7" + this.getBlocksAmount();
            final ScaledResolution scaledResolution = new ScaledResolution(Scaffold.mc);
            RenderUtils.drawBorderedRect((float)(scaledResolution.func_78326_a() / 2 - 2), (float)(scaledResolution.func_78328_b() / 2 + 5), (float)(scaledResolution.func_78326_a() / 2 + Fonts.font40.func_78256_a(info) + 2), (float)(scaledResolution.func_78328_b() / 2 + 16), 3.0f, Color.BLACK.getRGB(), Color.BLACK.getRGB());
            GlStateManager.func_179117_G();
            Fonts.font40.func_78276_b(info, scaledResolution.func_78326_a() / 2, scaledResolution.func_78328_b() / 2 + 7, Color.WHITE.getRGB());
            GlStateManager.func_179121_F();
        }
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (!this.markValue.asBoolean()) {
            return;
        }
        for (int i = 0; i < (this.modeValue.asString().equalsIgnoreCase("Expand") ? (this.expandLengthValue.asInteger() + 1) : 2); ++i) {
            final BlockPos blockPos = new BlockPos(Scaffold.mc.field_71439_g.field_70165_t + ((Scaffold.mc.field_71439_g.func_174811_aO() == EnumFacing.WEST) ? (-i) : ((Scaffold.mc.field_71439_g.func_174811_aO() == EnumFacing.EAST) ? i : 0)), Scaffold.mc.field_71439_g.field_70163_u - ((Scaffold.mc.field_71439_g.field_70163_u == (int)Scaffold.mc.field_71439_g.field_70163_u + 0.5) ? 0.0 : 1.0), Scaffold.mc.field_71439_g.field_70161_v + ((Scaffold.mc.field_71439_g.func_174811_aO() == EnumFacing.NORTH) ? (-i) : ((Scaffold.mc.field_71439_g.func_174811_aO() == EnumFacing.SOUTH) ? i : 0)));
            final PlaceInfo placeInfo = PlaceInfo.get(blockPos);
            if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                RenderUtils.drawBlockBox(blockPos, new Color(68, 117, 255, 100), false);
                break;
            }
        }
    }
    
    private boolean search(final BlockPos blockPosition) {
        if (!BlockUtils.isReplaceable(blockPosition)) {
            return false;
        }
        final Vec3 eyesPos = RotationUtils.getEyesPos();
        final Vec3 posVec = new Vec3((Vec3i)blockPosition).func_72441_c(0.5, 0.5, 0.5);
        final double distanceSqPosVec = eyesPos.func_72436_e(posVec);
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = blockPosition.func_177972_a(side);
            final Vec3 dirVec = new Vec3(side.func_176730_m());
            final Vec3 hitVec = posVec.func_178787_e(new Vec3(dirVec.field_72450_a * 0.5, dirVec.field_72448_b * 0.5, dirVec.field_72449_c * 0.5));
            if (BlockUtils.canBeClicked(neighbor) && eyesPos.func_72436_e(hitVec) <= 18.0625 && distanceSqPosVec <= eyesPos.func_72436_e(posVec.func_178787_e(dirVec))) {
                if (Scaffold.mc.field_71441_e.func_147447_a(eyesPos, hitVec, false, true, false) == null) {
                    final double diffX = hitVec.field_72450_a - eyesPos.field_72450_a;
                    final double diffY = hitVec.field_72448_b - eyesPos.field_72448_b;
                    final double diffZ = hitVec.field_72449_c - eyesPos.field_72449_c;
                    final double diffXZ = MathHelper.func_76133_a(diffX * diffX + diffZ * diffZ);
                    this.yaw = MathHelper.func_76142_g((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f);
                    this.pitch = MathHelper.func_76142_g((float)(-Math.toDegrees(Math.atan2(diffY, diffXZ))));
                    if (this.rotationsValue.asBoolean()) {
                        RotationUtils.setTargetRotation(this.yaw, this.pitch);
                    }
                    final Vec3 rotationVector = RotationUtils.getVectorForRotation(this.pitch, this.yaw);
                    final Vec3 vector = eyesPos.func_72441_c(rotationVector.field_72450_a * 4.0, rotationVector.field_72448_b * 4.0, rotationVector.field_72449_c * 4.0);
                    final MovingObjectPosition obj = Scaffold.mc.field_71441_e.func_147447_a(eyesPos, vector, false, false, true);
                    if (obj.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK) {
                        if (obj.func_178782_a().equals((Object)neighbor)) {
                            this.targetBlock = neighbor;
                            this.targetFacing = side.func_176734_d();
                            this.targetVec = hitVec;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private int findBlock() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = Scaffold.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (itemStack != null && itemStack.func_77973_b() instanceof ItemBlock) {
                return i;
            }
        }
        return -1;
    }
    
    private int getBlocksAmount() {
        int amount = 0;
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = Scaffold.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (itemStack != null && itemStack.func_77973_b() instanceof ItemBlock) {
                amount += itemStack.field_77994_a;
            }
        }
        return amount;
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
