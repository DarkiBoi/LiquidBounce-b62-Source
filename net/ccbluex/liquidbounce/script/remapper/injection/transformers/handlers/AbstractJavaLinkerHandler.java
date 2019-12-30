// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.script.remapper.injection.transformers.handlers;

import kotlin.jvm.JvmStatic;
import org.objectweb.asm.Type;
import net.ccbluex.liquidbounce.script.remapper.Remapper;
import java.lang.reflect.Method;
import kotlin.jvm.internal.Intrinsics;
import java.lang.reflect.AccessibleObject;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001c\u0010\u0003\u001a\u00020\u00042\n\u0010\u0005\u001a\u0006\u0012\u0002\b\u00030\u00062\u0006\u0010\u0007\u001a\u00020\u0004H\u0007J$\u0010\u0003\u001a\u00020\u00042\n\u0010\u0005\u001a\u0006\u0012\u0002\b\u00030\u00062\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0007J\u001c\u0010\n\u001a\u00020\u00042\n\u0010\u0005\u001a\u0006\u0012\u0002\b\u00030\u00062\u0006\u0010\u0007\u001a\u00020\u0004H\u0007¨\u0006\u000b" }, d2 = { "Lnet/ccbluex/liquidbounce/script/remapper/injection/transformers/handlers/AbstractJavaLinkerHandler;", "", "()V", "addMember", "", "clazz", "Ljava/lang/Class;", "name", "accessibleObject", "Ljava/lang/reflect/AccessibleObject;", "setPropertyGetter", "LiquidBounce" })
public final class AbstractJavaLinkerHandler
{
    public static final AbstractJavaLinkerHandler INSTANCE;
    
    @JvmStatic
    @NotNull
    public static final String addMember(@NotNull final Class<?> clazz, @NotNull final String name, @NotNull final AccessibleObject accessibleObject) {
        Intrinsics.checkParameterIsNotNull(clazz, "clazz");
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(accessibleObject, "accessibleObject");
        if (!(accessibleObject instanceof Method)) {
            return name;
        }
        Class superclass;
        for (Class currentClass = clazz; Intrinsics.areEqual(currentClass.getName(), "java.lang.Object") ^ true; currentClass = superclass) {
            final Remapper instance = Remapper.INSTANCE;
            final Class clazz2 = currentClass;
            final String methodDescriptor = Type.getMethodDescriptor((Method)accessibleObject);
            Intrinsics.checkExpressionValueIsNotNull(methodDescriptor, "Type.getMethodDescriptor(accessibleObject)");
            final String remapped = instance.remapMethod(clazz2, name, methodDescriptor);
            if (Intrinsics.areEqual(remapped, name) ^ true) {
                return remapped;
            }
            if (currentClass.getSuperclass() == null) {
                break;
            }
            superclass = currentClass.getSuperclass();
            Intrinsics.checkExpressionValueIsNotNull(superclass, "currentClass.superclass");
        }
        return name;
    }
    
    @JvmStatic
    @NotNull
    public static final String addMember(@NotNull final Class<?> clazz, @NotNull final String name) {
        Intrinsics.checkParameterIsNotNull(clazz, "clazz");
        Intrinsics.checkParameterIsNotNull(name, "name");
        Class superclass;
        for (Class currentClass = clazz; Intrinsics.areEqual(currentClass.getName(), "java.lang.Object") ^ true; currentClass = superclass) {
            final String remapped = Remapper.INSTANCE.remapField(currentClass, name);
            if (Intrinsics.areEqual(remapped, name) ^ true) {
                return remapped;
            }
            if (currentClass.getSuperclass() == null) {
                break;
            }
            superclass = currentClass.getSuperclass();
            Intrinsics.checkExpressionValueIsNotNull(superclass, "currentClass.superclass");
        }
        return name;
    }
    
    @JvmStatic
    @NotNull
    public static final String setPropertyGetter(@NotNull final Class<?> clazz, @NotNull final String name) {
        Intrinsics.checkParameterIsNotNull(clazz, "clazz");
        Intrinsics.checkParameterIsNotNull(name, "name");
        Class superclass;
        for (Class currentClass = clazz; Intrinsics.areEqual(currentClass.getName(), "java.lang.Object") ^ true; currentClass = superclass) {
            final String remapped = Remapper.INSTANCE.remapField(currentClass, name);
            if (Intrinsics.areEqual(remapped, name) ^ true) {
                return remapped;
            }
            if (currentClass.getSuperclass() == null) {
                break;
            }
            superclass = currentClass.getSuperclass();
            Intrinsics.checkExpressionValueIsNotNull(superclass, "currentClass.superclass");
        }
        return name;
    }
    
    private AbstractJavaLinkerHandler() {
    }
    
    static {
        INSTANCE = new AbstractJavaLinkerHandler();
    }
}
