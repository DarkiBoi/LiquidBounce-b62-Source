// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.io;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.util.Iterator;
import java.io.BufferedReader;
import kotlin.Metadata;
import kotlin.sequences.Sequence;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u000f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00020\u0007H\u0096\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\b" }, d2 = { "Lkotlin/io/LinesSequence;", "Lkotlin/sequences/Sequence;", "", "reader", "Ljava/io/BufferedReader;", "(Ljava/io/BufferedReader;)V", "iterator", "", "kotlin-stdlib" })
final class LinesSequence implements Sequence<String>
{
    private final BufferedReader reader;
    
    @NotNull
    @Override
    public Iterator<String> iterator() {
        return (Iterator<String>)new LinesSequence$iterator.LinesSequence$iterator$1(this);
    }
    
    public LinesSequence(@NotNull final BufferedReader reader) {
        Intrinsics.checkParameterIsNotNull(reader, "reader");
        this.reader = reader;
    }
}
