// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

final class OptEnvironment
{
    final MinMaxLen mmd;
    int options;
    int caseFoldFlag;
    ScanEnvironment scanEnv;
    
    OptEnvironment() {
        this.mmd = new MinMaxLen();
    }
    
    void copy(final OptEnvironment other) {
        this.mmd.copy(other.mmd);
        this.options = other.options;
        this.caseFoldFlag = other.caseFoldFlag;
        this.scanEnv = other.scanEnv;
    }
}
