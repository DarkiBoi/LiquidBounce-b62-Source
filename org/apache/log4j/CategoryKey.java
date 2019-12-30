// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j;

class CategoryKey
{
    String name;
    int hashCache;
    
    CategoryKey(final String name) {
        this.name = name;
        this.hashCache = name.hashCode();
    }
    
    public final int hashCode() {
        return this.hashCache;
    }
    
    public final boolean equals(final Object rArg) {
        return this == rArg || (rArg != null && CategoryKey.class == rArg.getClass() && this.name.equals(((CategoryKey)rArg).name));
    }
}
