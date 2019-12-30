// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.script.api;

import kotlin.jvm.JvmStatic;
import net.ccbluex.liquidbounce.LiquidBounce;
import kotlin.TypeCastException;
import jdk.nashorn.internal.runtime.JSType;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.command.Command;
import org.jetbrains.annotations.NotNull;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J#\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00060\tH\u0007¢\u0006\u0002\u0010\nJ\u0010\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\rH\u0007J\u0010\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0007H\u0007¨\u0006\u000f" }, d2 = { "Lnet/ccbluex/liquidbounce/script/api/CommandManager;", "", "()V", "executeCommand", "", "command", "", "Lnet/ccbluex/liquidbounce/features/command/Command;", "args", "", "(Lnet/ccbluex/liquidbounce/features/command/Command;[Ljava/lang/String;)V", "registerCommand", "scriptObjectMirror", "Ljdk/nashorn/api/scripting/ScriptObjectMirror;", "unregisterCommand", "LiquidBounce" })
public final class CommandManager
{
    public static final CommandManager INSTANCE;
    
    @JvmStatic
    @NotNull
    public static final Command registerCommand(@NotNull final ScriptObjectMirror scriptObjectMirror) {
        Intrinsics.checkParameterIsNotNull(scriptObjectMirror, "scriptObjectMirror");
        final String string = JSType.toString(scriptObjectMirror.callMember("getName", new Object[0]));
        final Object javaArray = JSType.toJavaArray(scriptObjectMirror.callMember("getAliases", new Object[0]), String.class);
        if (javaArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<kotlin.String>");
        }
        final CommandManager$registerCommand$command.CommandManager$registerCommand$command$1 command = new CommandManager$registerCommand$command.CommandManager$registerCommand$command$1(scriptObjectMirror, string, (String[])javaArray);
        LiquidBounce.CLIENT.commandManager.registerCommand((Command)command);
        return (Command)command;
    }
    
    @JvmStatic
    public static final void unregisterCommand(@NotNull final Command command) {
        Intrinsics.checkParameterIsNotNull(command, "command");
        LiquidBounce.CLIENT.commandManager.unregisterCommand(command);
    }
    
    @JvmStatic
    public static final void executeCommand(@NotNull final Command command, @NotNull final String[] args) {
        Intrinsics.checkParameterIsNotNull(command, "command");
        Intrinsics.checkParameterIsNotNull(args, "args");
        command.execute(args);
    }
    
    @JvmStatic
    public static final void executeCommand(@NotNull final String command) {
        Intrinsics.checkParameterIsNotNull(command, "command");
        LiquidBounce.CLIENT.commandManager.executeCommands(command);
    }
    
    private CommandManager() {
    }
    
    static {
        INSTANCE = new CommandManager();
    }
}
