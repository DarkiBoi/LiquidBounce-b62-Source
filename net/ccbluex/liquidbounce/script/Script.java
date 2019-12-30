// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.script;

import kotlin.TypeCastException;
import java.nio.charset.Charset;
import net.ccbluex.liquidbounce.script.api.Chat;
import net.ccbluex.liquidbounce.script.api.Item;
import net.ccbluex.liquidbounce.script.api.CreativeTab;
import net.ccbluex.liquidbounce.script.api.ModuleManager;
import jdk.internal.dynalink.beans.StaticClass;
import net.ccbluex.liquidbounce.script.api.CommandManager;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraft.client.Minecraft;
import javax.script.ScriptEngineManager;
import kotlin.jvm.internal.Intrinsics;
import java.io.File;
import org.jetbrains.annotations.NotNull;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\bH\u0002J\u000e\u0010\u001f\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\bJ\u000e\u0010 \u001a\u00020\u001d2\u0006\u0010!\u001a\u00020\u001bJ\u0006\u0010\"\u001a\u00020\u001dJ\u0006\u0010#\u001a\u00020\u001dJ\u0006\u0010$\u001a\u00020\u001dJ\u0006\u0010%\u001a\u00020\u001dR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.¢\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u00020\bX\u0086.¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\u000eX\u0082.¢\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\bX\u0086.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\n\"\u0004\b\u0013\u0010\fR\u001a\u0010\u0014\u001a\u00020\u0015X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006&" }, d2 = { "Lnet/ccbluex/liquidbounce/script/Script;", "", "scriptFile", "Ljava/io/File;", "(Ljava/io/File;)V", "invocable", "Ljavax/script/Invocable;", "scriptAuthor", "", "getScriptAuthor", "()Ljava/lang/String;", "setScriptAuthor", "(Ljava/lang/String;)V", "scriptEngine", "Ljavax/script/ScriptEngine;", "getScriptFile", "()Ljava/io/File;", "scriptName", "getScriptName", "setScriptName", "scriptVersion", "", "getScriptVersion", "()D", "setScriptVersion", "(D)V", "state", "", "callFunction", "", "functionName", "import", "loadScript", "reload", "onDisable", "onEnable", "onLoad", "reloadScript", "LiquidBounce" })
public final class Script
{
    private ScriptEngine scriptEngine;
    private Invocable invocable;
    @NotNull
    public String scriptName;
    private double scriptVersion;
    @NotNull
    public String scriptAuthor;
    private boolean state;
    @NotNull
    private final File scriptFile;
    
    @NotNull
    public final String getScriptName() {
        final String scriptName = this.scriptName;
        if (scriptName == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptName");
        }
        return scriptName;
    }
    
    public final void setScriptName(@NotNull final String <set-?>) {
        Intrinsics.checkParameterIsNotNull(<set-?>, "<set-?>");
        this.scriptName = <set-?>;
    }
    
    public final double getScriptVersion() {
        return this.scriptVersion;
    }
    
    public final void setScriptVersion(final double <set-?>) {
        this.scriptVersion = <set-?>;
    }
    
    @NotNull
    public final String getScriptAuthor() {
        final String scriptAuthor = this.scriptAuthor;
        if (scriptAuthor == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptAuthor");
        }
        return scriptAuthor;
    }
    
    public final void setScriptAuthor(@NotNull final String <set-?>) {
        Intrinsics.checkParameterIsNotNull(<set-?>, "<set-?>");
        this.scriptAuthor = <set-?>;
    }
    
