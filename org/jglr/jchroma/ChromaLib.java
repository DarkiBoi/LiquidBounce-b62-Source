// 
// Decompiled by Procyon v0.5.36
// 

package org.jglr.jchroma;

import org.jglr.jchroma.devices.DeviceInfos;
import org.jglr.jchroma.devices.GUIDStruct;
import com.sun.jna.Pointer;
import com.sun.jna.Library;

interface ChromaLib extends Library
{
    int Init();
    
    int UnInit();
    
    int CreateKeyboardEffect(final int p0, final Pointer p1, final Pointer p2);
    
    int QueryDevice(final GUIDStruct p0, final DeviceInfos.DeviceInfosStruct p1);
}
