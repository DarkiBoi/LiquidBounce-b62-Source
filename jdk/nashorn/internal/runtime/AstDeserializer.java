// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;
import java.io.ByteArrayInputStream;
import jdk.nashorn.internal.ir.FunctionNode;

final class AstDeserializer
{
    static FunctionNode deserialize(final byte[] serializedAst) {
        try {
            return (FunctionNode)new ObjectInputStream(new InflaterInputStream(new ByteArrayInputStream(serializedAst))).readObject();
        }
        catch (ClassNotFoundException | IOException ex2) {
            final Exception ex;
            final Exception e = ex;
            throw new AssertionError("Unexpected exception deserializing function", e);
        }
    }
}
