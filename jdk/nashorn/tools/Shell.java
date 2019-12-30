// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.tools;

import java.util.Locale;
import jdk.nashorn.internal.runtime.JSType;
import java.io.Reader;
import java.io.InputStreamReader;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.api.scripting.NashornException;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.debug.PrintVisitor;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.debug.ASTWriter;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.parser.Parser;
import jdk.nashorn.internal.runtime.Source;
import java.io.BufferedReader;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import jdk.nashorn.internal.runtime.ScriptingFunctions;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileReader;
import java.io.File;
import jdk.nashorn.internal.runtime.options.Options;
import jdk.nashorn.internal.runtime.ErrorManager;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.util.List;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.Context;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ResourceBundle;

public class Shell
{
    private static final String MESSAGE_RESOURCE = "jdk.nashorn.tools.resources.Shell";
    private static final ResourceBundle bundle;
    public static final int SUCCESS = 0;
    public static final int COMMANDLINE_ERROR = 100;
    public static final int COMPILATION_ERROR = 101;
    public static final int RUNTIME_ERROR = 102;
    public static final int IO_ERROR = 103;
    public static final int INTERNAL_ERROR = 104;
    
    protected Shell() {
    }
    
    public static void main(final String[] args) {
        try {
            final int exitCode = main(System.in, System.out, System.err, args);
            if (exitCode != 0) {
                System.exit(exitCode);
            }
        }
        catch (IOException e) {
            System.err.println(e);
            System.exit(103);
        }
    }
    
    public static int main(final InputStream in, final OutputStream out, final OutputStream err, final String[] args) throws IOException {
        return new Shell().run(in, out, err, args);
    }
    
    protected final int run(final InputStream in, final OutputStream out, final OutputStream err, final String[] args) throws IOException {
        final Context context = makeContext(in, out, err, args);
        if (context == null) {
            return 100;
        }
        final Global global = context.createGlobal();
        final ScriptEnvironment env = context.getEnv();
        final List<String> files = env.getFiles();
        if (files.isEmpty()) {
            return readEvalPrint(context, global);
        }
        if (env._compile_only) {
            return compileScripts(context, global, files);
        }
        if (env._fx) {
            return runFXScripts(context, global, files);
        }
        return this.runScripts(context, global, files);
    }
    
    private static Context makeContext(final InputStream in, final OutputStream out, final OutputStream err, final String[] args) {
        final PrintStream pout = (PrintStream)((out instanceof PrintStream) ? out : new PrintStream(out));
        final PrintStream perr = (PrintStream)((err instanceof PrintStream) ? err : new PrintStream(err));
        final PrintWriter wout = new PrintWriter(pout, true);
        final PrintWriter werr = new PrintWriter(perr, true);
        final ErrorManager errors = new ErrorManager(werr);
        final Options options = new Options("nashorn", werr);
        if (args != null) {
            try {
                final String[] prepArgs = preprocessArgs(args);
                options.process(prepArgs);
            }
            catch (IllegalArgumentException e) {
                werr.println(Shell.bundle.getString("shell.usage"));
                options.displayHelp(e);
                return null;
            }
        }
        if (!options.getBoolean("scripting")) {
            for (final String fileName : options.getFiles()) {
                final File firstFile = new File(fileName);
                if (firstFile.isFile()) {
                    try (final FileReader fr = new FileReader(firstFile)) {
                        final int firstChar = fr.read();
                        if (firstChar == 35) {
                            options.set("scripting", true);
                            break;
                        }
                    }
                    catch (IOException ex) {}
                }
            }
        }
        return new Context(options, errors, wout, werr, Thread.currentThread().getContextClassLoader());
    }
    
    private static String[] preprocessArgs(final String[] args) {
        if (args.length == 0) {
            return args;
        }
        final List<String> processedArgs = new ArrayList<String>();
        processedArgs.addAll(Arrays.asList(args));
        if (args[0].startsWith("-") && !System.getProperty("os.name", "generic").startsWith("Mac OS X")) {
            processedArgs.addAll(0, ScriptingFunctions.tokenizeString(processedArgs.remove(0)));
        }
        int shebangFilePos = -1;
        int i = 0;
        while (i < processedArgs.size()) {
            final String a = processedArgs.get(i);
            if (!a.startsWith("-")) {
                final Path p = Paths.get(a, new String[0]);
                String l = "";
                try (final BufferedReader r = Files.newBufferedReader(p)) {
                    l = r.readLine();
                }
                catch (IOException ex) {}
                if (l.startsWith("#!")) {
                    shebangFilePos = i;
                    break;
                }
                break;
            }
            else {
                ++i;
            }
        }
        if (shebangFilePos != -1) {
            processedArgs.add(shebangFilePos + 1, "--");
        }
        return processedArgs.toArray(new String[0]);
    }
    
