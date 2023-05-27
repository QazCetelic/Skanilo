package com.github.qazcetelic;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mod(modid = SkaniloMod.MODID, name = SkaniloMod.NAME, version = SkaniloMod.VERSION)
public class SkaniloMod
{
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    public static final String MODID = "skanilo";
    public static final String NAME = "Skanilo";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    
    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MODID)
    public static SkaniloMod INSTANCE;
    
    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        // n/a
    }
    
    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            File shopsFolder = new File(Minecraft.getMinecraft().mcDataDir, "shops");
            if (shopsFolder.mkdir()) {
                SkaniloMod.LOGGER.info("Created shops folder. '{}'", shopsFolder.getAbsolutePath());
            }
            
            String name = LocalDateTime.now().format(DATE_TIME_FORMATTER) + "_scan.json";
            File shopsFile = new File(shopsFolder, name);
            try {
                if (shopsFile.createNewFile()) {
                    String json = new ScanData(ShopData.shops).toJsonString();
                    try (FileWriter writer = new FileWriter(shopsFile)) {
                        writer.write(json);
                        SkaniloMod.LOGGER.info("Saved shops to " + shopsFile.getAbsolutePath());
                    }
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }
    
    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        // n/a
    }
}
