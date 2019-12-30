// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.cape.service.services;

import java.util.UUID;
import java.util.Map;
import net.ccbluex.liquidbounce.cape.service.CapeService;

public class ServiceList implements CapeService
{
    private final Map<String, String> users;
    
    public ServiceList(final Map<String, String> users) {
        this.users = users;
    }
    
    @Override
    public String getCape(final UUID uuid) {
        return this.users.getOrDefault(uuid.toString().replace("-", ""), null);
    }
}
