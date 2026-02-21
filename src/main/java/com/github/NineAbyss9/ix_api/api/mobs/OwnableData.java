
package com.github.NineAbyss9.ix_api.api.mobs;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class OwnableData {
    private Flag flag = Flag.WANDERING;
    public final Ownable ownable;
    public OwnableData(Ownable pOwnable) {
        this.ownable = pOwnable;
    }

    public int getFlag() {
        return this.flag.id;
    }

    public void setFlag(int pFlag) {
        this.flag = Flag.byId(pFlag);
    }

    public void nextFlag() {
        this.setFlag(this.flag.id + 1);
        if (this.ownable instanceof Entity entity) {
            entity.playSound(SoundEvents.UI_BUTTON_CLICK.get());
            //if (entity.level().isClientSide) {
                Minecraft.getInstance().gui.setOverlayMessage(Component.translatable(
                        "info.noixmodapi.ownable_data.next", entity.getDisplayName(), Component.translatable(
                                "info.noixmodapi.ownable_data." + this.flag.id)), false);
            //}
        }
    }

    public boolean nextFlag(Player player, InteractionHand hand) {
        return player == this.ownable.getOwner() && player.getItemInHand(hand).isEmpty();
    }

    public boolean isFollowing() {
        return flag == Flag.FOLLOWING;
    }

    public boolean isStaying() {
        return flag == Flag.STAYING;
    }

    public void addOwnableAdditionalSaveData(CompoundTag tag) {
        tag.putInt("OwnableFlag", this.flag.id);
    }

    public void readOwnableAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("OwnableFlag")) {
            this.setFlag(tag.getInt("OwnableFlag"));
        }
    }

    public enum Flag {
        WANDERING(0),
        FOLLOWING(1),
        STAYING(2);

        public final int id;
        Flag(int pId) {
            id = pId;
        }

        public static Flag byId(int pId) {
            switch (pId) {
                case 1 -> {
                    return Flag.FOLLOWING;
                }
                case 2 -> {
                    return Flag.STAYING;
                }
                default -> {
                    return Flag.WANDERING;
                }
            }
        }
    }
}
