package com.axiom.auctionmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import com.axiom.auctionmod.gui.AuctionMainScreenFabric;

public class AuctionModClient implements ClientModInitializer {
    public static KeyBinding openAuctionKey;

    @Override
    public void onInitializeClient() {
        openAuctionKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.auctionmod.open_auction",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            "category.auctionmod"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openAuctionKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new AuctionMainScreenFabric(null));
                }
            }
        });
    }
}