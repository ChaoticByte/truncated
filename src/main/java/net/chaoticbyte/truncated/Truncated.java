package net.chaoticbyte.truncated;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Truncated implements ModInitializer {
	public static final String MOD_ID = "truncated";
	public static final GameRules.Key<GameRules.IntRule> CHUNK_GEN_LIMIT_KEY = GameRuleRegistry.register(
			"chunkGenerationLimit", GameRules.Category.MISC, GameRuleFactory.createIntRule((28_000_000 / 16) - 1 ));
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static MinecraftServer server;

	public static int getLimit() {
		return server.getGameRules().getInt(CHUNK_GEN_LIMIT_KEY);
	}

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTING.register(minecraftServer -> {
			server = minecraftServer;
		});
	}

}