// 
// Decompiled by Procyon v0.5.36
// 

package org.jglr.jchroma.devices;

import java.util.Arrays;
import java.util.List;
import com.sun.jna.Structure;

public class DeviceInfos
{
    private final DeviceInfosStruct struct;
    
    public DeviceInfos() {
        this.struct = new DeviceInfosStruct();
    }
    
    public boolean isConnected() {
        return this.struct.isConnected == 1;
    }
    
    public EnumDeviceType getType() {
        return EnumDeviceType.values()[this.struct.type];
    }
    
    public DeviceInfosStruct getUnderlyingStructure() {
        return this.struct;
    }
    
    public class DeviceInfosStruct extends Structure implements Structure.ByReference
    {
        public int type;
        public int isConnected;
        
        protected List getFieldOrder() {
            return Arrays.asList("type", "isConnected");
        }
    }
}
