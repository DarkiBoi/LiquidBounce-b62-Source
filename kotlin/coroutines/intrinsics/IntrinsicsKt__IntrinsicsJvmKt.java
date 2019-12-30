// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.intrinsics;

import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import org.jetbrains.annotations.Nullable;
import kotlin.ResultKt;
import kotlin.coroutines.jvm.internal.RestrictedContinuationImpl;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.jvm.internal.BaseContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.internal.InlineOnly;
import kotlin.SinceKotlin;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.TypeCastException;
import org.jetbrains.annotations.NotNull;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function1;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000&\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u001aF\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u00012\u001c\b\u0004\u0010\u0005\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006H\u0083\b¢\u0006\u0002\b\b\u001aD\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u0003*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007\u00f8\u0001\u0000¢\u0006\u0002\u0010\n\u001a]\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u000b\"\u0004\b\u0001\u0010\u0003*#\b\u0001\u0012\u0004\u0012\u0002H\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\f¢\u0006\u0002\b\r2\u0006\u0010\u000e\u001a\u0002H\u000b2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007\u00f8\u0001\u0000¢\u0006\u0002\u0010\u000f\u001a\u001e\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007\u001aA\u0010\u0011\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0003*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0012\u001aZ\u0010\u0011\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u000b\"\u0004\b\u0001\u0010\u0003*#\b\u0001\u0012\u0004\u0012\u0002H\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\f¢\u0006\u0002\b\r2\u0006\u0010\u000e\u001a\u0002H\u000b2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0013\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0014" }, d2 = { "createCoroutineFromSuspendFunction", "Lkotlin/coroutines/Continuation;", "", "T", "completion", "block", "Lkotlin/Function1;", "", "createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt", "createCoroutineUnintercepted", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "R", "Lkotlin/Function2;", "Lkotlin/ExtensionFunctionType;", "receiver", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "intercepted", "startCoroutineUninterceptedOrReturn", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlin-stdlib" }, xs = "kotlin/coroutines/intrinsics/IntrinsicsKt")
class IntrinsicsKt__IntrinsicsJvmKt
{
    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final <T> Object startCoroutineUninterceptedOrReturn(@NotNull final Function1<? super Continuation<? super T>, ?> $receiver, final Continuation<? super T> completion) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type (kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
        }
        return ((Function1)TypeIntrinsics.beforeCheckcastToFunctionOfArity($receiver, 1)).invoke(completion);
    }
    
    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final <R, T> Object startCoroutineUninterceptedOrReturn(@NotNull final Function2<? super R, ? super Continuation<? super T>, ?> $receiver, final R receiver, final Continuation<? super T> completion) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type (R, kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
        }
        return ((Function2)TypeIntrinsics.beforeCheckcastToFunctionOfArity($receiver, 2)).invoke(receiver, completion);
    }
    
    @SinceKotlin(version = "1.3")
    @NotNull
    public static final <T> Continuation<Unit> createCoroutineUnintercepted(@NotNull final Function1<? super Continuation<? super T>, ?> $receiver, @NotNull final Continuation<? super T> completion) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        final Continuation probeCompletion = DebugProbesKt.probeCoroutineCreated((Continuation<? super Object>)completion);
        Object create;
        if ($receiver instanceof BaseContinuationImpl) {
            create = ((BaseContinuationImpl)$receiver).create(probeCompletion);
        }
        else {
            final CoroutineContext context$iv = probeCompletion.getContext();
            if (context$iv == EmptyCoroutineContext.INSTANCE) {
                final Continuation $captured_local_variable$1 = probeCompletion;
                final Continuation $super_call_param$2 = probeCompletion;
                if ($super_call_param$2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
                }
                create = new RestrictedContinuationImpl($captured_local_variable$1, $super_call_param$2, $receiver) {
                    private int label;
                    
                    @Nullable
                    @Override
                    protected Object invokeSuspend(@NotNull final Object result) {
                        Object invoke = null;
                        switch (this.label) {
                            case 0: {
                                this.label = 1;
                                ResultKt.throwOnFailure(result);
                                final Continuation it = this;
                                final Function1 $this_createCoroutineUnintercepted$inlined = this.$this_createCoroutineUnintercepted$inlined;
                                if ($this_createCoroutineUnintercepted$inlined == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type (kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
                                }
                                invoke = ((Function1)TypeIntrinsics.beforeCheckcastToFunctionOfArity($this_createCoroutineUnintercepted$inlined, 1)).invoke(it);
                                break;
                            }
                            case 1: {
                                this.label = 2;
                                ResultKt.throwOnFailure(result);
                                invoke = result;
                                break;
                            }
                            default: {
                                throw new IllegalStateException("This coroutine had already completed".toString());
                            }
                        }
                        return invoke;
                    }
                };
            }
            else {
                final Continuation $captured_local_variable$2 = probeCompletion;
                final CoroutineContext $captured_local_variable$3 = context$iv;
                final Continuation $super_call_param$3 = probeCompletion;
                if ($super_call_param$3 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
                }
                create = new ContinuationImpl($captured_local_variable$2, $captured_local_variable$3, $super_call_param$3, context$iv, $receiver) {
                    private int label;
                    
                    @Nullable
                    @Override
                    protected Object invokeSuspend(@NotNull final Object result) {
                        Object invoke = null;
                        switch (this.label) {
                            case 0: {
                                this.label = 1;
                                ResultKt.throwOnFailure(result);
                                final Continuation it = this;
                                final Function1 $this_createCoroutineUnintercepted$inlined = this.$this_createCoroutineUnintercepted$inlined;
                                if ($this_createCoroutineUnintercepted$inlined == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type (kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
                                }
                                invoke = ((Function1)TypeIntrinsics.beforeCheckcastToFunctionOfArity($this_createCoroutineUnintercepted$inlined, 1)).invoke(it);
                                break;
                            }
                            case 1: {
                                this.label = 2;
                                ResultKt.throwOnFailure(result);
                                invoke = result;
                                break;
                            }
                            default: {
                                throw new IllegalStateException("This coroutine had already completed".toString());
                            }
                        }
                        return invoke;
                    }
                };
            }
        }
        return (Continuation<Unit>)create;
    }
    
    @SinceKotlin(version = "1.3")
    @NotNull
    public static final <R, T> Continuation<Unit> createCoroutineUnintercepted(@NotNull final Function2<? super R, ? super Continuation<? super T>, ?> $receiver, final R receiver, @NotNull final Continuation<? super T> completion) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        final Continuation probeCompletion = DebugProbesKt.probeCoroutineCreated((Continuation<? super Object>)completion);
        Object create;
        if ($receiver instanceof BaseContinuationImpl) {
            create = ((BaseContinuationImpl)$receiver).create(receiver, probeCompletion);
        }
        else {
            final CoroutineContext context$iv = probeCompletion.getContext();
            if (context$iv == EmptyCoroutineContext.INSTANCE) {
                final Continuation $captured_local_variable$1 = probeCompletion;
                final Continuation $super_call_param$2 = probeCompletion;
                if ($super_call_param$2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
                }
                create = new RestrictedContinuationImpl($captured_local_variable$1, $super_call_param$2, $receiver, receiver) {
                    private int label;
                    
                    @Nullable
                    @Override
                    protected Object invokeSuspend(@NotNull final Object result) {
                        Object invoke = null;
                        switch (this.label) {
                            case 0: {
                                this.label = 1;
                                ResultKt.throwOnFailure(result);
                                final Continuation it = this;
                                final Function2 $this_createCoroutineUnintercepted$inlined = this.$this_createCoroutineUnintercepted$inlined;
                                if ($this_createCoroutineUnintercepted$inlined == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type (R, kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
                                }
                                invoke = ((Function2)TypeIntrinsics.beforeCheckcastToFunctionOfArity($this_createCoroutineUnintercepted$inlined, 2)).invoke(this.$receiver$inlined, it);
                                break;
                            }
                            case 1: {
                                this.label = 2;
                                ResultKt.throwOnFailure(result);
                                invoke = result;
                                break;
                            }
                            default: {
                                throw new IllegalStateException("This coroutine had already completed".toString());
                            }
                        }
                        return invoke;
                    }
                };
            }
            else {
                final Continuation $captured_local_variable$2 = probeCompletion;
                final CoroutineContext $captured_local_variable$3 = context$iv;
                final Continuation $super_call_param$3 = probeCompletion;
                if ($super_call_param$3 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
                }
                create = new ContinuationImpl($captured_local_variable$2, $captured_local_variable$3, $super_call_param$3, context$iv, $receiver, receiver) {
                    private int label;
                    
                    @Nullable
                    @Override
                    protected Object invokeSuspend(@NotNull final Object result) {
                        Object invoke = null;
                        switch (this.label) {
                            case 0: {
                                this.label = 1;
                                ResultKt.throwOnFailure(result);
                                final Continuation it = this;
                                final Function2 $this_createCoroutineUnintercepted$inlined = this.$this_createCoroutineUnintercepted$inlined;
                                if ($this_createCoroutineUnintercepted$inlined == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type (R, kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
                                }
                                invoke = ((Function2)TypeIntrinsics.beforeCheckcastToFunctionOfArity($this_createCoroutineUnintercepted$inlined, 2)).invoke(this.$receiver$inlined, it);
                                break;
                            }
                            case 1: {
                                this.label = 2;
                                ResultKt.throwOnFailure(result);
                                invoke = result;
                                break;
                            }
                            default: {
                                throw new IllegalStateException("This coroutine had already completed".toString());
                            }
                        }
                        return invoke;
                    }
                };
            }
        }
        return (Continuation<Unit>)create;
    }
    
    @SinceKotlin(version = "1.3")
    @NotNull
    public static final <T> Continuation<T> intercepted(@NotNull final Continuation<? super T> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        ContinuationImpl continuationImpl = (ContinuationImpl)$receiver;
        if (!($receiver instanceof ContinuationImpl)) {
            continuationImpl = null;
        }
        final ContinuationImpl continuationImpl2 = continuationImpl;
        Continuation<Object> intercepted;
        if (continuationImpl2 == null || (intercepted = continuationImpl2.intercepted()) == null) {
            intercepted = (Continuation<Object>)$receiver;
        }
        return (Continuation<T>)intercepted;
    }
    
    @SinceKotlin(version = "1.3")
    private static final <T> Continuation<Unit> createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt(final Continuation<? super T> completion, final Function1<? super Continuation<? super T>, ?> block) {
        final CoroutineContext context = completion.getContext();
        Continuation<Unit> continuation;
        if (context == EmptyCoroutineContext.INSTANCE) {
            if (completion == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
            }
            continuation = (Continuation<Unit>)new IntrinsicsKt__IntrinsicsJvmKt$createCoroutineFromSuspendFunction.IntrinsicsKt__IntrinsicsJvmKt$createCoroutineFromSuspendFunction$1((Function1)block, (Continuation)completion, (Continuation)completion);
        }
        else {
            final CoroutineContext coroutineContext = context;
            if (completion == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
            }
            continuation = (Continuation<Unit>)new IntrinsicsKt__IntrinsicsJvmKt$createCoroutineFromSuspendFunction.IntrinsicsKt__IntrinsicsJvmKt$createCoroutineFromSuspendFunction$2((Function1)block, (Continuation)completion, coroutineContext, (Continuation)completion, context);
        }
        return continuation;
    }
    
    public IntrinsicsKt__IntrinsicsJvmKt() {
    }
}
