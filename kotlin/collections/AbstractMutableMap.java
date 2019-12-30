// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import java.util.Collection;
import java.util.Set;
import org.jetbrains.annotations.Nullable;
import kotlin.SinceKotlin;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMutableMap;
import java.util.Map;
import java.util.AbstractMap;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\b\u0006\b'\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u00032\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u0004B\u0007\b\u0004¢\u0006\u0002\u0010\u0005J\u001f\u0010\u0006\u001a\u0004\u0018\u00018\u00012\u0006\u0010\u0007\u001a\u00028\u00002\u0006\u0010\b\u001a\u00028\u0001H&¢\u0006\u0002\u0010\t¨\u0006\n" }, d2 = { "Lkotlin/collections/AbstractMutableMap;", "K", "V", "", "Ljava/util/AbstractMap;", "()V", "put", "key", "value", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "kotlin-stdlib" })
@SinceKotlin(version = "1.1")
public abstract class AbstractMutableMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, KMutableMap
{
    @Nullable
    @Override
    public abstract V put(final K p0, final V p1);
    
    protected AbstractMutableMap() {
    }
    
    @Override
    public final /* bridge */ Set<Map.Entry<K, V>> entrySet() {
        return (Set<Map.Entry<K, V>>)this.getEntries();
    }
    
    public abstract Set getEntries();
    
    public /* bridge */ Set getKeys() {
        return super.keySet();
    }
    
    @Override
    public final /* bridge */ Set<K> keySet() {
        return (Set<K>)this.getKeys();
    }
    
    public /* bridge */ int getSize() {
        return super.size();
    }
    
    @Override
    public final /* bridge */ int size() {
        return this.getSize();
    }
    
    public /* bridge */ Collection getValues() {
        return super.values();
    }
    
    @Override
    public final /* bridge */ Collection<V> values() {
        return (Collection<V>)this.getValues();
    }
}
