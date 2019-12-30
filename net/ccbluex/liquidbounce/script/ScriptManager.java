// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.script;

import net.ccbluex.liquidbounce.LiquidBounce;
import java.util.Iterator;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import kotlin.jvm.internal.Intrinsics;
import java.io.FileFilter;
import java.io.File;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u000b\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0007J\u0006\u0010\u0012\u001a\u00020\u0010J\u0006\u0010\u0013\u001a\u00020\u0010J\u000e\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\fJ\u000e\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0017\u001a\u00020\fJ\u0006\u0010\u0018\u001a\u00020\u0010J\u0006\u0010\u0019\u001a\u00020\u0010J\u0006\u0010\u001a\u001a\u00020\u0010R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D¢\u0006\u0002\n\u0000R!\u0010\u0005\u001a\u0012\u0012\u0004\u0012\u00020\u00070\u0006j\b\u0012\u0004\u0012\u00020\u0007`\b¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u001b" }, d2 = { "Lnet/ccbluex/liquidbounce/script/ScriptManager;", "", "()V", "scriptFileExtension", "", "scripts", "Ljava/util/ArrayList;", "Lnet/ccbluex/liquidbounce/script/Script;", "Lkotlin/collections/ArrayList;", "getScripts", "()Ljava/util/ArrayList;", "scriptsFolder", "Ljava/io/File;", "getScriptsFolder", "()Ljava/io/File;", "deleteScript", "", "script", "disableScripts", "enableScripts", "importScript", "file", "loadScript", "scriptFile", "loadScripts", "reloadScripts", "unloadScripts", "LiquidBounce" })
public final class ScriptManager
{
    @NotNull
    private final ArrayList<Script> scripts;
    @NotNull
    private final File scriptsFolder;
    private final String scriptFileExtension = ".js";
    
    @NotNull
    public final ArrayList<Script> getScripts() {
        return this.scripts;
    }
    
    @NotNull
    public final File getScriptsFolder() {
        return this.scriptsFolder;
    }
    
    public final void loadScripts() {
        if (!this.scriptsFolder.exists()) {
            this.scriptsFolder.mkdir();
        }
        final File[] listFiles = this.scriptsFolder.listFiles((FileFilter)new ScriptManager$loadScripts.ScriptManager$loadScripts$1(this));
        Intrinsics.checkExpressionValueIsNotNull(listFiles, "scriptsFolder.listFiles(\u2026h(scriptFileExtension) })");
        final File[] array;
        final Object[] $receiver$iv = array = listFiles;
        for (final File it : array) {
            final Object element$iv = it;
            final int n = 0;
            final File file = it;
            Intrinsics.checkExpressionValueIsNotNull(file, "it");
            this.loadScript(file);
        }
    }
    
    public final void unloadScripts() {
        this.scripts.clear();
    }
    
    public final void loadScript(@NotNull final File scriptFile) {
        Intrinsics.checkParameterIsNotNull(scriptFile, "scriptFile");
        try {
            this.scripts.add(new Script(scriptFile));
            ClientUtils.getLogger().info("Successfully loaded script: " + scriptFile.getName());
        }
        catch (Throwable t) {
            ClientUtils.getLogger().error("Failed to load script: " + scriptFile.getName(), t);
        }
    }
    
    public final void enableScripts() {
        final Iterable $receiver$iv = this.scripts;
        for (final Object element$iv : $receiver$iv) {
            final Script it = (Script)element$iv;
            final int n = 0;
            it.onEnable();
        }
    }
    
    public final void disableScripts() {
        final Iterable $receiver$iv = this.scripts;
        for (final Object element$iv : $receiver$iv) {
            final Script it = (Script)element$iv;
            final int n = 0;
            it.onDisable();
        }
    }
    
    public final void importScript(@NotNull final File file) {
        Intrinsics.checkParameterIsNotNull(file, "file");
        final File scriptFile = new File(this.scriptsFolder, file.getName());
        FilesKt__UtilsKt.copyTo$default(file, scriptFile, false, 0, 6, null);
        this.loadScript(scriptFile);
        ClientUtils.getLogger().info("Successfully imported script: " + scriptFile.getName());
    }
    
    public final void deleteScript(@NotNull final Script script) {
        Intrinsics.checkParameterIsNotNull(script, "script");
        script.onDisable();
        this.scripts.remove(script);
        script.getScriptFile().delete();
        ClientUtils.getLogger().info("Successfully deleted script: " + script.getScriptFile().getName());
    }
    
    public final void reloadScripts() {
        this.disableScripts();
        this.unloadScripts();
        this.loadScripts();
        this.enableScripts();
        ClientUtils.getLogger().info("Successfully reloaded scripts.");
    }
    
    public ScriptManager() {
        this.scripts = new ArrayList<Script>();
        this.scriptsFolder = new File(LiquidBounce.CLIENT.fileManager.dir, "scripts");
    }
}
