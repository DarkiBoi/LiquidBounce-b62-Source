// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.file.configs;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import net.ccbluex.liquidbounce.utils.misc.JsonUtils;
import java.util.ArrayList;
import java.io.File;
import net.ccbluex.liquidbounce.utils.login.MinecraftAccount;
import java.util.List;
import net.ccbluex.liquidbounce.file.FileConfig;

public class AccountsConfig extends FileConfig
{
    public final List<MinecraftAccount> altManagerMinecraftAccounts;
    
    public AccountsConfig(final File file) {
        super(file);
        this.altManagerMinecraftAccounts = new ArrayList<MinecraftAccount>();
    }
    
    @Override
    protected void loadConfig() throws IOException {
        final List<String> accountList = (List<String>)JsonUtils.GSON.fromJson((Reader)new BufferedReader(new FileReader(this.getFile())), (Class)List.class);
        if (accountList == null) {
            return;
        }
        this.altManagerMinecraftAccounts.clear();
        for (final String account : accountList) {
            final String[] information = account.split(":");
            if (information.length >= 3) {
                this.altManagerMinecraftAccounts.add(new MinecraftAccount(information[0], information[1], information[2]));
            }
            else if (information.length == 2) {
                this.altManagerMinecraftAccounts.add(new MinecraftAccount(information[0], information[1]));
            }
            else {
                this.altManagerMinecraftAccounts.add(new MinecraftAccount(information[0]));
            }
        }
    }
    
    @Override
    protected void saveConfig() throws IOException {
        final List<String> accountList = new ArrayList<String>();
        for (final MinecraftAccount minecraftAccount : this.altManagerMinecraftAccounts) {
            accountList.add(minecraftAccount.getName() + ":" + ((minecraftAccount.getPassword() == null) ? "" : minecraftAccount.getPassword()) + ":" + ((minecraftAccount.getAccountName() == null) ? "" : minecraftAccount.getAccountName()));
        }
        final PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        printWriter.println(JsonUtils.PRETTY_GSON.toJson((Object)accountList));
        printWriter.close();
    }
}
