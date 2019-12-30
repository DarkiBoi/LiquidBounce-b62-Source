// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.file;

import java.io.IOException;
import java.io.File;

public abstract class FileConfig
{
    private final File file;
    
    public FileConfig(final File file) {
        this.file = file;
    }
    
    protected abstract void loadConfig() throws IOException;
    
    protected abstract void saveConfig() throws IOException;
    
    public void createConfig() throws IOException {
        this.file.createNewFile();
    }
    
    public boolean hasConfig() {
        return this.file.exists();
    }
    
    public File getFile() {
        return this.file;
    }
}
