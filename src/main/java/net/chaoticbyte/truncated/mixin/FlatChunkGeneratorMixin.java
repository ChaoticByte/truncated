package net.chaoticbyte.truncated.mixin;

import net.chaoticbyte.truncated.Truncated;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Mixin(FlatChunkGenerator.class)
public abstract class FlatChunkGeneratorMixin extends ChunkGenerator {

    @Shadow public abstract VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig);

    public FlatChunkGeneratorMixin(BiomeSource biomeSource, Function<RegistryEntry<Biome>, GenerationSettings> generationSettingsGetter) {
        super(biomeSource, generationSettingsGetter);
    }

    @Inject(
            method = "populateNoise(Lnet/minecraft/world/gen/chunk/Blender;Lnet/minecraft/world/gen/noise/NoiseConfig;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/chunk/Chunk;)Ljava/util/concurrent/CompletableFuture;",
            at = @At("HEAD"),
            cancellable = true
    )
    public void populateNoise(Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk, CallbackInfoReturnable<CompletableFuture<Chunk>> ci) {
        ChunkPos p = chunk.getPos();
        int limit = Truncated.getLimit();
        if (p.x > limit || p.x < -limit || p.z > limit || p.z < -limit) {
            ci.setReturnValue(CompletableFuture.completedFuture(chunk));
        }
    }

    @Inject(
            method = "getHeight",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig noiseConfig, CallbackInfoReturnable<Integer> ci) {
        int limit = Truncated.getLimit();
        if (x > limit || x < -limit || z > limit || z < -limit) {
            ci.setReturnValue(0);
        }
    }

}
