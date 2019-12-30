// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm;

import org.jetbrains.annotations.Nullable;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0003\b\u0016\u0018\u00002\u00020\u0001B\u0007\b\u0016¢\u0006\u0002\u0010\u0002B\u0011\b\u0016\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004¢\u0006\u0002\u0010\u0005B\u001b\b\u0016\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bB\u0011\b\u0016\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\t¨\u0006\n" }, d2 = { "Lkotlin/jvm/KotlinReflectionNotSupportedError;", "Ljava/lang/Error;", "()V", "message", "", "(Ljava/lang/String;)V", "cause", "", "(Ljava/lang/String;Ljava/lang/Throwable;)V", "(Ljava/lang/Throwable;)V", "kotlin-stdlib" })
public class KotlinReflectionNotSupportedError extends Error
{
    public KotlinReflectionNotSupportedError() {
        super("Kotlin reflection implementation is not found at runtime. Make sure you have kotlin-reflect.jar in the classpath");
    }
    
    public KotlinReflectionNotSupportedError(@Nullable final String message) {
        super(message);
    }
    
    public KotlinReflectionNotSupportedError(@Nullable final String message, @Nullable final Throwable cause) {
        super(message, cause);
    }
    
    public KotlinReflectionNotSupportedError(@Nullable final Throwable cause) {
        super(cause);
    }
}
