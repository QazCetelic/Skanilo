package com.github.qazcetelic;

import com.google.common.base.Objects;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.math.BlockPos;

import java.time.LocalDateTime;
import java.util.Map;

public class ScanData {
    
    /**
     * Used to check if the data is outdated.
     */
    public final LocalDateTime lastUpdated = LocalDateTime.now();
    
    /**
     * Can be used for checking whether users use recent client.
     */
    public final String modVersion = SkaniloMod.VERSION;
    
    /**
     * Device identifier.
     * Used for counting unique users.
     * Data is just used for hashing and is not stored for privacy reasons
     * Can also be used for temporary protection against spam and abuse until a proper system is implemented on the server.
     */
    public final int identifier = Objects.hashCode(System.getProperty("os.name"), System.getProperty("user.name"), System.getProperty("os.arch"));
    
    /**
     * The actual shop data.
     */
    public final Map<BlockPos, ShopData> shops;
    
    public ScanData(Map<BlockPos, ShopData> shops) {
        this.shops = shops;
    }
    
    public String toJsonString() {
        JsonArray shopsJson = new JsonArray();
        shops.forEach((blockPos, shopData) -> {
            JsonObject shopJson = new JsonObject();
    
            shopJson.addProperty("x", blockPos.getX());
            shopJson.addProperty("y", blockPos.getY());
            shopJson.addProperty("z", blockPos.getZ());
            
            shopJson.add("buying", shopData.buying.toJsonObject());
            shopJson.add("selling", shopData.selling.toJsonObject());
    
            shopsJson.add(shopJson);
        });
        
        JsonObject reportJson = new JsonObject();
        reportJson.addProperty("lastUpdated", lastUpdated.format(SkaniloMod.DATE_TIME_FORMATTER));
        reportJson.addProperty("modVersion", modVersion);
        reportJson.addProperty("identifier", identifier);
        reportJson.add("shops", shopsJson);
        
        return new Gson().toJson(reportJson);
    }
}