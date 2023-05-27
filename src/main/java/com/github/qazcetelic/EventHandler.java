package com.github.qazcetelic;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;

import static com.github.qazcetelic.ShopScanner.CHUNK_SIZE;

@Mod.EventBusSubscriber(modid = SkaniloMod.MODID, value = Side.CLIENT)
public class EventHandler {
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        BlockPos playerPos = event.player.getPosition();
        BlockPos chunkPos = new BlockPos(playerPos.getX() / CHUNK_SIZE, 0, playerPos.getZ() / CHUNK_SIZE);
        ShopScanner.scanChunk(chunkPos);
    }
}