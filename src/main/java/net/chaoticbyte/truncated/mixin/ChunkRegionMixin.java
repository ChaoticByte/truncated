package net.chaoticbyte.truncated.mixin;

import net.chaoticbyte.truncated.Truncated;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.StructureWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkRegion.class)
public abstract class ChunkRegionMixin implements StructureWorldAccess {

    @Inject(
            method = "isValidForSetBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    public void isValidForSetBlock(BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
        var limit = Truncated.getLimit();
        var x = pos.getX() / 16;
        var z = pos.getZ() / 16;
        if (x > limit || x < -limit || z > limit || z < -limit) {
            ci.setReturnValue(false);
        }
    }
}
