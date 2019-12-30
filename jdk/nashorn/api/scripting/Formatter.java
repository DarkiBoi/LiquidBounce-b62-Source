// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.api.scripting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class Formatter
{
    private static final String formatSpecifier = "%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])";
    private static final Pattern FS_PATTERN;
    
    private Formatter() {
    }
    
    static String format(final String format, final Object[] args) {
        final Matcher m = Formatter.FS_PATTERN.matcher(format);
        int positionalParameter = 1;
        while (m.find()) {
            int index = index(m.group(1));
            final boolean previous = isPreviousArgument(m.group(2));
            final char conversion = m.group(6).charAt(0);
            if (index >= 0 && !previous && conversion != 'n') {
                if (conversion == '%') {
                    continue;
                }
                if (index == 0) {
                    index = positionalParameter++;
                }
                if (index > args.length) {
                    continue;
                }
                final Object arg = args[index - 1];
                if (m.group(5) != null) {
                    if (!(arg instanceof Double)) {
                        continue;
                    }
                    args[index - 1] = ((Double)arg).longValue();
                }
                else {
                    switch (conversion) {
                        case 'X':
                        case 'd':
                        case 'o':
                        case 'x': {
                            if (arg instanceof Double) {
                                args[index - 1] = ((Double)arg).longValue();
                                continue;
                            }
                            if (arg instanceof String && ((String)arg).length() > 0) {
                                args[index - 1] = ((String)arg).charAt(0);
                                continue;
                            }
                            continue;
                        }
                        case 'A':
                        case 'E':
                        case 'G':
                        case 'a':
                        case 'e':
                        case 'f':
                        case 'g': {
                            if (arg instanceof Integer) {
                                args[index - 1] = arg;
                                continue;
                            }
                            continue;
                        }
                        case 'c': {
                            if (arg instanceof Double) {
                                args[index - 1] = ((Double)arg).intValue();
                                continue;
                            }
                            if (arg instanceof String && ((String)arg).length() > 0) {
                                args[index - 1] = ((String)arg).charAt(0);
                                continue;
                            }
                            continue;
                        }
                    }
                }
            }
        }
        return String.format(format, args);
    }
    
    private static int index(final String s) {
        int index = -1;
        if (s != null) {
            try {
                index = Integer.parseInt(s.substring(0, s.length() - 1));
            }
            catch (NumberFormatException ex) {}
        }
        else {
            index = 0;
        }
        return index;
    }
    
    private static boolean isPreviousArgument(final String s) {
        return s != null && s.indexOf(60) >= 0;
    }
    
    static {
        FS_PATTERN = Pattern.compile("%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])");
    }
}
