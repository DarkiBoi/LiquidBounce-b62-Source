// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import java.util.HashMap;

public class Namespace
{
    private final Namespace parent;
    private final HashMap<String, Integer> directory;
    
    public Namespace() {
        this(null);
    }
    
    public Namespace(final Namespace parent) {
        this.parent = parent;
        this.directory = new HashMap<String, Integer>();
    }
    
    public Namespace getParent() {
        return this.parent;
    }
    
    public String uniqueName(final String base) {
        final String truncatedBase = (base.length() > 32768) ? base.substring(0, 32768) : base;
        for (Namespace namespace = this; namespace != null; namespace = namespace.getParent()) {
            final HashMap<String, Integer> namespaceDirectory = namespace.directory;
            final Integer counter = namespaceDirectory.get(truncatedBase);
            if (counter != null) {
                final int count = counter + 1;
                namespaceDirectory.put(truncatedBase, count);
                return truncatedBase + CompilerConstants.ID_FUNCTION_SEPARATOR.symbolName() + count;
            }
        }
        this.directory.put(truncatedBase, 0);
        return truncatedBase;
    }
    
    @Override
    public String toString() {
        return this.directory.toString();
    }
}
