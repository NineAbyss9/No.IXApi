
package com.bilibili.player_ix.noixmod_api.register;

import com.bilibili.player_ix.noixmod_api.entities.monster.horror.HuntedVillager;
import com.bilibili.player_ix.noixmod_api.entities.monster.horror.Tracker;
import com.bilibili.player_ix.noixmod_api.item.enchantment.Pioneer;
import com.bilibili.player_ix.noixmod_api.item.weapon.WindHammer;
import com.bilibili.player_ix.noixmod_api.server.HorrorModeSavedData;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.HorrorLookAtEntityGoal;
import com.bilibili.player_ix.noixmod_api.world.ApiSavedData;
import com.bilibili.player_ix.noixmod_api.world.HorrorModeManager;
import com.github.NineAbyss9.ix_api.api.mobs.effect.EffectInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.level.BlockEvent;
import org.NineAbyss9.annotation.PAMAreNonnullByDefault;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.boss.Apostle;
import com.bilibili.player_ix.noixmod_api.entities.monster.illager.Intruder;
import com.bilibili.player_ix.noixmod_api.entities.monster.illager.Mourner;
import com.bilibili.player_ix.noixmod_api.entities.monster.nihilist.NihilisticOrderSpawner;
import com.bilibili.player_ix.noixmod_api.entities.projectile.VillagerFangs;
import com.bilibili.player_ix.noixmod_api.entities.servant.Healing;
import com.bilibili.player_ix.noixmod_api.entities.servant.worm.Worm;
import com.bilibili.player_ix.noixmod_api.entities.villager.*;
import com.bilibili.player_ix.noixmod_api.magic.Spell;
import com.bilibili.player_ix.noixmod_api.register.event.SpellCastEvent;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.api.mobs.ApiVillager;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.ObjectUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.npc.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@PAMAreNonnullByDefault
@Mod.EventBusSubscriber(modid = NoixmodAPI.MOD_ID)
public class NoixmodAPIEvents {
    private NoixmodAPIEvents() {
    }

