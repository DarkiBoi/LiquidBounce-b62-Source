// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import javax.script.ScriptException;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.api.scripting.NashornException;

public final class ECMAException extends NashornException
{
    public static final CompilerConstants.Call CREATE;
    public static final CompilerConstants.FieldAccess THROWN;
    private static final String EXCEPTION_PROPERTY = "nashornException";
    public final Object thrown;
    
    private ECMAException(final Object thrown, final String fileName, final int line, final int column) {
        super(ScriptRuntime.safeToString(thrown), asThrowable(thrown), fileName, line, column);
        this.thrown = thrown;
        this.setExceptionToThrown();
    }
    
    public ECMAException(final Object thrown, final Throwable cause) {
        super(ScriptRuntime.safeToString(thrown), cause);
        this.thrown = thrown;
        this.setExceptionToThrown();
    }
    
    public static ECMAException create(final Object thrown, final String fileName, final int line, final int column) {
        if (thrown instanceof ScriptObject) {
            final Object exception = getException((ScriptObject)thrown);
            if (exception instanceof ECMAException) {
                final ECMAException ee = (ECMAException)exception;
                if (ee.getThrown() == thrown) {
                    ee.setFileName(fileName);
                    ee.setLineNumber(line);
                    ee.setColumnNumber(column);
                    return ee;
                }
            }
        }
        return new ECMAException(thrown, fileName, line, column);
    }
    
    public Object getThrown() {
        return this.thrown;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final String fileName = this.getFileName();
        final int line = this.getLineNumber();
        final int column = this.getColumnNumber();
        if (fileName != null) {
            sb.append(fileName);
            if (line >= 0) {
                sb.append(':');
                sb.append(line);
            }
            if (column >= 0) {
                sb.append(':');
                sb.append(column);
            }
            sb.append(' ');
        }
        else {
            sb.append("ECMAScript Exception: ");
        }
        sb.append(this.getMessage());
        return sb.toString();
    }
    
    public static Object getException(final ScriptObject errObj) {
        if (errObj.hasOwnProperty("nashornException")) {
            return errObj.get("nashornException");
        }
        return null;
    }
    
    public static Object printStackTrace(final ScriptObject errObj) {
        final Object exception = getException(errObj);
        if (exception instanceof Throwable) {
            ((Throwable)exception).printStackTrace(Context.getCurrentErr());
        }
        else {
            Context.err("<stack trace not available>");
        }
        return ScriptRuntime.UNDEFINED;
    }
    
    public static Object getLineNumber(final ScriptObject errObj) {
        final Object e = getException(errObj);
        if (e instanceof NashornException) {
            return ((NashornException)e).getLineNumber();
        }
        if (e instanceof ScriptException) {
            return ((ScriptException)e).getLineNumber();
        }
        return ScriptRuntime.UNDEFINED;
    }
    
    public static Object getColumnNumber(final ScriptObject errObj) {
        final Object e = getException(errObj);
        if (e instanceof NashornException) {
            return ((NashornException)e).getColumnNumber();
        }
        if (e instanceof ScriptException) {
            return ((ScriptException)e).getColumnNumber();
        }
        return ScriptRuntime.UNDEFINED;
    }
    
    public static Object getFileName(final ScriptObject errObj) {
        final Object e = getException(errObj);
        if (e instanceof NashornException) {
            return ((NashornException)e).getFileName();
        }
        if (e instanceof ScriptException) {
            return ((ScriptException)e).getFileName();
        }
        return ScriptRuntime.UNDEFINED;
    }
    
    public static String safeToString(final ScriptObject errObj) {
        Object name = ScriptRuntime.UNDEFINED;
        try {
            name = errObj.get("name");
        }
        catch (Exception ex) {}
        if (name == ScriptRuntime.UNDEFINED) {
            name = "Error";
        }
        else {
            name = ScriptRuntime.safeToString(name);
        }
        Object msg = ScriptRuntime.UNDEFINED;
        try {
            msg = errObj.get("message");
        }
        catch (Exception ex2) {}
        if (msg == ScriptRuntime.UNDEFINED) {
            msg = "";
        }
        else {
            msg = ScriptRuntime.safeToString(msg);
        }
        if (((String)name).isEmpty()) {
            return (String)msg;
        }
        if (((String)msg).isEmpty()) {
            return (String)name;
        }
        return name + ": " + msg;
    }
    
    private static Throwable asThrowable(final Object obj) {
        return (obj instanceof Throwable) ? ((Throwable)obj) : null;
    }
    
    private void setExceptionToThrown() {
        if (this.thrown instanceof ScriptObject) {
            final ScriptObject sobj = (ScriptObject)this.thrown;
            if (!sobj.has("nashornException")) {
                sobj.addOwnProperty("nashornException", 2, this);
            }
            else {
                sobj.set("nashornException", this, 0);
            }
        }
    }
    
    static {
        CREATE = CompilerConstants.staticCallNoLookup(ECMAException.class, "create", ECMAException.class, Object.class, String.class, Integer.TYPE, Integer.TYPE);
        THROWN = CompilerConstants.virtualField(ECMAException.class, "thrown", Object.class);
    }
}
