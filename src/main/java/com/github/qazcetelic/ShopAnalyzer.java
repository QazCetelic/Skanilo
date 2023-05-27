package com.github.qazcetelic;

import net.minecraft.util.math.BlockPos;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShopAnalyzer {
    public static void analyzeShops(Map<BlockPos, ShopData> shops) {
        Map<String, List<Shop>> sellingLowest = shops
                .entrySet()
                .stream()
                .map(entry -> new Shop(entry.getKey(), entry.getValue()))
                .collect(Collectors.groupingBy((Shop shop) -> shop.data.selling.getItemString()));
        sellingLowest.replaceAll((sellingItem, shopsSelling) -> shopsSelling.stream().sorted(Comparator.comparingInt(shop -> shop.data.selling.amount)).collect(Collectors.toList()));
    
        Map<String, List<Shop>> buyingHighest = shops
                .entrySet()
                .stream()
                .map(entry -> new Shop(entry.getKey(), entry.getValue()))
                .collect(Collectors.groupingBy((Shop shop) -> shop.data.buying.getItemString()));
        buyingHighest.replaceAll((buyingItem, shopsBuying) -> shopsBuying.stream().sorted(Comparator.comparingInt(shop -> -shop.data.buying.amount)).collect(Collectors.toList()));
    }
    
    public static class Analysis {
        public Analysis() {
        }
    }
}
