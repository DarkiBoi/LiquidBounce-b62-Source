// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines;

import kotlin.TypeCastException;
import kotlin.jvm.functions.Function2;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.SinceKotlin;
import kotlin.Metadata;
import java.io.Serializable;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0001\u0018\u00002\u00020\u00012\u00060\u0002j\u0002`\u0003:\u0001!B\u0015\u0012\u0006\u0010\u0004\u001a\u00020\u0001\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u0010\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\u0000H\u0002J\u0013\u0010\f\u001a\u00020\t2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0096\u0002J5\u0010\u000f\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u00102\u0006\u0010\u0011\u001a\u0002H\u00102\u0018\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H\u00100\u0013H\u0016¢\u0006\u0002\u0010\u0014J(\u0010\u0015\u001a\u0004\u0018\u0001H\u0016\"\b\b\u0000\u0010\u0016*\u00020\u00062\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0018H\u0096\u0002¢\u0006\u0002\u0010\u0019J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\u0014\u0010\u001c\u001a\u00020\u00012\n\u0010\u0017\u001a\u0006\u0012\u0002\b\u00030\u0018H\u0016J\b\u0010\u001d\u001a\u00020\u001bH\u0002J\b\u0010\u001e\u001a\u00020\u001fH\u0016J\b\u0010 \u001a\u00020\u000eH\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\"" }, d2 = { "Lkotlin/coroutines/CombinedContext;", "Lkotlin/coroutines/CoroutineContext;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "left", "element", "Lkotlin/coroutines/CoroutineContext$Element;", "(Lkotlin/coroutines/CoroutineContext;Lkotlin/coroutines/CoroutineContext$Element;)V", "contains", "", "containsAll", "context", "equals", "other", "", "fold", "R", "initial", "operation", "Lkotlin/Function2;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "get", "E", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", "hashCode", "", "minusKey", "size", "toString", "", "writeReplace", "Serialized", "kotlin-stdlib" })
@SinceKotlin(version = "1.3")
public final class CombinedContext implements CoroutineContext, Serializable
{
    private final CoroutineContext left;
    private final Element element;
    
    @Nullable
    @Override
    public <E extends Element> E get(@NotNull final Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        CombinedContext cur = this;
        while (true) {
            final Element value = cur.element.get(key);
            if (value != null) {
                final Element it = value;
                return (E)it;
            }
            final CoroutineContext next = cur.left;
            if (!(next instanceof CombinedContext)) {
                return next.get(key);
            }
            cur = (CombinedContext)next;
        }
    }
    
    @Override
    public <R> R fold(final R initial, @NotNull final Function2<? super R, ? super Element, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        return (R)operation.invoke(this.left.fold(initial, operation), this.element);
    }
    
    @NotNull
    @Override
    public CoroutineContext minusKey(@NotNull final Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        final Element value = this.element.get(key);
        if (value != null) {
            final Element it = value;
            return this.left;
        }
        final CoroutineContext newLeft = this.left.minusKey(key);
        return (CoroutineContext)((newLeft == this.left) ? ((CombinedContext)this) : ((newLeft == EmptyCoroutineContext.INSTANCE) ? ((Element)this.element) : ((CombinedContext)new CombinedContext(newLeft, this.element))));
    }
    
    private final int size() {
        CombinedContext cur = this;
        int size = 2;
        while (true) {
            CoroutineContext left;
            if (!((left = cur.left) instanceof CombinedContext)) {
                left = null;
            }
            final CombinedContext combinedContext = (CombinedContext)left;
            if (combinedContext == null) {
                break;
            }
            cur = combinedContext;
            ++size;
        }
        return size;
    }
    
    private final boolean contains(final Element element) {
        return Intrinsics.areEqual(this.get(element.getKey()), element);
    }
    
    private final boolean containsAll(final CombinedContext context) {
        CombinedContext cur = context;
        while (this.contains(cur.element)) {
            final CoroutineContext next = cur.left;
            if (next instanceof CombinedContext) {
                cur = (CombinedContext)next;
            }
            else {
                final CoroutineContext coroutineContext = next;
                if (coroutineContext == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.CoroutineContext.Element");
                }
                return this.contains((Element)coroutineContext);
            }
        }
        return false;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        return this == other || (other instanceof CombinedContext && ((CombinedContext)other).size() == this.size() && ((CombinedContext)other).containsAll(this));
    }
    
    @Override
    public int hashCode() {
        return this.left.hashCode() + this.element.hashCode();
    }
    
    @NotNull
    @Override
    public String toString() {
        return "[" + this.fold("", (Function2<? super String, ? super Element, ? extends String>)CombinedContext$toString.CombinedContext$toString$1.INSTANCE) + "]";
    }
    
    private final Object writeReplace() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokespecial   kotlin/coroutines/CombinedContext.size:()I
        //     4: istore_1        /* n */
        //     5: iload_1         /* n */
        //     6: anewarray       Lkotlin/coroutines/CoroutineContext;
        //     9: astore_2        /* elements */
        //    10: new             Lkotlin/jvm/internal/Ref$IntRef;
        //    13: dup            
        //    14: invokespecial   kotlin/jvm/internal/Ref$IntRef.<init>:()V
        //    17: astore_3       
        //    18: aload_3        
        //    19: iconst_0       
        //    20: putfield        kotlin/jvm/internal/Ref$IntRef.element:I
        //    23: aload_0         /* this */
        //    24: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //    27: new             Lkotlin/coroutines/CombinedContext$writeReplace$1;
        //    30: dup            
        //    31: aload_2         /* elements */
        //    32: aload_3         /* index */
        //    33: invokespecial   kotlin/coroutines/CombinedContext$writeReplace$1.<init>:([Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/internal/Ref$IntRef;)V
        //    36: checkcast       Lkotlin/jvm/functions/Function2;
        //    39: invokevirtual   kotlin/coroutines/CombinedContext.fold:(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;
        //    42: pop            
        //    43: aload_3         /* index */
        //    44: getfield        kotlin/jvm/internal/Ref$IntRef.element:I
        //    47: iload_1         /* n */
        //    48: if_icmpne       55
        //    51: iconst_1       
        //    52: goto            56
        //    55: iconst_0       
        //    56: istore          4
        //    58: iload           4
        //    60: ifne            83
        //    63: ldc             "Check failed."
        //    65: astore          5
        //    67: new             Ljava/lang/IllegalStateException;
        //    70: dup            
        //    71: aload           5
        //    73: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //    76: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //    79: checkcast       Ljava/lang/Throwable;
        //    82: athrow         
        //    83: new             Lkotlin/coroutines/CombinedContext$Serialized;
        //    86: dup            
        //    87: aload_2         /* elements */
        //    88: invokespecial   kotlin/coroutines/CombinedContext$Serialized.<init>:([Lkotlin/coroutines/CoroutineContext;)V
        //    91: areturn        
        //    StackMapTable: 00 03 FE 00 37 01 07 00 B0 07 00 A3 40 01 FC 00 1A 01
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public CombinedContext(@NotNull final CoroutineContext left, @NotNull final Element element) {
        Intrinsics.checkParameterIsNotNull(left, "left");
        Intrinsics.checkParameterIsNotNull(element, "element");
        this.left = left;
        this.element = element;
    }
    
    @NotNull
    @Override
    public CoroutineContext plus(@NotNull final CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return DefaultImpls.plus(this, context);
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0002\u0018\u0000 \f2\u00060\u0001j\u0002`\u0002:\u0001\fB\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\u0002\u0010\u0006J\b\u0010\n\u001a\u00020\u000bH\u0002R\u0019\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\n\n\u0002\u0010\t\u001a\u0004\b\u0007\u0010\b¨\u0006\r" }, d2 = { "Lkotlin/coroutines/CombinedContext$Serialized;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "elements", "", "Lkotlin/coroutines/CoroutineContext;", "([Lkotlin/coroutines/CoroutineContext;)V", "getElements", "()[Lkotlin/coroutines/CoroutineContext;", "[Lkotlin/coroutines/CoroutineContext;", "readResolve", "", "Companion", "kotlin-stdlib" })
    private static final class Serialized implements Serializable
    {
        @NotNull
        private final CoroutineContext[] elements;
        private static final long serialVersionUID = 0L;
        public static final Companion Companion;
        
        private final Object readResolve() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        kotlin/coroutines/CombinedContext$Serialized.elements:[Lkotlin/coroutines/CoroutineContext;
            //     4: astore_1       
            //     5: getstatic       kotlin/coroutines/EmptyCoroutineContext.INSTANCE:Lkotlin/coroutines/EmptyCoroutineContext;
            //     8: astore_2        /* initial$iv */
            //     9: aload_2         /* initial$iv */
            //    10: astore_3        /* accumulator$iv */
            //    11: aload_1         /* $receiver$iv */
            //    12: astore          4
            //    14: aload           4
            //    16: arraylength    
            //    17: istore          5
            //    19: iconst_0       
            //    20: istore          6
            //    22: iload           6
            //    24: iload           5
            //    26: if_icmpge       62
            //    29: aload           4
            //    31: iload           6
            //    33: aaload         
            //    34: astore          element$iv
            //    36: aload_3         /* accumulator$iv */
            //    37: aload           element$iv
            //    39: astore          8
            //    41: checkcast       Lkotlin/coroutines/CoroutineContext;
            //    44: astore          p1
            //    46: aload           p1
            //    48: aload           p2
            //    50: invokeinterface kotlin/coroutines/CoroutineContext.plus:(Lkotlin/coroutines/CoroutineContext;)Lkotlin/coroutines/CoroutineContext;
            //    55: astore_3        /* accumulator$iv */
            //    56: iinc            6, 1
            //    59: goto            22
            //    62: aload_3         /* accumulator$iv */
            //    63: areturn        
            //    StackMapTable: 00 02 FF 00 16 00 07 07 00 02 07 00 34 07 00 2F 07 00 04 07 00 34 01 01 00 00 27
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
            //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @NotNull
        public final CoroutineContext[] getElements() {
            return this.elements;
        }
        
        public Serialized(@NotNull final CoroutineContext[] elements) {
            Intrinsics.checkParameterIsNotNull(elements, "elements");
            this.elements = elements;
        }
        
        static {
            Companion = new Companion(null);
        }
        
        @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0005" }, d2 = { "Lkotlin/coroutines/CombinedContext$Serialized$Companion;", "", "()V", "serialVersionUID", "", "kotlin-stdlib" })
        public static final class Companion
        {
            private Companion() {
            }
        }
    }
}
