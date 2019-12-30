// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.misc;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.Gson;

public final class JsonUtils
{
    public static final Gson GSON;
    public static final Gson PRETTY_GSON;
    public static final JsonParser JSON_PARSER;
    
    static {
        GSON = new Gson();
        PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();
        JSON_PARSER = new JsonParser();
    }
}
