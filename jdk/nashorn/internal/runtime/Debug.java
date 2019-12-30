// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.parser.TokenStream;
import jdk.nashorn.internal.parser.Lexer;
import jdk.nashorn.api.scripting.NashornException;

public final class Debug
{
    private Debug() {
    }
    
    public static String firstJSFrame(final Throwable t) {
        for (final StackTraceElement ste : t.getStackTrace()) {
            if (ECMAErrors.isScriptFrame(ste)) {
                return ste.toString();
            }
        }
        return "<native code>";
    }
    
    public static String firstJSFrame() {
        return firstJSFrame(new Throwable());
    }
    
    public static String scriptStack() {
        return NashornException.getScriptStackString(new Throwable());
    }
    
    public static String id(final Object x) {
        return String.format("0x%08x", System.identityHashCode(x));
    }
    
    public static int intId(final Object x) {
        return System.identityHashCode(x);
    }
    
    public static String stackTraceElementAt(final int depth) {
        return new Throwable().getStackTrace()[depth + 1].toString();
    }
    
    public static String caller(final int depth, final int count, final String... ignores) {
        String result = "";
        final StackTraceElement[] callers = Thread.currentThread().getStackTrace();
        int c = count;
        int i = depth + 1;
    Label_0019:
        while (i < callers.length && c != 0) {
            final StackTraceElement element = callers[i];
            final String method = element.getMethodName();
            while (true) {
                for (final String ignore : ignores) {
                    if (method.compareTo(ignore) == 0) {
                        ++i;
                        continue Label_0019;
                    }
                }
                result += (method + ":" + element.getLineNumber() + "                              ").substring(0, 30);
                --c;
                continue;
            }
        }
        return result.isEmpty() ? "<no caller>" : result;
    }
    
    public static void dumpTokens(final Source source, final Lexer lexer, final TokenStream stream) {
        int k = 0;
        while (true) {
            if (k > stream.last()) {
                lexer.lexify();
            }
            else {
                final long token = stream.get(k);
                final TokenType type = Token.descType(token);
                System.out.println("" + k + ": " + Token.toString(source, token, true));
                ++k;
                if (type == TokenType.EOF) {
                    break;
                }
                continue;
            }
        }
    }
}
