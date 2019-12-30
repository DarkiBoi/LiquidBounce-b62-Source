// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.api.scripting;

import jdk.nashorn.internal.runtime.ScriptObject;
import java.util.List;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.runtime.ECMAErrors;
import java.util.ArrayList;
import jdk.Exported;

@Exported
public abstract class NashornException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    private String fileName;
    private int line;
    private boolean lineAndFileNameUnknown;
    private int column;
    private Object ecmaError;
    
    protected NashornException(final String msg, final String fileName, final int line, final int column) {
        this(msg, null, fileName, line, column);
    }
    
    protected NashornException(final String msg, final Throwable cause, final String fileName, final int line, final int column) {
        super(msg, (cause == null) ? null : cause);
        this.fileName = fileName;
        this.line = line;
        this.column = column;
    }
    
    protected NashornException(final String msg, final Throwable cause) {
        super(msg, (cause == null) ? null : cause);
        this.column = -1;
        this.lineAndFileNameUnknown = true;
    }
    
    public final String getFileName() {
        this.ensureLineAndFileName();
        return this.fileName;
    }
    
    public final void setFileName(final String fileName) {
        this.fileName = fileName;
        this.lineAndFileNameUnknown = false;
    }
    
    public final int getLineNumber() {
        this.ensureLineAndFileName();
        return this.line;
    }
    
    public final void setLineNumber(final int line) {
        this.lineAndFileNameUnknown = false;
        this.line = line;
    }
    
    public final int getColumnNumber() {
        return this.column;
    }
    
    public final void setColumnNumber(final int column) {
        this.column = column;
    }
    
    public static StackTraceElement[] getScriptFrames(final Throwable exception) {
        final StackTraceElement[] frames = exception.getStackTrace();
        final List<StackTraceElement> filtered = new ArrayList<StackTraceElement>();
        for (final StackTraceElement st : frames) {
            if (ECMAErrors.isScriptFrame(st)) {
                final String className = "<" + st.getFileName() + ">";
                String methodName = st.getMethodName();
                if (methodName.equals(CompilerConstants.PROGRAM.symbolName())) {
                    methodName = "<program>";
                }
                else {
                    methodName = stripMethodName(methodName);
                }
                filtered.add(new StackTraceElement(className, methodName, st.getFileName(), st.getLineNumber()));
            }
        }
        return filtered.toArray(new StackTraceElement[filtered.size()]);
    }
    
    private static String stripMethodName(final String methodName) {
        String name = methodName;
        final int nestedSeparator = name.lastIndexOf(CompilerConstants.NESTED_FUNCTION_SEPARATOR.symbolName());
        if (nestedSeparator >= 0) {
            name = name.substring(nestedSeparator + 1);
        }
        final int idSeparator = name.indexOf(CompilerConstants.ID_FUNCTION_SEPARATOR.symbolName());
        if (idSeparator >= 0) {
            name = name.substring(0, idSeparator);
        }
        return name.contains(CompilerConstants.ANON_FUNCTION_PREFIX.symbolName()) ? "<anonymous>" : name;
    }
    
    public static String getScriptStackString(final Throwable exception) {
        final StringBuilder buf = new StringBuilder();
        final StackTraceElement[] scriptFrames;
        final StackTraceElement[] frames = scriptFrames = getScriptFrames(exception);
        for (final StackTraceElement st : scriptFrames) {
            buf.append("\tat ");
            buf.append(st.getMethodName());
            buf.append(" (");
            buf.append(st.getFileName());
            buf.append(':');
            buf.append(st.getLineNumber());
            buf.append(")\n");
        }
        final int len = buf.length();
        if (len > 0) {
            assert buf.charAt(len - 1) == '\n';
            buf.deleteCharAt(len - 1);
        }
        return buf.toString();
    }
    
    protected Object getThrown() {
        return null;
    }
    
    protected NashornException initEcmaError(final ScriptObject global) {
        if (this.ecmaError != null) {
            return this;
        }
        final Object thrown = this.getThrown();
        if (thrown instanceof ScriptObject) {
            this.setEcmaError(ScriptObjectMirror.wrap(thrown, global));
        }
        else {
            this.setEcmaError(thrown);
        }
        return this;
    }
    
    public Object getEcmaError() {
        return this.ecmaError;
    }
    
    public void setEcmaError(final Object ecmaError) {
        this.ecmaError = ecmaError;
    }
    
    private void ensureLineAndFileName() {
        if (this.lineAndFileNameUnknown) {
            for (final StackTraceElement ste : this.getStackTrace()) {
                if (ECMAErrors.isScriptFrame(ste)) {
                    this.fileName = ste.getFileName();
                    this.line = ste.getLineNumber();
                    return;
                }
            }
            this.lineAndFileNameUnknown = false;
        }
    }
}
