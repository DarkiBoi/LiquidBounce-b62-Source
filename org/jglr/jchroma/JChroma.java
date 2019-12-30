// 
// Decompiled by Procyon v0.5.36
// 

package org.jglr.jchroma;

import org.jglr.jchroma.devices.GUIDStruct;
import org.jglr.jchroma.devices.DeviceInfos;
import java.util.UUID;
import com.sun.jna.Structure;
import com.sun.jna.Pointer;
import org.jglr.jchroma.effects.KeyboardEffect;
import com.sun.jna.Native;

public class JChroma
{
    private static JChroma instance;
    private final ChromaLib wrapper;
    public boolean initialized;
    
    private JChroma() {
        String libName = "RzChromaSDK";
        if (System.getProperty("os.arch").contains("64")) {
            libName += "64";
        }
        this.wrapper = (ChromaLib)Native.loadLibrary(libName, (Class)ChromaLib.class);
    }
    
    public static JChroma getInstance() {
        if (JChroma.instance == null) {
            JChroma.instance = new JChroma();
        }
        return JChroma.instance;
    }
    
    private void throwIfError(final int err, final String method) {
        if (err != 0) {
            throw new JChromaException(method, err);
        }
    }
    
    public void init() {
        if (this.initialized) {
            return;
        }
        final int err = this.wrapper.Init();
        this.throwIfError(err, "init()");
        this.initialized = true;
    }
    
    public void free() {
        if (!this.initialized) {
            return;
        }
        final int err = this.wrapper.UnInit();
        this.throwIfError(err, "free()");
        this.initialized = false;
    }
    
    public void createKeyboardEffect(final KeyboardEffect effect) {
        final Structure param = effect.createParameter();
        int err;
        if (param == null) {
            err = this.wrapper.CreateKeyboardEffect(effect.getType().ordinal(), Pointer.NULL, Pointer.NULL);
        }
        else {
            param.write();
            err = this.wrapper.CreateKeyboardEffect(effect.getType().ordinal(), param.getPointer(), Pointer.NULL);
        }
        this.throwIfError(err, "createKeyboardEffect(" + effect.getType().name() + ")");
    }
    
    public DeviceInfos queryDevice(final UUID deviceID) {
        final DeviceInfos deviceInfos = new DeviceInfos();
        this.queryDevice(deviceID, deviceInfos);
        return deviceInfos;
    }
    
    public void queryDevice(final UUID deviceID, final DeviceInfos deviceInfos) {
        final DeviceInfos.DeviceInfosStruct struct = deviceInfos.getUnderlyingStructure();
        final int err = this.wrapper.QueryDevice(this.createGUID(deviceID), struct);
        this.throwIfError(err, "queryDevice");
        struct.read();
    }
    
    public boolean isDeviceConnected(final UUID deviceID) {
        try {
            return this.queryDevice(deviceID).isConnected();
        }
        catch (JChromaException e) {
            return false;
        }
    }
    
    private GUIDStruct createGUID(final UUID uuid) {
        final GUIDStruct struct = new GUIDStruct();
        struct.data1 = (int)(uuid.getMostSignificantBits() >> 32);
        struct.data2 = (short)(uuid.getMostSignificantBits() >> 16 & 0xFFFFL);
        struct.data3 = (short)(uuid.getMostSignificantBits() & 0xFFFFL);
        struct.data4 = this.longToBytes(uuid.getLeastSignificantBits());
        struct.write();
        return struct;
    }
    
    public byte[] longToBytes(final long x) {
        final byte[] bytes = new byte[8];
        for (int i = 0; i < 8; ++i) {
            bytes[8 - i - 1] = (byte)(x >> i * 8 & 0xFFL);
        }
        return bytes;
    }
}
