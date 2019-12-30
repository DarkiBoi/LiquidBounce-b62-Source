// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.script.api;

import kotlin.jvm.JvmStatic;
import jdk.nashorn.internal.runtime.JSType;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007¨\u0006\u0007" }, d2 = { "Lnet/ccbluex/liquidbounce/script/api/CreativeTab;", "", "()V", "registerTab", "", "scriptObjectMirror", "Ljdk/nashorn/api/scripting/ScriptObjectMirror;", "LiquidBounce" })
public final class CreativeTab
{
    public static final CreativeTab INSTANCE;
    
    @JvmStatic
    public static final void registerTab(@NotNull final ScriptObjectMirror scriptObjectMirror) {
        Intrinsics.checkParameterIsNotNull(scriptObjectMirror, "scriptObjectMirror");
        new CreativeTab$registerTab.CreativeTab$registerTab$1(scriptObjectMirror, JSType.toString(scriptObjectMirror.callMember("getLabel", new Object[0])));
    }
    
    private CreativeTab() {
    }
    
    static {
        INSTANCE = new CreativeTab();
    }
}
