// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.experimental.intrinsics;

import kotlin.jvm.functions.Function0;
import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.internal.InlineOnly;
import kotlin.SinceKotlin;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.TypeCastException;
import org.jetbrains.annotations.NotNull;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function1;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u00002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a:\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\"\u0004\b\u0000\u0010\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\t0\u00072\u0010\b\u0004\u0010\u000b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00010\fH\u0082\b¢\u0006\u0002\b\r\u001aD\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\"\u0004\b\u0000\u0010\t*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u0007\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u000f2\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\t0\u0007H\u0007\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0010\u001a]\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\"\u0004\b\u0000\u0010\u0011\"\u0004\b\u0001\u0010\t*#\b\u0001\u0012\u0004\u0012\u0002H\u0011\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u0007\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0012¢\u0006\u0002\b\u00132\u0006\u0010\u0014\u001a\u0002H\u00112\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\t0\u0007H\u0007\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0015\u001aA\u0010\u0016\u001a\u0004\u0018\u00010\u0001\"\u0004\b\u0000\u0010\t*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u0007\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u000f2\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\t0\u0007H\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0017\u001aZ\u0010\u0016\u001a\u0004\u0018\u00010\u0001\"\u0004\b\u0000\u0010\u0011\"\u0004\b\u0001\u0010\t*#\b\u0001\u0012\u0004\u0012\u0002H\u0011\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u0007\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0012¢\u0006\u0002\b\u00132\u0006\u0010\u0014\u001a\u0002H\u00112\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\t0\u0007H\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0018\"\u001a\u0010\u0000\u001a\u00020\u00018FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0002\u0010\u0003\u001a\u0004\b\u0004\u0010\u0005\u0082\u0002\u0004\n\u0002\b\t¨\u0006\u0019" }, d2 = { "COROUTINE_SUSPENDED", "", "COROUTINE_SUSPENDED$annotations", "()V", "getCOROUTINE_SUSPENDED", "()Ljava/lang/Object;", "buildContinuationByInvokeCall", "Lkotlin/coroutines/experimental/Continuation;", "", "T", "completion", "block", "Lkotlin/Function0;", "buildContinuationByInvokeCall$IntrinsicsKt__IntrinsicsJvmKt", "createCoroutineUnchecked", "Lkotlin/Function1;", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/experimental/Continuation;)Lkotlin/coroutines/experimental/Continuation;", "R", "Lkotlin/Function2;", "Lkotlin/ExtensionFunctionType;", "receiver", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/experimental/Continuation;)Lkotlin/coroutines/experimental/Continuation;", "startCoroutineUninterceptedOrReturn", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "kotlin-stdlib_coroutinesExperimental" }, xs = "kotlin/coroutines/experimental/intrinsics/IntrinsicsKt")
class IntrinsicsKt__IntrinsicsJvmKt
{
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> Object startCoroutineUninterceptedOrReturn(@NotNull final Function1<? super Continuation<? super T>, ?> $receiver, final Continuation<? super T> completion) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type (kotlin.coroutines.experimental.Continuation<T>) -> kotlin.Any?");
        }
        return ((Function1)TypeIntrinsics.beforeCheckcastToFunctionOfArity($receiver, 1)).invoke(completion);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <R, T> Object startCoroutineUninterceptedOrReturn(@NotNull final Function2<? super R, ? super Continuation<? super T>, ?> $receiver, final R receiver, final Continuation<? super T> completion) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type (R, kotlin.coroutines.experimental.Continuation<T>) -> kotlin.Any?");
        }
        return ((Function2)TypeIntrinsics.beforeCheckcastToFunctionOfArity($receiver, 2)).invoke(receiver, completion);
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T> Continuation<Unit> createCoroutineUnchecked(@NotNull final Function1<? super Continuation<? super T>, ?> $receiver, @NotNull final Continuation<? super T> completion) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        Object o;
        if (!($receiver instanceof CoroutineImpl)) {
            final Continuation<Unit> continuation$iv = new Continuation<Unit>(completion, $receiver, completion) {
                @NotNull
                @Override
                public CoroutineContext getContext() {
                    return this.$completion.getContext();
                }
                
                @Override
                public void resume(@NotNull final Unit value) {
                    Intrinsics.checkParameterIsNotNull(value, "value");
                    final Continuation $completion = this.$completion;
                    try {
                        final Function1 $this_createCoroutineUnchecked$inlined = this.$this_createCoroutineUnchecked$inlined;
                        if ($this_createCoroutineUnchecked$inlined == null) {
                            throw new TypeCastException("null cannot be cast to non-null type (kotlin.coroutines.experimental.Continuation<T>) -> kotlin.Any?");
                        }
                        final Object invoke = ((Function1)TypeIntrinsics.beforeCheckcastToFunctionOfArity($this_createCoroutineUnchecked$inlined, 1)).invoke(this.$completion$inlined);
                        if (invoke != IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                            final Continuation continuation = $completion;
                            if (continuation == null) {
                                throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.Continuation<kotlin.Any?>");
                            }
                            continuation.resume(invoke);
                        }
                    }
                    catch (Throwable t) {
                        $completion.resumeWithException(t);
                    }
                }
                
                @Override
                public void resumeWithException(@NotNull final Throwable exception) {
                    Intrinsics.checkParameterIsNotNull(exception, "exception");
                    this.$completion.resumeWithException(exception);
                }
            };
            o = CoroutineIntrinsics.interceptContinuationIfNeeded(completion.getContext(), (Continuation<? super Object>)continuation$iv);
        }
        else {
            final Continuation<Unit> create = ((CoroutineImpl)$receiver).create(completion);
            if (create == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.jvm.internal.CoroutineImpl");
            }
            o = ((CoroutineImpl)create).getFacade();
        }
        return (Continuation<Unit>)o;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <R, T> Continuation<Unit> createCoroutineUnchecked(@NotNull final Function2<? super R, ? super Continuation<? super T>, ?> $receiver, final R receiver, @NotNull final Continuation<? super T> completion) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        Object o;
        if (!($receiver instanceof CoroutineImpl)) {
            final Continuation<Unit> continuation$iv = new Continuation<Unit>(completion, $receiver, receiver, completion) {
                @NotNull
                @Override
                public CoroutineContext getContext() {
                    return this.$completion.getContext();
                }
                
                @Override
                public void resume(@NotNull final Unit value) {
                    Intrinsics.checkParameterIsNotNull(value, "value");
                    final Continuation $completion = this.$completion;
                    try {
                        final Function2 $this_createCoroutineUnchecked$inlined = this.$this_createCoroutineUnchecked$inlined;
                        if ($this_createCoroutineUnchecked$inlined == null) {
                            throw new TypeCastException("null cannot be cast to non-null type (R, kotlin.coroutines.experimental.Continuation<T>) -> kotlin.Any?");
                        }
                        final Object invoke = ((Function2)TypeIntrinsics.beforeCheckcastToFunctionOfArity($this_createCoroutineUnchecked$inlined, 2)).invoke(this.$receiver$inlined, this.$completion$inlined);
                        if (invoke != IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                            final Continuation continuation = $completion;
                            if (continuation == null) {
                                throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.Continuation<kotlin.Any?>");
                            }
                            continuation.resume(invoke);
                        }
                    }
                    catch (Throwable t) {
                        $completion.resumeWithException(t);
                    }
                }
                
                @Override
                public void resumeWithException(@NotNull final Throwable exception) {
                    Intrinsics.checkParameterIsNotNull(exception, "exception");
                    this.$completion.resumeWithException(exception);
                }
            };
            o = CoroutineIntrinsics.interceptContinuationIfNeeded(completion.getContext(), (Continuation<? super Object>)continuation$iv);
        }
        else {
            final Continuation<Unit> create = ((CoroutineImpl)$receiver).create(receiver, completion);
            if (create == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.jvm.internal.CoroutineImpl");
            }
            o = ((CoroutineImpl)create).getFacade();
        }
        return (Continuation<Unit>)o;
    }
    
    private static final <T> Continuation<Unit> buildContinuationByInvokeCall$IntrinsicsKt__IntrinsicsJvmKt(final Continuation<? super T> completion, final Function0<?> block) {
        final IntrinsicsKt__IntrinsicsJvmKt$buildContinuationByInvokeCall$continuation.IntrinsicsKt__IntrinsicsJvmKt$buildContinuationByInvokeCall$continuation$1 continuation = new IntrinsicsKt__IntrinsicsJvmKt$buildContinuationByInvokeCall$continuation.IntrinsicsKt__IntrinsicsJvmKt$buildContinuationByInvokeCall$continuation$1((Continuation)completion, (Function0)block);
        return CoroutineIntrinsics.interceptContinuationIfNeeded(completion.getContext(), (Continuation<? super Unit>)continuation);
    }
    
    @NotNull
    public static final Object getCOROUTINE_SUSPENDED() {
        return IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
    }
    
    public IntrinsicsKt__IntrinsicsJvmKt() {
    }
}
