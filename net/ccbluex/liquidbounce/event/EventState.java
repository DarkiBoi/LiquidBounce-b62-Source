// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.event;

public enum EventState
{
    PRE("PRE"), 
    POST("POST");
    
    private String stateName;
    
    private EventState(final String stateName) {
        this.stateName = stateName;
    }
    
    public String getStateName() {
        return this.stateName;
    }
}
