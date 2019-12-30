// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.Compiler;
import java.security.ProtectionDomain;
import java.security.CodeSource;
import jdk.nashorn.internal.codegen.ObjectClassGenerator;

final class StructureLoader extends NashornLoader
{
    private static final String SINGLE_FIELD_PREFIX;
    private static final String DUAL_FIELD_PREFIX;
    
    StructureLoader(final ClassLoader parent) {
        super(parent);
    }
    
    private static boolean isDualFieldStructure(final String name) {
        return name.startsWith(StructureLoader.DUAL_FIELD_PREFIX);
    }
    
    static boolean isSingleFieldStructure(final String name) {
        return name.startsWith(StructureLoader.SINGLE_FIELD_PREFIX);
    }
    
    static boolean isStructureClass(final String name) {
        return isDualFieldStructure(name) || isSingleFieldStructure(name);
    }
    
    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        if (isDualFieldStructure(name)) {
            return this.generateClass(name, name.substring(StructureLoader.DUAL_FIELD_PREFIX.length()), true);
        }
        if (isSingleFieldStructure(name)) {
            return this.generateClass(name, name.substring(StructureLoader.SINGLE_FIELD_PREFIX.length()), false);
        }
        return super.findClass(name);
    }
    
    private Class<?> generateClass(final String name, final String descriptor, final boolean dualFields) {
        final Context context = Context.getContextTrusted();
        final byte[] code = new ObjectClassGenerator(context, dualFields).generate(descriptor);
        return this.defineClass(name, code, 0, code.length, new ProtectionDomain(null, this.getPermissions(null)));
    }
    
    static {
        SINGLE_FIELD_PREFIX = Compiler.binaryName("jdk/nashorn/internal/scripts") + '.' + CompilerConstants.JS_OBJECT_SINGLE_FIELD_PREFIX.symbolName();
        DUAL_FIELD_PREFIX = Compiler.binaryName("jdk/nashorn/internal/scripts") + '.' + CompilerConstants.JS_OBJECT_DUAL_FIELD_PREFIX.symbolName();
    }
}
