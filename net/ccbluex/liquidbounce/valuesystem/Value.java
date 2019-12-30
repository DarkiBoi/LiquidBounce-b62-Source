// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.valuesystem;

import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class Value
{
    private final String valueName;
    
    public Value(final String valueName) {
        this.valueName = valueName;
    }
    
    public String getValueName() {
        return this.valueName;
    }
    
    public void setValue(final Object o) {
        final Object oldValue = this.asObject();
        try {
            this.onChange(oldValue, o);
            this.setValueSilent(o);
            this.onChanged(oldValue, o);
            LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.valuesConfig);
        }
        catch (Exception e) {
            ClientUtils.getLogger().error("[ValueSystem (" + this.valueName + ")]: " + e.getClass().getName() + " (" + e.getMessage() + ") [" + oldValue + " >> " + o + "]");
        }
    }
    
    public abstract void setValueSilent(final Object p0);
    
    public abstract Object asObject();
    
    public String asString() {
        return (String)this.asObject();
    }
    
    public int asInteger() {
        return (int)this.asObject();
    }
    
    public float asFloat() {
        return (float)this.asObject();
    }
    
    public double asDouble() {
        return (double)this.asObject();
    }
    
    public boolean asBoolean() {
        return (boolean)this.asObject();
    }
    
    protected void onChange(final Object oldValue, final Object newValue) {
    }
    
    protected void onChanged(final Object oldValue, final Object newValue) {
    }
}
