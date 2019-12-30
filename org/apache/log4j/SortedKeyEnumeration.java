// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

class SortedKeyEnumeration implements Enumeration
{
    private Enumeration e;
    
    public SortedKeyEnumeration(final Hashtable ht) {
        final Enumeration f = ht.keys();
        final Vector keys = new Vector(ht.size());
        int last = 0;
        while (f.hasMoreElements()) {
            final String key = f.nextElement();
            int i;
            for (i = 0; i < last; ++i) {
                final String s = keys.get(i);
                if (key.compareTo(s) <= 0) {
                    break;
                }
            }
            keys.add(i, key);
            ++last;
        }
        this.e = keys.elements();
    }
    
    public boolean hasMoreElements() {
        return this.e.hasMoreElements();
    }
    
    public Object nextElement() {
        return this.e.nextElement();
    }
}
