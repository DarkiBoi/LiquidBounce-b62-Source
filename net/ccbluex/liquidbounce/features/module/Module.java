// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;
import net.ccbluex.liquidbounce.valuesystem.Value;
import net.ccbluex.liquidbounce.ui.hud.element.elements.notifications.Notification;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.render.ChatColor;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.ccbluex.liquidbounce.event.EventListener;

@SideOnly(Side.CLIENT)
public class Module implements EventListener
{
    protected String name;
    protected String description;
    protected ModuleCategory category;
    protected boolean state;
    private int keyBind;
    private final boolean canEnable;
    protected static final Minecraft mc;
    public final float hue;
    public float slide;
    
    public Module() {
        this.name = this.getClass().getAnnotation(ModuleInfo.class).name();
        this.description = this.getClass().getAnnotation(ModuleInfo.class).description();
        this.category = this.getClass().getAnnotation(ModuleInfo.class).category();
        this.keyBind = this.getClass().getAnnotation(ModuleInfo.class).keyBind();
        this.canEnable = this.getClass().getAnnotation(ModuleInfo.class).canEnable();
        this.hue = RandomUtils.getRandom().nextFloat();
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getTagName() {
        return this.getName() + ((this.getTag() == null) ? "" : (" §7" + this.getTag()));
    }
    
    public String getColorlessTagName() {
        return this.getName() + ((this.getTag() == null) ? "" : (" " + ChatColor.stripColor(this.getTag())));
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public ModuleCategory getCategory() {
        return this.category;
    }
    
    public boolean getState() {
        return this.state;
    }
    
    public int getKeyBind() {
        return this.keyBind;
    }
    
    public void setKeyBind(final int keyBind) {
        this.keyBind = keyBind;
        LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.modulesConfig);
    }
    
    public String getTag() {
        return null;
    }
    
    public void setState(final boolean state) {
        try {
            if (this.getState() == state) {
                return;
            }
            this.onToggle(state);
            if (state) {
                this.onEnable();
                if (this.canEnable) {
                    this.state = true;
                }
            }
            else {
                this.onDisable();
                this.state = false;
            }
            LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.modulesConfig);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void toggle() {
        this.setState(!this.getState());
    }
    
    public void onToggle(final boolean state) {
        if (!LiquidBounce.CLIENT.isStarting && this.getState() != state) {
            Module.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.click"), 1.0f));
            LiquidBounce.CLIENT.hud.addNotification(new Notification((state ? "Enabled " : "Disabled ") + this.getName()));
        }
    }
    
    public void onEnable() {
    }
    
    public void onDisable() {
    }
    
    public void onStarted() {
    }
    
    public boolean showArray() {
        return true;
    }
    
    public Value getValue(final String valueName) {
        for (final Field field : this.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                final Object o = field.get(this);
                if (o instanceof Value) {
                    final Value value = (Value)o;
                    if (value.getValueName().equalsIgnoreCase(valueName)) {
                        return value;
                    }
                }
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public List<Value> getValues() {
        final List<Value> values = new ArrayList<Value>();
        for (final Field field : this.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                final Object o = field.get(this);
                if (o instanceof Value) {
                    values.add((Value)o);
                }
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return values;
    }
    
    @Override
    public boolean handleEvents() {
        return this.getState();
    }
    
    static {
        mc = Minecraft.func_71410_x();
    }
}
