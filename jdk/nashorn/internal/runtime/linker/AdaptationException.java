// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

final class AdaptationException extends Exception
{
    private final AdaptationResult adaptationResult;
    
    AdaptationException(final AdaptationResult.Outcome outcome, final String classList) {
        super(null, null, false, false);
        this.adaptationResult = new AdaptationResult(outcome, new String[] { classList });
    }
    
    AdaptationResult getAdaptationResult() {
        return this.adaptationResult;
    }
}
