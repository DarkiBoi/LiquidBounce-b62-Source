// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.io;

import java.nio.charset.CoderResult;
import java.nio.Buffer;
import java.nio.CharBuffer;
import java.nio.ByteBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.InputStream;
import kotlin.jvm.internal.Intrinsics;
import java.nio.charset.CharsetDecoder;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.PropertyReference0;
import kotlin.jvm.internal.PropertyReference0Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty0;
import kotlin.Lazy;
import kotlin.reflect.KProperty;
import kotlin.jvm.JvmName;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000n\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0002\u0010\u0005\n\u0002\u0010\f\n\u0002\u0010\u0019\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\t\n\u0002\u0010\n\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\rH\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u000eH\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u000fH\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0010H\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0011H\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0012H\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0013H\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0014H\u0087\b\u001a\t\u0010\u0015\u001a\u00020\nH\u0087\b\u001a\u0013\u0010\u0015\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\rH\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u000eH\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u000fH\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0010H\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0011H\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0012H\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0013H\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0014H\u0087\b\u001a\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017\u001a\u001a\u0010\u0016\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0003\u001a\u00020\u0004H\u0000\u001a\f\u0010\u001a\u001a\u00020\r*\u00020\u001bH\u0002\u001a\f\u0010\u001c\u001a\u00020\n*\u00020\u001dH\u0002\u001a\u0018\u0010\u001e\u001a\u00020\n*\u00020\u001b2\n\u0010\u001f\u001a\u00060 j\u0002`!H\u0002\u001a$\u0010\"\u001a\u00020\r*\u00020\u00042\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\u001b2\u0006\u0010&\u001a\u00020\rH\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u001b\u0010\u0003\u001a\u00020\u00048BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006¨\u0006'" }, d2 = { "BUFFER_SIZE", "", "LINE_SEPARATOR_MAX_LENGTH", "decoder", "Ljava/nio/charset/CharsetDecoder;", "getDecoder", "()Ljava/nio/charset/CharsetDecoder;", "decoder$delegate", "Lkotlin/Lazy;", "print", "", "message", "", "", "", "", "", "", "", "", "", "println", "readLine", "", "inputStream", "Ljava/io/InputStream;", "endsWithLineSeparator", "Ljava/nio/CharBuffer;", "flipBack", "Ljava/nio/Buffer;", "offloadPrefixTo", "builder", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "tryDecode", "byteBuffer", "Ljava/nio/ByteBuffer;", "charBuffer", "isEndOfStream", "kotlin-stdlib" })
@JvmName(name = "ConsoleKt")
public final class ConsoleKt
{
    static final /* synthetic */ KProperty[] $$delegatedProperties;
    private static final int BUFFER_SIZE = 32;
    private static final int LINE_SEPARATOR_MAX_LENGTH = 2;
    private static final Lazy decoder$delegate;
    
    static {
        $$delegatedProperties = new KProperty[] { Reflection.property0(new PropertyReference0Impl(Reflection.getOrCreateKotlinPackage(ConsoleKt.class, "kotlin-stdlib"), "decoder", "getDecoder()Ljava/nio/charset/CharsetDecoder;")) };
        decoder$delegate = LazyKt__LazyJVMKt.lazy((Function0<?>)ConsoleKt$decoder.ConsoleKt$decoder$2.INSTANCE);
    }
    
    @InlineOnly
    private static final void print(final Object message) {
        System.out.print(message);
    }
    
    @InlineOnly
    private static final void print(final int message) {
        System.out.print(message);
    }
    
    @InlineOnly
    private static final void print(final long message) {
        System.out.print(message);
    }
    
    @InlineOnly
    private static final void print(final byte message) {
        System.out.print((Object)message);
    }
    
    @InlineOnly
    private static final void print(final short message) {
        System.out.print((Object)message);
    }
    
    @InlineOnly
    private static final void print(final char message) {
        System.out.print(message);
    }
    
    @InlineOnly
    private static final void print(final boolean message) {
        System.out.print(message);
    }
    
    @InlineOnly
    private static final void print(final float message) {
        System.out.print(message);
    }
    
    @InlineOnly
    private static final void print(final double message) {
        System.out.print(message);
    }
    
    @InlineOnly
    private static final void print(final char[] message) {
        System.out.print(message);
    }
    
    @InlineOnly
    private static final void println(final Object message) {
        System.out.println(message);
    }
    
    @InlineOnly
    private static final void println(final int message) {
        System.out.println(message);
    }
    
    @InlineOnly
    private static final void println(final long message) {
        System.out.println(message);
    }
    
    @InlineOnly
    private static final void println(final byte message) {
        System.out.println((Object)message);
    }
    
    @InlineOnly
    private static final void println(final short message) {
        System.out.println((Object)message);
    }
    
    @InlineOnly
    private static final void println(final char message) {
        System.out.println(message);
    }
    
    @InlineOnly
    private static final void println(final boolean message) {
        System.out.println(message);
    }
    
    @InlineOnly
    private static final void println(final float message) {
        System.out.println(message);
    }
    
    @InlineOnly
    private static final void println(final double message) {
        System.out.println(message);
    }
    
    @InlineOnly
    private static final void println(final char[] message) {
        System.out.println(message);
    }
    
    @InlineOnly
    private static final void println() {
        System.out.println();
    }
    
    private static final CharsetDecoder getDecoder() {
        final Lazy decoder$delegate = ConsoleKt.decoder$delegate;
        final KProperty kProperty = ConsoleKt.$$delegatedProperties[0];
        return decoder$delegate.getValue();
    }
    
    @Nullable
    public static final String readLine() {
        final InputStream in = System.in;
        Intrinsics.checkExpressionValueIsNotNull(in, "System.`in`");
        return readLine(in, getDecoder());
    }
    
    @Nullable
    public static final String readLine(@NotNull final InputStream inputStream, @NotNull final CharsetDecoder decoder) {
        Intrinsics.checkParameterIsNotNull(inputStream, "inputStream");
        Intrinsics.checkParameterIsNotNull(decoder, "decoder");
        if (decoder.maxCharsPerByte() > 1) {
            throw new IllegalArgumentException("Encodings with multiple chars per byte are not supported".toString());
        }
        final ByteBuffer byteBuffer = ByteBuffer.allocate(32);
        final CharBuffer charBuffer = CharBuffer.allocate(4);
        final StringBuilder stringBuilder = new StringBuilder();
        int read = inputStream.read();
        if (read == -1) {
            return null;
        }
        do {
            byteBuffer.put((byte)read);
            final ByteBuffer byteBuffer2 = byteBuffer;
            Intrinsics.checkExpressionValueIsNotNull(byteBuffer2, "byteBuffer");
            final CharBuffer charBuffer2 = charBuffer;
            Intrinsics.checkExpressionValueIsNotNull(charBuffer2, "charBuffer");
            if (tryDecode(decoder, byteBuffer2, charBuffer2, false)) {
                if (endsWithLineSeparator(charBuffer)) {
                    break;
                }
                if (charBuffer.remaining() < 2) {
                    offloadPrefixTo(charBuffer, stringBuilder);
                }
            }
            read = inputStream.read();
        } while (read != -1);
        final CharsetDecoder $receiver = decoder;
        tryDecode($receiver, byteBuffer, charBuffer, true);
        $receiver.reset();
        final CharBuffer $receiver2 = charBuffer;
        int length = $receiver2.position();
        if (length > 0 && $receiver2.get(length - 1) == '\n' && --length > 0 && $receiver2.get(length - 1) == '\r') {
            --length;
        }
        $receiver2.flip();
        for (int i = 0; i < length; ++i) {
            final int it = i;
            stringBuilder.append($receiver2.get());
        }
        return stringBuilder.toString();
    }
    
    private static final boolean tryDecode(@NotNull final CharsetDecoder $receiver, final ByteBuffer byteBuffer, final CharBuffer charBuffer, final boolean isEndOfStream) {
        final int positionBefore = charBuffer.position();
        byteBuffer.flip();
        final CoderResult $receiver2 = $receiver.decode(byteBuffer, charBuffer, isEndOfStream);
        if ($receiver2.isError()) {
            $receiver2.throwException();
        }
        final boolean isDecoded = charBuffer.position() > positionBefore;
        if (isDecoded) {
            byteBuffer.clear();
        }
        else {
            flipBack(byteBuffer);
        }
        return isDecoded;
    }
    
    private static final boolean endsWithLineSeparator(@NotNull final CharBuffer $receiver) {
        final int p = $receiver.position();
        return p > 0 && $receiver.get(p - 1) == '\n';
    }
    
    private static final void flipBack(@NotNull final Buffer $receiver) {
        $receiver.position($receiver.limit());
        $receiver.limit($receiver.capacity());
    }
    
    private static final void offloadPrefixTo(@NotNull final CharBuffer $receiver, final StringBuilder builder) {
        $receiver.flip();
        for (int n = $receiver.limit() - 1, i = 0; i < n; ++i) {
            final int it = i;
            builder.append($receiver.get());
        }
        $receiver.compact();
    }
}
