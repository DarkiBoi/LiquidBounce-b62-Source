// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.helpers;

import java.util.NoSuchElementException;
import java.util.Enumeration;

public class NullEnumeration implements Enumeration
{
    private static final NullEnumeration instance;
    
    private NullEnumeration() {
    }
    
    public static NullEnumeration getInstance() {
        return NullEnumeration.instance;
    }
    
    public boolean hasMoreElements() {
        return false;
    }
    
    public Object nextElement() {
        throw new NoSuchElementException();
    }
    
    static {
        instance = new NullEnumeration();
    }
}