    @SuppressWarnings("all")
    @SubscribeEvent
    public static void onLivingJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        Level level = event.getLevel();
        if (level.isClientSide) return;
        if (entity instanceof AbstractIllager illager) {
            illager.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(illager,
                    LivingEntity.class, true, livingEntity -> livingEntity instanceof ApiVillager));
        }
        if (NoixmodAPIMainConfig.HorrorMode.get()) {
            if (entity instanceof Mob mob) {
                if (!(mob instanceof Animal) && !(mob instanceof Enemy)) {
                    return;
                }
                mob.goalSelector.addGoal(4, new HorrorLookAtEntityGoal(mob));
            }
        }
    }

    private static final Map<Level, NihilisticOrderSpawner> ORDER_SPAWNER = new HashMap<>();

    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        LevelAccessor accessor = event.getLevel();
        if (!(accessor instanceof ServerLevel level)) return;
        HorrorModeSavedData.load(level);
        ApiSavedData.load(level);
        if (level.dimension() == Level.OVERWORLD) {
            ORDER_SPAWNER.put(level, new NihilisticOrderSpawner());
        }
    }

    public static void registerMobSpawns() {
        
    }

    @SubscribeEvent
    public static void onWorldEnd(LevelEvent.Unload event) {
        LevelAccessor accessor = event.getLevel();
        if (accessor instanceof ServerLevel serverLevel) {
            ORDER_SPAWNER.remove(serverLevel);
        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.level.isClientSide) return;
        ServerLevel serverLevel = (ServerLevel)event.level;
        if (HorrorModeManager.horrorModeEnabled()) {
            HorrorModeSavedData.get(serverLevel).tick(serverLevel);
        }
        NihilisticOrderSpawner orderSpawner = ORDER_SPAWNER.get(serverLevel);
        if (orderSpawner != null) {
            orderSpawner.tick(serverLevel);
        }
    }

    /*@SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event)
    {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;
        if (NoixmodAPIMainConfig.ApostleCanCancelLivingHeal.get() || NoixmodAPIMainConfig.HorrorMode.get()) {
            List<ApostleBoss> apostles = entity.level().getEntitiesOfClass(ApostleBoss.class, entity.getBoundingBox()
                    .inflate(48.0D));
            if (apostles.isEmpty()) {
                return;
            }
            for (Apostle apostle : apostles) {
                if (MobUtils.canHurt(entity, apostle)) {
                    if (NoixmodAPIMainConfig.HorrorMode.get()) {
                        event.setAmount(0);
                        event.setCanceled(true);
                        break;
                    } else if ((apostle.getTarget() == entity || (entity instanceof Mob mob && mob.getTarget() == apostle))
                            && apostle.getCancelHealTick() > 0) {
                        event.setAmount(0);
                        event.setCanceled(true);
                        break;
                    }
                }
            }
        } else if (entity.hasEffect(NoixmodAPIMobEffects.NIHILISTIC.get())) {
            event.setAmount(0);
            event.setCanceled(true);
        }
    }*/

    public static void onSpellCasts(SpellCastEvent event) {
        Spell.Type spellType = event.getSpellType();
    }

    @SubscribeEvent
    public static void onBabySpawn(BabyEntitySpawnEvent event) {
        Mob mob = event.getChild();
        if (mob == null) return;
        Level level = mob.level();
        if (level.isClientSide) return;
        if (mob instanceof Villager villager && ThreadLocalRandom.current().nextFloat() < 0.5F) {
            ServerLevel serverLevel = (ServerLevel)level;
            float f = ThreadLocalRandom.current().nextFloat();
            VillagerFighter fighter;
            if (f < 0.4F) {
                fighter = new VillagerSpellcaster(NoixmodAPIEntities.VILLAGER_SPELLCASTER.get(), serverLevel);
            } else if (f < 0.6F) {
                fighter = new VillagerMaster(NoixmodAPIEntities.VILLAGER_MASTER.get(), serverLevel);
            } else if (f < 0.8F) {
                fighter = new VillagerEvoker(NoixmodAPIEntities.VILLAGER_EVOKER.get(), serverLevel);
            } else {
                fighter = new Ambusher(NoixmodAPIEntities.AMBUSHER.get(), serverLevel);
            }
            fighter.moveTo(villager.blockPosition(), 0, 0);
            fighter.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(villager.blockPosition()),
                    MobSpawnType.BREEDING, null, null);
            fighter.setBaby(true);
            if (serverLevel.addFreshEntity(fighter)) {
                villager.discard();
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event)
    {
        Mob mob = event.getEntity();
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }
        if (mob instanceof Villager villager && event.getSpawnType() == MobSpawnType.STRUCTURE) {
            if (HorrorModeManager.ENABLED_SPAWN) {
                HuntedVillager huntedVillager = NoixmodAPIEntities.HUNTED_VILLAGER.get().create(serverLevel);
                if (huntedVillager == null) return;
                huntedVillager.moveTo(villager.position());
                huntedVillager.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(villager.blockPosition()),
                        MobSpawnType.STRUCTURE);
                if (serverLevel.addFreshEntity(huntedVillager)) {
                    villager.discard();
                    event.setSpawnCancelled(true);
                    event.setCanceled(true);
                }
            } else if (NoixmodAPIMainConfig.VillagerFighterSpawn.get() &&
                    ThreadLocalRandom.current().nextFloat() < 0.25F && !mob.isBaby()) {
                int i = ThreadLocalRandom.current().nextInt(11);
                VillagerFighter fighter;
                if (i < 3) {
                    fighter = new VillagerSpellcaster(NoixmodAPIEntities.VILLAGER_SPELLCASTER.get(), serverLevel);
                } else if (i < 5) {
                    fighter = new VillagerMaster(NoixmodAPIEntities.VILLAGER_MASTER.get(), serverLevel);
                } else if (i < 7) {
                    fighter = new VillagerEvoker(NoixmodAPIEntities.VILLAGER_EVOKER.get(), serverLevel);
                } else if (i < 9) {
                    fighter = new Exorcist(NoixmodAPIEntities.EXORCIST.get(), serverLevel);
                } else {
                    fighter = new Ambusher(NoixmodAPIEntities.AMBUSHER.get(), serverLevel);
                }
                fighter.moveTo(villager.blockPosition(), 0, 0);
                fighter.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(villager.blockPosition()),
                        MobSpawnType.STRUCTURE, null, null);
                if (serverLevel.addFreshEntity(fighter)) {
                    villager.discard();
                    event.setSpawnCancelled(true);
                    event.setCanceled(true);
                }
            }
        } else if (mob instanceof WanderingTrader trader && ThreadLocalRandom.current().nextFloat() < 0.25F
                && event.getSpawnType() == MobSpawnType.EVENT && NoixmodAPIMainConfig.IntruderWillSpawn.get()) {
            Intruder intruder = NoixmodAPIEntities.INTRUDER.get().create(serverLevel);
            if (intruder == null) {
                return;
            }
            intruder.moveTo(trader.position());
            intruder.setBoss(true);
            intruder.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(trader.blockPosition()),
                    MobSpawnType.EVENT, null, null);
            serverLevel.addFreshEntity(intruder);
            event.setSpawnCancelled(true);
            trader.setRemoved(Entity.RemovalReason.KILLED);
            event.setCanceled(true);
        } else if (mob instanceof Allay allay && ThreadLocalRandom.current().nextBoolean() &&
                event.getSpawnType() == MobSpawnType.STRUCTURE) {
            Healing healing = NoixmodAPIEntities.HEALING.get().create(serverLevel);
            if (healing == null) {
                return;
            }
            healing.moveTo(allay.position());
            if (serverLevel.addFreshEntity(healing)) {
                event.setSpawnCancelled(true);
                allay.discard();
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDestroyBlock(BlockEvent.BreakEvent event) {
        if (!HorrorModeManager.spawnTerribleMobs()) return;
        var level = event.getLevel();
        if (level.isClientSide()) return;
        var pos = event.getPos();
        float c = 0.01F;
        if (level.canSeeSky(pos)) c = 0.00005F;
        if (ThreadLocalRandom.current().nextFloat() > c) return;
        var player = event.getPlayer();
        if (player == null || player.isCreative()) return;
        if (!level.getEntitiesOfClass(Tracker.class, player.getBoundingBox().inflate(16)).isEmpty()) return;
        var d = player.getDirection();
        var spawnPos = pos.relative(d);
        var tracker = NoixmodAPIEntities.TRACKER.get().create((Level)level);
        if (tracker == null) return;
        player.addEffect(EffectInstance.create(MobEffects.BLINDNESS, 30, 0));
        level.destroyBlock(spawnPos, true);
        if (spawnPos.getY() <= player.getY()) {
            level.destroyBlock(spawnPos.above(), true);
        } else {
            level.destroyBlock(spawnPos.below(), true);
        }
        tracker.setCave();
        tracker.setLife(80);
        tracker.moveTo(spawnPos, 0, 0);
        tracker.getLookControl().setLookAt(player, 30F, 30F);
        level.addFreshEntity(tracker);
        player.playSound(SoundEvents.AMBIENT_CAVE.value());
        BlockPos second = player.blockPosition().relative(d.getOpposite());
        if (level.getBlockState(second).isAir()) {
            var newTracker = NoixmodAPIEntities.TRACKER.get().create((Level)level);
            if (newTracker == null) return;
            level.destroyBlock(second, true);
            if (second.getY() <= player.getY()) {
                level.destroyBlock(second.above(), true);
            } else {
                level.destroyBlock(second.below(), true);
            }
            newTracker.setCanAttack();
            newTracker.setLife(80);
            newTracker.moveTo(second, 0, 0);
            newTracker.getLookControl().setLookAt(player, 30F, 30F);
            level.addFreshEntity(newTracker);
        }
    }

    //@SubscribeEvent
    public static void playerInteract(PlayerInteractEvent.EntityInteract event) {
        Level level = event.getLevel();
        if (level.isClientSide) return;
        var player = event.getEntity();
        ItemStack stack = event.getItemStack();
        Entity entity = event.getTarget();
        if (entity.getType().is(NoixmodAPITags.SILVER_FISHES)) {
            if (stack.is(Items.STONE)) {
                var fish = NoixmodAPIEntities.SILVERFISH_SERVANT.get().create(level);
                if (fish == null) return;
                fish.moveTo(player.position());
                fish.setOwner(player);
                if (level.addFreshEntity(fish)) {
                    entity.discard();
                    event.setCanceled(true);
                }
            } else if (stack.is(NoixmodAPIItems.WORM_REAGENT.get())) {
                Worm worm = new Worm(NoixmodAPIEntities.WORM.get(), level);
                worm.moveTo(entity.position());
                if (level.addFreshEntity(worm)) {
                    entity.discard();
                    event.setCanceled(true);
                }
            }
        }
    }

    public static void onEffectAdd(MobEffectEvent.Added event) {
        MobEffectInstance instance = event.getEffectInstance();
        MobEffect effect = instance.getEffect();
    }

    @SubscribeEvent
    public static void onChangeArmor(LivingEquipmentChangeEvent event)
    {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack newStack = player.getMainHandItem();
        ItemStack oldStack = event.getFrom();
        if (ItemStack.isSameItemSameTags(newStack, oldStack)) return;
        updateSpeedModifier(player);
    }

    public static void updateSpeedModifier(Player player)
    {
        ItemStack mainHand = player.getMainHandItem();
        AttributeInstance speedAttribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttribute == null) return;
        speedAttribute.removeModifier(Pioneer.MODIFIER_UUID);
        int enchantmentLevel = mainHand.getEnchantmentLevel(ApiEnchantments.PIONEER.get());
        if (enchantmentLevel <= 0) {
            return;
        }
        AttributeModifier modifier = new AttributeModifier(
                Pioneer.MODIFIER_UUID,
                "Pioneer Speed Boost",
                (double)enchantmentLevel * 0.1D,
                AttributeModifier.Operation.MULTIPLY_TOTAL);
        speedAttribute.addPermanentModifier(modifier);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity hurt = event.getEntity();
        Entity pEntity = event.getSource().getEntity();
        if (pEntity instanceof Player player) {
            ItemStack stack = player.getMainHandItem();
            int level;
            if (stack.getItem() instanceof WindHammer)
            {
                event.setAmount(WindHammer.calculateMaceDamage(player, event.getAmount()));
            }
            level = stack.getEnchantmentLevel(ApiEnchantments.PIONEER.get());
            float attackStrengthScale = player.getAttackStrengthScale(1.0F);
            float damage = event.getAmount();
            if (level > 0 && attackStrengthScale > 0.6F && player.fallDistance <= 0.1F) {
                event.setAmount(damage + damage * (float)level * 0.3F);
                return;
            }
            level = stack.getEnchantmentLevel(ApiEnchantments.HEAVY_STRIKE.get());
            if (level <= 0) {
                return;
            }
            if (player.fallDistance <= 0.0F)
            {
                return;
            }
            if (stack.getItem() instanceof WindHammer)
            {
                event.setAmount(damage + damage * level * 0.35F + 2.0F);
                return;
            }
            if (attackStrengthScale <= 0.9F) {
                return;
            }
            event.setAmount(damage + damage * level * 0.25F + 1.0F);
            return;
        }
        if (pEntity instanceof Apostle apostle && MobUtils.canHurt(hurt, apostle)) {
            apostle.setCancelHealTick(Math.max(apostle.getCancelHealTick(), 30));
        }
        if (!VillagerFangs.canDamage(hurt, pEntity)) {
            event.setAmount(0);
            event.setCanceled(true);
            return;
        }
        List<Healing> healings = healings(hurt);
        if (healings.isEmpty()) return;
        healings.stream().findAny().ifPresent(healing -> {
            healing.hurt(event.getSource(), event.getAmount());
            hurt.heal(event.getAmount());
            if (!hurt.level().isClientSide) {
                ParticleUtil.addParticleAroundSelf(hurt, ParticleTypes.HEART, 6);
            }
            event.setCanceled(true);
        });
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity death = event.getEntity();
        DamageSource source = event.getSource();
        Entity entity = source.getEntity();
        Level level = death.level();
        if (level.isClientSide) return;
        if (death instanceof Player && HorrorModeManager.ENABLED_SPAWN) {
            var human = NoixmodAPIEntities.THE_HUMAN.get().create(level);
            if (human == null) return;
            human.moveTo(death.position());
            level.addFreshEntity(human);
            return;
        }
        List<Mourner> mourners = death.level().getEntitiesOfClass(Mourner.class, death.getBoundingBox()
                .inflate(8));
        if (mourners.isEmpty()) {
            return;
        }
        boolean flag = true;
        for (Mourner mourner : mourners) {
            if (death instanceof AbstractIllager) {
                mourner.setDeathPlus();
            }
            if (entity instanceof Player player && flag) {
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, Maths.toTick(5) *
                        Math.min(mourner.getDeath(), 5), 0));
                flag = false;
            }
        }
    }

    public static void onPlayerEnters(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        CompoundTag data;
        CompoundTag tag = player.getPersistentData();
        if (!tag.contains(Player.PERSISTED_NBT_TAG)) {
            data = new CompoundTag();
        } else {
            data = tag.getCompound(Player.PERSISTED_NBT_TAG);
        }
        if (NoixmodAPIMainConfig.HorrorMode.get()) {
            if (!tag.getBoolean("Noixapi.hasStartNote")) {
                data.putBoolean("Noixapi.hasStartNote", true);
                tag.put(Player.PERSISTED_NBT_TAG, data);
                if (!player.isCreative()) {
                    player.addItem(new ItemStack(NoixmodAPIItems.NIHILISTIC_LORD_S_NOTE.get()));
                }
            }
        }
    }

    //@SubscribeEvent
    public static void onLivingTarget(LivingChangeTargetEvent event) {
        LivingEntity target =  event.getNewTarget();
        LivingEntity entity = event.getEntity();
        List<VillagerFighter> fighters = entity.level().getEntitiesOfClass(VillagerFighter.class, entity.getBoundingBox()
                .inflate(19));
        if (fighters.isEmpty()) return;
        if (!(target instanceof AbstractVillager)) {
            return;
        }
        for (VillagerFighter fighter : fighters) {
            if (fighter.getTarget() == null || !fighter.isAggressive()) {
                fighter.setTarget(entity);
            }
        }
    }

    private static List<Healing> healings(LivingEntity entity) {
        return entity.level().getEntitiesOfClass(Healing.class, entity.getBoundingBox().inflate(30),
                healing -> ObjectUtil.nonnullEquals(healing.getOwner(), entity));
    }
}
