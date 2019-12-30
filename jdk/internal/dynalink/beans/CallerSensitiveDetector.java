// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import sun.reflect.CallerSensitive;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;

public class CallerSensitiveDetector
{
    private static final DetectionStrategy DETECTION_STRATEGY;
    
    static boolean isCallerSensitive(final AccessibleObject ao) {
        return CallerSensitiveDetector.DETECTION_STRATEGY.isCallerSensitive(ao);
    }
    
    private static DetectionStrategy getDetectionStrategy() {
        try {
            return new PrivilegedDetectionStrategy();
        }
        catch (Throwable t) {
            return new UnprivilegedDetectionStrategy();
        }
    }
    
    static {
        DETECTION_STRATEGY = getDetectionStrategy();
    }
    
    private abstract static class DetectionStrategy
    {
        abstract boolean isCallerSensitive(final AccessibleObject p0);
    }
    
    private static class PrivilegedDetectionStrategy extends DetectionStrategy
    {
        private static final Class<? extends Annotation> CALLER_SENSITIVE_ANNOTATION_CLASS;
        
        @Override
        boolean isCallerSensitive(final AccessibleObject ao) {
            return ao.getAnnotation(PrivilegedDetectionStrategy.CALLER_SENSITIVE_ANNOTATION_CLASS) != null;
        }
        
        static {
            CALLER_SENSITIVE_ANNOTATION_CLASS = CallerSensitive.class;
        }
    }
    
    private static class UnprivilegedDetectionStrategy extends DetectionStrategy
    {
        private static final String CALLER_SENSITIVE_ANNOTATION_STRING = "@sun.reflect.CallerSensitive()";
        
        @Override
        boolean isCallerSensitive(final AccessibleObject o) {
            for (final Annotation a : o.getAnnotations()) {
                if (String.valueOf(a).equals("@sun.reflect.CallerSensitive()")) {
                    return true;
                }
            }
            return false;
        }
    }
}
