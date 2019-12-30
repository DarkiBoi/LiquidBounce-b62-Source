// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.script.api.module;

import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.event.events.Render2DEvent;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import org.jetbrains.annotations.NotNull;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "ScriptModule", description = "Empty", category = ModuleCategory.MISC)
@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J)\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0012\u0010\u000b\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0\f\"\u00020\rH\u0002¢\u0006\u0002\u0010\u000eJ\b\u0010\u000f\u001a\u00020\bH\u0016J\b\u0010\u0010\u001a\u00020\bH\u0016J\u0010\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u0013H\u0007J\u0010\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\u0016H\u0007J\u0010\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\u0019H\u0007J\u0010\u0010\u001a\u001a\u00020\b2\u0006\u0010\u001b\u001a\u00020\u001cH\u0007R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u001d" }, d2 = { "Lnet/ccbluex/liquidbounce/script/api/module/ScriptModule;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "scriptObjectMirror", "Ljdk/nashorn/api/scripting/ScriptObjectMirror;", "(Ljdk/nashorn/api/scripting/ScriptObjectMirror;)V", "getScriptObjectMirror", "()Ljdk/nashorn/api/scripting/ScriptObjectMirror;", "call", "", "member", "", "args", "", "", "(Ljava/lang/String;[Ljava/lang/Object;)V", "onDisable", "onEnable", "onMotion", "motionEvent", "Lnet/ccbluex/liquidbounce/event/events/MotionEvent;", "onRender2D", "render2DEvent", "Lnet/ccbluex/liquidbounce/event/events/Render2DEvent;", "onRender3D", "render3DEvent", "Lnet/ccbluex/liquidbounce/event/events/Render3DEvent;", "onUpdate", "updateEvent", "Lnet/ccbluex/liquidbounce/event/events/UpdateEvent;", "LiquidBounce" })
public final class ScriptModule extends Module
{
    @NotNull
    private final ScriptObjectMirror scriptObjectMirror;
    
    @Override
    public void onEnable() {
        this.call("onEnable", new Object[0]);
    }
    
    @Override
    public void onDisable() {
        this.call("onDisable", new Object[0]);
    }
    
    @EventTarget
    public final void onUpdate(@NotNull final UpdateEvent updateEvent) {
        Intrinsics.checkParameterIsNotNull(updateEvent, "updateEvent");
        this.call("onUpdate", new Object[0]);
    }
    
    @EventTarget
    public final void onMotion(@NotNull final MotionEvent motionEvent) {
        Intrinsics.checkParameterIsNotNull(motionEvent, "motionEvent");
        final String member = "onMotion";
        final Object[] args = { null };
        final int n = 0;
        final EventState eventState = motionEvent.getEventState();
        Intrinsics.checkExpressionValueIsNotNull(eventState, "motionEvent.eventState");
        final String stateName = eventState.getStateName();
        Intrinsics.checkExpressionValueIsNotNull(stateName, "motionEvent.eventState.stateName");
        args[n] = stateName;
        this.call(member, args);
    }
    
    @EventTarget
    public final void onRender2D(@NotNull final Render2DEvent render2DEvent) {
        Intrinsics.checkParameterIsNotNull(render2DEvent, "render2DEvent");
        this.call("onRender2D", render2DEvent.getPartialTicks());
    }
    
    @EventTarget
    public final void onRender3D(@NotNull final Render3DEvent render3DEvent) {
        Intrinsics.checkParameterIsNotNull(render3DEvent, "render3DEvent");
        this.call("onRender3D", render3DEvent.getPartialTicks());
    }
    
    private final void call(final String member, final Object... args) {
        if (this.scriptObjectMirror.hasMember(member)) {
            try {
                this.scriptObjectMirror.callMember(member, args);
            }
            catch (Throwable throwable) {
                ClientUtils.getLogger().error("A error happend with a the script module: " + this.getName(), throwable);
            }
        }
    }
    
    @NotNull
    public final ScriptObjectMirror getScriptObjectMirror() {
        return this.scriptObjectMirror;
    }
    
    public ScriptModule(@NotNull final ScriptObjectMirror scriptObjectMirror) {
        Intrinsics.checkParameterIsNotNull(scriptObjectMirror, "scriptObjectMirror");
        this.scriptObjectMirror = scriptObjectMirror;
        final Object callMember = this.scriptObjectMirror.callMember("getName", new Object[0]);
        if (callMember == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        this.name = (String)callMember;
        final Object callMember2 = this.scriptObjectMirror.callMember("getDescription", new Object[0]);
        if (callMember2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        this.description = (String)callMember2;
        final Object callMember3 = this.scriptObjectMirror.callMember("getCategory", new Object[0]);
        if (callMember3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        final String categoryString = (String)callMember3;
        for (final ModuleCategory category : ModuleCategory.values()) {
            if (StringsKt__StringsJVMKt.equals(categoryString, category.getDisplayName(), true)) {
                this.category = category;
            }
        }
    }
}
