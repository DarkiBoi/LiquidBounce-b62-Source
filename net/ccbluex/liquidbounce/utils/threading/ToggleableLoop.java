// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.threading;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ToggleableLoop
{
    private boolean running;
    private final String threadName;
    private final Runnable runnable;
    
    public ToggleableLoop(final Runnable runnable, final String name) {
        this.runnable = runnable;
        this.threadName = name;
    }
    
    public void startThread() {
        if (this.running) {
            return;
        }
        new Thread(() -> {
            while (this.running) {
                this.runnable.run();
            }
            return;
        }, this.threadName).start();
        this.running = true;
    }
    
    public void stopThread() {
        if (!this.running) {
            return;
        }
        this.running = false;
    }
    
    public boolean isRunning() {
        return this.running;
    }
}
