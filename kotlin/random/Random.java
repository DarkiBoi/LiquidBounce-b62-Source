// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.random;

import kotlin.DeprecationLevel;
import kotlin.Deprecated;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.DoubleCompanionObject;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.JvmField;
import kotlin.SinceKotlin;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\b'\u0018\u0000 \u00182\u00020\u0001:\u0002\u0017\u0018B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H&J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH\u0016J$\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\b\b\u0002\u0010\u000b\u001a\u00020\u00042\b\b\u0002\u0010\f\u001a\u00020\u0004H\u0016J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u0004H\u0016J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u0004H\u0016J\u0010\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004H\u0016J\u0018\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u0016H\u0016J\u0018\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0011\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u0016H\u0016¨\u0006\u0019" }, d2 = { "Lkotlin/random/Random;", "", "()V", "nextBits", "", "bitCount", "nextBoolean", "", "nextBytes", "", "array", "fromIndex", "toIndex", "size", "nextDouble", "", "until", "from", "nextFloat", "", "nextInt", "nextLong", "", "Companion", "Default", "kotlin-stdlib" })
@SinceKotlin(version = "1.3")
public abstract class Random
{
    private static final Random defaultRandom;
    @JvmField
    @NotNull
    @java.lang.Deprecated
    public static final Companion Companion;
    public static final Default Default;
    
    public abstract int nextBits(final int p0);
    
    public int nextInt() {
        return this.nextBits(32);
    }
    
    public int nextInt(final int until) {
        return this.nextInt(0, until);
    }
    
    public int nextInt(final int from, final int until) {
        RandomKt.checkRangeBounds(from, until);
        final int n = until - from;
        if (n > 0 || n == Integer.MIN_VALUE) {
            int nextBits;
            if ((n & -n) == n) {
                final int bitCount = PlatformRandomKt.fastLog2(n);
                nextBits = this.nextBits(bitCount);
            }
            else {
                int bits;
                int v;
                do {
                    bits = this.nextInt() >>> 1;
                    v = bits % n;
                } while (bits - v + (n - 1) < 0);
                nextBits = v;
            }
            final int rnd = nextBits;
            return from + rnd;
        }
        int rnd;
        while (true) {
            final int nextInt;
            rnd = (nextInt = this.nextInt());
            if (from > nextInt) {
                continue;
            }
            if (until > nextInt) {
                break;
            }
        }
        return rnd;
    }
    
    public long nextLong() {
        return ((long)this.nextInt() << 32) + this.nextInt();
    }
    
    public long nextLong(final long until) {
        return this.nextLong(0L, until);
    }
    
    public long nextLong(final long from, final long until) {
        RandomKt.checkRangeBounds(from, until);
        final long n = until - from;
        if (n > 0L) {
            long rnd;
            if ((n & -n) == n) {
                final int nLow = (int)n;
                final int nHigh = (int)(n >>> 32);
                long n2;
                if (nLow != 0) {
                    final int bitCount = PlatformRandomKt.fastLog2(nLow);
                    n2 = ((long)this.nextBits(bitCount) & 0xFFFFFFFFL);
                }
                else if (nHigh == 1) {
                    n2 = ((long)this.nextInt() & 0xFFFFFFFFL);
                }
                else {
                    final int bitCount = PlatformRandomKt.fastLog2(nHigh);
                    n2 = ((long)this.nextBits(bitCount) << 32) + this.nextInt();
                }
                rnd = n2;
            }
            else {
                long bits;
                long v;
                do {
                    bits = this.nextLong() >>> 1;
                    v = bits % n;
                } while (bits - v + (n - 1L) < 0L);
                rnd = v;
            }
            return from + rnd;
        }
        long rnd;
        while (true) {
            final long nextLong;
            rnd = (nextLong = this.nextLong());
            if (from > nextLong) {
                continue;
            }
            if (until > nextLong) {
                break;
            }
        }
        return rnd;
    }
    
    public boolean nextBoolean() {
        return this.nextBits(1) != 0;
    }
    
    public double nextDouble() {
        return PlatformRandomKt.doubleFromParts(this.nextBits(26), this.nextBits(27));
    }
    
    public double nextDouble(final double until) {
        return this.nextDouble(0.0, until);
    }
    
    public double nextDouble(final double from, final double until) {
        RandomKt.checkRangeBounds(from, until);
        final double size = until - from;
        double n;
        if (Double.isInfinite(size) && (!Double.isInfinite(from) && !Double.isNaN(from)) && (!Double.isInfinite(until) && !Double.isNaN(until))) {
            final double r1 = this.nextDouble() * (until / 2 - from / 2);
            n = from + r1 + r1;
        }
        else {
            n = from + this.nextDouble() * size;
        }
        final double r2 = n;
        return (r2 >= until) ? Math.nextAfter(until, DoubleCompanionObject.INSTANCE.getNEGATIVE_INFINITY()) : r2;
    }
    
    public float nextFloat() {
        return this.nextBits(24) / (float)16777216;
    }
    
