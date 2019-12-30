// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp;

import java.util.regex.Matcher;
import jdk.nashorn.internal.runtime.ParserException;
import java.util.regex.PatternSyntaxException;
import java.util.regex.Pattern;

public class JdkRegExp extends RegExp
{
    private Pattern pattern;
    
    public JdkRegExp(final String source, final String flags) throws ParserException {
        super(source, flags);
        int intFlags = 0;
        if (this.isIgnoreCase()) {
            intFlags |= 0x42;
        }
        while (true) {
            if (this.isMultiline()) {
                intFlags |= 0x8;
                try {
                    RegExpScanner parsed;
                    try {
                        parsed = RegExpScanner.scan(source);
                    }
                    catch (PatternSyntaxException e) {
                        Pattern.compile(source, intFlags);
                        throw e;
                    }
                    if (parsed != null) {
                        this.pattern = Pattern.compile(parsed.getJavaPattern(), intFlags);
                        this.groupsInNegativeLookahead = parsed.getGroupsInNegativeLookahead();
                    }
                }
                catch (PatternSyntaxException e2) {
                    RegExp.throwParserException("syntax", e2.getMessage());
                }
                return;
            }
            continue;
        }
    }
    
    @Override
    public RegExpMatcher match(final String str) {
        if (this.pattern == null) {
            return null;
        }
        return new DefaultMatcher(str);
    }
    
    class DefaultMatcher implements RegExpMatcher
    {
        final String input;
        final Matcher defaultMatcher;
        
        DefaultMatcher(final String input) {
            this.input = input;
            this.defaultMatcher = JdkRegExp.this.pattern.matcher(input);
        }
        
        @Override
        public boolean search(final int start) {
            return this.defaultMatcher.find(start);
        }
        
        @Override
        public String getInput() {
            return this.input;
        }
        
        @Override
        public int start() {
            return this.defaultMatcher.start();
        }
        
        @Override
        public int start(final int group) {
            return this.defaultMatcher.start(group);
        }
        
        @Override
        public int end() {
            return this.defaultMatcher.end();
        }
        
        @Override
        public int end(final int group) {
            return this.defaultMatcher.end(group);
        }
        
        @Override
        public String group() {
            return this.defaultMatcher.group();
        }
        
        @Override
        public String group(final int group) {
            return this.defaultMatcher.group(group);
        }
        
        @Override
        public int groupCount() {
            return this.defaultMatcher.groupCount();
        }
    }
}
