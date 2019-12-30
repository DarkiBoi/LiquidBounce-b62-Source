// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import jdk.nashorn.internal.runtime.linker.NashornGuards;
import java.lang.invoke.MethodHandle;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import java.lang.invoke.SwitchPoint;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.CallSiteDescriptor;

final class SetMethodCreator
{
    private final ScriptObject sobj;
    private final PropertyMap map;
    private final FindProperty find;
    private final CallSiteDescriptor desc;
    private final Class<?> type;
    private final LinkRequest request;
    
    SetMethodCreator(final ScriptObject sobj, final FindProperty find, final CallSiteDescriptor desc, final LinkRequest request) {
        this.sobj = sobj;
        this.map = sobj.getMap();
        this.find = find;
        this.desc = desc;
        this.type = desc.getMethodType().parameterType(1);
        this.request = request;
    }
    
    private String getName() {
        return this.desc.getNameToken(2);
    }
    
    private PropertyMap getMap() {
        return this.map;
    }
    
    GuardedInvocation createGuardedInvocation(final SwitchPoint builtinSwitchPoint) {
        return this.createSetMethod(builtinSwitchPoint).createGuardedInvocation();
    }
    
    private SetMethod createSetMethod(final SwitchPoint builtinSwitchPoint) {
        if (this.find != null) {
            return this.createExistingPropertySetter();
        }
        this.checkStrictCreateNewVariable();
        if (this.sobj.isScope()) {
            return this.createGlobalPropertySetter();
        }
        return this.createNewPropertySetter(builtinSwitchPoint);
    }
    
    private void checkStrictCreateNewVariable() {
        if (NashornCallSiteDescriptor.isScope(this.desc) && NashornCallSiteDescriptor.isStrict(this.desc)) {
            throw ECMAErrors.referenceError("not.defined", this.getName());
        }
    }
    
    private SetMethod createExistingPropertySetter() {
        final Property property = this.find.getProperty();
        final boolean isStrict = NashornCallSiteDescriptor.isStrict(this.desc);
        MethodHandle methodHandle;
        if (NashornCallSiteDescriptor.isDeclaration(this.desc)) {
            assert property.needsDeclaration();
            final PropertyMap oldMap = this.getMap();
            final Property newProperty = property.removeFlags(512);
            final PropertyMap newMap = oldMap.replaceProperty(property, newProperty);
            final MethodHandle fastSetter = this.find.replaceProperty(newProperty).getSetter(this.type, isStrict, this.request);
            final MethodHandle slowSetter = Lookup.MH.insertArguments(ScriptObject.DECLARE_AND_SET, 1, this.getName()).asType(fastSetter.type());
            MethodHandle casMap = Lookup.MH.insertArguments(ScriptObject.CAS_MAP, 1, oldMap, newMap);
            casMap = Lookup.MH.dropArguments(casMap, 1, this.type);
            casMap = Lookup.MH.asType(casMap, casMap.type().changeParameterType(0, (Class<?>)Object.class));
            methodHandle = Lookup.MH.guardWithTest(casMap, fastSetter, slowSetter);
        }
        else {
            methodHandle = this.find.getSetter(this.type, isStrict, this.request);
        }
        assert methodHandle != null;
        assert property != null;
        MethodHandle boundHandle;
        if (!(property instanceof UserAccessorProperty) && this.find.isInherited()) {
            boundHandle = ScriptObject.addProtoFilter(methodHandle, this.find.getProtoChainLength());
        }
        else {
            boundHandle = methodHandle;
        }
        return new SetMethod(boundHandle, property);
    }
    
    private SetMethod createGlobalPropertySetter() {
        final ScriptObject global = Context.getGlobal();
        return new SetMethod(Lookup.MH.filterArguments(global.addSpill(this.type, this.getName()), 0, ScriptObject.GLOBALFILTER), null);
    }
    
    private SetMethod createNewPropertySetter(final SwitchPoint builtinSwitchPoint) {
        final SetMethod sm = (this.map.getFreeFieldSlot() > -1) ? this.createNewFieldSetter(builtinSwitchPoint) : this.createNewSpillPropertySetter(builtinSwitchPoint);
        this.map.propertyAdded(sm.property, true);
        return sm;
    }
    