    @NotNull
    public byte[] nextBytes(@NotNull final byte[] array, final int fromIndex, final int toIndex) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "array"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* array */
        //     7: arraylength    
        //     8: iconst_0       
        //     9: iload_2         /* fromIndex */
        //    10: istore          4
        //    12: iload           4
        //    14: if_icmple       21
        //    17: pop            
        //    18: goto            50
        //    21: iload           4
        //    23: if_icmplt       50
        //    26: aload_1         /* array */
        //    27: arraylength    
        //    28: iconst_0       
        //    29: iload_3         /* toIndex */
        //    30: istore          4
        //    32: iload           4
        //    34: if_icmple       41
        //    37: pop            
        //    38: goto            50
        //    41: iload           4
        //    43: if_icmplt       50
        //    46: iconst_1       
        //    47: goto            51
        //    50: iconst_0       
        //    51: istore          4
        //    53: iload           4
        //    55: ifne            119
        //    58: new             Ljava/lang/StringBuilder;
        //    61: dup            
        //    62: invokespecial   java/lang/StringBuilder.<init>:()V
        //    65: ldc             "fromIndex ("
        //    67: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    70: iload_2         /* fromIndex */
        //    71: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    74: ldc             ") or toIndex ("
        //    76: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    79: iload_3         /* toIndex */
        //    80: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    83: ldc             ") are out of range: 0.."
        //    85: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    88: aload_1         /* array */
        //    89: arraylength    
        //    90: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    93: bipush          46
        //    95: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //    98: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   101: astore          6
        //   103: new             Ljava/lang/IllegalArgumentException;
        //   106: dup            
        //   107: aload           6
        //   109: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   112: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //   115: checkcast       Ljava/lang/Throwable;
        //   118: athrow         
        //   119: iload_2         /* fromIndex */
        //   120: iload_3         /* toIndex */
        //   121: if_icmpgt       128
        //   124: iconst_1       
        //   125: goto            129
        //   128: iconst_0       
        //   129: istore          4
        //   131: iload           4
        //   133: ifne            187
        //   136: new             Ljava/lang/StringBuilder;
        //   139: dup            
        //   140: invokespecial   java/lang/StringBuilder.<init>:()V
        //   143: ldc             "fromIndex ("
        //   145: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   148: iload_2         /* fromIndex */
        //   149: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   152: ldc             ") must be not greater than toIndex ("
        //   154: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   157: iload_3         /* toIndex */
        //   158: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   161: ldc             ")."
        //   163: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   166: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   169: astore          6
        //   171: new             Ljava/lang/IllegalArgumentException;
        //   174: dup            
        //   175: aload           6
        //   177: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   180: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //   183: checkcast       Ljava/lang/Throwable;
        //   186: athrow         
        //   187: iload_3         /* toIndex */
        //   188: iload_2         /* fromIndex */
        //   189: isub           
        //   190: iconst_4       
        //   191: idiv           
        //   192: istore          steps
        //   194: iload_2         /* fromIndex */
        //   195: istore          position
        //   197: iconst_0       
        //   198: istore          6
        //   200: iload           steps
        //   202: istore          7
        //   204: iload           6
        //   206: iload           7
        //   208: if_icmpge       278
        //   211: iload           6
        //   213: istore          it
        //   215: aload_0         /* this */
        //   216: invokevirtual   kotlin/random/Random.nextInt:()I
        //   219: istore          v
        //   221: aload_1         /* array */
        //   222: iload           position
        //   224: iload           v
        //   226: i2b            
        //   227: bastore        
        //   228: aload_1         /* array */
        //   229: iload           position
        //   231: iconst_1       
        //   232: iadd           
        //   233: iload           v
        //   235: bipush          8
        //   237: iushr          
        //   238: i2b            
        //   239: bastore        
        //   240: aload_1         /* array */
        //   241: iload           position
        //   243: iconst_2       
        //   244: iadd           
        //   245: iload           v
        //   247: bipush          16
        //   249: iushr          
        //   250: i2b            
        //   251: bastore        
        //   252: aload_1         /* array */
        //   253: iload           position
        //   255: iconst_3       
        //   256: iadd           
        //   257: iload           v
        //   259: bipush          24
        //   261: iushr          
        //   262: i2b            
        //   263: bastore        
        //   264: iload           position
        //   266: iconst_4       
        //   267: iadd           
        //   268: istore          position
        //   270: nop            
        //   271: nop            
        //   272: iinc            6, 1
        //   275: goto            204
        //   278: iload_3         /* toIndex */
        //   279: iload           position
        //   281: isub           
        //   282: istore          remainder
        //   284: aload_0         /* this */
        //   285: iload           remainder
        //   287: bipush          8
        //   289: imul           
        //   290: invokevirtual   kotlin/random/Random.nextBits:(I)I
        //   293: istore          vr
        //   295: iconst_0       
        //   296: istore          8
        //   298: iload           remainder
        //   300: istore          9
        //   302: iload           8
        //   304: iload           9
        //   306: if_icmpge       331
        //   309: aload_1         /* array */
        //   310: iload           position
        //   312: iload           i
        //   314: iadd           
        //   315: iload           vr
        //   317: iload           i
        //   319: bipush          8
        //   321: imul           
        //   322: iushr          
        //   323: i2b            
        //   324: bastore        
        //   325: iinc            i, 1
        //   328: goto            302
        //   331: aload_1         /* array */
        //   332: areturn        
        //    StackMapTable: 00 0C FF 00 15 00 05 07 00 02 07 00 8F 01 01 01 00 01 01 53 01 08 40 01 FB 00 43 08 40 01 39 FE 00 10 01 01 01 FB 00 49 FD 00 17 01 01 1C
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public byte[] nextBytes(@NotNull final byte[] array) {
        Intrinsics.checkParameterIsNotNull(array, "array");
        return this.nextBytes(array, 0, array.length);
    }
    
