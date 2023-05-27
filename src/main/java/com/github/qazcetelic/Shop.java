package com.github.qazcetelic;

import net.minecraft.util.math.BlockPos;

public class Shop {
    public final BlockPos pos;
    public final ShopData data;
    
    public Shop(BlockPos pos, ShopData data) {
        this.pos = pos;
        this.data = data;
    }
}