    private SetMethod createNewSetter(final Property property, final SwitchPoint builtinSwitchPoint) {
        property.setBuiltinSwitchPoint(builtinSwitchPoint);
        final PropertyMap oldMap = this.getMap();
        final PropertyMap newMap = this.getNewMap(property);
        final boolean isStrict = NashornCallSiteDescriptor.isStrict(this.desc);
        final String name = this.desc.getNameToken(2);
        final MethodHandle fastSetter = property.getSetter(this.type, newMap);
        MethodHandle slowSetter = ScriptObject.SET_SLOW[JSType.getAccessorTypeIndex(this.type)];
        slowSetter = Lookup.MH.insertArguments(slowSetter, 3, NashornCallSiteDescriptor.getFlags(this.desc));
        slowSetter = Lookup.MH.insertArguments(slowSetter, 1, name);
        slowSetter = Lookup.MH.asType(slowSetter, slowSetter.type().changeParameterType(0, (Class<?>)Object.class));
        assert slowSetter.type().equals((Object)fastSetter.type()) : "slow=" + slowSetter + " != fast=" + fastSetter;
        MethodHandle casMap = Lookup.MH.insertArguments(ScriptObject.CAS_MAP, 1, oldMap, newMap);
        casMap = Lookup.MH.dropArguments(casMap, 1, this.type);
        casMap = Lookup.MH.asType(casMap, casMap.type().changeParameterType(0, (Class<?>)Object.class));
        final MethodHandle casGuard = Lookup.MH.guardWithTest(casMap, fastSetter, slowSetter);
        MethodHandle extCheck = Lookup.MH.insertArguments(ScriptObject.EXTENSION_CHECK, 1, isStrict, name);
        extCheck = Lookup.MH.asType(extCheck, extCheck.type().changeParameterType(0, (Class<?>)Object.class));
        extCheck = Lookup.MH.dropArguments(extCheck, 1, this.type);
        MethodHandle nop = JSType.VOID_RETURN.methodHandle();
        nop = Lookup.MH.dropArguments(nop, 0, Object.class, this.type);
        return new SetMethod(Lookup.MH.asType(Lookup.MH.guardWithTest(extCheck, casGuard, nop), fastSetter.type()), property);
    }
    
    private SetMethod createNewFieldSetter(final SwitchPoint builtinSwitchPoint) {
        return this.createNewSetter(new AccessorProperty(this.getName(), getFlags(this.sobj), this.sobj.getClass(), this.getMap().getFreeFieldSlot(), this.type), builtinSwitchPoint);
    }
    
    private SetMethod createNewSpillPropertySetter(final SwitchPoint builtinSwitchPoint) {
        return this.createNewSetter(new SpillProperty(this.getName(), getFlags(this.sobj), this.getMap().getFreeSpillSlot(), this.type), builtinSwitchPoint);
    }
    
    private PropertyMap getNewMap(final Property property) {
        return this.getMap().addProperty(property);
    }
    
    private static int getFlags(final ScriptObject scriptObject) {
        return scriptObject.useDualFields() ? 2048 : 0;
    }
    
    private class SetMethod
    {
        private final MethodHandle methodHandle;
        private final Property property;
        
        SetMethod(final MethodHandle methodHandle, final Property property) {
            assert methodHandle != null;
            this.methodHandle = methodHandle;
            this.property = property;
        }
        
        GuardedInvocation createGuardedInvocation() {
            final boolean explicitInstanceOfCheck = NashornGuards.explicitInstanceOfCheck(SetMethodCreator.this.desc, SetMethodCreator.this.request);
            return new GuardedInvocation(this.methodHandle, NashornGuards.getGuard(SetMethodCreator.this.sobj, this.property, SetMethodCreator.this.desc, explicitInstanceOfCheck), (SwitchPoint)null, explicitInstanceOfCheck ? null : ClassCastException.class);
        }
    }
}
