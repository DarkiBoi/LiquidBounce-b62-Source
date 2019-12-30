// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.experimental.migration;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.Metadata;
import kotlin.coroutines.AbstractCoroutineContextElement;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0002\u0018\u0000 \u00072\u00020\u0001:\u0001\u0007B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\b" }, d2 = { "Lkotlin/coroutines/experimental/migration/ContextMigration;", "Lkotlin/coroutines/AbstractCoroutineContextElement;", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "(Lkotlin/coroutines/experimental/CoroutineContext;)V", "getContext", "()Lkotlin/coroutines/experimental/CoroutineContext;", "Key", "kotlin-stdlib_coroutinesExperimental" })
final class ContextMigration extends AbstractCoroutineContextElement
{
    @NotNull
    private final kotlin.coroutines.experimental.CoroutineContext context;
    public static final Key Key;
    
    @NotNull
    public final kotlin.coroutines.experimental.CoroutineContext getContext() {
        return this.context;
    }
    
    public ContextMigration(@NotNull final kotlin.coroutines.experimental.CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super(ContextMigration.Key);
        this.context = context;
    }
    
    static {
        Key = new Key(null);
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003¨\u0006\u0004" }, d2 = { "Lkotlin/coroutines/experimental/migration/ContextMigration$Key;", "Lkotlin/coroutines/CoroutineContext$Key;", "Lkotlin/coroutines/experimental/migration/ContextMigration;", "()V", "kotlin-stdlib_coroutinesExperimental" })
    public static final class Key implements CoroutineContext.Key<ContextMigration>
    {
        private Key() {
        }
    }
}
