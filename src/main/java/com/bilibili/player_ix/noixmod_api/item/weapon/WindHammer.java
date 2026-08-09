
package com.bilibili.player_ix.noixmod_api.item.weapon;

import com.bilibili.player_ix.noixmod_api.item.core.IWindItem;
import com.bilibili.player_ix.noixmod_api.network.ApiNetwork;
import com.bilibili.player_ix.noixmod_api.network.packet.ClientSmashParticlePacket;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPISounds;
import com.bilibili.player_ix.noixmod_api.register.data.ApiItemProvider;
import com.github.NineAbyss9.ix_api.util.ItemUtil;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class WindHammer
extends DiggerItem
implements IWindItem, ApiItemProvider.Handed
{
    private static final Tier TIER = ItemUtil.getTier(999, 8.0F, 3.0F, 3,
            12, Ingredient.of(NoixmodAPIItems.WIND_ESSENCE.get()));
    protected final float speed = TIER.getSpeed();
    public WindHammer()
    {
        super(3.0F, -3.4F, TIER,
                BlockTags.MINEABLE_WITH_PICKAXE,
                new Item.Properties().rarity(Rarity.EPIC).craftRemainder(NoixmodAPIItems.WIND_ESSENCE.get()));
    }

    public float getDestroySpeed(ItemStack pStack, BlockState pState)
    {
        return pState.is(BlockTags.MINEABLE_WITH_PICKAXE) ? this.speed : 1.0F;
    }

    public boolean isCorrectToolForDrops(ItemStack pStack, BlockState pBlock)
    {
        return pBlock.is(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
    {
        this.fly(pLevel, pPlayer, pUsedHand);
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker)
    {
        if (canSmashAttack(pAttacker))
        {
            ServerLevel level = (ServerLevel)pAttacker.level();
            //pAttacker.setDeltaMovement(pAttacker.getDeltaMovement().with(Direction.Axis.Y, 0.01D));
            /*if (pAttacker instanceof ServerPlayer) {
                ServerPlayer player = (ServerPlayer)pAttacker;
                player.connection.send(new ClientboundSetEntityMotionPacket(player));
            }*/
            if (pTarget.onGround()) {
                /*if (pAttacker instanceof ServerPlayer) {
                    ServerPlayer player = (ServerPlayer)pAttacker;
                    player.setSpawnExtraParticlesOnFall(true);
                }*/
                Supplier<SoundEvent> sound = pAttacker.fallDistance > 5.0F ?
                        NoixmodAPISounds.MACE_SMASH_GROUND_HEAVY : NoixmodAPISounds.MACE_SMASH_GROUND;
                level.playSound((Player)null, pAttacker.getX(), pAttacker.getY(), pAttacker.getZ(), sound.get(),
                        pAttacker.getSoundSource(), 1.0F, 1.0F);
            } else {
                level.playSound((Player)null, pAttacker.getX(), pAttacker.getY(), pAttacker.getZ(),
                        NoixmodAPISounds.MACE_SMASH_AIR.get(), pAttacker.getSoundSource(), 1.0F, 1.0F);
            }
            knockback(level, pAttacker, pTarget);
            this.flyUp(level, pAttacker, InteractionHand.MAIN_HAND);
            return true;
        }
        return false;
    }

    //@SuppressWarnings("unchecked")
    public static void knockback(final Level level, final Entity attacker, final Entity entity) {
        //level.levelEvent(2013, entity.getOnPos(), 750);
        //((Decomposer<LevelAccessor, BlockPos, Integer>)Cache.get(2013)).accept(level, entity.blockPosition(), 750);
        if (attacker instanceof ServerPlayer) {
            ApiNetwork.sendToClient((ServerPlayer)attacker, new ClientSmashParticlePacket(
                    entity.getOnPos(), 750));
        }
        level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(3.5D),
                knockbackPredicate(attacker, entity)).forEach((nearby) -> {
            Vec3 direction = nearby.position().subtract(entity.position());
            double knockbackPower = getKnockbackPower(attacker, nearby, direction);
            Vec3 knockbackVector = direction.normalize().scale(knockbackPower);
            if (knockbackPower > 0.0D) {
                nearby.push(knockbackVector.x, 0.7D, knockbackVector.z);
                if (nearby instanceof ServerPlayer) {
                    ServerPlayer otherPlayer = (ServerPlayer)nearby;
                    otherPlayer.connection.send(new ClientboundSetEntityMotionPacket(otherPlayer));
                }
            }
        });
    }

    private static Predicate<LivingEntity> knockbackPredicate(final Entity attacker, final Entity entity) {
        return (nearby) -> {
            boolean notSpectator;
            boolean notPlayer;
            boolean notAlliedToPlayer;
            boolean var10000;
            label82: {
                notSpectator = !nearby.isSpectator();
                notPlayer = nearby != attacker && nearby != entity;
                notAlliedToPlayer = !attacker.isAlliedTo(nearby);
                if (nearby instanceof OwnableEntity animal) {
                    if (entity instanceof LivingEntity livingAttacker) {
                        if (animal.getOwner() == livingAttacker) {
                            var10000 = true;
                            break label82;
                        }
                    }
                }
                var10000 = false;
            }
            boolean notTamedByPlayer;
            label74: {
                notTamedByPlayer = !var10000;
                if (nearby instanceof ArmorStand armorStand) {
                    if (armorStand.isMarker()) {
                        break label74;
                    }
                }
                var10000 = true;
            }
            boolean notArmorStand;
            boolean withinRange;
            label68: {
                notArmorStand = var10000;
                withinRange = entity.distanceToSqr(nearby) <= Math.pow(3.5D, 2.0D);
                if (nearby instanceof Player player) {
                    if (player.isCreative() && player.getAbilities().flying) {
                        break label68;
                    }
                }
                var10000 = false;
            }
            boolean notFlyingInCreative = !var10000;
            return notSpectator && notPlayer && notAlliedToPlayer && notTamedByPlayer && notArmorStand &&
                    withinRange && notFlyingInCreative;
        };
    }

    private static double getKnockbackPower(final Entity attacker, final LivingEntity nearby, final Vec3 direction) {
        return (3.5D - direction.length()) * 0.7D * (attacker.fallDistance > 5.0F ? 2.0D : 1.0D) * (1.0D -
                nearby.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
    }

    public static float calculateMaceDamage(Player player, float baseDamage) {
        /*if (player.onGround()) return baseDamage;
        if (player.getDeltaMovement().y >= 0) return baseDamage;
        if (player.isSwimming() || player.isInWater()) return baseDamage;
        if (player.getAbilities().flying) return baseDamage;
        if (player.onClimbable()) return baseDamage;*/
        if (!canSmashAttack(player)) return baseDamage;
        float fallDistance = player.fallDistance;
        if (fallDistance <= 3.0F) {
            return baseDamage + 4.0F * fallDistance;
        } else if (fallDistance <= 8.0F) {
            return baseDamage + 12.0F + 2.0F * (fallDistance - 3.0F);
        } else {
            return baseDamage + 22.0F + fallDistance - 8.0F;
        }
        /*float extra = (fallDistance - 3.0F) * 0.5F;
        return baseDamage + extra;*/
    }

    /*public void postHurtEnemy(//final ItemStack itemStack, final LivingEntity mob,
                              final LivingEntity attacker) {
        if (canSmashAttack(attacker)) {
            attacker.resetFallDistance();
        }
    }*/

    public static boolean canSmashAttack(final LivingEntity attacker) {
        return attacker.fallDistance > 1.5F && !attacker.isFallFlying();
    }
}
