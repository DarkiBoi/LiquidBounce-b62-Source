// 
// Decompiled by Procyon v0.5.36
// 

package org.jglr.jchroma.devices;

import java.util.Arrays;
import java.util.List;
import com.sun.jna.Structure;

public class GUIDStruct extends Structure implements Structure.ByValue
{
    public int data1;
    public short data2;
    public short data3;
    public byte[] data4;
    
    public GUIDStruct() {
        this.data4 = new byte[8];
    }
    
    protected List getFieldOrder() {
        return Arrays.asList("data1", "data2", "data3", "data4");
    }
}
