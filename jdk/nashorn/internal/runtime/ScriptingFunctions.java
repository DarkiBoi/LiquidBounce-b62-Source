// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.util.ArrayList;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.lookup.Lookup;
import java.util.Iterator;
import java.util.List;
import java.io.OutputStreamWriter;
import java.util.Map;
import jdk.nashorn.internal.objects.NativeArray;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandle;

public final class ScriptingFunctions
{
    public static final MethodHandle READLINE;
    public static final MethodHandle READFULLY;
    public static final MethodHandle EXEC;
    public static final String EXEC_NAME = "$EXEC";
    public static final String OUT_NAME = "$OUT";
    public static final String ERR_NAME = "$ERR";
    public static final String EXIT_NAME = "$EXIT";
    public static final String THROW_ON_ERROR_NAME = "throwOnError";
    public static final String ENV_NAME = "$ENV";
    public static final String PWD_NAME = "PWD";
    
    private ScriptingFunctions() {
    }
    
    public static Object readLine(final Object self, final Object prompt) throws IOException {
        if (prompt != ScriptRuntime.UNDEFINED) {
            System.out.print(JSType.toString(prompt));
        }
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }
    
    public static Object readFully(final Object self, final Object file) throws IOException {
        File f = null;
        if (file instanceof File) {
            f = (File)file;
        }
        else if (JSType.isString(file)) {
            f = new File(((CharSequence)file).toString());
        }
        if (f == null || !f.isFile()) {
            throw ECMAErrors.typeError("not.a.file", ScriptRuntime.safeToString(file));
        }
        return new String(Source.readFully(f));
    }
    
    public static Object exec(final Object self, final Object... args) throws IOException, InterruptedException {
        final ScriptObject global = Context.getGlobal();
        final Object string = (args.length > 0) ? args[0] : ScriptRuntime.UNDEFINED;
        final Object input = (args.length > 1) ? args[1] : ScriptRuntime.UNDEFINED;
        final Object[] argv = (args.length > 2) ? Arrays.copyOfRange(args, 2, args.length) : ScriptRuntime.EMPTY_ARRAY;
        final List<String> cmdLine = tokenizeString(JSType.toString(string));
        final Object[] array;
        final Object[] additionalArgs = array = ((argv.length == 1 && argv[0] instanceof NativeArray) ? ((NativeArray)argv[0]).asObjectArray() : argv);
        for (final Object arg : array) {
            cmdLine.add(JSType.toString(arg));
        }
        final ProcessBuilder processBuilder = new ProcessBuilder(cmdLine);
        final Object env = global.get("$ENV");
        if (env instanceof ScriptObject) {
            final ScriptObject envProperties = (ScriptObject)env;
            final Object pwd = envProperties.get("PWD");
            if (pwd != ScriptRuntime.UNDEFINED) {
                final File pwdFile = new File(JSType.toString(pwd));
                if (pwdFile.exists()) {
                    processBuilder.directory(pwdFile);
                }
            }
            final Map<String, String> environment = processBuilder.environment();
            environment.clear();
            for (final Map.Entry<Object, Object> entry : envProperties.entrySet()) {
                environment.put(JSType.toString(entry.getKey()), JSType.toString(entry.getValue()));
            }
        }
        final Process process = processBuilder.start();
        final IOException[] exception = new IOException[2];
        final StringBuilder outBuffer = new StringBuilder();
        final Thread outThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final char[] buffer = new char[1024];
                try (final InputStreamReader inputStream = new InputStreamReader(process.getInputStream())) {
                    int length;
                    while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
                        outBuffer.append(buffer, 0, length);
                    }
                }
                catch (IOException ex) {
                    exception[0] = ex;
                }
            }
        }, "$EXEC output");
        final StringBuilder errBuffer = new StringBuilder();
        final Thread errThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final char[] buffer = new char[1024];
                try (final InputStreamReader inputStream = new InputStreamReader(process.getErrorStream())) {
                    int length;
                    while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
                        errBuffer.append(buffer, 0, length);
                    }
                }
                catch (IOException ex) {
                    exception[1] = ex;
                }
            }
        }, "$EXEC error");
        outThread.start();
        errThread.start();
        if (!JSType.nullOrUndefined(input)) {
            try (final OutputStreamWriter outputStream = new OutputStreamWriter(process.getOutputStream())) {
                final String in = JSType.toString(input);
                outputStream.write(in, 0, in.length());
            }
            catch (IOException ex) {}
        }
        final int exit = process.waitFor();
        outThread.join();
        errThread.join();
        final String out = outBuffer.toString();
        final String err = errBuffer.toString();
        global.set("$OUT", out, 0);
        global.set("$ERR", err, 0);
        global.set("$EXIT", exit, 0);
        for (final IOException element : exception) {
            if (element != null) {
                throw element;
            }
        }
        if (exit != 0) {
            final Object exec = global.get("$EXEC");
            assert exec instanceof ScriptObject : "$EXEC is not a script object!";
            if (JSType.toBoolean(((ScriptObject)exec).get("throwOnError"))) {
                throw ECMAErrors.rangeError("exec.returned.non.zero", ScriptRuntime.safeToString(exit));
            }
        }
        return out;
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), ScriptingFunctions.class, name, Lookup.MH.type(rtype, types));
    }
    
    public static List<String> tokenizeString(final String str) {
        final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(str));
        tokenizer.resetSyntax();
        tokenizer.wordChars(0, 255);
        tokenizer.whitespaceChars(0, 32);
        tokenizer.commentChar(35);
        tokenizer.quoteChar(34);
        tokenizer.quoteChar(39);
        final List<String> tokenList = new ArrayList<String>();
        final StringBuilder toAppend = new StringBuilder();
        while (nextToken(tokenizer) != -1) {
            final String s = tokenizer.sval;
            if (s.endsWith("\\")) {
                toAppend.append(s.substring(0, s.length() - 1)).append(' ');
            }
            else {
                tokenList.add(toAppend.append(s).toString());
                toAppend.setLength(0);
            }
        }
        if (toAppend.length() != 0) {
            tokenList.add(toAppend.toString());
        }
        return tokenList;
    }
    
    private static int nextToken(final StreamTokenizer tokenizer) {
        try {
            return tokenizer.nextToken();
        }
        catch (IOException ioe) {
            return -1;
        }
    }
    
    static {
        READLINE = findOwnMH("readLine", Object.class, Object.class, Object.class);
        READFULLY = findOwnMH("readFully", Object.class, Object.class, Object.class);
        EXEC = findOwnMH("exec", Object.class, Object.class, Object[].class);
    }
}
