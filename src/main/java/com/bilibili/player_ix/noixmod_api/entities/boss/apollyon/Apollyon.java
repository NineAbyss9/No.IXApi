
package com.bilibili.player_ix.noixmod_api.entities.boss.apollyon;

import com.bilibili.player_ix.noixmod_api.magic.ISpell;
import com.bilibili.player_ix.noixmod_api.magic.Spells;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPathfinderMob;
import com.github.NineAbyss9.ix_api.api.mobs.IFlagMob;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.NineAbyss9.math.AbyssMath;

public class Apollyon
extends ApiPathfinderMob
implements IFlagMob
{
    private static final EntityDataAccessor<Integer> DATA_FLAG;
    private static final EntityDataAccessor<Boolean> DATA_SECOND;
    private final boolean nether;
    public static final Component APOLLYON_NAME;
    public static final ISpell blast_spell = Spells.LAVA_TRAP.get();
    public Apollyon(EntityType<? extends Apollyon> pEntityType, Level pLevel)
    {
        super(pEntityType, pLevel);
        this.nether = pLevel.dimension() == Level.NETHER;
    }

    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAG, 0);
        this.entityData.define(DATA_SECOND, Boolean.FALSE);
    }

    public void aiStep()
    {
        super.aiStep();
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.SMOKE, this.getRandomX(0.8),
                    this.getRandomY(), this.getRandomZ(0.8), AbyssMath.random(0.1),
                    0.1, AbyssMath.random(0.1));
            if (this.tickCount % 5 != 0) return;
            ParticleUtil.addFlatParticle(ParticleTypes.ASH, this, 1, 1);
            //if (this.tickCount % 2 != 0) return;
        }
    }

    protected void customServerAiStep()
    {
        super.customServerAiStep();
        if (nether) {
            if (this.tickCount % 20 == 0) {
                blast_spell.castSpell(this.serverLevel(), this);
            }
        }
    }

    protected void registerGoals()
    {
        super.registerGoals();

    }

    public int getFlag() {return this.entityData.get(DATA_FLAG);}
    public void setFlag(int i) {this.entityData.set(DATA_FLAG, i);}
    public Component getDisplayName() {return APOLLYON_NAME;}
    public Component getName() {return APOLLYON_NAME;}

    static {
        DATA_FLAG = SynchedEntityData.defineId(Apollyon.class, EntityDataSerializers.INT);
        DATA_SECOND = SynchedEntityData.defineId(Apollyon.class, EntityDataSerializers.BOOLEAN);
        APOLLYON_NAME = Component.literal("Apollyon").withStyle(ChatFormatting.DARK_RED,
                ChatFormatting.BOLD);
    }

    public static enum ApollyonStatus
    {
        IDLE,
        HOS,
        SCARING;
    }
}
