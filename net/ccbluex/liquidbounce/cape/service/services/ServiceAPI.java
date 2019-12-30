// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.cape.service.services;

import java.util.UUID;
import net.ccbluex.liquidbounce.cape.service.CapeService;

public class ServiceAPI implements CapeService
{
    private final String baseURL;
    
    public ServiceAPI(final String baseURL) {
        this.baseURL = baseURL;
    }
    
    @Override
    public String getCape(final UUID uuid) {
        return String.format(this.baseURL, uuid);
    }
}
