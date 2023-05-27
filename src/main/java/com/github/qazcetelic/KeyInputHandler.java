package com.github.qazcetelic;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber(modid = SkaniloMod.MODID)
public class KeyInputHandler {
    public static KeyBinding toggleInfoOverlay = new KeyBinding("Toggle scanning info", Keyboard.KEY_O, SkaniloMod.NAME);
    public static KeyBinding toggleScanning = new KeyBinding("Toggle scanning", Keyboard.KEY_P, SkaniloMod.NAME);
    
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (toggleInfoOverlay.isPressed()) {
            ScanningStatusOverlay.toggle();
        }
        if (toggleScanning.isPressed()) {
            ShopScanner.toggle();
        }
    }
}
