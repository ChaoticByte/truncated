package net.chaoticbyte.truncated;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Truncated implements ModInitializer {
	public static final String MOD_ID = "truncated";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// gamerules
	public static final GameRules.Key<GameRules.IntRule> CHUNK_GEN_LIMIT_KEY = GameRuleRegistry.register(
			"chunkGenerationLimit",
			GameRules.Category.MISC,
			GameRuleFactory.createIntRule((28_000_000 / 16) - 1 )
	);
	public static final GameRules.Key<GameRules.BooleanRule> TRUNCATED_ALT_ENDING = GameRuleRegistry.register(
			"truncatedAlternativeEnding",
			GameRules.Category.MISC,
			GameRuleFactory.createBooleanRule (true)
	);

	// advancement criterion
	public static PlayerReachedLimitCriterion PLAYER_REACHED_LIMIT = Criteria.register(
			MOD_ID + "/reached_limit",
			new PlayerReachedLimitCriterion());

	// variable temporary holding the server reference later
	private static MinecraftServer server;

	// get the limit set by the gamerule
	public static int getLimit() {
		return server.getGameRules().getInt(CHUNK_GEN_LIMIT_KEY);
	}

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTING.register(minecraftServer -> {
			server = minecraftServer;
		});
		ServerTickEvents.START_WORLD_TICK.register(minecraftServer -> {
			if (minecraftServer.getGameRules().getBoolean(TRUNCATED_ALT_ENDING)) {
				float currentBlockLimit = getLimit() * 16;
				java.util.List<net.minecraft.server.network.ServerPlayerEntity> playerEntitiesReachedEnd = new ArrayList<>();
				minecraftServer.getPlayers().forEach(playerEntity -> {
					if (!playerEntity.isSpectator()) {
						Vec3d playerPos = playerEntity.getPos();
						if (
								playerPos.x > currentBlockLimit + 16
										|| playerPos.x < -currentBlockLimit
										|| playerPos.z > currentBlockLimit + 16
										|| playerPos.z < -currentBlockLimit
						) {
							playerEntitiesReachedEnd.add(playerEntity);
						}
					}
				});
				// trigger the end credits for all players out of bounds
				playerEntitiesReachedEnd.forEach(playerEntity -> {
					PLAYER_REACHED_LIMIT.trigger(playerEntity); // trigger advancement
					playerEntity.detachForDimensionChange();    // end credits
				});
			}
		});
	}

}