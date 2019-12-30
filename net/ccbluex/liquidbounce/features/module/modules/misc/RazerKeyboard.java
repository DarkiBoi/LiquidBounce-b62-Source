// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.misc;

import java.lang.reflect.Field;
import org.jglr.jchroma.utils.KeyboardKeys;
import org.lwjgl.input.Keyboard;
import java.util.Iterator;
import org.jglr.jchroma.effects.KeyboardEffect;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import org.jglr.jchroma.utils.ColorRef;
import org.jglr.jchroma.effects.CustomKeyboardEffect;
import org.jglr.jchroma.JChroma;
import net.ccbluex.liquidbounce.utils.threading.ToggleableLoop;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "RazerKeyboard", description = "Enables custom colored keys on Razer keyboards.", category = ModuleCategory.MISC)
public class RazerKeyboard extends Module
{
    private final BoolValue keysValue;
    private final BoolValue healthValue;
    private final IntegerValue updateDelayValue;
    private final ToggleableLoop toggleableLoop;
    
    public RazerKeyboard() {
        this.keysValue = new BoolValue("Keys", true);
        this.healthValue = new BoolValue("Health", false);
        this.updateDelayValue = new IntegerValue("UpdateDelay", 50, 1, 1000);
        JChroma.getInstance();
        this.toggleableLoop = new ToggleableLoop(() -> {
            this.updateKeyboard();
            try {
                Thread.sleep(this.updateDelayValue.asInteger());
            }
            catch (InterruptedException ex) {}
        }, "Razer Visuals Updater");
    }
    
    private void updateKeyboard() {
        if (RazerKeyboard.mc.field_71439_g != null && RazerKeyboard.mc.field_71441_e != null) {
            JChroma.getInstance().init();
            final CustomKeyboardEffect customKeyboardEffect = new CustomKeyboardEffect();
            if (this.healthValue.asBoolean()) {
                final double health = RazerKeyboard.mc.field_71439_g.func_110143_aJ() / RazerKeyboard.mc.field_71439_g.func_110138_aP();
                customKeyboardEffect.fill(new ColorRef((int)(255.0 - health * 255.0), (int)(health * 255.0), 0));
            }
            if (this.keysValue.asBoolean()) {
                for (final Module module : ModuleManager.getModules()) {
                    if (module.getKeyBind() == 0) {
                        continue;
                    }
                    final int keyCode = this.translateKeyCode(module.getKeyBind());
                    if (keyCode == Integer.MIN_VALUE) {
                        continue;
                    }
                    customKeyboardEffect.setKeyColor(keyCode, module.getState() ? new ColorRef(66, 244, 98) : new ColorRef(255, 66, 66));
                }
            }
            JChroma.getInstance().createKeyboardEffect(customKeyboardEffect);
        }
        else {
            JChroma.getInstance().free();
        }
    }
    
    @Override
    public void onEnable() {
        this.toggleableLoop.startThread();
    }
    
    @Override
    public void onDisable() {
        this.toggleableLoop.stopThread();
        JChroma.getInstance().free();
    }
    
    private int translateKeyCode(final int keyCode) {
        for (final Field lwjglKeyboardField : Keyboard.class.getDeclaredFields()) {
            if (!lwjglKeyboardField.isAccessible()) {
                lwjglKeyboardField.setAccessible(true);
            }
            try {
                if (lwjglKeyboardField.getGenericType().getTypeName().equals("int")) {
                    final int lwjglKey = lwjglKeyboardField.getInt(null);
                    if (lwjglKey == keyCode) {
                        final String keyName = lwjglKeyboardField.getName().substring(4);
                        for (final Field razerKeyboardField : KeyboardKeys.class.getDeclaredFields()) {
                            if (!razerKeyboardField.isAccessible()) {
                                razerKeyboardField.setAccessible(true);
                            }
                            try {
                                final String razerKeyName = razerKeyboardField.getName().substring(6);
                                if (razerKeyName.equalsIgnoreCase(keyName)) {
                                    return razerKeyboardField.getInt(null);
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                                return Integer.MIN_VALUE;
                            }
                        }
                        return Integer.MIN_VALUE;
                    }
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
                return Integer.MIN_VALUE;
            }
        }
        return Integer.MIN_VALUE;
    }
}
