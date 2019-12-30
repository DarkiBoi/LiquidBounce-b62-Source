// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.script.api.module;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.module.Module;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\u0005\u001a\u00020\u0006J\u0006\u0010\u0007\u001a\u00020\bJ\u0006\u0010\t\u001a\u00020\bJ\u0006\u0010\n\u001a\u00020\bJ\u0006\u0010\u000b\u001a\u00020\fJ\u0006\u0010\r\u001a\u00020\u000eJ\u000e\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0006J\u000e\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\fJ\u0006\u0010\u0013\u001a\u00020\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014" }, d2 = { "Lnet/ccbluex/liquidbounce/script/api/module/AdaptedModule;", "", "module", "Lnet/ccbluex/liquidbounce/features/module/Module;", "(Lnet/ccbluex/liquidbounce/features/module/Module;)V", "getBind", "", "getCategory", "", "getDescription", "getName", "getState", "", "register", "", "setBind", "key", "setState", "state", "unregister", "LiquidBounce" })
public final class AdaptedModule
{
    private final Module module;
    
    @NotNull
    public final String getName() {
        final String name = this.module.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, "module.name");
        return name;
    }
    
    @NotNull
    public final String getDescription() {
        final String description = this.module.getDescription();
        Intrinsics.checkExpressionValueIsNotNull(description, "module.description");
        return description;
    }
    
    @NotNull
    public final String getCategory() {
        final ModuleCategory category = this.module.getCategory();
        Intrinsics.checkExpressionValueIsNotNull(category, "module.category");
        final String displayName = category.getDisplayName();
        Intrinsics.checkExpressionValueIsNotNull(displayName, "module.category.displayName");
        return displayName;
    }
    
    public final boolean getState() {
        return this.module.getState();
    }
    
    public final void setState(final boolean state) {
        this.module.setState(state);
    }
    
    public final int getBind() {
        return this.module.getKeyBind();
    }
    
    public final void setBind(final int key) {
        this.module.setKeyBind(key);
    }
    
    public final void register() {
        LiquidBounce.CLIENT.moduleManager.registerModule(this.module);
    }
    
    public final void unregister() {
        LiquidBounce.CLIENT.moduleManager.unregisterModule(this.module);
    }
    
    public AdaptedModule(@NotNull final Module module) {
        Intrinsics.checkParameterIsNotNull(module, "module");
        this.module = module;
    }
}
