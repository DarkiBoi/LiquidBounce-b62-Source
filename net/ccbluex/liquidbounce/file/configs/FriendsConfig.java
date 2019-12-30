// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.file.configs;

import java.util.Iterator;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.File;
import java.util.List;
import net.ccbluex.liquidbounce.file.FileConfig;

public class FriendsConfig extends FileConfig
{
    private final List<Friend> friends;
    
    public FriendsConfig(final File file) {
        super(file);
        this.friends = new ArrayList<Friend>();
    }
    
    @Override
    protected void loadConfig() throws IOException {
        this.clearFriends();
        final BufferedReader bufferedReader = new BufferedReader(new FileReader(this.getFile()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (!line.contains("{") && !line.contains("}")) {
                line = line.replace(" ", "").replace("\"", "").replace(",", "");
                if (line.contains(":")) {
                    final String[] data = line.split(":");
                    this.addFriend(data[0], data[1]);
                }
                else {
                    this.addFriend(line);
                }
            }
        }
        bufferedReader.close();
    }
    
    @Override
    protected void saveConfig() throws IOException {
        final PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        for (final Friend friend : this.getFriends()) {
            printWriter.append(friend.getPlayerName()).append(":").append(friend.getAlias()).append("\n");
        }
        printWriter.close();
    }
    
    public boolean addFriend(final String playerName) {
        return this.addFriend(playerName, playerName);
    }
    
    public boolean addFriend(final String playerName, final String alias) {
        if (this.isFriend(playerName)) {
            return false;
        }
        this.friends.add(new Friend(playerName, alias));
        return true;
    }
    
    public boolean removeFriend(final String playerName) {
        if (!this.isFriend(playerName)) {
            return false;
        }
        this.friends.removeIf(friend -> friend.getPlayerName().equals(playerName));
        return true;
    }
    
    public boolean isFriend(final String playerName) {
        for (final Friend friend : this.friends) {
            if (friend.getPlayerName().equals(playerName)) {
                return true;
            }
        }
        return false;
    }
    
    public void clearFriends() {
        this.friends.clear();
    }
    
    public List<Friend> getFriends() {
        return this.friends;
    }
    
    public class Friend
    {
        private final String playerName;
        private final String alias;
        
        Friend(final String playerName, final String alias) {
            this.playerName = playerName;
            this.alias = alias;
        }
        
        public String getPlayerName() {
            return this.playerName;
        }
        
        public String getAlias() {
            return this.alias;
        }
    }
}