    private static int compileScripts(final Context context, final Global global, final List<String> files) throws IOException {
        final Global oldGlobal = Context.getGlobal();
        final boolean globalChanged = oldGlobal != global;
        final ScriptEnvironment env = context.getEnv();
        try {
            if (globalChanged) {
                Context.setGlobal(global);
            }
            final ErrorManager errors = context.getErrorManager();
            for (final String fileName : files) {
                final FunctionNode functionNode = new Parser(env, Source.sourceFor(fileName, new File(fileName)), errors, env._strict, 0, context.getLogger(Parser.class)).parse();
                if (errors.getNumberOfErrors() != 0) {
                    return 101;
                }
                Compiler.forNoInstallerCompilation(context, functionNode.getSource(), env._strict | functionNode.isStrict()).compile(functionNode, Compiler.CompilationPhases.COMPILE_ALL_NO_INSTALL);
                if (env._print_ast) {
                    context.getErr().println(new ASTWriter(functionNode));
                }
                if (env._print_parse) {
                    context.getErr().println(new PrintVisitor(functionNode));
                }
                if (errors.getNumberOfErrors() != 0) {
                    return 101;
                }
            }
        }
        finally {
            env.getOut().flush();
            env.getErr().flush();
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
        return 0;
    }
    
    private int runScripts(final Context context, final Global global, final List<String> files) throws IOException {
        final Global oldGlobal = Context.getGlobal();
        final boolean globalChanged = oldGlobal != global;
        try {
            if (globalChanged) {
                Context.setGlobal(global);
            }
            final ErrorManager errors = context.getErrorManager();
            for (final String fileName : files) {
                if ("-".equals(fileName)) {
                    final int res = readEvalPrint(context, global);
                    if (res != 0) {
                        return res;
                    }
                    continue;
                }
                else {
                    final File file = new File(fileName);
                    final ScriptFunction script = context.compileScript(Source.sourceFor(fileName, file), global);
                    if (script == null || errors.getNumberOfErrors() != 0) {
                        return 101;
                    }
                    try {
                        this.apply(script, global);
                    }
                    catch (NashornException e) {
                        errors.error(e.toString());
                        if (context.getEnv()._dump_on_error) {
                            e.printStackTrace(context.getErr());
                        }
                        return 102;
                    }
                }
            }
        }
        finally {
            context.getOut().flush();
            context.getErr().flush();
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
        return 0;
    }
    
    private static int runFXScripts(final Context context, final Global global, final List<String> files) throws IOException {
        final Global oldGlobal = Context.getGlobal();
        final boolean globalChanged = oldGlobal != global;
        try {
            if (globalChanged) {
                Context.setGlobal(global);
            }
            global.addOwnProperty("$GLOBAL", 2, global);
            global.addOwnProperty("$SCRIPTS", 2, files);
            context.load(global, "fx:bootstrap.js");
        }
        catch (NashornException e) {
            context.getErrorManager().error(e.toString());
            if (context.getEnv()._dump_on_error) {
                e.printStackTrace(context.getErr());
            }
            return 102;
        }
        finally {
            context.getOut().flush();
            context.getErr().flush();
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
        return 0;
    }
    
    protected Object apply(final ScriptFunction target, final Object self) {
        return ScriptRuntime.apply(target, self, new Object[0]);
    }
    
    private static int readEvalPrint(final Context context, final Global global) {
        final String prompt = Shell.bundle.getString("shell.prompt");
        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        final PrintWriter err = context.getErr();
        final Global oldGlobal = Context.getGlobal();
        final boolean globalChanged = oldGlobal != global;
        final ScriptEnvironment env = context.getEnv();
        try {
            if (globalChanged) {
                Context.setGlobal(global);
            }
            global.addShellBuiltins();
            while (true) {
                err.print(prompt);
                err.flush();
                String source = "";
                try {
                    source = in.readLine();
                }
                catch (IOException ioe) {
                    err.println(ioe.toString());
                }
                if (source == null) {
                    break;
                }
                if (source.isEmpty()) {
                    continue;
                }
                try {
                    final Object res = context.eval(global, source, global, "<shell>");
                    if (res == ScriptRuntime.UNDEFINED) {
                        continue;
                    }
                    err.println(JSType.toString(res));
                }
                catch (Exception e) {
                    err.println(e);
                    if (!env._dump_on_error) {
                        continue;
                    }
                    e.printStackTrace(err);
                }
            }
        }
        finally {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
        return 0;
    }
    
    static {
        bundle = ResourceBundle.getBundle("jdk.nashorn.tools.resources.Shell", Locale.getDefault());
    }
}
