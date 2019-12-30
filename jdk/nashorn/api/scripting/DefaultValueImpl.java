// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.api.scripting;

import jdk.nashorn.internal.runtime.JSType;

class DefaultValueImpl
{
    private static final String[] DEFAULT_VALUE_FNS_NUMBER;
    private static final String[] DEFAULT_VALUE_FNS_STRING;
    
    static Object getDefaultValue(final JSObject jsobj, final Class<?> hint) throws UnsupportedOperationException {
        final boolean isNumber = hint == null || hint == Number.class;
        for (final String methodName : isNumber ? DefaultValueImpl.DEFAULT_VALUE_FNS_NUMBER : DefaultValueImpl.DEFAULT_VALUE_FNS_STRING) {
            final Object objMember = jsobj.getMember(methodName);
            if (objMember instanceof JSObject) {
                final JSObject member = (JSObject)objMember;
                if (member.isFunction()) {
                    final Object value = member.call(jsobj, new Object[0]);
                    if (JSType.isPrimitive(value)) {
                        return value;
                    }
                }
            }
        }
        throw new UnsupportedOperationException(isNumber ? "cannot.get.default.number" : "cannot.get.default.string");
    }
    
    static {
        DEFAULT_VALUE_FNS_NUMBER = new String[] { "valueOf", "toString" };
        DEFAULT_VALUE_FNS_STRING = new String[] { "toString", "valueOf" };
    }
}
