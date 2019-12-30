// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.ranges;

import org.jetbrains.annotations.Nullable;
import kotlin.Deprecated;
import kotlin.jvm.JvmName;
import java.util.NoSuchElementException;
import kotlin.random.RandomKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.internal.InlineOnly;
import kotlin.SinceKotlin;
import kotlin.random.Random;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000n\n\u0002\b\u0002\n\u0002\u0010\u000f\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\u0010\n\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0016\u001a'\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\u0006\u0010\u0003\u001a\u0002H\u0001¢\u0006\u0002\u0010\u0004\u001a\u0012\u0010\u0000\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005\u001a\u0012\u0010\u0000\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0006\u001a\u0012\u0010\u0000\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0007\u001a\u0012\u0010\u0000\u001a\u00020\b*\u00020\b2\u0006\u0010\u0003\u001a\u00020\b\u001a\u0012\u0010\u0000\u001a\u00020\t*\u00020\t2\u0006\u0010\u0003\u001a\u00020\t\u001a\u0012\u0010\u0000\u001a\u00020\n*\u00020\n2\u0006\u0010\u0003\u001a\u00020\n\u001a'\u0010\u000b\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\u0006\u0010\f\u001a\u0002H\u0001¢\u0006\u0002\u0010\u0004\u001a\u0012\u0010\u000b\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\f\u001a\u00020\u0005\u001a\u0012\u0010\u000b\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\f\u001a\u00020\u0006\u001a\u0012\u0010\u000b\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\f\u001a\u00020\u0007\u001a\u0012\u0010\u000b\u001a\u00020\b*\u00020\b2\u0006\u0010\f\u001a\u00020\b\u001a\u0012\u0010\u000b\u001a\u00020\t*\u00020\t2\u0006\u0010\f\u001a\u00020\t\u001a\u0012\u0010\u000b\u001a\u00020\n*\u00020\n2\u0006\u0010\f\u001a\u00020\n\u001a3\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\b\u0010\u0003\u001a\u0004\u0018\u0001H\u00012\b\u0010\f\u001a\u0004\u0018\u0001H\u0001¢\u0006\u0002\u0010\u000e\u001a/\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0010H\u0007¢\u0006\u0002\u0010\u0011\u001a-\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0012¢\u0006\u0002\u0010\u0013\u001a\u001a\u0010\r\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u0005\u001a\u001a\u0010\r\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u0006\u001a\u001a\u0010\r\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0007\u001a\u001a\u0010\r\u001a\u00020\b*\u00020\b2\u0006\u0010\u0003\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\b\u001a\u0018\u0010\r\u001a\u00020\b*\u00020\b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\b0\u0012\u001a\u001a\u0010\r\u001a\u00020\t*\u00020\t2\u0006\u0010\u0003\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\t\u001a\u0018\u0010\r\u001a\u00020\t*\u00020\t2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\t0\u0012\u001a\u001a\u0010\r\u001a\u00020\n*\u00020\n2\u0006\u0010\u0003\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\n\u001a\u001c\u0010\u0014\u001a\u00020\u0015*\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0087\n¢\u0006\u0002\u0010\u0019\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b \u001a\u001c\u0010\u0014\u001a\u00020\u0015*\u00020!2\b\u0010\u0017\u001a\u0004\u0018\u00010\bH\u0087\n¢\u0006\u0002\u0010\"\u001a\u001c\u0010\u0014\u001a\u00020\u0015*\u00020#2\b\u0010\u0017\u001a\u0004\u0018\u00010\tH\u0087\n¢\u0006\u0002\u0010$\u001a\u0015\u0010%\u001a\u00020&*\u00020\u00052\u0006\u0010'\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010%\u001a\u00020&*\u00020\u00052\u0006\u0010'\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020(*\u00020\u00052\u0006\u0010'\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020&*\u00020\u00052\u0006\u0010'\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020)*\u00020\u00182\u0006\u0010'\u001a\u00020\u0018H\u0086\u0004\u001a\u0015\u0010%\u001a\u00020&*\u00020\b2\u0006\u0010'\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010%\u001a\u00020&*\u00020\b2\u0006\u0010'\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020(*\u00020\b2\u0006\u0010'\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020&*\u00020\b2\u0006\u0010'\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020(*\u00020\t2\u0006\u0010'\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010%\u001a\u00020(*\u00020\t2\u0006\u0010'\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020(*\u00020\t2\u0006\u0010'\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020(*\u00020\t2\u0006\u0010'\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020&*\u00020\n2\u0006\u0010'\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010%\u001a\u00020&*\u00020\n2\u0006\u0010'\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020(*\u00020\n2\u0006\u0010'\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020&*\u00020\n2\u0006\u0010'\u001a\u00020\nH\u0086\u0004\u001a\r\u0010*\u001a\u00020\u0018*\u00020\u0016H\u0087\b\u001a\u0014\u0010*\u001a\u00020\u0018*\u00020\u00162\u0006\u0010*\u001a\u00020+H\u0007\u001a\r\u0010*\u001a\u00020\b*\u00020!H\u0087\b\u001a\u0014\u0010*\u001a\u00020\b*\u00020!2\u0006\u0010*\u001a\u00020+H\u0007\u001a\r\u0010*\u001a\u00020\t*\u00020#H\u0087\b\u001a\u0014\u0010*\u001a\u00020\t*\u00020#2\u0006\u0010*\u001a\u00020+H\u0007\u001a\n\u0010,\u001a\u00020)*\u00020)\u001a\n\u0010,\u001a\u00020&*\u00020&\u001a\n\u0010,\u001a\u00020(*\u00020(\u001a\u0015\u0010-\u001a\u00020)*\u00020)2\u0006\u0010-\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010-\u001a\u00020&*\u00020&2\u0006\u0010-\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010-\u001a\u00020(*\u00020(2\u0006\u0010-\u001a\u00020\tH\u0086\u0004\u001a\u0013\u0010.\u001a\u0004\u0018\u00010\u0005*\u00020\u0006H\u0000¢\u0006\u0002\u0010/\u001a\u0013\u0010.\u001a\u0004\u0018\u00010\u0005*\u00020\u0007H\u0000¢\u0006\u0002\u00100\u001a\u0013\u0010.\u001a\u0004\u0018\u00010\u0005*\u00020\bH\u0000¢\u0006\u0002\u00101\u001a\u0013\u0010.\u001a\u0004\u0018\u00010\u0005*\u00020\tH\u0000¢\u0006\u0002\u00102\u001a\u0013\u0010.\u001a\u0004\u0018\u00010\u0005*\u00020\nH\u0000¢\u0006\u0002\u00103\u001a\u0013\u00104\u001a\u0004\u0018\u00010\b*\u00020\u0006H\u0000¢\u0006\u0002\u00105\u001a\u0013\u00104\u001a\u0004\u0018\u00010\b*\u00020\u0007H\u0000¢\u0006\u0002\u00106\u001a\u0013\u00104\u001a\u0004\u0018\u00010\b*\u00020\tH\u0000¢\u0006\u0002\u00107\u001a\u0013\u00108\u001a\u0004\u0018\u00010\t*\u00020\u0006H\u0000¢\u0006\u0002\u00109\u001a\u0013\u00108\u001a\u0004\u0018\u00010\t*\u00020\u0007H\u0000¢\u0006\u0002\u0010:\u001a\u0013\u0010;\u001a\u0004\u0018\u00010\n*\u00020\u0006H\u0000¢\u0006\u0002\u0010<\u001a\u0013\u0010;\u001a\u0004\u0018\u00010\n*\u00020\u0007H\u0000¢\u0006\u0002\u0010=\u001a\u0013\u0010;\u001a\u0004\u0018\u00010\n*\u00020\bH\u0000¢\u0006\u0002\u0010>\u001a\u0013\u0010;\u001a\u0004\u0018\u00010\n*\u00020\tH\u0000¢\u0006\u0002\u0010?\u001a\u0015\u0010@\u001a\u00020!*\u00020\u00052\u0006\u0010'\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010@\u001a\u00020!*\u00020\u00052\u0006\u0010'\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010@\u001a\u00020#*\u00020\u00052\u0006\u0010'\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010@\u001a\u00020!*\u00020\u00052\u0006\u0010'\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010@\u001a\u00020\u0016*\u00020\u00182\u0006\u0010'\u001a\u00020\u0018H\u0086\u0004\u001a\u0015\u0010@\u001a\u00020!*\u00020\b2\u0006\u0010'\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010@\u001a\u00020!*\u00020\b2\u0006\u0010'\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010@\u001a\u00020#*\u00020\b2\u0006\u0010'\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010@\u001a\u00020!*\u00020\b2\u0006\u0010'\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010@\u001a\u00020#*\u00020\t2\u0006\u0010'\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010@\u001a\u00020#*\u00020\t2\u0006\u0010'\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010@\u001a\u00020#*\u00020\t2\u0006\u0010'\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010@\u001a\u00020#*\u00020\t2\u0006\u0010'\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010@\u001a\u00020!*\u00020\n2\u0006\u0010'\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010@\u001a\u00020!*\u00020\n2\u0006\u0010'\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010@\u001a\u00020#*\u00020\n2\u0006\u0010'\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010@\u001a\u00020!*\u00020\n2\u0006\u0010'\u001a\u00020\nH\u0086\u0004¨\u0006A" }, d2 = { "coerceAtLeast", "T", "", "minimumValue", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "", "", "", "", "", "", "coerceAtMost", "maximumValue", "coerceIn", "(Ljava/lang/Comparable;Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "range", "Lkotlin/ranges/ClosedFloatingPointRange;", "(Ljava/lang/Comparable;Lkotlin/ranges/ClosedFloatingPointRange;)Ljava/lang/Comparable;", "Lkotlin/ranges/ClosedRange;", "(Ljava/lang/Comparable;Lkotlin/ranges/ClosedRange;)Ljava/lang/Comparable;", "contains", "", "Lkotlin/ranges/CharRange;", "element", "", "(Lkotlin/ranges/CharRange;Ljava/lang/Character;)Z", "value", "byteRangeContains", "doubleRangeContains", "floatRangeContains", "intRangeContains", "longRangeContains", "shortRangeContains", "Lkotlin/ranges/IntRange;", "(Lkotlin/ranges/IntRange;Ljava/lang/Integer;)Z", "Lkotlin/ranges/LongRange;", "(Lkotlin/ranges/LongRange;Ljava/lang/Long;)Z", "downTo", "Lkotlin/ranges/IntProgression;", "to", "Lkotlin/ranges/LongProgression;", "Lkotlin/ranges/CharProgression;", "random", "Lkotlin/random/Random;", "reversed", "step", "toByteExactOrNull", "(D)Ljava/lang/Byte;", "(F)Ljava/lang/Byte;", "(I)Ljava/lang/Byte;", "(J)Ljava/lang/Byte;", "(S)Ljava/lang/Byte;", "toIntExactOrNull", "(D)Ljava/lang/Integer;", "(F)Ljava/lang/Integer;", "(J)Ljava/lang/Integer;", "toLongExactOrNull", "(D)Ljava/lang/Long;", "(F)Ljava/lang/Long;", "toShortExactOrNull", "(D)Ljava/lang/Short;", "(F)Ljava/lang/Short;", "(I)Ljava/lang/Short;", "(J)Ljava/lang/Short;", "until", "kotlin-stdlib" }, xs = "kotlin/ranges/RangesKt")
class RangesKt___RangesKt extends RangesKt__RangesKt
{
    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final int random(@NotNull final IntRange $receiver) {
        return random($receiver, Random.Default);
    }
    
    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final long random(@NotNull final LongRange $receiver) {
        return random($receiver, Random.Default);
    }
    
    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final char random(@NotNull final CharRange $receiver) {
        return random($receiver, Random.Default);
    }
    
    @SinceKotlin(version = "1.3")
    public static final int random(@NotNull final IntRange $receiver, @NotNull final Random random) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(random, "random");
        try {
            return RandomKt.nextInt(random, $receiver);
        }
        catch (IllegalArgumentException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }
    
    @SinceKotlin(version = "1.3")
    public static final long random(@NotNull final LongRange $receiver, @NotNull final Random random) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(random, "random");
        try {
            return RandomKt.nextLong(random, $receiver);
        }
        catch (IllegalArgumentException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }
    
    @SinceKotlin(version = "1.3")
    public static final char random(@NotNull final CharRange $receiver, @NotNull final Random random) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(random, "random");
        try {
            return (char)random.nextInt($receiver.getFirst(), $receiver.getLast() + '\u0001');
        }
        catch (IllegalArgumentException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }
    
    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final boolean contains(@NotNull final IntRange $receiver, final Integer element) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return element != null && $receiver.contains((int)element);
    }
    
    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final boolean contains(@NotNull final LongRange $receiver, final Long element) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return element != null && $receiver.contains((long)element);
    }
    
    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final boolean contains(@NotNull final CharRange $receiver, final Character element) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return element != null && $receiver.contains((char)element);
    }
    
    @JvmName(name = "intRangeContains")
    public static final boolean intRangeContains(@NotNull final ClosedRange<Integer> $receiver, final byte value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return $receiver.contains((int)value);
    }
    
    @JvmName(name = "longRangeContains")
    public static final boolean longRangeContains(@NotNull final ClosedRange<Long> $receiver, final byte value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return $receiver.contains((long)value);
    }
    
    @JvmName(name = "shortRangeContains")
    public static final boolean shortRangeContains(@NotNull final ClosedRange<Short> $receiver, final byte value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return $receiver.contains((short)value);
    }
    
    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @JvmName(name = "doubleRangeContains")
    @java.lang.Deprecated
    public static final boolean doubleRangeContains(@NotNull final ClosedRange<Double> $receiver, final byte value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return $receiver.contains((double)value);
    }
    
    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @JvmName(name = "floatRangeContains")
    @java.lang.Deprecated
    public static final boolean floatRangeContains(@NotNull final ClosedRange<Float> $receiver, final byte value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return $receiver.contains((float)value);
    }
    
    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @JvmName(name = "intRangeContains")
    @java.lang.Deprecated
    public static final boolean intRangeContains(@NotNull final ClosedRange<Integer> $receiver, final double value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Integer it = toIntExactOrNull(value);
        return it != null && $receiver.contains(it);
    }
    
    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @JvmName(name = "longRangeContains")
    @java.lang.Deprecated
    public static final boolean longRangeContains(@NotNull final ClosedRange<Long> $receiver, final double value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Long it = toLongExactOrNull(value);
        return it != null && $receiver.contains(it);
    }
    
    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @JvmName(name = "byteRangeContains")
    @java.lang.Deprecated
    public static final boolean byteRangeContains(@NotNull final ClosedRange<Byte> $receiver, final double value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Byte it = toByteExactOrNull(value);
        return it != null && $receiver.contains(it);
    }
    
    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @JvmName(name = "shortRangeContains")
    @java.lang.Deprecated
    public static final boolean shortRangeContains(@NotNull final ClosedRange<Short> $receiver, final double value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Short it = toShortExactOrNull(value);
        return it != null && $receiver.contains(it);
    }
    
    @JvmName(name = "floatRangeContains")
    public static final boolean floatRangeContains(@NotNull final ClosedRange<Float> $receiver, final double value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return $receiver.contains((float)value);
    }
    
    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @JvmName(name = "intRangeContains")
    @java.lang.Deprecated
    public static final boolean intRangeContains(@NotNull final ClosedRange<Integer> $receiver, final float value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Integer it = toIntExactOrNull(value);
        return it != null && $receiver.contains(it);
    }
    
    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @JvmName(name = "longRangeContains")
    @java.lang.Deprecated
    public static final boolean longRangeContains(@NotNull final ClosedRange<Long> $receiver, final float value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Long it = toLongExactOrNull(value);
        return it != null && $receiver.contains(it);
    }
    
    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @JvmName(name = "byteRangeContains")
    @java.lang.Deprecated
    public static final boolean byteRangeContains(@NotNull final ClosedRange<Byte> $receiver, final float value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Byte it = toByteExactOrNull(value);
        return it != null && $receiver.contains(it);
    }
    
    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @JvmName(name = "shortRangeContains")
    @java.lang.Deprecated
    public static final boolean shortRangeContains(@NotNull final ClosedRange<Short> $receiver, final float value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Short it = toShortExactOrNull(value);
        return it != null && $receiver.contains(it);
    }
    
    @JvmName(name = "doubleRangeContains")
    public static final boolean doubleRangeContains(@NotNull final ClosedRange<Double> $receiver, final float value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return $receiver.contains((double)value);
    }
    
    @JvmName(name = "longRangeContains")
    public static final boolean longRangeContains(@NotNull final ClosedRange<Long> $receiver, final int value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return $receiver.contains((long)value);
    }
    
    @JvmName(name = "byteRangeContains")
    public static final boolean byteRangeContains(@NotNull final ClosedRange<Byte> $receiver, final int value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Byte it = toByteExactOrNull(value);
        return it != null && $receiver.contains(it);
    }
    
    @JvmName(name = "shortRangeContains")
    public static final boolean shortRangeContains(@NotNull final ClosedRange<Short> $receiver, final int value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Short it = toShortExactOrNull(value);
        return it != null && $receiver.contains(it);
    }
    
    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @JvmName(name = "doubleRangeContains")
    @java.lang.Deprecated
    public static final boolean doubleRangeContains(@NotNull final ClosedRange<Double> $receiver, final int value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return $receiver.contains((double)value);
    }
    
    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @JvmName(name = "floatRangeContains")
    @java.lang.Deprecated
    public static final boolean floatRangeContains(@NotNull final ClosedRange<Float> $receiver, final int value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return $receiver.contains(Float.valueOf(value));
    }
    
    @JvmName(name = "intRangeContains")
    public static final boolean intRangeContains(@NotNull final ClosedRange<Integer> $receiver, final long value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Integer it = toIntExactOrNull(value);
        return it != null && $receiver.contains(it);
    }
    
    @JvmName(name = "byteRangeContains")
    public static final boolean byteRangeContains(@NotNull final ClosedRange<Byte> $receiver, final long value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Byte it = toByteExactOrNull(value);
        return it != null && $receiver.contains(it);
    }
    
    @JvmName(name = "shortRangeContains")
    public static final boolean shortRangeContains(@NotNull final ClosedRange<Short> $receiver, final long value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Short it = toShortExactOrNull(value);
        return it != null && $receiver.contains(it);
    }
    
    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @JvmName(name = "doubleRangeContains")
    @java.lang.Deprecated
    public static final boolean doubleRangeContains(@NotNull final ClosedRange<Double> $receiver, final long value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return $receiver.contains(Double.valueOf(value));
    }
    
    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @JvmName(name = "floatRangeContains")
    @java.lang.Deprecated
    public static final boolean floatRangeContains(@NotNull final ClosedRange<Float> $receiver, final long value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return $receiver.contains(Float.valueOf(value));
    }
    
    @JvmName(name = "intRangeContains")
    public static final boolean intRangeContains(@NotNull final ClosedRange<Integer> $receiver, final short value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return $receiver.contains((int)value);
    }
    
    @JvmName(name = "longRangeContains")
    public static final boolean longRangeContains(@NotNull final ClosedRange<Long> $receiver, final short value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return $receiver.contains((long)value);
    }
    
    @JvmName(name = "byteRangeContains")
    public static final boolean byteRangeContains(@NotNull final ClosedRange<Byte> $receiver, final short value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Byte it = toByteExactOrNull(value);
        return it != null && $receiver.contains(it);
    }
    
    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @JvmName(name = "doubleRangeContains")
    @java.lang.Deprecated
    public static final boolean doubleRangeContains(@NotNull final ClosedRange<Double> $receiver, final short value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return $receiver.contains((double)value);
    }
    
    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @JvmName(name = "floatRangeContains")
    @java.lang.Deprecated
    public static final boolean floatRangeContains(@NotNull final ClosedRange<Float> $receiver, final short value) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return $receiver.contains((float)value);
    }
    
    @NotNull
    public static final IntProgression downTo(final int $receiver, final byte to) {
        return IntProgression.Companion.fromClosedRange($receiver, to, -1);
    }
    
    @NotNull
    public static final LongProgression downTo(final long $receiver, final byte to) {
        return LongProgression.Companion.fromClosedRange($receiver, to, -1L);
    }
    
    @NotNull
    public static final IntProgression downTo(final byte $receiver, final byte to) {
        return IntProgression.Companion.fromClosedRange($receiver, to, -1);
    }
    
    @NotNull
    public static final IntProgression downTo(final short $receiver, final byte to) {
        return IntProgression.Companion.fromClosedRange($receiver, to, -1);
    }
    
    @NotNull
    public static final CharProgression downTo(final char $receiver, final char to) {
        return CharProgression.Companion.fromClosedRange($receiver, to, -1);
    }
    
    @NotNull
    public static final IntProgression downTo(final int $receiver, final int to) {
        return IntProgression.Companion.fromClosedRange($receiver, to, -1);
    }
    
    @NotNull
    public static final LongProgression downTo(final long $receiver, final int to) {
        return LongProgression.Companion.fromClosedRange($receiver, to, -1L);
    }
    
    @NotNull
    public static final IntProgression downTo(final byte $receiver, final int to) {
        return IntProgression.Companion.fromClosedRange($receiver, to, -1);
    }
    
    @NotNull
    public static final IntProgression downTo(final short $receiver, final int to) {
        return IntProgression.Companion.fromClosedRange($receiver, to, -1);
    }
    
    @NotNull
    public static final LongProgression downTo(final int $receiver, final long to) {
        return LongProgression.Companion.fromClosedRange($receiver, to, -1L);
    }
    
    @NotNull
    public static final LongProgression downTo(final long $receiver, final long to) {
        return LongProgression.Companion.fromClosedRange($receiver, to, -1L);
    }
    
    @NotNull
    public static final LongProgression downTo(final byte $receiver, final long to) {
        return LongProgression.Companion.fromClosedRange($receiver, to, -1L);
    }
    
    @NotNull
    public static final LongProgression downTo(final short $receiver, final long to) {
        return LongProgression.Companion.fromClosedRange($receiver, to, -1L);
    }
    
    @NotNull
    public static final IntProgression downTo(final int $receiver, final short to) {
        return IntProgression.Companion.fromClosedRange($receiver, to, -1);
    }
    
    @NotNull
    public static final LongProgression downTo(final long $receiver, final short to) {
        return LongProgression.Companion.fromClosedRange($receiver, to, -1L);
    }
    
    @NotNull
    public static final IntProgression downTo(final byte $receiver, final short to) {
        return IntProgression.Companion.fromClosedRange($receiver, to, -1);
    }
    
    @NotNull
    public static final IntProgression downTo(final short $receiver, final short to) {
        return IntProgression.Companion.fromClosedRange($receiver, to, -1);
    }
    
    @NotNull
    public static final IntProgression reversed(@NotNull final IntProgression $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return IntProgression.Companion.fromClosedRange($receiver.getLast(), $receiver.getFirst(), -$receiver.getStep());
    }
    
    @NotNull
    public static final LongProgression reversed(@NotNull final LongProgression $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return LongProgression.Companion.fromClosedRange($receiver.getLast(), $receiver.getFirst(), -$receiver.getStep());
    }
    
    @NotNull
    public static final CharProgression reversed(@NotNull final CharProgression $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return CharProgression.Companion.fromClosedRange($receiver.getLast(), $receiver.getFirst(), -$receiver.getStep());
    }
    
    @NotNull
    public static final IntProgression step(@NotNull final IntProgression $receiver, final int step) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        RangesKt__RangesKt.checkStepIsPositive(step > 0, step);
        return IntProgression.Companion.fromClosedRange($receiver.getFirst(), $receiver.getLast(), ($receiver.getStep() > 0) ? step : (-step));
    }
    
    @NotNull
    public static final LongProgression step(@NotNull final LongProgression $receiver, final long step) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        RangesKt__RangesKt.checkStepIsPositive(step > 0L, step);
        return LongProgression.Companion.fromClosedRange($receiver.getFirst(), $receiver.getLast(), ($receiver.getStep() > 0L) ? step : (-step));
    }
    
    @NotNull
    public static final CharProgression step(@NotNull final CharProgression $receiver, final int step) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        RangesKt__RangesKt.checkStepIsPositive(step > 0, step);
        return CharProgression.Companion.fromClosedRange($receiver.getFirst(), $receiver.getLast(), ($receiver.getStep() > 0) ? step : (-step));
    }
    
    @Nullable
    public static final Byte toByteExactOrNull(final int $receiver) {
        final int n = 127;
        if (-128 <= $receiver) {
            if (n >= $receiver) {
                return (byte)$receiver;
            }
        }
        return null;
    }
    
    @Nullable
    public static final Byte toByteExactOrNull(final long $receiver) {
        final long n = -128;
        final long n2 = 127;
        if (n <= $receiver) {
            if (n2 >= $receiver) {
                return (byte)$receiver;
            }
        }
        return null;
    }
    
    @Nullable
    public static final Byte toByteExactOrNull(final short $receiver) {
        final short n = -128;
        final short n2 = 127;
        if (n <= $receiver) {
            if (n2 >= $receiver) {
                return (byte)$receiver;
            }
        }
        return null;
    }
    
    @Nullable
    public static final Byte toByteExactOrNull(final double $receiver) {
        final double n = -128;
        final double n2 = 127;
        return ($receiver >= n && $receiver <= n2) ? Byte.valueOf((byte)$receiver) : null;
    }
    
    @Nullable
    public static final Byte toByteExactOrNull(final float $receiver) {
        final float n = -128;
        final float n2 = 127;
        return ($receiver >= n && $receiver <= n2) ? Byte.valueOf((byte)$receiver) : null;
    }
    
    @Nullable
    public static final Integer toIntExactOrNull(final long $receiver) {
        final long n = Integer.MIN_VALUE;
        final long n2 = Integer.MAX_VALUE;
        if (n <= $receiver) {
            if (n2 >= $receiver) {
                return (int)$receiver;
            }
        }
        return null;
    }
    
    @Nullable
    public static final Integer toIntExactOrNull(final double $receiver) {
        final double n = Integer.MIN_VALUE;
        final double n2 = Integer.MAX_VALUE;
        return ($receiver >= n && $receiver <= n2) ? Integer.valueOf((int)$receiver) : null;
    }
    
    @Nullable
    public static final Integer toIntExactOrNull(final float $receiver) {
        final float n = Integer.MIN_VALUE;
        final float n2 = Integer.MAX_VALUE;
        return ($receiver >= n && $receiver <= n2) ? Integer.valueOf((int)$receiver) : null;
    }
    
    @Nullable
    public static final Long toLongExactOrNull(final double $receiver) {
        final double n = Long.MIN_VALUE;
        final double n2 = Long.MAX_VALUE;
        return ($receiver >= n && $receiver <= n2) ? Long.valueOf((long)$receiver) : null;
    }
    
    @Nullable
    public static final Long toLongExactOrNull(final float $receiver) {
        final float n = Long.MIN_VALUE;
        final float n2 = Long.MAX_VALUE;
        return ($receiver >= n && $receiver <= n2) ? Long.valueOf((long)$receiver) : null;
    }
    
    @Nullable
    public static final Short toShortExactOrNull(final int $receiver) {
        final int n = 32767;
        if (-32768 <= $receiver) {
            if (n >= $receiver) {
                return (short)$receiver;
            }
        }
        return null;
    }
    
    @Nullable
    public static final Short toShortExactOrNull(final long $receiver) {
        final long n = -32768;
        final long n2 = 32767;
        if (n <= $receiver) {
            if (n2 >= $receiver) {
                return (short)$receiver;
            }
        }
        return null;
    }
    
    @Nullable
    public static final Short toShortExactOrNull(final double $receiver) {
        final double n = -32768;
        final double n2 = 32767;
        return ($receiver >= n && $receiver <= n2) ? Short.valueOf((short)$receiver) : null;
    }
    
    @Nullable
    public static final Short toShortExactOrNull(final float $receiver) {
        final float n = -32768;
        final float n2 = 32767;
        return ($receiver >= n && $receiver <= n2) ? Short.valueOf((short)$receiver) : null;
    }
    
    @NotNull
    public static final IntRange until(final int $receiver, final byte to) {
        return new IntRange($receiver, to - 1);
    }
    
    @NotNull
    public static final LongRange until(final long $receiver, final byte to) {
        return new LongRange($receiver, to - 1L);
    }
    
    @NotNull
    public static final IntRange until(final byte $receiver, final byte to) {
        return new IntRange($receiver, to - 1);
    }
    
    @NotNull
    public static final IntRange until(final short $receiver, final byte to) {
        return new IntRange($receiver, to - 1);
    }
    
    @NotNull
    public static final CharRange until(final char $receiver, final char to) {
        if (to <= '\0') {
            return CharRange.Companion.getEMPTY();
        }
        return new CharRange($receiver, (char)(to - '\u0001'));
    }
    
    @NotNull
    public static final IntRange until(final int $receiver, final int to) {
        if (to <= Integer.MIN_VALUE) {
            return IntRange.Companion.getEMPTY();
        }
        return new IntRange($receiver, to - 1);
    }
    
    @NotNull
    public static final LongRange until(final long $receiver, final int to) {
        return new LongRange($receiver, to - 1L);
    }
    
    @NotNull
    public static final IntRange until(final byte $receiver, final int to) {
        if (to <= Integer.MIN_VALUE) {
            return IntRange.Companion.getEMPTY();
        }
        return new IntRange($receiver, to - 1);
    }
    
    @NotNull
    public static final IntRange until(final short $receiver, final int to) {
        if (to <= Integer.MIN_VALUE) {
            return IntRange.Companion.getEMPTY();
        }
        return new IntRange($receiver, to - 1);
    }
    
    @NotNull
    public static final LongRange until(final int $receiver, final long to) {
        if (to <= Long.MIN_VALUE) {
            return LongRange.Companion.getEMPTY();
        }
        return new LongRange($receiver, to - 1L);
    }
    
    @NotNull
    public static final LongRange until(final long $receiver, final long to) {
        if (to <= Long.MIN_VALUE) {
            return LongRange.Companion.getEMPTY();
        }
        return new LongRange($receiver, to - 1L);
    }
    
    @NotNull
    public static final LongRange until(final byte $receiver, final long to) {
        if (to <= Long.MIN_VALUE) {
            return LongRange.Companion.getEMPTY();
        }
        return new LongRange($receiver, to - 1L);
    }
    
    @NotNull
    public static final LongRange until(final short $receiver, final long to) {
        if (to <= Long.MIN_VALUE) {
            return LongRange.Companion.getEMPTY();
        }
        return new LongRange($receiver, to - 1L);
    }
    
    @NotNull
    public static final IntRange until(final int $receiver, final short to) {
        return new IntRange($receiver, to - 1);
    }
    
    @NotNull
    public static final LongRange until(final long $receiver, final short to) {
        return new LongRange($receiver, to - 1L);
    }
    
    @NotNull
    public static final IntRange until(final byte $receiver, final short to) {
        return new IntRange($receiver, to - 1);
    }
    
    @NotNull
    public static final IntRange until(final short $receiver, final short to) {
        return new IntRange($receiver, to - 1);
    }
    
    @NotNull
    public static final <T extends Comparable<? super T>> T coerceAtLeast(@NotNull final T $receiver, @NotNull final T minimumValue) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(minimumValue, "minimumValue");
        return ($receiver.compareTo((Object)minimumValue) < 0) ? minimumValue : $receiver;
    }
    
    public static final byte coerceAtLeast(final byte $receiver, final byte minimumValue) {
        return ($receiver < minimumValue) ? minimumValue : $receiver;
    }
    
    public static final short coerceAtLeast(final short $receiver, final short minimumValue) {
        return ($receiver < minimumValue) ? minimumValue : $receiver;
    }
    
    public static final int coerceAtLeast(final int $receiver, final int minimumValue) {
        return ($receiver < minimumValue) ? minimumValue : $receiver;
    }
    
    public static final long coerceAtLeast(final long $receiver, final long minimumValue) {
        return ($receiver < minimumValue) ? minimumValue : $receiver;
    }
    
    public static final float coerceAtLeast(final float $receiver, final float minimumValue) {
        return ($receiver < minimumValue) ? minimumValue : $receiver;
    }
    
    public static final double coerceAtLeast(final double $receiver, final double minimumValue) {
        return ($receiver < minimumValue) ? minimumValue : $receiver;
    }
    
    @NotNull
    public static final <T extends Comparable<? super T>> T coerceAtMost(@NotNull final T $receiver, @NotNull final T maximumValue) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(maximumValue, "maximumValue");
        return ($receiver.compareTo((Object)maximumValue) > 0) ? maximumValue : $receiver;
    }
    
    public static final byte coerceAtMost(final byte $receiver, final byte maximumValue) {
        return ($receiver > maximumValue) ? maximumValue : $receiver;
    }
    
    public static final short coerceAtMost(final short $receiver, final short maximumValue) {
        return ($receiver > maximumValue) ? maximumValue : $receiver;
    }
    
    public static final int coerceAtMost(final int $receiver, final int maximumValue) {
        return ($receiver > maximumValue) ? maximumValue : $receiver;
    }
    
    public static final long coerceAtMost(final long $receiver, final long maximumValue) {
        return ($receiver > maximumValue) ? maximumValue : $receiver;
    }
    
    public static final float coerceAtMost(final float $receiver, final float maximumValue) {
        return ($receiver > maximumValue) ? maximumValue : $receiver;
    }
    
    public static final double coerceAtMost(final double $receiver, final double maximumValue) {
        return ($receiver > maximumValue) ? maximumValue : $receiver;
    }
    
    @NotNull
    public static final <T extends Comparable<? super T>> T coerceIn(@NotNull final T $receiver, @Nullable final T minimumValue, @Nullable final T maximumValue) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        if (minimumValue != null && maximumValue != null) {
            if (minimumValue.compareTo((Object)maximumValue) > 0) {
                throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + maximumValue + " is less than minimum " + minimumValue + '.');
            }
            if ($receiver.compareTo((Object)minimumValue) < 0) {
                return minimumValue;
            }
            if ($receiver.compareTo((Object)maximumValue) > 0) {
                return maximumValue;
            }
        }
        else {
            if (minimumValue != null && $receiver.compareTo((Object)minimumValue) < 0) {
                return minimumValue;
            }
            if (maximumValue != null && $receiver.compareTo((Object)maximumValue) > 0) {
                return maximumValue;
            }
        }
        return $receiver;
    }
    
    public static final byte coerceIn(final byte $receiver, final byte minimumValue, final byte maximumValue) {
        if (minimumValue > maximumValue) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + maximumValue + " is less than minimum " + minimumValue + '.');
        }
        if ($receiver < minimumValue) {
            return minimumValue;
        }
        if ($receiver > maximumValue) {
            return maximumValue;
        }
        return $receiver;
    }
    
    public static final short coerceIn(final short $receiver, final short minimumValue, final short maximumValue) {
        if (minimumValue > maximumValue) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + maximumValue + " is less than minimum " + minimumValue + '.');
        }
        if ($receiver < minimumValue) {
            return minimumValue;
        }
        if ($receiver > maximumValue) {
            return maximumValue;
        }
        return $receiver;
    }
    
    public static final int coerceIn(final int $receiver, final int minimumValue, final int maximumValue) {
        if (minimumValue > maximumValue) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + maximumValue + " is less than minimum " + minimumValue + '.');
        }
        if ($receiver < minimumValue) {
            return minimumValue;
        }
        if ($receiver > maximumValue) {
            return maximumValue;
        }
        return $receiver;
    }
    
    public static final long coerceIn(final long $receiver, final long minimumValue, final long maximumValue) {
        if (minimumValue > maximumValue) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + maximumValue + " is less than minimum " + minimumValue + '.');
        }
        if ($receiver < minimumValue) {
            return minimumValue;
        }
        if ($receiver > maximumValue) {
            return maximumValue;
        }
        return $receiver;
    }
    
    public static final float coerceIn(final float $receiver, final float minimumValue, final float maximumValue) {
        if (minimumValue > maximumValue) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + maximumValue + " is less than minimum " + minimumValue + '.');
        }
        if ($receiver < minimumValue) {
            return minimumValue;
        }
        if ($receiver > maximumValue) {
            return maximumValue;
        }
        return $receiver;
    }
    
    public static final double coerceIn(final double $receiver, final double minimumValue, final double maximumValue) {
        if (minimumValue > maximumValue) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + maximumValue + " is less than minimum " + minimumValue + '.');
        }
        if ($receiver < minimumValue) {
            return minimumValue;
        }
        if ($receiver > maximumValue) {
            return maximumValue;
        }
        return $receiver;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T extends Comparable<? super T>> T coerceIn(@NotNull final T $receiver, @NotNull final ClosedFloatingPointRange<T> range) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(range, "range");
        if (range.isEmpty()) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: " + range + '.');
        }
        return (range.lessThanOrEquals((Comparable<? super T>)$receiver, range.getStart()) && !range.lessThanOrEquals(range.getStart(), $receiver)) ? range.getStart() : ((range.lessThanOrEquals(range.getEndInclusive(), (Comparable<? super T>)$receiver) && !range.lessThanOrEquals((Comparable<? super T>)$receiver, range.getEndInclusive())) ? range.getEndInclusive() : $receiver);
    }
    
    @NotNull
    public static final <T extends Comparable<? super T>> T coerceIn(@NotNull final T $receiver, @NotNull final ClosedRange<T> range) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(range, "range");
        if (range instanceof ClosedFloatingPointRange) {
            return coerceIn($receiver, (ClosedFloatingPointRange<T>)range);
        }
        if (range.isEmpty()) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: " + range + '.');
        }
        return ($receiver.compareTo((Object)range.getStart()) < 0) ? range.getStart() : (($receiver.compareTo((Object)range.getEndInclusive()) > 0) ? range.getEndInclusive() : $receiver);
    }
    
    public static final int coerceIn(final int $receiver, @NotNull final ClosedRange<Integer> range) {
        Intrinsics.checkParameterIsNotNull(range, "range");
        if (range instanceof ClosedFloatingPointRange) {
            return coerceIn(Integer.valueOf($receiver), (ClosedFloatingPointRange<Integer>)range).intValue();
        }
        if (range.isEmpty()) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: " + range + '.');
        }
        return ($receiver < range.getStart().intValue()) ? range.getStart().intValue() : (($receiver > range.getEndInclusive().intValue()) ? range.getEndInclusive().intValue() : $receiver);
    }
    
    public static final long coerceIn(final long $receiver, @NotNull final ClosedRange<Long> range) {
        Intrinsics.checkParameterIsNotNull(range, "range");
        if (range instanceof ClosedFloatingPointRange) {
            return coerceIn(Long.valueOf($receiver), (ClosedFloatingPointRange<Long>)range).longValue();
        }
        if (range.isEmpty()) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: " + range + '.');
        }
        return ($receiver < range.getStart().longValue()) ? range.getStart().longValue() : (($receiver > range.getEndInclusive().longValue()) ? range.getEndInclusive().longValue() : $receiver);
    }
    
    public RangesKt___RangesKt() {
    }
}
