// 
// Decompiled by Procyon v0.5.36
// 

package kotlin;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Documented;
import kotlin.annotation.MustBeDocumented;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;
import kotlin.annotation.AnnotationTarget;
import kotlin.annotation.Target;
import java.lang.annotation.Annotation;

@Target(allowedTargets = { AnnotationTarget.ANNOTATION_CLASS })
@Retention(AnnotationRetention.BINARY)
@MustBeDocumented
@Documented
@java.lang.annotation.Retention(RetentionPolicy.CLASS)
@java.lang.annotation.Target({ ElementType.ANNOTATION_TYPE })
@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0087\u0002\u0018\u00002\u00020\u0001B\u0000¨\u0006\u0002" }, d2 = { "Lkotlin/DslMarker;", "", "kotlin-stdlib" })
@SinceKotlin(version = "1.1")
public @interface DslMarker {
}
