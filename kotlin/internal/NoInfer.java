// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.internal;

import kotlin.Metadata;
import java.lang.annotation.RetentionPolicy;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;
import kotlin.annotation.AnnotationTarget;
import kotlin.annotation.Target;
import java.lang.annotation.Annotation;

@Target(allowedTargets = { AnnotationTarget.TYPE })
@Retention(AnnotationRetention.BINARY)
@java.lang.annotation.Retention(RetentionPolicy.CLASS)
@java.lang.annotation.Target({})
@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0081\u0002\u0018\u00002\u00020\u0001B\u0000¨\u0006\u0002" }, d2 = { "Lkotlin/internal/NoInfer;", "", "kotlin-stdlib" })
public @interface NoInfer {
}
