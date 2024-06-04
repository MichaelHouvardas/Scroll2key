package net.malegolf100.scroll2keymod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.command.ExecuteCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scroll2key implements ModInitializer {
	public static final String MOD_ID = "scroll2keymod";
	public static final Logger LOGGER = LoggerFactory.getLogger("scroll2key");


	private static final KeyBinding keyBindingRight = KeyBindingHelper.registerKeyBinding(
			new KeyBinding(
					"key.scroll2keymod.scroll_right",
					InputUtil.Type.KEYSYM,
					GLFW.GLFW_KEY_RIGHT_BRACKET,
					"key.categories.scroll2keymod"
			)
	);

	private static final KeyBinding keyBindingLeft = KeyBindingHelper.registerKeyBinding(
			new KeyBinding(
					"key.scroll2keymod.scroll_left",
					InputUtil.Type.KEYSYM,
					GLFW.GLFW_KEY_LEFT_BRACKET,
					"key.categories.scroll2keymod"
			)
	);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		// Register a client tick event listener
		ClientTickEvents.START_CLIENT_TICK.register(client -> {
			while (keyBindingRight.wasPressed()) {
				scrollHotbarRight();
			}

			while (keyBindingLeft.wasPressed()) {
				scrollHotbarLeft();
			}
		});
	}
	private void scrollHotbarRight() {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null) {
			int currentSlot = player.getInventory().selectedSlot;
			int nextSlot = (currentSlot + 1) % 9; // Assuming 9 slots in the hotbar
			player.getInventory().selectedSlot = nextSlot;
			// Optionally, update the hotbar display (visual feedback)
		}
	}

	private void scrollHotbarLeft() {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null) {
			int currentSlot = player.getInventory().selectedSlot;
			int prevSlot = (currentSlot - 1 + 9) % 9; // Wrap around to the last slot
			player.getInventory().selectedSlot = prevSlot;
			// Optionally, update the hotbar display (visual feedback)
		}
	}
}