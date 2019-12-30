// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.internal;

import kotlin.SinceKotlin;
import kotlin.PublishedApi;
import kotlin.KotlinVersion;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.JvmField;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0004\u001a \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0005H\u0001\u001a\"\u0010\b\u001a\u0002H\t\"\n\b\u0000\u0010\t\u0018\u0001*\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0083\b¢\u0006\u0002\u0010\f\u001a\b\u0010\r\u001a\u00020\u0005H\u0002\"\u0010\u0010\u0000\u001a\u00020\u00018\u0000X\u0081\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e" }, d2 = { "IMPLEMENTATIONS", "Lkotlin/internal/PlatformImplementations;", "apiVersionIsAtLeast", "", "major", "", "minor", "patch", "castToBaseType", "T", "", "instance", "(Ljava/lang/Object;)Ljava/lang/Object;", "getJavaVersion", "kotlin-stdlib" })
public final class PlatformImplementationsKt
{
    @JvmField
    @NotNull
    public static final PlatformImplementations IMPLEMENTATIONS;
    
    @InlineOnly
    private static final <T> T castToBaseType(final Object instance) {
        try {
            Intrinsics.reifiedOperationMarker(1, "T");
            return (T)instance;
        }
        catch (ClassCastException e) {
            final ClassLoader instanceCL = instance.getClass().getClassLoader();
            Intrinsics.reifiedOperationMarker(4, "T");
            final ClassLoader baseTypeCL = Object.class.getClassLoader();
            final Throwable initCause = new ClassCastException("Instance classloader: " + instanceCL + ", base type classloader: " + baseTypeCL).initCause(e);
            Intrinsics.checkExpressionValueIsNotNull(initCause, "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)");
            throw initCause;
        }
    }
    
    private static final int getJavaVersion() {
        final int default1 = 65542;
        final String property = System.getProperty("java.specification.version");
        if (property == null) {
            return default1;
        }
        final String version = property;
        final int firstDot = StringsKt__StringsKt.indexOf$default(version, '.', 0, false, 6, null);
        if (firstDot < 0) {
            int n;
            try {
                n = Integer.parseInt(version) * 65536;
            }
            catch (NumberFormatException e) {
                n = default1;
            }
            return n;
        }
        int secondDot = StringsKt__StringsKt.indexOf$default(version, '.', firstDot + 1, false, 4, null);
        if (secondDot < 0) {
            secondDot = version.length();
        }
        final String s = version;
        final int beginIndex = 0;
        final String s2 = s;
        if (s2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        final String substring = s2.substring(beginIndex, firstDot);
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        final String firstPart = substring;
        final String s3 = version;
        final int beginIndex2 = firstDot + 1;
        final String s4 = s3;
        if (s4 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        final String substring2 = s4.substring(beginIndex2, secondDot);
        Intrinsics.checkExpressionValueIsNotNull(substring2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        final String secondPart = substring2;
        int n2;
        try {
            n2 = Integer.parseInt(firstPart) * 65536 + Integer.parseInt(secondPart);
        }
        catch (NumberFormatException e2) {
            n2 = default1;
        }
        return n2;
    }
    
    @PublishedApi
    @SinceKotlin(version = "1.2")
    public static final boolean apiVersionIsAtLeast(final int major, final int minor, final int patch) {
        return KotlinVersion.CURRENT.isAtLeast(major, minor, patch);
    }
    
    static {
        final int version = getJavaVersion();
        PlatformImplementations implementations = null;
        Label_0457: {
            if (version >= 65544) {
                try {
                    final Object instance = Class.forName("kotlin.internal.jdk8.JDK8PlatformImplementations").newInstance();
                    Intrinsics.checkExpressionValueIsNotNull(instance, "Class.forName(\"kotlin.in\u2026entations\").newInstance()");
                    final Object o = instance;
                    try {
                        final Object o2 = o;
                        if (o2 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.internal.PlatformImplementations");
                        }
                        implementations = (PlatformImplementations)o2;
                    }
                    catch (ClassCastException ex) {
                        final Throwable initCause = new ClassCastException("Instance classloader: " + ((PlatformImplementations)o).getClass().getClassLoader() + ", base type classloader: " + PlatformImplementations.class.getClassLoader()).initCause(ex);
                        Intrinsics.checkExpressionValueIsNotNull(initCause, "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)");
                        throw initCause;
                    }
                    break Label_0457;
                }
                catch (ClassNotFoundException ex5) {
                    try {
                        final Object instance2 = Class.forName("kotlin.internal.JRE8PlatformImplementations").newInstance();
                        Intrinsics.checkExpressionValueIsNotNull(instance2, "Class.forName(\"kotlin.in\u2026entations\").newInstance()");
                        final Object o3 = instance2;
                        try {
                            final Object o4 = o3;
                            if (o4 == null) {
                                throw new TypeCastException("null cannot be cast to non-null type kotlin.internal.PlatformImplementations");
                            }
                            implementations = (PlatformImplementations)o4;
                        }
                        catch (ClassCastException ex2) {
                            final Throwable initCause2 = new ClassCastException("Instance classloader: " + ((PlatformImplementations)o3).getClass().getClassLoader() + ", base type classloader: " + PlatformImplementations.class.getClassLoader()).initCause(ex2);
                            Intrinsics.checkExpressionValueIsNotNull(initCause2, "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)");
                            throw initCause2;
                        }
                    }
                    catch (ClassNotFoundException ex6) {}
                }
            }
            if (version >= 65543) {
                try {
                    final Object instance3 = Class.forName("kotlin.internal.jdk7.JDK7PlatformImplementations").newInstance();
                    Intrinsics.checkExpressionValueIsNotNull(instance3, "Class.forName(\"kotlin.in\u2026entations\").newInstance()");
                    final Object o5 = instance3;
                    try {
                        final Object o6 = o5;
                        if (o6 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.internal.PlatformImplementations");
                        }
                        implementations = (PlatformImplementations)o6;
                    }
                    catch (ClassCastException ex3) {
                        final Throwable initCause3 = new ClassCastException("Instance classloader: " + ((PlatformImplementations)o5).getClass().getClassLoader() + ", base type classloader: " + PlatformImplementations.class.getClassLoader()).initCause(ex3);
                        Intrinsics.checkExpressionValueIsNotNull(initCause3, "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)");
                        throw initCause3;
                    }
                    break Label_0457;
                }
                catch (ClassNotFoundException ex7) {
                    try {
                        final Object instance4 = Class.forName("kotlin.internal.JRE7PlatformImplementations").newInstance();
                        Intrinsics.checkExpressionValueIsNotNull(instance4, "Class.forName(\"kotlin.in\u2026entations\").newInstance()");
                        final Object o7 = instance4;
                        try {
                            final Object o8 = o7;
                            if (o8 == null) {
                                throw new TypeCastException("null cannot be cast to non-null type kotlin.internal.PlatformImplementations");
                            }
                            implementations = (PlatformImplementations)o8;
                        }
                        catch (ClassCastException ex4) {
                            final Throwable initCause4 = new ClassCastException("Instance classloader: " + ((PlatformImplementations)o7).getClass().getClassLoader() + ", base type classloader: " + PlatformImplementations.class.getClassLoader()).initCause(ex4);
                            Intrinsics.checkExpressionValueIsNotNull(initCause4, "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)");
                            throw initCause4;
                        }
                    }
                    catch (ClassNotFoundException ex8) {}
                }
            }
            implementations = new PlatformImplementations();
        }
        IMPLEMENTATIONS = implementations;
    }
}
