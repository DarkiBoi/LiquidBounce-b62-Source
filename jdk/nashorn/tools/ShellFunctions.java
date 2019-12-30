// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.tools;

import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.objects.Global;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import java.lang.invoke.MethodHandle;

public final class ShellFunctions
{
    public static final MethodHandle INPUT;
    public static final MethodHandle EVALINPUT;
    
    private ShellFunctions() {
    }
    
    public static Object input(final Object self, final Object endMarker, final Object prompt) throws IOException {
        final String endMarkerStr = (endMarker != ScriptRuntime.UNDEFINED) ? JSType.toString(endMarker) : "";
        final String promptStr = (prompt != ScriptRuntime.UNDEFINED) ? JSType.toString(prompt) : ">> ";
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        final StringBuilder buf = new StringBuilder();
        while (true) {
            System.out.print(promptStr);
            final String line = reader.readLine();
            if (line == null || line.equals(endMarkerStr)) {
                break;
            }
            buf.append(line);
            buf.append('\n');
        }
        return buf.toString();
    }
    
    public static Object evalinput(final Object self, final Object endMarker, final Object prompt) throws IOException {
        return Global.eval(self, input(self, endMarker, prompt));
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), ShellFunctions.class, name, Lookup.MH.type(rtype, types));
    }
    
    static {
        INPUT = findOwnMH("input", Object.class, Object.class, Object.class, Object.class);
        EVALINPUT = findOwnMH("evalinput", Object.class, Object.class, Object.class, Object.class);
    }
}
