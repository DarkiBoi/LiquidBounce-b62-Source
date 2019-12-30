// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;

public class SpreadBuilder
{
    private final ArrayList<Object> list;
    
    public SpreadBuilder(final int size) {
        this.list = new ArrayList<Object>(size);
    }
    
    public void addSpread(final Object container) {
        if (container == null) {
            return;
        }
        if (container instanceof Object[]) {
            final Object[] array = (Object[])container;
            if (array.length > 0) {
                this.list.ensureCapacity(this.list.size() + array.length);
                Collections.addAll(this.list, array);
            }
        }
        else if (container instanceof Collection) {
            this.list.addAll((Collection<?>)container);
        }
        else if (container instanceof Iterable) {
            for (final Object element : (Iterable)container) {
                this.list.add(element);
            }
        }
        else {
            if (!(container instanceof Iterator)) {
                throw new UnsupportedOperationException("Don't know how to spread " + container.getClass());
            }
            final Iterator iterator = (Iterator)container;
            while (iterator.hasNext()) {
                this.list.add(iterator.next());
            }
        }
    }
    
    public int size() {
        return this.list.size();
    }
    
    public void add(final Object element) {
        this.list.add(element);
    }
    
    public Object[] toArray(final Object[] a) {
        return this.list.toArray(a);
    }
}
