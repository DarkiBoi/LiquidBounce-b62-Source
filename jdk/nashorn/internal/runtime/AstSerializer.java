// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import jdk.nashorn.internal.runtime.options.Options;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Deflater;
import java.io.ByteArrayOutputStream;
import jdk.nashorn.internal.ir.FunctionNode;

final class AstSerializer
{
    private static final int COMPRESSION_LEVEL;
    
    static byte[] serialize(final FunctionNode fn) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final Deflater deflater = new Deflater(AstSerializer.COMPRESSION_LEVEL);
        try (final ObjectOutputStream oout = new ObjectOutputStream(new DeflaterOutputStream(out, deflater))) {
            oout.writeObject(fn);
        }
        catch (IOException e) {
            throw new AssertionError("Unexpected exception serializing function", e);
        }
        finally {
            deflater.end();
        }
        return out.toByteArray();
    }
    
    static {
        COMPRESSION_LEVEL = Options.getIntProperty("nashorn.serialize.compression", 4);
    }
}
