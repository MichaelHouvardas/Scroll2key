package net.malegolf100.scroll2keymod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NoMouseHotbarScroll implements ClientModInitializer {

    // Define key binding for toggling the hotbar scroll
    private static KeyBinding toggleKeyBinding;

    // Boolean flag to track whether hotbar scroll is enabled
    private boolean scrollDisabler = true;

    // Variable to store the original scroll callback
    private GLFWScrollCallback originalScrollCallback;

    // Logger instance
    private static final Logger LOGGER = LogManager.getLogger("NoMouseHotbarScroll");

    @Override
    public void onInitializeClient() {
        // Register the key binding with the specified key and category
        toggleKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.scroll2key.toggle", // Keybinding identifier
                InputUtil.Type.KEYSYM,    // Type of key binding
                GLFW.GLFW_KEY_O,          // Default key (can be changed)
                "category.scroll2key"     // Category in controls menu
        ));

        // Register a client tick event listener
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Check if the player is not null and the toggle key is pressed
            if (client.player != null && toggleKeyBinding.wasPressed()) {
                // Toggle the scrollDisabler flag
                scrollDisabler = !scrollDisabler;

                // Log the status of hotbar scroll
                LOGGER.info("scrollDisabler " + (scrollDisabler ? "false" : "true"));
                // Send a chat message to the player indicating the status of hotbar scroll
                client.player.sendMessage(Text.of("scrollDisabler " + (scrollDisabler ? "false" : "true")), true);
            }

            // Check if the game window is null
            if (client.getWindow() != null) {
                // Get the handle of the game window
                long windowHandle = client.getWindow().getHandle();

                // Check if the original scroll callback has not been stored yet
                if (originalScrollCallback == null) {
                    // Store the original scroll callback
                    originalScrollCallback = GLFW.glfwSetScrollCallback(windowHandle, new GLFWScrollCallback() {
                        @Override
                        public void invoke(long window, double xOffset, double yOffset) {
                            // Override the scroll callback to do nothing
                        }
                    });
                }
            }

            // Check if the game is paused or in a menu
            if (client.isPaused() || client.currentScreen != null) {
                // Set scrollDisabler to false to prevent hotbar scroll when in a menu
                if (!scrollDisabler) {

                }
                scrollDisabler = false;
                LOGGER.info("ScrollDisabler false");
            } else if (!scrollDisabler) { // If hotbar scroll is disabled
                // Restore the original scroll callback if it has been stored
                if (originalScrollCallback != null) {
                    long windowHandle = client.getWindow().getHandle();
                    GLFW.glfwSetScrollCallback(windowHandle, originalScrollCallback);
                    originalScrollCallback = null;
                }
            }
        });
    }
}
