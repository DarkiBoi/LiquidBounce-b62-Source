// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp;

public final class RegExpResult
{
    final Object[] groups;
    final int index;
    final String input;
    
    public RegExpResult(final String input, final int index, final Object[] groups) {
        this.input = input;
        this.index = index;
        this.groups = groups;
    }
    
    public Object[] getGroups() {
        return this.groups;
    }
    
    public String getInput() {
        return this.input;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public int length() {
        return ((String)this.groups[0]).length();
    }
    
    public Object getGroup(final int groupIndex) {
        return (groupIndex >= 0 && groupIndex < this.groups.length) ? this.groups[groupIndex] : "";
    }
    
    public Object getLastParen() {
        return (this.groups.length > 1) ? this.groups[this.groups.length - 1] : "";
    }
}
