// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {
    String name();
    
    String description();
    
    ModuleCategory category();
    
    int keyBind() default 0;
    
    boolean canEnable() default true;
}
