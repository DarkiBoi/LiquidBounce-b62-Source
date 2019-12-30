// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0004J\u0006\u0010\n\u001a\u00020\u0002J\f\u0010\u000b\u001a\u00020\u0004*\u00020\u0002H\u0014R\u000e\u0010\u0006\u001a\u00020\u0002X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\f" }, d2 = { "Lkotlin/jvm/internal/IntSpreadBuilder;", "Lkotlin/jvm/internal/PrimitiveSpreadBuilder;", "", "size", "", "(I)V", "values", "add", "", "value", "toArray", "getSize", "kotlin-stdlib" })
public final class IntSpreadBuilder extends PrimitiveSpreadBuilder<int[]>
{
    private final int[] values;
    
    @Override
    protected int getSize(@NotNull final int[] $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return $receiver.length;
    }
    
    public final void add(final int value) {
        final int[] values = this.values;
        final int position;
        this.setPosition((position = this.getPosition()) + 1);
        values[position] = value;
    }
    
    @NotNull
    public final int[] toArray() {
        return this.toArray(this.values, new int[this.size()]);
    }
    
    public IntSpreadBuilder(final int size) {
        super(size);
        this.values = new int[size];
    }
}