    public final void loadScript(final boolean reload) {
        final ScriptEngine engineByName = new ScriptEngineManager().getEngineByName("nashorn");
        Intrinsics.checkExpressionValueIsNotNull(engineByName, "ScriptEngineManager().getEngineByName(\"nashorn\")");
        this.scriptEngine = engineByName;
        final ScriptEngine scriptEngine = this.scriptEngine;
        if (scriptEngine == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptEngine");
        }
        scriptEngine.put("mc", Minecraft.func_71410_x());
        final ScriptEngine scriptEngine2 = this.scriptEngine;
        if (scriptEngine2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptEngine");
        }
        scriptEngine2.put("scriptManager", LiquidBounce.CLIENT.scriptManager);
        final ScriptEngine scriptEngine3 = this.scriptEngine;
        if (scriptEngine3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptEngine");
        }
        scriptEngine3.put("script", this);
        final ScriptEngine scriptEngine4 = this.scriptEngine;
        if (scriptEngine4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptEngine");
        }
        scriptEngine4.put("commandManager", StaticClass.forClass(CommandManager.INSTANCE.getClass()));
        final ScriptEngine scriptEngine5 = this.scriptEngine;
        if (scriptEngine5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptEngine");
        }
        scriptEngine5.put("moduleManager", StaticClass.forClass(ModuleManager.INSTANCE.getClass()));
        final ScriptEngine scriptEngine6 = this.scriptEngine;
        if (scriptEngine6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptEngine");
        }
        scriptEngine6.put("creativeTabs", StaticClass.forClass(CreativeTab.INSTANCE.getClass()));
        final ScriptEngine scriptEngine7 = this.scriptEngine;
        if (scriptEngine7 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptEngine");
        }
        scriptEngine7.put("item", StaticClass.forClass(Item.INSTANCE.getClass()));
        final ScriptEngine scriptEngine8 = this.scriptEngine;
        if (scriptEngine8 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptEngine");
        }
        scriptEngine8.put("chat", StaticClass.forClass(Chat.INSTANCE.getClass()));
        final String scriptText = FilesKt__FileReadWriteKt.readText$default(this.scriptFile, null, 1, null);
        final ScriptEngine scriptEngine9 = this.scriptEngine;
        if (scriptEngine9 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptEngine");
        }
        scriptEngine9.eval(scriptText);
        final ScriptEngine scriptEngine10 = this.scriptEngine;
        if (scriptEngine10 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptEngine");
        }
        if (scriptEngine10 == null) {
            throw new TypeCastException("null cannot be cast to non-null type javax.script.Invocable");
        }
        this.invocable = (Invocable)scriptEngine10;
        final ScriptEngine scriptEngine11 = this.scriptEngine;
        if (scriptEngine11 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptEngine");
        }
        final Object value = scriptEngine11.get("scriptName");
        if (value == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        this.scriptName = (String)value;
        final ScriptEngine scriptEngine12 = this.scriptEngine;
        if (scriptEngine12 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptEngine");
        }
        final Object value2 = scriptEngine12.get("scriptVersion");
        if (value2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Double");
        }
        this.scriptVersion = (double)value2;
        final ScriptEngine scriptEngine13 = this.scriptEngine;
        if (scriptEngine13 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptEngine");
        }
        final Object value3 = scriptEngine13.get("scriptAuthor");
        if (value3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        this.scriptAuthor = (String)value3;
        if (!reload) {
            this.onLoad();
        }
    }
    
    public final void reloadScript() {
        this.onDisable();
        this.loadScript(true);
        this.onEnable();
    }
    
    public final void onLoad() {
        this.callFunction("onLoad");
    }
    
    public final void onEnable() {
        if (this.state) {
            return;
        }
        this.callFunction("onEnable");
        this.state = true;
    }
    
    public final void onDisable() {
        if (!this.state) {
            return;
        }
        this.callFunction("onDisable");
        this.state = false;
    }
    
    public final void import(@NotNull final String scriptFile) {
        Intrinsics.checkParameterIsNotNull(scriptFile, "scriptFile");
        final ScriptEngine scriptEngine = this.scriptEngine;
        if (scriptEngine == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptEngine");
        }
        scriptEngine.eval(FilesKt__FileReadWriteKt.readText$default(new File(LiquidBounce.CLIENT.scriptManager.getScriptsFolder(), scriptFile), null, 1, null));
    }
    
    private final void callFunction(final String functionName) {
        try {
            final Invocable invocable = this.invocable;
            if (invocable == null) {
                Intrinsics.throwUninitializedPropertyAccessException("invocable");
            }
            invocable.invokeFunction(functionName, new Object[0]);
        }
        catch (NoSuchMethodException ex) {}
    }
    
    @NotNull
    public final File getScriptFile() {
        return this.scriptFile;
    }
    
    public Script(@NotNull final File scriptFile) {
        Intrinsics.checkParameterIsNotNull(scriptFile, "scriptFile");
        this.scriptFile = scriptFile;
        this.scriptVersion = 1.0;
        this.loadScript(false);
    }
}
