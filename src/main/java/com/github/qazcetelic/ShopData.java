package com.github.qazcetelic;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ShopData {
    public static final Map<BlockPos, ShopData> shops = new HashMap<>();
    
    public final Items buying;
    public final Items selling;
    
    /**
     * Create a new ShopData object.
     */
    public ShopData(Items buying, Items selling) {
        this(LocalDateTime.now(), buying, selling);
    }
    
    /**
     * Load a ShopData object.
     * @param lastUpdated The loaded time.
     */
    public ShopData(LocalDateTime lastUpdated, Items buying, Items selling) {
        this.buying = buying;
        this.selling = selling;
        SkaniloMod.LOGGER.info("Loaded a shop that " + describe());
    }
    
    public String describe() {
        return String.format("sells %d %s (%d) in exchange for %d %s (%d)",
            selling.amount,
            selling.getItemString(),
            selling.damage,
            buying.amount,
            buying.getItemString(),
            buying.damage
        );
    }
    
    public static class Items {
        public final int id;
        public final int amount;
        /**
         * Zero when no value is present.
         */
        public final int damage;
        
        public Items(int id, int amount, int damage) {
            this.id = id;
            this.amount = amount;
            this.damage = damage;
        }
        
        public String getItemString() {
            Item item = Item.getItemById(id);
            if (item == null /* IntelliSense is being dumb here, this CAN be null */) {
                return "unknown";
            }
            else {
                return item.getRegistryName().toString();
            }
        }
        
        public JsonObject toJsonObject() {
            JsonObject json = new JsonObject();
            json.addProperty("id", id);
            json.addProperty("amount", amount);
            json.addProperty("damage", damage);
            return json;
        }
    }
}
