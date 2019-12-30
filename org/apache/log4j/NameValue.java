// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j;

class NameValue
{
    String key;
    String value;
    
    public NameValue(final String key, final String value) {
        this.key = key;
        this.value = value;
    }
    
    public String toString() {
        return this.key + "=" + this.value;
    }
}
