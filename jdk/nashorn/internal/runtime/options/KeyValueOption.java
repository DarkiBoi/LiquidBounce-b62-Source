// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.options;

import java.util.StringTokenizer;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.Map;

public class KeyValueOption extends Option<String>
{
    protected Map<String, String> map;
    
    KeyValueOption(final String value) {
        super(value);
        this.initialize();
    }
    
    Map<String, String> getValues() {
        return Collections.unmodifiableMap((Map<? extends String, ? extends String>)this.map);
    }
    
    public boolean hasValue(final String key) {
        return this.map != null && this.map.get(key) != null;
    }
    
    String getValue(final String key) {
        if (this.map == null) {
            return null;
        }
        final String val = this.map.get(key);
        return "".equals(val) ? null : val;
    }
    
    private void initialize() {
        if (this.getValue() == null) {
            return;
        }
        this.map = new LinkedHashMap<String, String>();
        final StringTokenizer st = new StringTokenizer(this.getValue(), ",");
        while (st.hasMoreElements()) {
            final String token = st.nextToken();
            final String[] keyValue = token.split(":");
            if (keyValue.length == 1) {
                this.map.put(keyValue[0], "");
            }
            else {
                if (keyValue.length != 2) {
                    throw new IllegalArgumentException(token);
                }
                this.map.put(keyValue[0], keyValue[1]);
            }
        }
    }
}
