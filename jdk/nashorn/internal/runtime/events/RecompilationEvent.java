// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.events;

import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import jdk.nashorn.internal.runtime.Context;
import java.util.logging.Level;
import jdk.nashorn.internal.runtime.RewriteException;

public final class RecompilationEvent extends RuntimeEvent<RewriteException>
{
    private final Object returnValue;
    
    public RecompilationEvent(final Level level, final RewriteException rewriteException, final Object returnValue) {
        super(level, rewriteException);
        assert Context.getContext().getLogger(RecompilableScriptFunctionData.class).isEnabled() : "Unit test/instrumentation purpose only: RecompilationEvent instances should not be created without '--log=recompile', or we will leak memory in the general case";
        this.returnValue = returnValue;
    }
    
    public Object getReturnValue() {
        return this.returnValue;
    }
}
