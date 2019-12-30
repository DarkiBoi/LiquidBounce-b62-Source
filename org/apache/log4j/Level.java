// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j;

import java.io.ObjectStreamException;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Level extends Priority implements Serializable
{
    public static final int TRACE_INT = 5000;
    public static final Level OFF;
    public static final Level FATAL;
    public static final Level ERROR;
    public static final Level WARN;
    public static final Level INFO;
    public static final Level DEBUG;
    public static final Level TRACE;
    public static final Level ALL;
    static final long serialVersionUID = 3491141966387921974L;
    
    protected Level(final int level, final String levelStr, final int syslogEquivalent) {
        super(level, levelStr, syslogEquivalent);
    }
    
    public static Level toLevel(final String sArg) {
        return toLevel(sArg, Level.DEBUG);
    }
    
    public static Level toLevel(final int val) {
        return toLevel(val, Level.DEBUG);
    }
    
    public static Level toLevel(final int val, final Level defaultLevel) {
        switch (val) {
            case Integer.MIN_VALUE: {
                return Level.ALL;
            }
            case 10000: {
                return Level.DEBUG;
            }
            case 20000: {
                return Level.INFO;
            }
            case 30000: {
                return Level.WARN;
            }
            case 40000: {
                return Level.ERROR;
            }
            case 50000: {
                return Level.FATAL;
            }
            case Integer.MAX_VALUE: {
                return Level.OFF;
            }
            case 5000: {
                return Level.TRACE;
            }
            default: {
                return defaultLevel;
            }
        }
    }
    
    public static Level toLevel(final String sArg, final Level defaultLevel) {
        if (sArg == null) {
            return defaultLevel;
        }
        final String s = sArg.toUpperCase();
        if (s.equals("ALL")) {
            return Level.ALL;
        }
        if (s.equals("DEBUG")) {
            return Level.DEBUG;
        }
        if (s.equals("INFO")) {
            return Level.INFO;
        }
        if (s.equals("WARN")) {
            return Level.WARN;
        }
        if (s.equals("ERROR")) {
            return Level.ERROR;
        }
        if (s.equals("FATAL")) {
            return Level.FATAL;
        }
        if (s.equals("OFF")) {
            return Level.OFF;
        }
        if (s.equals("TRACE")) {
            return Level.TRACE;
        }
        if (s.equals("\u0130NFO")) {
            return Level.INFO;
        }
        return defaultLevel;
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.level = s.readInt();
        this.syslogEquivalent = s.readInt();
        this.levelStr = s.readUTF();
        if (this.levelStr == null) {
            this.levelStr = "";
        }
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(this.level);
        s.writeInt(this.syslogEquivalent);
        s.writeUTF(this.levelStr);
    }
    
    private Object readResolve() throws ObjectStreamException {
        if (this.getClass() == Level.class) {
            return toLevel(this.level);
        }
        return this;
    }
    
    static {
        OFF = new Level(Integer.MAX_VALUE, "OFF", 0);
        FATAL = new Level(50000, "FATAL", 0);
        ERROR = new Level(40000, "ERROR", 3);
        WARN = new Level(30000, "WARN", 4);
        INFO = new Level(20000, "INFO", 6);
        DEBUG = new Level(10000, "DEBUG", 7);
        TRACE = new Level(5000, "TRACE", 7);
        ALL = new Level(Integer.MIN_VALUE, "ALL", 7);
    }
}
