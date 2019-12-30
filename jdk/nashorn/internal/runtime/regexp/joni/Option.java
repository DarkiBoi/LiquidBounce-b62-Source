// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

public class Option
{
    public static final int NONE = 0;
    public static final int IGNORECASE = 1;
    public static final int EXTEND = 2;
    public static final int MULTILINE = 4;
    public static final int SINGLELINE = 8;
    public static final int FIND_LONGEST = 16;
    public static final int FIND_NOT_EMPTY = 32;
    public static final int NEGATE_SINGLELINE = 64;
    public static final int DONT_CAPTURE_GROUP = 128;
    public static final int CAPTURE_GROUP = 256;
    public static final int NOTBOL = 512;
    public static final int NOTEOL = 1024;
    public static final int POSIX_REGION = 2048;
    public static final int MAXBIT = 4096;
    public static final int DEFAULT = 0;
    
    public static String toString(final int option) {
        String options = "";
        if (isIgnoreCase(option)) {
            options += "IGNORECASE ";
        }
        if (isExtend(option)) {
            options += "EXTEND ";
        }
        if (isMultiline(option)) {
            options += "MULTILINE ";
        }
        if (isSingleline(option)) {
            options += "SINGLELINE ";
        }
        if (isFindLongest(option)) {
            options += "FIND_LONGEST ";
        }
        if (isFindNotEmpty(option)) {
            options += "FIND_NOT_EMPTY  ";
        }
        if (isNegateSingleline(option)) {
            options += "NEGATE_SINGLELINE ";
        }
        if (isDontCaptureGroup(option)) {
            options += "DONT_CAPTURE_GROUP ";
        }
        if (isCaptureGroup(option)) {
            options += "CAPTURE_GROUP ";
        }
        if (isNotBol(option)) {
            options += "NOTBOL ";
        }
        if (isNotEol(option)) {
            options += "NOTEOL ";
        }
        if (isPosixRegion(option)) {
            options += "POSIX_REGION ";
        }
        return options;
    }
    
    public static boolean isIgnoreCase(final int option) {
        return (option & 0x1) != 0x0;
    }
    
    public static boolean isExtend(final int option) {
        return (option & 0x2) != 0x0;
    }
    
    public static boolean isSingleline(final int option) {
        return (option & 0x8) != 0x0;
    }
    
    public static boolean isMultiline(final int option) {
        return (option & 0x4) != 0x0;
    }
    
    public static boolean isFindLongest(final int option) {
        return (option & 0x10) != 0x0;
    }
    
    public static boolean isFindNotEmpty(final int option) {
        return (option & 0x20) != 0x0;
    }
    
    public static boolean isFindCondition(final int option) {
        return (option & 0x30) != 0x0;
    }
    
    public static boolean isNegateSingleline(final int option) {
        return (option & 0x40) != 0x0;
    }
    
    public static boolean isDontCaptureGroup(final int option) {
        return (option & 0x80) != 0x0;
    }
    
    public static boolean isCaptureGroup(final int option) {
        return (option & 0x100) != 0x0;
    }
    
    public static boolean isNotBol(final int option) {
        return (option & 0x200) != 0x0;
    }
    
    public static boolean isNotEol(final int option) {
        return (option & 0x400) != 0x0;
    }
    
    public static boolean isPosixRegion(final int option) {
        return (option & 0x800) != 0x0;
    }
    
    public static boolean isDynamic(final int option) {
        return false;
    }
}
