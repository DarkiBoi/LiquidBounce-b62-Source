// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.contracts;

import org.jetbrains.annotations.NotNull;
import kotlin.SinceKotlin;
import kotlin.internal.ContractsDsl;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\bg\u0018\u00002\u00020\u0001J\u0011\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H§\u0004¨\u0006\u0006" }, d2 = { "Lkotlin/contracts/SimpleEffect;", "Lkotlin/contracts/Effect;", "implies", "Lkotlin/contracts/ConditionalEffect;", "booleanExpression", "", "kotlin-stdlib" })
@ContractsDsl
@ExperimentalContracts
@SinceKotlin(version = "1.3")
public interface SimpleEffect extends Effect
{
    @ContractsDsl
    @ExperimentalContracts
    @NotNull
    ConditionalEffect implies(final boolean p0);
}
