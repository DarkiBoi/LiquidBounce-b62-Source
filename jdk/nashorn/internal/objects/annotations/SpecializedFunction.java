// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects.annotations;

import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.CallSiteDescriptor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface SpecializedFunction {
    String name() default "";
    
    Class<?> linkLogic() default LinkLogic.Empty.class;
    
    boolean isConstructor() default false;
    
    boolean isOptimistic() default false;
    
    public abstract static class LinkLogic
    {
        public static final LinkLogic EMPTY_INSTANCE;
        
        public static Class<? extends LinkLogic> getEmptyLinkLogicClass() {
            return Empty.class;
        }
        
        public Class<? extends Throwable> getRelinkException() {
            return null;
        }
        
        public static boolean isEmpty(final Class<? extends LinkLogic> clazz) {
            return clazz == Empty.class;
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public abstract boolean canLink(final Object p0, final CallSiteDescriptor p1, final LinkRequest p2);
        
        public boolean needsGuard(final Object self) {
            return true;
        }
        
        public boolean needsGuard(final Object self, final Object... args) {
            return true;
        }
        
        public MethodHandle getGuard() {
            return null;
        }
        
        public boolean checkLinkable(final Object self, final CallSiteDescriptor desc, final LinkRequest request) {
            return this.canLink(self, desc, request);
        }
        
        static {
            EMPTY_INSTANCE = new Empty();
        }
        
        private static final class Empty extends LinkLogic
        {
            @Override
            public boolean canLink(final Object self, final CallSiteDescriptor desc, final LinkRequest request) {
                return true;
            }
            
            @Override
            public boolean isEmpty() {
                return true;
            }
        }
    }
}
