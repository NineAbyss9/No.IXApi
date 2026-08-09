
package com.bilibili.player_ix.noixmod_api.world.structure;

import com.github.NineAbyss9.ix_api.api.annotation.MaybeDeprecated;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Optional;

@MaybeDeprecated
public class SmallGatheringPlace
extends Structure {
    public SmallGatheringPlace(StructureSettings pSettings) {
        super(pSettings);
    }

    public Optional<GenerationStub> findGenerationPoint(GenerationContext pContext) {
        return Optional.empty();
    }

    public StructureType<?> type() {
        return null;
    }
}
