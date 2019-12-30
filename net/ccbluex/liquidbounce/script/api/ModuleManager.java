// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.script.api;

import net.ccbluex.liquidbounce.script.api.module.AdaptedModule;
import kotlin.jvm.JvmStatic;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.script.api.module.ScriptModule;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.module.Module;
import org.jetbrains.annotations.NotNull;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0007J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH\u0007J\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u000fH\u0007¨\u0006\u0010" }, d2 = { "Lnet/ccbluex/liquidbounce/script/api/ModuleManager;", "", "()V", "getModule", "Lnet/ccbluex/liquidbounce/script/api/module/AdaptedModule;", "moduleName", "", "registerModule", "Lnet/ccbluex/liquidbounce/features/module/Module;", "scriptObjectMirror", "Ljdk/nashorn/api/scripting/ScriptObjectMirror;", "unregisterModule", "", "module", "autoDisable", "", "LiquidBounce" })
public final class ModuleManager
{
    public static final ModuleManager INSTANCE;
    
    @JvmStatic
    @NotNull
    public static final Module registerModule(@NotNull final ScriptObjectMirror scriptObjectMirror) {
        Intrinsics.checkParameterIsNotNull(scriptObjectMirror, "scriptObjectMirror");
        final ScriptModule module = new ScriptModule(scriptObjectMirror);
        LiquidBounce.CLIENT.moduleManager.registerModule(module);
        return module;
    }
    
    @JvmStatic
    public static final void unregisterModule(@NotNull final Module module) {
        Intrinsics.checkParameterIsNotNull(module, "module");
        unregisterModule(module, true);
    }
    
    @JvmStatic
    public static final void unregisterModule(@NotNull final Module module, final boolean autoDisable) {
        Intrinsics.checkParameterIsNotNull(module, "module");
        if (autoDisable && module.getState()) {
            module.setState(false);
        }
        LiquidBounce.CLIENT.moduleManager.unregisterModule(module);
    }
    
    @JvmStatic
    @NotNull
    public static final AdaptedModule getModule(@NotNull final String moduleName) {
        Intrinsics.checkParameterIsNotNull(moduleName, "moduleName");
        final Module module = net.ccbluex.liquidbounce.features.module.ModuleManager.getModule(moduleName);
        Intrinsics.checkExpressionValueIsNotNull(module, "ModuleManager.getModule(moduleName)");
        return new AdaptedModule(module);
    }
    
    private ModuleManager() {
    }
    
    static {
        INSTANCE = new ModuleManager();
    }
}
