// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.linker.GuardedInvocation;

class GuardedInvocationComponent
{
    private final GuardedInvocation guardedInvocation;
    private final Validator validator;
    
    GuardedInvocationComponent(final MethodHandle invocation) {
        this(invocation, null, ValidationType.NONE);
    }
    
    GuardedInvocationComponent(final MethodHandle invocation, final MethodHandle guard, final ValidationType validationType) {
        this(invocation, guard, null, validationType);
    }
    
    GuardedInvocationComponent(final MethodHandle invocation, final MethodHandle guard, final Class<?> validatorClass, final ValidationType validationType) {
        this(invocation, guard, new Validator(validatorClass, validationType));
    }
    
    GuardedInvocationComponent(final GuardedInvocation guardedInvocation, final Class<?> validatorClass, final ValidationType validationType) {
        this(guardedInvocation, new Validator(validatorClass, validationType));
    }
    
    GuardedInvocationComponent replaceInvocation(final MethodHandle newInvocation) {
        return this.replaceInvocation(newInvocation, this.guardedInvocation.getGuard());
    }
    
    GuardedInvocationComponent replaceInvocation(final MethodHandle newInvocation, final MethodHandle newGuard) {
        return new GuardedInvocationComponent(this.guardedInvocation.replaceMethods(newInvocation, newGuard), this.validator);
    }
    
    private GuardedInvocationComponent(final MethodHandle invocation, final MethodHandle guard, final Validator validator) {
        this(new GuardedInvocation(invocation, guard), validator);
    }
    
    private GuardedInvocationComponent(final GuardedInvocation guardedInvocation, final Validator validator) {
        this.guardedInvocation = guardedInvocation;
        this.validator = validator;
    }
    
    GuardedInvocation getGuardedInvocation() {
        return this.guardedInvocation;
    }
    
    Class<?> getValidatorClass() {
        return this.validator.validatorClass;
    }
    
    ValidationType getValidationType() {
        return this.validator.validationType;
    }
    
    GuardedInvocationComponent compose(final MethodHandle compositeInvocation, final MethodHandle otherGuard, final Class<?> otherValidatorClass, final ValidationType otherValidationType) {
        final Validator compositeValidator = this.validator.compose(new Validator(otherValidatorClass, otherValidationType));
        final MethodHandle compositeGuard = (compositeValidator == this.validator) ? this.guardedInvocation.getGuard() : otherGuard;
        return new GuardedInvocationComponent(compositeInvocation, compositeGuard, compositeValidator);
    }
    
    enum ValidationType
    {
        NONE, 
        INSTANCE_OF, 
        EXACT_CLASS, 
        IS_ARRAY;
    }
    
    private static class Validator
    {
        final Class<?> validatorClass;
        final ValidationType validationType;
        
        Validator(final Class<?> validatorClass, final ValidationType validationType) {
            this.validatorClass = validatorClass;
            this.validationType = validationType;
        }
        
        Validator compose(final Validator other) {
            if (other.validationType == ValidationType.NONE) {
                return this;
            }
            Label_0290: {
                switch (this.validationType) {
                    case NONE: {
                        return other;
                    }
                    case INSTANCE_OF: {
                        switch (other.validationType) {
                            case INSTANCE_OF: {
                                if (this.isAssignableFrom(other)) {
                                    return other;
                                }
                                if (other.isAssignableFrom(this)) {
                                    return this;
                                }
                                break Label_0290;
                            }
                            case EXACT_CLASS: {
                                if (this.isAssignableFrom(other)) {
                                    return other;
                                }
                                break Label_0290;
                            }
                            case IS_ARRAY: {
                                if (this.validatorClass.isArray()) {
                                    return this;
                                }
                                break Label_0290;
                            }
                            default: {
                                throw new AssertionError();
                            }
                        }
                        break;
                    }
                    case EXACT_CLASS: {
                        switch (other.validationType) {
                            case INSTANCE_OF: {
                                if (other.isAssignableFrom(this)) {
                                    return this;
                                }
                                break Label_0290;
                            }
                            case EXACT_CLASS: {
                                if (this.validatorClass == other.validatorClass) {
                                    return this;
                                }
                                break Label_0290;
                            }
                            case IS_ARRAY: {
                                if (this.validatorClass.isArray()) {
                                    return this;
                                }
                                break Label_0290;
                            }
                            default: {
                                throw new AssertionError();
                            }
                        }
                        break;
                    }
                    case IS_ARRAY: {
                        switch (other.validationType) {
                            case INSTANCE_OF:
                            case EXACT_CLASS: {
                                if (other.validatorClass.isArray()) {
                                    return other;
                                }
                                break Label_0290;
                            }
                            case IS_ARRAY: {
                                return this;
                            }
                            default: {
                                throw new AssertionError();
                            }
                        }
                        break;
                    }
                    default: {
                        throw new AssertionError();
                    }
                }
            }
            throw new AssertionError((Object)("Incompatible composition " + this + " vs " + other));
        }
        
        private boolean isAssignableFrom(final Validator other) {
            return this.validatorClass.isAssignableFrom(other.validatorClass);
        }
        
        @Override
        public String toString() {
            return "Validator[" + this.validationType + ((this.validatorClass == null) ? "" : (" " + this.validatorClass.getName())) + "]";
        }
    }
}
