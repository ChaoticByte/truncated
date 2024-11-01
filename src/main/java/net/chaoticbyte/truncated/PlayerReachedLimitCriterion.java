package net.chaoticbyte.truncated;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.entity.LootContextPredicateValidator;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class PlayerReachedLimitCriterion extends AbstractCriterion<PlayerReachedLimitCriterion.Conditions> {

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, conditions -> true);
    }

    // practically unnecessary boilerplate:

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return PlayerReachedLimitCriterion.Conditions.CODEC;
    }

    public record Conditions() implements AbstractCriterion.Conditions {
        public static final Codec<PlayerReachedLimitCriterion.Conditions> CODEC = Codec.unit(new PlayerReachedLimitCriterion.Conditions());

        @Override
        public void validate(LootContextPredicateValidator validator) {}

        @Override
        public Optional<LootContextPredicate> player() {
            return Optional.empty();
        }
    }
}
