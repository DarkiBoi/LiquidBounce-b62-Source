// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import java.util.Objects;
import java.lang.invoke.MethodHandles;
import jdk.internal.dynalink.CallSiteDescriptor;

public abstract class AbstractCallSiteDescriptor implements CallSiteDescriptor
{
    @Override
    public String getName() {
        return this.appendName(new StringBuilder(this.getNameLength())).toString();
    }
    
    @Override
    public MethodHandles.Lookup getLookup() {
        return MethodHandles.publicLookup();
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof CallSiteDescriptor && this.equals((CallSiteDescriptor)obj);
    }
    
    public boolean equals(final CallSiteDescriptor csd) {
        if (csd == null) {
            return false;
        }
        if (csd == this) {
            return true;
        }
        final int ntc = this.getNameTokenCount();
        if (ntc != csd.getNameTokenCount()) {
            return false;
        }
        int i = ntc;
        while (i-- > 0) {
            if (!Objects.equals(this.getNameToken(i), csd.getNameToken(i))) {
                return false;
            }
        }
        return this.getMethodType().equals((Object)csd.getMethodType()) && lookupsEqual(this.getLookup(), csd.getLookup());
    }
    
    @Override
    public int hashCode() {
        final MethodHandles.Lookup lookup = this.getLookup();
        int h = lookup.lookupClass().hashCode() + 31 * lookup.lookupModes();
        for (int c = this.getNameTokenCount(), i = 0; i < c; ++i) {
            h = h * 31 + this.getNameToken(i).hashCode();
        }
        return h * 31 + this.getMethodType().hashCode();
    }
    
    @Override
    public String toString() {
        final String mt = this.getMethodType().toString();
        final String l = this.getLookup().toString();
        final StringBuilder b = new StringBuilder(l.length() + 1 + mt.length() + this.getNameLength());
        return this.appendName(b).append(mt).append("@").append(l).toString();
    }
    
    private int getNameLength() {
        final int c = this.getNameTokenCount();
        int l = 0;
        for (int i = 0; i < c; ++i) {
            l += this.getNameToken(i).length();
        }
        return l + c - 1;
    }
    
    private StringBuilder appendName(final StringBuilder b) {
        b.append(this.getNameToken(0));
        for (int c = this.getNameTokenCount(), i = 1; i < c; ++i) {
            b.append(':').append(this.getNameToken(i));
        }
        return b;
    }
    
    private static boolean lookupsEqual(final MethodHandles.Lookup l1, final MethodHandles.Lookup l2) {
        return l1 == l2 || (l1.lookupClass() == l2.lookupClass() && l1.lookupModes() == l2.lookupModes());
    }
}
