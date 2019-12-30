// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.jmx;

import java.lang.reflect.Method;

class MethodUnion
{
    Method readMethod;
    Method writeMethod;
    
    MethodUnion(final Method readMethod, final Method writeMethod) {
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
    }
}
