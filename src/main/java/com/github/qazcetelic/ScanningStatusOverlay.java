package com.github.qazcetelic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mod.EventBusSubscriber
public class ScanningStatusOverlay {
    private static final int TEXT_COLOR = 0xFFFFFF;
    private static final int TEXT_DISTANCE = 10;
    private static final int TEXT_Y = 10;
    
    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            renderTextOverlay();
        }
    }
    
    private static void renderTextOverlay() {
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fontRenderer = mc.fontRenderer;
        
        String timeString = ShopScanner.lastScan == null ? "never" : ShopScanner.lastScan.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String scanningStatus = String.format("Last scan %s", timeString);
        fontRenderer.drawString(scanningStatus, 10, 1 * TEXT_DISTANCE + TEXT_Y, TEXT_COLOR);
        String scannedInfo = String.format("Scanned %d chunks containing %d shops", ShopScanner.scannedChunks.size(), ShopData.shops.size());
        fontRenderer.drawString(scannedInfo, 10, 2 * TEXT_DISTANCE + TEXT_Y, TEXT_COLOR);
        String failedScansInfo = String.format("%d failed scans", ShopScanner.failedScanAttempts);
        fontRenderer.drawString(failedScansInfo, 10, 3 * TEXT_DISTANCE + TEXT_Y, TEXT_COLOR);
    }
}
