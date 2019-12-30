// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import kotlin.reflect.KMutableProperty2;
import kotlin.reflect.KProperty2;
import kotlin.reflect.KMutableProperty1;
import kotlin.reflect.KProperty1;
import kotlin.reflect.KMutableProperty0;
import kotlin.reflect.KProperty0;
import kotlin.reflect.KFunction;
import kotlin.SinceKotlin;
import kotlin.reflect.KDeclarationContainer;
import kotlin.reflect.KClass;

public class Reflection
{
    private static final ReflectionFactory factory;
    static final String REFLECTION_NOT_AVAILABLE = " (Kotlin reflection is not available)";
    private static final KClass[] EMPTY_K_CLASS_ARRAY;
    
    public static KClass createKotlinClass(final Class javaClass) {
        return Reflection.factory.createKotlinClass(javaClass);
    }
    
    public static KClass createKotlinClass(final Class javaClass, final String internalName) {
        return Reflection.factory.createKotlinClass(javaClass, internalName);
    }
    
    public static KDeclarationContainer getOrCreateKotlinPackage(final Class javaClass, final String moduleName) {
        return Reflection.factory.getOrCreateKotlinPackage(javaClass, moduleName);
    }
    
    public static KClass getOrCreateKotlinClass(final Class javaClass) {
        return Reflection.factory.getOrCreateKotlinClass(javaClass);
    }
    
    public static KClass getOrCreateKotlinClass(final Class javaClass, final String internalName) {
        return Reflection.factory.getOrCreateKotlinClass(javaClass, internalName);
    }
    
    public static KClass[] getOrCreateKotlinClasses(final Class[] javaClasses) {
        final int size = javaClasses.length;
        if (size == 0) {
            return Reflection.EMPTY_K_CLASS_ARRAY;
        }
        final KClass[] kClasses = new KClass[size];
        for (int i = 0; i < size; ++i) {
            kClasses[i] = getOrCreateKotlinClass(javaClasses[i]);
        }
        return kClasses;
    }
    
    @SinceKotlin(version = "1.1")
    public static String renderLambdaToString(final Lambda lambda) {
        return Reflection.factory.renderLambdaToString(lambda);
    }
    
    @SinceKotlin(version = "1.3")
    public static String renderLambdaToString(final FunctionBase lambda) {
        return Reflection.factory.renderLambdaToString(lambda);
    }
    
    public static KFunction function(final FunctionReference f) {
        return Reflection.factory.function(f);
    }
    
    public static KProperty0 property0(final PropertyReference0 p) {
        return Reflection.factory.property0(p);
    }
    
    public static KMutableProperty0 mutableProperty0(final MutablePropertyReference0 p) {
        return Reflection.factory.mutableProperty0(p);
    }
    
    public static KProperty1 property1(final PropertyReference1 p) {
        return Reflection.factory.property1(p);
    }
    
    public static KMutableProperty1 mutableProperty1(final MutablePropertyReference1 p) {
        return Reflection.factory.mutableProperty1(p);
    }
    
    public static KProperty2 property2(final PropertyReference2 p) {
        return Reflection.factory.property2(p);
    }
    
    public static KMutableProperty2 mutableProperty2(final MutablePropertyReference2 p) {
        return Reflection.factory.mutableProperty2(p);
    }
    
    static {
        ReflectionFactory impl;
        try {
            final Class<?> implClass = Class.forName("kotlin.reflect.jvm.internal.ReflectionFactoryImpl");
            impl = (ReflectionFactory)implClass.newInstance();
        }
        catch (ClassCastException e) {
            impl = null;
        }
        catch (ClassNotFoundException e2) {
            impl = null;
        }
        catch (InstantiationException e3) {
            impl = null;
        }
        catch (IllegalAccessException e4) {
            impl = null;
        }
        factory = ((impl != null) ? impl : new ReflectionFactory());
        EMPTY_K_CLASS_ARRAY = new KClass[0];
    }
}
