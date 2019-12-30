// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.CompileUnit;
import java.io.Serializable;
import java.util.List;

public interface Splittable
{
    List<SplitRange> getSplitRanges();
    
    public static final class SplitRange implements CompileUnitHolder, Serializable
    {
        private static final long serialVersionUID = 1L;
        private final CompileUnit compileUnit;
        private final int low;
        private final int high;
        
        public SplitRange(final CompileUnit compileUnit, final int low, final int high) {
            this.compileUnit = compileUnit;
            this.low = low;
            this.high = high;
        }
        
        public int getHigh() {
            return this.high;
        }
        
        public int getLow() {
            return this.low;
        }
        
        @Override
        public CompileUnit getCompileUnit() {
            return this.compileUnit;
        }
    }
}
