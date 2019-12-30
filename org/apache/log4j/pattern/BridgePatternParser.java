// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;

public final class BridgePatternParser extends PatternParser
{
    public BridgePatternParser(final String conversionPattern) {
        super(conversionPattern);
    }
    
    public PatternConverter parse() {
        return new BridgePatternConverter(this.pattern);
    }
}
