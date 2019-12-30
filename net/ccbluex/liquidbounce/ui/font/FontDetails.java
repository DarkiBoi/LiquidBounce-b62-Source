// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.font;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
public @interface FontDetails {
    String fontName();
    
    int fontSize() default -1;
}