    @NotNull
    public byte[] nextBytes(final int size) {
        return this.nextBytes(new byte[size]);
    }
    
    static {
        Default = new Default(null);
        defaultRandom = PlatformImplementationsKt.IMPLEMENTATIONS.defaultPlatformRandom();
        Companion = Random.Companion.INSTANCE;
    }
    
    public static final /* synthetic */ Random access$getDefaultRandom$cp() {
        return Random.defaultRandom;
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0016J\b\u0010\n\u001a\u00020\u000bH\u0016J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rH\u0016J \u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\bH\u0016J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\bH\u0016J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013H\u0016J\u0018\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013H\u0016J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\b\u0010\u0018\u001a\u00020\bH\u0016J\u0010\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\bH\u0016J\u0018\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\bH\u0016J\b\u0010\u0019\u001a\u00020\u001aH\u0016J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0014\u001a\u00020\u001aH\u0016J\u0018\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0015\u001a\u00020\u001a2\u0006\u0010\u0014\u001a\u00020\u001aH\u0016R\u0016\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0005\u0010\u0002R\u000e\u0010\u0006\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001b" }, d2 = { "Lkotlin/random/Random$Default;", "Lkotlin/random/Random;", "()V", "Companion", "Lkotlin/random/Random$Companion;", "Companion$annotations", "defaultRandom", "nextBits", "", "bitCount", "nextBoolean", "", "nextBytes", "", "array", "fromIndex", "toIndex", "size", "nextDouble", "", "until", "from", "nextFloat", "", "nextInt", "nextLong", "", "kotlin-stdlib" })
    public static final class Default extends Random
    {
        @Override
        public int nextBits(final int bitCount) {
            return Random.access$getDefaultRandom$cp().nextBits(bitCount);
        }
        
        @Override
        public int nextInt() {
            return Random.access$getDefaultRandom$cp().nextInt();
        }
        
        @Override
        public int nextInt(final int until) {
            return Random.access$getDefaultRandom$cp().nextInt(until);
        }
        
        @Override
        public int nextInt(final int from, final int until) {
            return Random.access$getDefaultRandom$cp().nextInt(from, until);
        }
        
        @Override
        public long nextLong() {
            return Random.access$getDefaultRandom$cp().nextLong();
        }
        
        @Override
        public long nextLong(final long until) {
            return Random.access$getDefaultRandom$cp().nextLong(until);
        }
        
        @Override
        public long nextLong(final long from, final long until) {
            return Random.access$getDefaultRandom$cp().nextLong(from, until);
        }
        
        @Override
        public boolean nextBoolean() {
            return Random.access$getDefaultRandom$cp().nextBoolean();
        }
        
        @Override
        public double nextDouble() {
            return Random.access$getDefaultRandom$cp().nextDouble();
        }
        
        @Override
        public double nextDouble(final double until) {
            return Random.access$getDefaultRandom$cp().nextDouble(until);
        }
        
        @Override
        public double nextDouble(final double from, final double until) {
            return Random.access$getDefaultRandom$cp().nextDouble(from, until);
        }
        
        @Override
        public float nextFloat() {
            return Random.access$getDefaultRandom$cp().nextFloat();
        }
        
        @NotNull
        @Override
        public byte[] nextBytes(@NotNull final byte[] array) {
            Intrinsics.checkParameterIsNotNull(array, "array");
            return Random.access$getDefaultRandom$cp().nextBytes(array);
        }
        
        @NotNull
        @Override
        public byte[] nextBytes(final int size) {
            return Random.access$getDefaultRandom$cp().nextBytes(size);
        }
        
        @NotNull
        @Override
        public byte[] nextBytes(@NotNull final byte[] array, final int fromIndex, final int toIndex) {
            Intrinsics.checkParameterIsNotNull(array, "array");
            return Random.access$getDefaultRandom$cp().nextBytes(array, fromIndex, toIndex);
        }
        
        private Default() {
        }
    }
    
    @Deprecated(message = "Use Default companion object instead", level = DeprecationLevel.HIDDEN)
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0016¨\u0006\u0006" }, d2 = { "Lkotlin/random/Random$Companion;", "Lkotlin/random/Random;", "()V", "nextBits", "", "bitCount", "kotlin-stdlib" })
    public static final class Companion extends Random
    {
        public static final Companion INSTANCE;
        
        @Override
        public int nextBits(final int bitCount) {
            return Random.Default.nextBits(bitCount);
        }
        
        private Companion() {
        }
        
        static {
            INSTANCE = new Companion();
        }
    }
}
