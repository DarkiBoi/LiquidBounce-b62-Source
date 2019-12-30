// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm;

import java.lang.annotation.Annotation;
import kotlin.DeprecationLevel;
import kotlin.ReplaceWith;
import kotlin.Deprecated;
import kotlin.jvm.internal.Reflection;
import org.jetbrains.annotations.Nullable;
import java.io.Serializable;
import kotlin.TypeCastException;
import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.reflect.KClass;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000,\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\u0010\u0011\n\u0002\b\u0002\u001a\u001f\u0010\u0018\u001a\u00020\u0019\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\r*\u0006\u0012\u0002\b\u00030\u001a¢\u0006\u0002\u0010\u001b\"'\u0010\u0000\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u0003*\u0002H\u00028F¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\"-\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00018G¢\u0006\f\u0012\u0004\b\b\u0010\t\u001a\u0004\b\n\u0010\u000b\"&\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\"\b\b\u0000\u0010\u0002*\u00020\r*\u0002H\u00028\u00c6\u0002¢\u0006\u0006\u001a\u0004\b\n\u0010\u000e\";\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0007\"\b\b\u0000\u0010\u0002*\u00020\r*\b\u0012\u0004\u0012\u0002H\u00020\u00018\u00c7\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u000f\u0010\t\u001a\u0004\b\u0010\u0010\u000b\"+\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\"\b\b\u0000\u0010\u0002*\u00020\r*\b\u0012\u0004\u0012\u0002H\u00020\u00018F¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u000b\"-\u0010\u0013\u001a\n\u0012\u0004\u0012\u0002H\u0002\u0018\u00010\u0007\"\b\b\u0000\u0010\u0002*\u00020\r*\b\u0012\u0004\u0012\u0002H\u00020\u00018F¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u000b\"+\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\r*\b\u0012\u0004\u0012\u0002H\u00020\u00078G¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017¨\u0006\u001c" }, d2 = { "annotationClass", "Lkotlin/reflect/KClass;", "T", "", "getAnnotationClass", "(Ljava/lang/annotation/Annotation;)Lkotlin/reflect/KClass;", "java", "Ljava/lang/Class;", "java$annotations", "(Lkotlin/reflect/KClass;)V", "getJavaClass", "(Lkotlin/reflect/KClass;)Ljava/lang/Class;", "javaClass", "", "(Ljava/lang/Object;)Ljava/lang/Class;", "javaClass$annotations", "getRuntimeClassOfKClassInstance", "javaObjectType", "getJavaObjectType", "javaPrimitiveType", "getJavaPrimitiveType", "kotlin", "getKotlinClass", "(Ljava/lang/Class;)Lkotlin/reflect/KClass;", "isArrayOf", "", "", "([Ljava/lang/Object;)Z", "kotlin-stdlib" })
@JvmName(name = "JvmClassMappingKt")
public final class JvmClassMappingKt
{
    @JvmName(name = "getJavaClass")
    @NotNull
    public static final <T> Class<T> getJavaClass(@NotNull final KClass<T> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Class<?> jClass = ((ClassBasedDeclarationContainer)$receiver).getJClass();
        if (jClass == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<T>");
        }
        return (Class<T>)jClass;
    }
    
    @Nullable
    public static final <T> Class<T> getJavaPrimitiveType(@NotNull final KClass<T> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Class thisJClass = ((ClassBasedDeclarationContainer)$receiver).getJClass();
        if (!thisJClass.isPrimitive()) {
            final String name = thisJClass.getName();
            if (name != null) {
                final String s = name;
                switch (s.hashCode()) {
                    case -527879800: {
                        if (s.equals("java.lang.Float")) {
                            final Serializable s2 = Float.TYPE;
                            return (Class<T>)s2;
                        }
                        break;
                    }
                    case 399092968: {
                        if (s.equals("java.lang.Void")) {
                            final Serializable s2 = Void.TYPE;
                            return (Class<T>)s2;
                        }
                        break;
                    }
                    case 155276373: {
                        if (s.equals("java.lang.Character")) {
                            final Serializable s2 = Character.TYPE;
                            return (Class<T>)s2;
                        }
                        break;
                    }
                    case 398795216: {
                        if (s.equals("java.lang.Long")) {
                            final Serializable s2 = Long.TYPE;
                            return (Class<T>)s2;
                        }
                        break;
                    }
                    case 761287205: {
                        if (s.equals("java.lang.Double")) {
                            final Serializable s2 = Double.TYPE;
                            return (Class<T>)s2;
                        }
                        break;
                    }
                    case -515992664: {
                        if (s.equals("java.lang.Short")) {
                            final Serializable s2 = Short.TYPE;
                            return (Class<T>)s2;
                        }
                        break;
                    }
                    case 344809556: {
                        if (s.equals("java.lang.Boolean")) {
                            final Serializable s2 = Boolean.TYPE;
                            return (Class<T>)s2;
                        }
                        break;
                    }
                    case 398507100: {
                        if (s.equals("java.lang.Byte")) {
                            final Serializable s2 = Byte.TYPE;
                            return (Class<T>)s2;
                        }
                        break;
                    }
                    case -2056817302: {
                        if (s.equals("java.lang.Integer")) {
                            final Serializable s2 = Integer.TYPE;
                            return (Class<T>)s2;
                        }
                        break;
                    }
                }
            }
            final Serializable s2 = null;
            return (Class<T>)s2;
        }
        final Class clazz = thisJClass;
        if (clazz == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<T>");
        }
        return (Class<T>)clazz;
    }
    
