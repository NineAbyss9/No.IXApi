
package com.bilibili.player_ix.noixmod_api.entities.boss;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiNihilisticBoss;
import com.github.NineAbyss9.ix_api.ix_api.util.Option;
import com.github.NineAbyss9.ix_api.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

public class ApostleBoss
extends Apostle
implements ApiNihilisticBoss {
    private static final EntityDataAccessor<Float> APOSTLE_HEALTH;
    public ApostleBoss(EntityType<ApostleBoss> apostle, Level world) {
        super(apostle, world);
    }

    @SuppressWarnings("unused")
    public ApostleBoss(PlayMessages.SpawnEntity entity, Level world) {
        this(NoixmodAPIEntities.APOSTLE.get(), world);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(APOSTLE_HEALTH, 320F);
    }

    public boolean isBoss() {
        return true;
    }

    public int getExperienceReward() {
        if (this.isInEnd()) {
            return XP_APOSTLE_HARD;
        } else {
            return XP_APOSTLE;
        }
    }

    public static Component normal(String st) {
        return Component.literal(st).withStyle(ChatFormatting.DARK_PURPLE);
    }

    public static Component horror(String st) {
        if (NoixmodAPIMainConfig.HorrorMode.get()) {
            return Component.literal(st).withStyle(ChatFormatting.DARK_RED, ChatFormatting.OBFUSCATED);
        }
        return Component.literal(st).withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.OBFUSCATED);
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide && NoixmodAPIMainConfig.HorrorMode.get()) {
            ParticleUtil.addRedStoneParticle(this, this.getRandomX(1), this.getRandomY(),
                    this.getRandomZ(1), 0, 0, 0);
        }
    }

    public void baseTick() {
        if (this.firstTick) {
            Component component = Option.of(Component.literal("Απόστολος")
                            .withStyle(ChatFormatting.DARK_RED, ChatFormatting.OBFUSCATED))
                    .ifOrElse(NoixmodAPIMainConfig.HorrorMode.get(),
                            Component.literal("Απόστολος")
                                    .withStyle(ChatFormatting.DARK_PURPLE));
            Minecraft.getInstance().gui.setTitle(component);
            if (!this.level().isClientSide) {
                this.sendSystemMessage(Component.literal("We will have a great time!")
                        .withStyle(NoixmodAPIMainConfig.HorrorMode.get() ? ChatFormatting.DARK_RED :
                                        ChatFormatting.DARK_PURPLE, ChatFormatting.OBFUSCATED));
            }
        }
        super.baseTick();
    }

    public Component getDisplayName() {
        return Option.of(horror("Apostle")).ifOrElse(NoixmodAPIMainConfig.HorrorMode.get(),
                Component.translatable("title.noixmodapi.apostle_" + this.getTitleNumber())
                        .withStyle(ChatFormatting.DARK_PURPLE));
    }

    public boolean isHostile() {
        return true;
    }

    static {
        APOSTLE_HEALTH = SynchedEntityData.defineId(ApostleBoss.class, EntityDataSerializers.FLOAT);
    }
}
