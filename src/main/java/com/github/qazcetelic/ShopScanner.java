package com.github.qazcetelic;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopScanner {
    
    private static boolean enabled = false;
    public static void toggle() {
        enabled = !enabled;
    }
    public static boolean isEnabled() {
        return enabled;
    }
    
    @SideOnly(Side.CLIENT)
    private static Optional<ShopData> parseSign(TileEntitySign sign) {
        String shopText = "[Shop]";
        String line1 = sign.signText[0].getUnformattedText();
        if (line1.equals(shopText)) {
            String line2 = sign.signText[1].getUnformattedText();
            String line3 = sign.signText[2].getUnformattedText();
            ShopData.Items buying = parseItems(line2);
            ShopData.Items selling = parseItems(line3);
            
            if (buying != null && selling != null) {
                return Optional.of(new ShopData(buying, selling));
            }
            else {
                SkaniloMod.LOGGER.info("Invalid shop sign at " + sign.getPos());
            }
        }
        return Optional.empty();
    }
    
    private static final Pattern shopPattern = Pattern.compile("(?<id>\\d+) (?<amount>\\d+)( (?<damage>\\d+))?\\r?");
    @SideOnly(Side.CLIENT)
    private static ShopData.Items parseItems(String line) {
        Matcher matcher = shopPattern.matcher(line);
        if (matcher.matches()) {
            int id = Integer.parseInt(matcher.group("id"));
            int amount = Integer.parseInt(matcher.group("amount"));
            int damage = 0;
            String damageString = matcher.group("damage");
            if (damageString != null) damage = Integer.parseInt(matcher.group("damage"));
            return new ShopData.Items(id, amount, damage);
        }
        else {
            SkaniloMod.LOGGER.error("Invalid shop sign line: '" + line + "', does not match with '" + shopPattern.pattern() + "'");
            return null;
        }
    }
    
    private static final Set<Block> sign_blocks = new HashSet<Block>() {{
        add(Block.getBlockFromName("minecraft:standing_sign"));
        add(Block.getBlockFromName("minecraft:wall_sign"));
    }};
    
    public static final int CHUNK_SIZE = 16;
    public static final Set<BlockPos> scannedChunks = new HashSet<>();
    public static int failedScanAttempts = 0;
    public static LocalDateTime lastScan = null;
    public static void scanChunk(BlockPos chunkPos) {
        if (!scannedChunks.contains(chunkPos) && isEnabled()) {
            try {
                World world = Minecraft.getMinecraft().world;
                for (int x = 0; x < CHUNK_SIZE; x++) {
                    for (int y = 0; y < 256; y++) {
                        for (int z = 0; z < CHUNK_SIZE; z++) {
                            BlockPos pos = new BlockPos(chunkPos.getX() * CHUNK_SIZE + x, y, chunkPos.getZ() * CHUNK_SIZE + z);
                            if (sign_blocks.contains(world.getBlockState(pos).getBlock())) {
                                TileEntity tileEntity = world.getTileEntity(pos);
                                if (tileEntity instanceof TileEntitySign) {
                                    SkaniloMod.LOGGER.info("Found sign at " + pos);
                                    TileEntitySign sign = (TileEntitySign) tileEntity;
                                    parseSign(sign).ifPresent(data -> {
                                        ShopData.shops.put(sign.getPos(), data);
                                        SkaniloMod.LOGGER.info("Found shop sign at " + sign.getPos().getX() + ", " + sign.getPos().getY() + ", " + sign.getPos().getZ());
                                    });
                                }
                            }
                        }
                    }
                }
                SkaniloMod.LOGGER.info("Scanned chunk " + chunkPos.getX() + ", " + chunkPos.getZ());
                lastScan = LocalDateTime.now();
                scannedChunks.add(chunkPos);
            }
            catch (Exception e) {
                SkaniloMod.LOGGER.error("Error while scanning chunk " + chunkPos.getX() + ", " + chunkPos.getZ());
                failedScanAttempts++;
                e.printStackTrace();
            }
        }
    }
}
