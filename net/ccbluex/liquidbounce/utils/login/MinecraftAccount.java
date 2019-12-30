// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.login;

public final class MinecraftAccount
{
    private final String username;
    private String password;
    private String inGameName;
    
    public MinecraftAccount(final String username) {
        this.username = username;
    }
    
    public MinecraftAccount(final String name, final String password) {
        this.username = name;
        this.password = password;
    }
    
    public MinecraftAccount(final String name, final String password, final String inGameName) {
        this.username = name;
        this.password = password;
        this.inGameName = inGameName;
    }
    
    public boolean isCracked() {
        return this.password == null;
    }
    
    public String getName() {
        return this.username;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public String getAccountName() {
        return this.inGameName;
    }
    
    public void setAccountName(final String accountName) {
        this.inGameName = accountName;
    }
}
