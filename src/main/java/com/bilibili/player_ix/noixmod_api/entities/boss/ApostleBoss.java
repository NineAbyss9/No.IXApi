
package com.bilibili.player_ix.noixmod_api.entities.boss;

import com.github.NineAbyss9.ix_api.api.mobs.ApiNihilisticBoss;
import org.NineAbyss9.util.Option;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
/*import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;*/
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ApostleBoss
extends Apostle
implements ApiNihilisticBoss {
    public ApostleBoss(EntityType<ApostleBoss> apostle, Level world) {
        super(apostle, world);
        this.xpReward = world.dimension() == Level.END ? XP_APOSTLE_HARD : XP_APOSTLE;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public boolean isBoss() {
        return true;
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

    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide && NoixmodAPIMainConfig.HorrorMode.get()) {
            ParticleUtil.addRedStoneParticle(this, this.getRandomX(1), this.getRandomY(),
                    this.getRandomZ(1), 0, 0, 0);
        }
    }

    public void baseTick() {
        if (this.firstTick) {
            if (this.level().isClientSide) {
                Component component = Option.of(Component.literal("Apostle")
                                .withStyle(ChatFormatting.DARK_RED, ChatFormatting.OBFUSCATED))
                        .ifOrElse(NoixmodAPIMainConfig.HorrorMode.get(),
                                Component.literal("Apostle")
                                        .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.OBFUSCATED));
                Minecraft.getInstance().gui.setTitle(component);
            } else {
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

    public boolean canChangeDimensions()
    {
        return false;
    }

    public boolean isHostile() {
        return true;
    }
}
