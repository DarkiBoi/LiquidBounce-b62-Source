// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import java.util.List;
import java.util.Arrays;
import kotlin.SinceKotlin;
import kotlin.UninitializedPropertyAccessException;
import kotlin.KotlinNullPointerException;

public class Intrinsics
{
    private Intrinsics() {
    }
    
    public static String stringPlus(final String self, final Object other) {
        return self + other;
    }
    
    public static void checkNotNull(final Object object) {
        if (object == null) {
            throwNpe();
        }
    }
    
    public static void checkNotNull(final Object object, final String message) {
        if (object == null) {
            throwNpe(message);
        }
    }
    
    public static void throwNpe() {
        throw sanitizeStackTrace(new KotlinNullPointerException());
    }
    
    public static void throwNpe(final String message) {
        throw sanitizeStackTrace(new KotlinNullPointerException(message));
    }
    
    public static void throwUninitializedProperty(final String message) {
        throw sanitizeStackTrace(new UninitializedPropertyAccessException(message));
    }
    
    public static void throwUninitializedPropertyAccessException(final String propertyName) {
        throwUninitializedProperty("lateinit property " + propertyName + " has not been initialized");
    }
    
    public static void throwAssert() {
        throw sanitizeStackTrace(new AssertionError());
    }
    
    public static void throwAssert(final String message) {
        throw sanitizeStackTrace(new AssertionError((Object)message));
    }
    
    public static void throwIllegalArgument() {
        throw sanitizeStackTrace(new IllegalArgumentException());
    }
    
    public static void throwIllegalArgument(final String message) {
        throw sanitizeStackTrace(new IllegalArgumentException(message));
    }
    
    public static void throwIllegalState() {
        throw sanitizeStackTrace(new IllegalStateException());
    }
    
    public static void throwIllegalState(final String message) {
        throw sanitizeStackTrace(new IllegalStateException(message));
    }
    
    public static void checkExpressionValueIsNotNull(final Object value, final String expression) {
        if (value == null) {
            throw sanitizeStackTrace(new IllegalStateException(expression + " must not be null"));
        }
    }
    
    public static void checkNotNullExpressionValue(final Object value, final String message) {
        if (value == null) {
            throw sanitizeStackTrace(new IllegalStateException(message));
        }
    }
    
    public static void checkReturnedValueIsNotNull(final Object value, final String className, final String methodName) {
        if (value == null) {
            throw sanitizeStackTrace(new IllegalStateException("Method specified as non-null returned null: " + className + "." + methodName));
        }
    }
    
    public static void checkReturnedValueIsNotNull(final Object value, final String message) {
        if (value == null) {
            throw sanitizeStackTrace(new IllegalStateException(message));
        }
    }
    
    public static void checkFieldIsNotNull(final Object value, final String className, final String fieldName) {
        if (value == null) {
            throw sanitizeStackTrace(new IllegalStateException("Field specified as non-null is null: " + className + "." + fieldName));
        }
    }
    
    public static void checkFieldIsNotNull(final Object value, final String message) {
        if (value == null) {
            throw sanitizeStackTrace(new IllegalStateException(message));
        }
    }
    
    public static void checkParameterIsNotNull(final Object value, final String paramName) {
        if (value == null) {
            throwParameterIsNullException(paramName);
        }
    }
    
    public static void checkNotNullParameter(final Object value, final String message) {
        if (value == null) {
            throw sanitizeStackTrace(new IllegalArgumentException(message));
        }
    }
    
    private static void throwParameterIsNullException(final String paramName) {
        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        final StackTraceElement caller = stackTraceElements[3];
        final String className = caller.getClassName();
        final String methodName = caller.getMethodName();
        final IllegalArgumentException exception = new IllegalArgumentException("Parameter specified as non-null is null: method " + className + "." + methodName + ", parameter " + paramName);
        throw sanitizeStackTrace(exception);
    }
    
    public static int compare(final long thisVal, final long anotherVal) {
        return (thisVal < anotherVal) ? -1 : ((thisVal == anotherVal) ? 0 : 1);
    }
    
    public static int compare(final int thisVal, final int anotherVal) {
        return (thisVal < anotherVal) ? -1 : ((thisVal == anotherVal) ? 0 : 1);
    }
    
    public static boolean areEqual(final Object first, final Object second) {
        return (first == null) ? (second == null) : first.equals(second);
    }
    
    @SinceKotlin(version = "1.1")
    public static boolean areEqual(final Double first, final Double second) {
        return (first == null) ? (second == null) : (second != null && first == (double)second);
    }
    
    @SinceKotlin(version = "1.1")
    public static boolean areEqual(final Double first, final double second) {
        return first != null && first == second;
    }
    
    @SinceKotlin(version = "1.1")
    public static boolean areEqual(final double first, final Double second) {
        return second != null && first == second;
    }
    
    @SinceKotlin(version = "1.1")
    public static boolean areEqual(final Float first, final Float second) {
        return (first == null) ? (second == null) : (second != null && first == (float)second);
    }
    
    @SinceKotlin(version = "1.1")
    public static boolean areEqual(final Float first, final float second) {
        return first != null && first == second;
    }
    
    @SinceKotlin(version = "1.1")
    public static boolean areEqual(final float first, final Float second) {
        return second != null && first == second;
    }
    
    public static void throwUndefinedForReified() {
        throwUndefinedForReified("This function has a reified type parameter and thus can only be inlined at compilation time, not called directly.");
    }
    
    public static void throwUndefinedForReified(final String message) {
        throw new UnsupportedOperationException(message);
    }
    
    public static void reifiedOperationMarker(final int id, final String typeParameterIdentifier) {
        throwUndefinedForReified();
    }
    
    public static void reifiedOperationMarker(final int id, final String typeParameterIdentifier, final String message) {
        throwUndefinedForReified(message);
    }
    
    public static void needClassReification() {
        throwUndefinedForReified();
    }
    
    public static void needClassReification(final String message) {
        throwUndefinedForReified(message);
    }
    
    public static void checkHasClass(final String internalName) throws ClassNotFoundException {
        final String fqName = internalName.replace('/', '.');
        try {
            Class.forName(fqName);
        }
        catch (ClassNotFoundException e) {
            throw sanitizeStackTrace(new ClassNotFoundException("Class " + fqName + " is not found. Please update the Kotlin runtime to the latest version", e));
        }
    }
    
    public static void checkHasClass(final String internalName, final String requiredVersion) throws ClassNotFoundException {
        final String fqName = internalName.replace('/', '.');
        try {
            Class.forName(fqName);
        }
        catch (ClassNotFoundException e) {
            throw sanitizeStackTrace(new ClassNotFoundException("Class " + fqName + " is not found: this code requires the Kotlin runtime of version at least " + requiredVersion, e));
        }
    }
    
    private static <T extends Throwable> T sanitizeStackTrace(final T throwable) {
        return sanitizeStackTrace(throwable, Intrinsics.class.getName());
    }
    
    static <T extends Throwable> T sanitizeStackTrace(final T throwable, final String classNameToDrop) {
        final StackTraceElement[] stackTrace = throwable.getStackTrace();
        final int size = stackTrace.length;
        int lastIntrinsic = -1;
        for (int i = 0; i < size; ++i) {
            if (classNameToDrop.equals(stackTrace[i].getClassName())) {
                lastIntrinsic = i;
            }
        }
        final List<StackTraceElement> list = Arrays.asList(stackTrace).subList(lastIntrinsic + 1, size);
        throwable.setStackTrace(list.toArray(new StackTraceElement[list.size()]));
        return throwable;
    }
}
