// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.runtime.ECMAErrors;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.ScriptEnvironment;

public final class DumpBytecode
{
    public static void dumpBytecode(final ScriptEnvironment env, final DebugLogger logger, final byte[] bytecode, final String className) {
        File dir = null;
        try {
            if (env._print_code) {
                final StringBuilder sb = new StringBuilder();
                sb.append("class: " + className).append('\n').append(ClassEmitter.disassemble(bytecode)).append("=====");
                if (env._print_code_dir != null) {
                    String name = className;
                    final int dollar = name.lastIndexOf(36);
                    if (dollar != -1) {
                        name = name.substring(dollar + 1);
                    }
                    dir = new File(env._print_code_dir);
                    if (!dir.exists() && !dir.mkdirs()) {
                        throw new IOException(dir.toString());
                    }
                    int uniqueId = 0;
                    File file;
                    do {
                        final String fileName = name + ((uniqueId == 0) ? "" : ("_" + uniqueId)) + ".bytecode";
                        file = new File(env._print_code_dir, fileName);
                        ++uniqueId;
                    } while (file.exists());
                    try (final PrintWriter pw = new PrintWriter(new FileOutputStream(file))) {
                        pw.print(sb.toString());
                        pw.flush();
                    }
                }
                else {
                    env.getErr().println(sb);
                }
            }
            if (env._dest_dir != null) {
                final String fileName2 = className.replace('.', File.separatorChar) + ".class";
                final int index = fileName2.lastIndexOf(File.separatorChar);
                if (index != -1) {
                    dir = new File(env._dest_dir, fileName2.substring(0, index));
                }
                else {
                    dir = new File(env._dest_dir);
                }
                if (!dir.exists() && !dir.mkdirs()) {
                    throw new IOException(dir.toString());
                }
                final File file2 = new File(env._dest_dir, fileName2);
                try (final FileOutputStream fos = new FileOutputStream(file2)) {
                    fos.write(bytecode);
                }
                logger.info("Wrote class to '" + file2.getAbsolutePath() + '\'');
            }
        }
        catch (IOException e) {
            logger.warning("Skipping class dump for ", className, ": ", ECMAErrors.getMessage("io.error.cant.write", dir.toString()));
        }
    }
}