    @NotNull
    public static final <T> Class<T> getJavaObjectType(@NotNull final KClass<T> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Class thisJClass = ((ClassBasedDeclarationContainer)$receiver).getJClass();
        if (!thisJClass.isPrimitive()) {
            final Class clazz = thisJClass;
            if (clazz == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<T>");
            }
            return (Class<T>)clazz;
        }
        else {
            final String name = thisJClass.getName();
            Serializable s3 = null;
            Serializable s2 = null;
            Label_0294: {
                if (name != null) {
                    final String s = name;
                    switch (s.hashCode()) {
                        case 64711720: {
                            if (s.equals("boolean")) {
                                s2 = (s3 = Boolean.class);
                                break Label_0294;
                            }
                            break;
                        }
                        case 3625364: {
                            if (s.equals("void")) {
                                s2 = (s3 = Void.class);
                                break Label_0294;
                            }
                            break;
                        }
                        case 3039496: {
                            if (s.equals("byte")) {
                                s2 = (s3 = Byte.class);
                                break Label_0294;
                            }
                            break;
                        }
                        case -1325958191: {
                            if (s.equals("double")) {
                                s2 = (s3 = Double.class);
                                break Label_0294;
                            }
                            break;
                        }
                        case 3052374: {
                            if (s.equals("char")) {
                                s2 = (s3 = Character.class);
                                break Label_0294;
                            }
                            break;
                        }
                        case 109413500: {
                            if (s.equals("short")) {
                                s2 = (s3 = Short.class);
                                break Label_0294;
                            }
                            break;
                        }
                        case 97526364: {
                            if (s.equals("float")) {
                                s2 = (s3 = Float.class);
                                break Label_0294;
                            }
                            break;
                        }
                        case 104431: {
                            if (s.equals("int")) {
                                s2 = (s3 = Integer.class);
                                break Label_0294;
                            }
                            break;
                        }
                        case 3327612: {
                            if (s.equals("long")) {
                                s2 = (s3 = Long.class);
                                break Label_0294;
                            }
                            break;
                        }
                    }
                }
                s2 = (s3 = thisJClass);
            }
            if (s3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<T>");
            }
            return (Class<T>)s2;
        }
    }
    
    @JvmName(name = "getKotlinClass")
    @NotNull
    public static final <T> KClass<T> getKotlinClass(@NotNull final Class<T> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return (KClass<T>)Reflection.getOrCreateKotlinClass($receiver);
    }
    
    @NotNull
    public static final <T> Class<T> getJavaClass(@NotNull final T $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Class<?> class1 = $receiver.getClass();
        if (class1 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<T>");
        }
        return (Class<T>)class1;
    }
    
    @JvmName(name = "getRuntimeClassOfKClassInstance")
    @NotNull
    @java.lang.Deprecated
    public static final <T> Class<KClass<T>> getRuntimeClassOfKClassInstance(@NotNull final KClass<T> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Class<? extends KClass> class1 = $receiver.getClass();
        if (class1 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<kotlin.reflect.KClass<T>>");
        }
        return (Class<KClass<T>>)class1;
    }
    
    private static final <T> boolean isArrayOf(@NotNull final Object[] $receiver) {
        Intrinsics.reifiedOperationMarker(4, "T");
        return Object.class.isAssignableFrom($receiver.getClass().getComponentType());
    }
    
    @NotNull
    public static final <T extends Annotation> KClass<? extends T> getAnnotationClass(@NotNull final T $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Class<? extends Annotation> annotationType = $receiver.annotationType();
        Intrinsics.checkExpressionValueIsNotNull(annotationType, "(this as java.lang.annot\u2026otation).annotationType()");
        final KClass<Object> kotlinClass = getKotlinClass((Class<Object>)annotationType);
        if (kotlinClass == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.reflect.KClass<out T>");
        }
        return (KClass<? extends T>)kotlinClass;
    }
}
