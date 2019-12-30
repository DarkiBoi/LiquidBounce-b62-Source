// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import kotlin.DeprecationLevel;
import kotlin.jvm.functions.Function22;
import kotlin.jvm.functions.Function21;
import kotlin.jvm.functions.Function20;
import kotlin.jvm.functions.Function19;
import kotlin.jvm.functions.Function18;
import kotlin.jvm.functions.Function17;
import kotlin.jvm.functions.Function16;
import kotlin.jvm.functions.Function15;
import kotlin.jvm.functions.Function14;
import kotlin.jvm.functions.Function13;
import kotlin.jvm.functions.Function12;
import kotlin.jvm.functions.Function11;
import kotlin.jvm.functions.Function10;
import kotlin.jvm.functions.Function9;
import kotlin.jvm.functions.Function8;
import kotlin.jvm.functions.Function7;
import kotlin.jvm.functions.Function6;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function0;
import java.io.Serializable;
import kotlin.Function;

@Deprecated
@kotlin.Deprecated(message = "This class is no longer supported, do not use it.", level = DeprecationLevel.ERROR)
public abstract class FunctionImpl implements Function, Serializable, Function0, Function1, Function2, Function3, Function4, Function5, Function6, Function7, Function8, Function9, Function10, Function11, Function12, Function13, Function14, Function15, Function16, Function17, Function18, Function19, Function20, Function21, Function22
{
    public abstract int getArity();
    
    public Object invokeVararg(final Object... p) {
        throw new UnsupportedOperationException();
    }
    
    private void checkArity(final int expected) {
        if (this.getArity() != expected) {
            this.throwWrongArity(expected);
        }
    }
    
    private void throwWrongArity(final int expected) {
        throw new IllegalStateException("Wrong function arity, expected: " + expected + ", actual: " + this.getArity());
    }
    
    @Override
    public Object invoke() {
        this.checkArity(0);
        return this.invokeVararg(new Object[0]);
    }
    
    @Override
    public Object invoke(final Object p1) {
        this.checkArity(1);
        return this.invokeVararg(p1);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2) {
        this.checkArity(2);
        return this.invokeVararg(p1, p2);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2, final Object p3) {
        this.checkArity(3);
        return this.invokeVararg(p1, p2, p3);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2, final Object p3, final Object p4) {
        this.checkArity(4);
        return this.invokeVararg(p1, p2, p3, p4);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        this.checkArity(5);
        return this.invokeVararg(p1, p2, p3, p4, p5);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        this.checkArity(6);
        return this.invokeVararg(p1, p2, p3, p4, p5, p6);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        this.checkArity(7);
        return this.invokeVararg(p1, p2, p3, p4, p5, p6, p7);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        this.checkArity(8);
        return this.invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        this.checkArity(9);
        return this.invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9, final Object p10) {
        this.checkArity(10);
        return this.invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9, final Object p10, final Object p11) {
        this.checkArity(11);
        return this.invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9, final Object p10, final Object p11, final Object p12) {
        this.checkArity(12);
        return this.invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9, final Object p10, final Object p11, final Object p12, final Object p13) {
        this.checkArity(13);
        return this.invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9, final Object p10, final Object p11, final Object p12, final Object p13, final Object p14) {
        this.checkArity(14);
        return this.invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9, final Object p10, final Object p11, final Object p12, final Object p13, final Object p14, final Object p15) {
        this.checkArity(15);
        return this.invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9, final Object p10, final Object p11, final Object p12, final Object p13, final Object p14, final Object p15, final Object p16) {
        this.checkArity(16);
        return this.invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9, final Object p10, final Object p11, final Object p12, final Object p13, final Object p14, final Object p15, final Object p16, final Object p17) {
        this.checkArity(17);
        return this.invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9, final Object p10, final Object p11, final Object p12, final Object p13, final Object p14, final Object p15, final Object p16, final Object p17, final Object p18) {
        this.checkArity(18);
        return this.invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9, final Object p10, final Object p11, final Object p12, final Object p13, final Object p14, final Object p15, final Object p16, final Object p17, final Object p18, final Object p19) {
        this.checkArity(19);
        return this.invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9, final Object p10, final Object p11, final Object p12, final Object p13, final Object p14, final Object p15, final Object p16, final Object p17, final Object p18, final Object p19, final Object p20) {
        this.checkArity(20);
        return this.invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9, final Object p10, final Object p11, final Object p12, final Object p13, final Object p14, final Object p15, final Object p16, final Object p17, final Object p18, final Object p19, final Object p20, final Object p21) {
        this.checkArity(21);
        return this.invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21);
    }
    
    @Override
    public Object invoke(final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9, final Object p10, final Object p11, final Object p12, final Object p13, final Object p14, final Object p15, final Object p16, final Object p17, final Object p18, final Object p19, final Object p20, final Object p21, final Object p22) {
        this.checkArity(22);
        return this.invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21, p22);
    }
}
