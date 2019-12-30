// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.spi;

import java.io.Writer;
import org.apache.log4j.Layout;
import java.lang.reflect.InvocationTargetException;
import java.io.InterruptedIOException;
import org.apache.log4j.helpers.LogLog;
import java.lang.reflect.Method;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Serializable;

public class LocationInfo implements Serializable
{
    transient String lineNumber;
    transient String fileName;
    transient String className;
    transient String methodName;
    public String fullInfo;
    private static StringWriter sw;
    private static PrintWriter pw;
    private static Method getStackTraceMethod;
    private static Method getClassNameMethod;
    private static Method getMethodNameMethod;
    private static Method getFileNameMethod;
    private static Method getLineNumberMethod;
    public static final String NA = "?";
    static final long serialVersionUID = -1325822038990805636L;
    public static final LocationInfo NA_LOCATION_INFO;
    static boolean inVisualAge;
    
    public LocationInfo(final Throwable t, final String fqnOfCallingClass) {
        if (t == null || fqnOfCallingClass == null) {
            return;
        }
        if (LocationInfo.getLineNumberMethod != null) {
            try {
                final Object[] noArgs = null;
                final Object[] elements = (Object[])LocationInfo.getStackTraceMethod.invoke(t, noArgs);
                String prevClass = "?";
                for (int i = elements.length - 1; i >= 0; --i) {
                    final String thisClass = (String)LocationInfo.getClassNameMethod.invoke(elements[i], noArgs);
                    if (fqnOfCallingClass.equals(thisClass)) {
                        final int caller = i + 1;
                        if (caller < elements.length) {
                            this.className = prevClass;
                            this.methodName = (String)LocationInfo.getMethodNameMethod.invoke(elements[caller], noArgs);
                            this.fileName = (String)LocationInfo.getFileNameMethod.invoke(elements[caller], noArgs);
                            if (this.fileName == null) {
                                this.fileName = "?";
                            }
                            final int line = (int)LocationInfo.getLineNumberMethod.invoke(elements[caller], noArgs);
                            if (line < 0) {
                                this.lineNumber = "?";
                            }
                            else {
                                this.lineNumber = String.valueOf(line);
                            }
                            final StringBuffer buf = new StringBuffer();
                            buf.append(this.className);
                            buf.append(".");
                            buf.append(this.methodName);
                            buf.append("(");
                            buf.append(this.fileName);
                            buf.append(":");
                            buf.append(this.lineNumber);
                            buf.append(")");
                            this.fullInfo = buf.toString();
                        }
                        return;
                    }
                    prevClass = thisClass;
                }
                return;
            }
            catch (IllegalAccessException ex) {
                LogLog.debug("LocationInfo failed using JDK 1.4 methods", ex);
            }
            catch (InvocationTargetException ex2) {
                if (ex2.getTargetException() instanceof InterruptedException || ex2.getTargetException() instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                LogLog.debug("LocationInfo failed using JDK 1.4 methods", ex2);
            }
            catch (RuntimeException ex3) {
                LogLog.debug("LocationInfo failed using JDK 1.4 methods", ex3);
            }
        }
        final String s;
        synchronized (LocationInfo.sw) {
            t.printStackTrace(LocationInfo.pw);
            s = LocationInfo.sw.toString();
            LocationInfo.sw.getBuffer().setLength(0);
        }
        int ibegin = s.lastIndexOf(fqnOfCallingClass);
        if (ibegin == -1) {
            return;
        }
        if (ibegin + fqnOfCallingClass.length() < s.length() && s.charAt(ibegin + fqnOfCallingClass.length()) != '.') {
            final int i = s.lastIndexOf(fqnOfCallingClass + ".");
            if (i != -1) {
                ibegin = i;
            }
        }
        ibegin = s.indexOf(Layout.LINE_SEP, ibegin);
        if (ibegin == -1) {
            return;
        }
        ibegin += Layout.LINE_SEP_LEN;
        final int iend = s.indexOf(Layout.LINE_SEP, ibegin);
        if (iend == -1) {
            return;
        }
        if (!LocationInfo.inVisualAge) {
            ibegin = s.lastIndexOf("at ", iend);
            if (ibegin == -1) {
                return;
            }
            ibegin += 3;
        }
        this.fullInfo = s.substring(ibegin, iend);
    }
    
    private static final void appendFragment(final StringBuffer buf, final String fragment) {
        if (fragment == null) {
            buf.append("?");
        }
        else {
            buf.append(fragment);
        }
    }
    
    public LocationInfo(final String file, final String classname, final String method, final String line) {
        this.fileName = file;
        this.className = classname;
        this.methodName = method;
        this.lineNumber = line;
        final StringBuffer buf = new StringBuffer();
        appendFragment(buf, classname);
        buf.append(".");
        appendFragment(buf, method);
        buf.append("(");
        appendFragment(buf, file);
        buf.append(":");
        appendFragment(buf, line);
        buf.append(")");
        this.fullInfo = buf.toString();
    }
    
    public String getClassName() {
        if (this.fullInfo == null) {
            return "?";
        }
        if (this.className == null) {
            int iend = this.fullInfo.lastIndexOf(40);
            if (iend == -1) {
                this.className = "?";
            }
            else {
                iend = this.fullInfo.lastIndexOf(46, iend);
                int ibegin = 0;
                if (LocationInfo.inVisualAge) {
                    ibegin = this.fullInfo.lastIndexOf(32, iend) + 1;
                }
                if (iend == -1) {
                    this.className = "?";
                }
                else {
                    this.className = this.fullInfo.substring(ibegin, iend);
                }
            }
        }
        return this.className;
    }
    
    public String getFileName() {
        if (this.fullInfo == null) {
            return "?";
        }
        if (this.fileName == null) {
            final int iend = this.fullInfo.lastIndexOf(58);
            if (iend == -1) {
                this.fileName = "?";
            }
            else {
                final int ibegin = this.fullInfo.lastIndexOf(40, iend - 1);
                this.fileName = this.fullInfo.substring(ibegin + 1, iend);
            }
        }
        return this.fileName;
    }
    
    public String getLineNumber() {
        if (this.fullInfo == null) {
            return "?";
        }
        if (this.lineNumber == null) {
            final int iend = this.fullInfo.lastIndexOf(41);
            final int ibegin = this.fullInfo.lastIndexOf(58, iend - 1);
            if (ibegin == -1) {
                this.lineNumber = "?";
            }
            else {
                this.lineNumber = this.fullInfo.substring(ibegin + 1, iend);
            }
        }
        return this.lineNumber;
    }
    
    public String getMethodName() {
        if (this.fullInfo == null) {
            return "?";
        }
        if (this.methodName == null) {
            final int iend = this.fullInfo.lastIndexOf(40);
            final int ibegin = this.fullInfo.lastIndexOf(46, iend);
            if (ibegin == -1) {
                this.methodName = "?";
            }
            else {
                this.methodName = this.fullInfo.substring(ibegin + 1, iend);
            }
        }
        return this.methodName;
    }
    
    static {
        LocationInfo.sw = new StringWriter();
        LocationInfo.pw = new PrintWriter(LocationInfo.sw);
        NA_LOCATION_INFO = new LocationInfo("?", "?", "?", "?");
        LocationInfo.inVisualAge = false;
        try {
            LocationInfo.inVisualAge = (Class.forName("com.ibm.uvm.tools.DebugSupport") != null);
            LogLog.debug("Detected IBM VisualAge environment.");
        }
        catch (Throwable t) {}
        try {
            final Class[] noArgs = null;
            LocationInfo.getStackTraceMethod = Throwable.class.getMethod("getStackTrace", (Class[])noArgs);
            final Class stackTraceElementClass = Class.forName("java.lang.StackTraceElement");
            LocationInfo.getClassNameMethod = stackTraceElementClass.getMethod("getClassName", (Class[])noArgs);
            LocationInfo.getMethodNameMethod = stackTraceElementClass.getMethod("getMethodName", (Class[])noArgs);
            LocationInfo.getFileNameMethod = stackTraceElementClass.getMethod("getFileName", (Class[])noArgs);
            LocationInfo.getLineNumberMethod = stackTraceElementClass.getMethod("getLineNumber", (Class[])noArgs);
        }
        catch (ClassNotFoundException ex) {
            LogLog.debug("LocationInfo will use pre-JDK 1.4 methods to determine location.");
        }
        catch (NoSuchMethodException ex2) {
            LogLog.debug("LocationInfo will use pre-JDK 1.4 methods to determine location.");
        }
    }
}
