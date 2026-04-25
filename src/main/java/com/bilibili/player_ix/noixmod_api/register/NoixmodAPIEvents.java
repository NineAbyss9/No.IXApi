
package com.bilibili.player_ix.noixmod_api.register;

import com.bilibili.player_ix.noixmod_api.compat.Compatable;
import com.bilibili.player_ix.noixmod_api.entities.monster.horror.HuntedVillager;
import com.bilibili.player_ix.noixmod_api.entities.monster.horror.Tracker;
import com.bilibili.player_ix.noixmod_api.server.HorrorModeSavedData;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.HorrorLookAtEntityGoal;
import com.bilibili.player_ix.noixmod_api.world.HorrorModeManager;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.fml.ModList;
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
import org.NineAbyss9.math.MathSupport;

import java.util.*;

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
        if (entity instanceof AbstractIllager illager) {
            illager.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(illager,
                    LivingEntity.class, true, livingEntity -> livingEntity instanceof ApiVillager));
        }
        if (NoixmodAPIMainConfig.HorrorMode.get()) {
            if (entity instanceof Mob mob) {
                if (mob instanceof OwnableMob ownableMob) {
                    if (ownableMob.isHostile() || ownableMob instanceof Enemy) {
                        ownableMob.goalSelector.addGoal(3, new HorrorLookAtEntityGoal(ownableMob));
                    }
                } else {
                    mob.goalSelector.addGoal(3, new HorrorLookAtEntityGoal(mob));
                }
            }
            if (!NoixmodAPIMainConfig.disableXMinMap.get()) return;
            String xaerominimap = "xaerominimap";
            if (!ModList.get().isLoaded(xaerominimap)) return;
            var option = Compatable.mobEffect(xaerominimap, "no_entity_radar");
            if (option.isEmpty()) return;
            if (entity instanceof Player player) {
                player.addEffect(new MobEffectInstance(option.get(), -1, 0));
            }
        }
    }

    public static final Map<ServerLevel, NihilisticOrderSpawner> ORDER_SPAWNER = new HashMap<>();

    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        LevelAccessor accessor = event.getLevel();
        if (NoixmodAPIMainConfig.HorrorMode.get()) {
            if (accessor instanceof ServerLevel serverLevel) {
                //HorrorModeManager.horrorModeManagers.put(serverLevel, new HorrorModeManager(serverLevel));
                HorrorModeSavedData.load(serverLevel);
            }
        }
        if (!accessor.isClientSide() && accessor instanceof Level level
            && level.dimension() == Level.OVERWORLD) {
            ORDER_SPAWNER.put(((ServerLevel)event.getLevel()), new NihilisticOrderSpawner());
        }
    }

    @SubscribeEvent
    public static void onWorldEnd(LevelEvent.Unload event) {
        LevelAccessor accessor = event.getLevel();
        //HorrorModeManager.horrorModeManagers.remove((Level)accessor);
        if (!accessor.isClientSide() && accessor instanceof Level level) {
            ORDER_SPAWNER.remove((ServerLevel)level);
        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.level.isClientSide) return;
        ServerLevel serverLevel = (ServerLevel)event.level;
        if (HorrorModeManager.horrorModeEnabled()) {
            HorrorModeSavedData.load(serverLevel).tick(serverLevel);
        }
        NihilisticOrderSpawner orderSpawner = ORDER_SPAWNER.get(serverLevel);
        if (orderSpawner != null) {
            orderSpawner.tick(serverLevel);
        }
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity != null) {
            if (NoixmodAPIMainConfig.ApostleCanCancelLivingHeal.get() || NoixmodAPIMainConfig.HorrorMode.get()) {
                List<Apostle> apostles = entity.level().getEntitiesOfClass(Apostle.class, entity.getBoundingBox()
                        .inflate(64));
                if (!apostles.isEmpty()) {
                    for (Apostle apostle : apostles) {
                        if (MobUtils.canHurt(entity, apostle)) {
                            if ((apostle.getTarget() == entity || (entity instanceof Mob mob && mob.getTarget() == apostle))
                                    && apostle.getCancelHealTick() > 0) {
                                event.setAmount(0);
                                event.setCanceled(true);
                                break;
                            } else if (NoixmodAPIMainConfig.HorrorMode.get()) {
                                event.setAmount(0);
                                event.setCanceled(true);
                                break;
                            }
                        }
                    }
                }
            } else if (entity.hasEffect(NoixmodAPIMobEffects.NIHILISTIC.get())) {
                event.setAmount(0);
                event.setCanceled(true);
            }
        }
    }

    public static void onSpellCasts(SpellCastEvent event) {
        Spell.Type spellType = event.getSpellType();
    }

    @SubscribeEvent
    public static void onBabySpawn(BabyEntitySpawnEvent event) {
        Mob mob = event.getChild();
        if (mob == null) return;
        Level level = mob.level();
        if (!level.isClientSide && mob instanceof Villager villager && Math.random() < 0.5) {
            ServerLevel serverLevel = (ServerLevel)level;
            int i = MathSupport.random.nextInt(11);
            VillagerFighter fighter;
            if (i < 4) {
                fighter = new VillagerSpellcaster(NoixmodAPIEntities.VILLAGER_SPELLCASTER.get(), serverLevel);
            } else if (i < 6) {
                fighter = new VillagerMaster(NoixmodAPIEntities.VILLAGER_MASTER.get(), serverLevel);
            } else if (i < 8) {
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
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        Mob mob = event.getEntity();
        ServerLevel serverLevel = event.getLevel().getLevel();
        if (mob instanceof Villager villager && event.getSpawnType() == MobSpawnType.STRUCTURE) {
            if (NoixmodAPIMainConfig.SpawnHorror.get()) {
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
            } else if (MathSupport.random.nextFloat() < 0.25F
                    && !mob.isBaby()) {
                int i = MathSupport.random.nextInt(11);
                VillagerFighter fighter;
                if (i < 4) {
                    fighter = new VillagerSpellcaster(NoixmodAPIEntities.VILLAGER_SPELLCASTER.get(), serverLevel);
                } else if (i < 6) {
                    fighter = new VillagerMaster(NoixmodAPIEntities.VILLAGER_MASTER.get(), serverLevel);
                } else if (i < 8) {
                    fighter = new VillagerEvoker(NoixmodAPIEntities.VILLAGER_EVOKER.get(), serverLevel);
                } else {
                    fighter = new Ambusher(NoixmodAPIEntities.AMBUSHER.get(), serverLevel);
                }
                fighter.moveTo(villager.blockPosition(), 0, 0);
                fighter.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(villager.blockPosition()),
                        MobSpawnType.STRUCTURE, null, null);
                if (serverLevel.addFreshEntity(fighter)) {
                    event.setSpawnCancelled(true);
                    villager.discard();
                    event.setCanceled(true);
                }
            }
        } else if (mob instanceof WanderingTrader trader && MathSupport.random.nextFloat() < 0.25F
                && event.getSpawnType() == MobSpawnType.EVENT && NoixmodAPIMainConfig.IntruderWillSpawn.get()) {
            Intruder intruder = NoixmodAPIEntities.INTRUDER.get().create(serverLevel);
            if (intruder != null) {
                intruder.moveTo(trader.position());
                intruder.setBoss(true);
                intruder.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(trader.blockPosition()),
                        MobSpawnType.EVENT, null, null);
                serverLevel.addFreshEntity(intruder);
                event.setSpawnCancelled(true);
                trader.setRemoved(Entity.RemovalReason.KILLED);
                event.setCanceled(true);
            }
        } else if (mob instanceof Allay allay && MathSupport.random.nextBoolean() &&
                event.getSpawnType() == MobSpawnType.STRUCTURE) {
            Healing healing = NoixmodAPIEntities.HEALING.get().create(serverLevel);
            if (healing != null) {
                healing.moveTo(allay.position());
                if (serverLevel.addFreshEntity(healing)) {
                    event.setSpawnCancelled(true);
                    allay.discard();
                    event.setCanceled(true);
                }
            }
        }
    }

    //private static boolean willSpawn;

    @SubscribeEvent
    public static void playerDestroyBlock(BlockEvent.BreakEvent event) {
        //willSpawn = MathSupport.random.nextFloat() < 0.3F;
        if (!HorrorModeManager.spawnTerribleMobs()) return;
        var level = event.getLevel();
        if (level.isClientSide()) return;
        var pos = event.getPos();
        float c = 0.005F;
        if (level.canSeeSky(pos)) c = 0.00005F;
        if (MathSupport.random.nextFloat() > c) return;
        var player = event.getPlayer();
        if (player == null || player.isCreative()) return;
        if (!level.getEntitiesOfClass(Tracker.class, player.getBoundingBox().inflate(16)).isEmpty()) return;
        var d = player.getDirection();
        var spawnPos = pos.relative(d);
        var tracker = NoixmodAPIEntities.TRACKER.get().create((Level)level);
        if (tracker == null) return;
        level.destroyBlock(spawnPos, true);
        if (spawnPos.getY() <= player.getY()) {
            level.destroyBlock(spawnPos.above(), true);
        } else {
            level.destroyBlock(spawnPos.below(), true);
        }
        tracker.setLife(80);
        tracker.moveTo(spawnPos, 0, 0);
        tracker.getLookControl().setLookAt(player, 30F, 30F);
        level.addFreshEntity(tracker);
    }

    @SubscribeEvent
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
            } else if (stack.is(NoixmodAPIItems.WORM_REAGENT.get()))
            {
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
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity hurt = event.getEntity();
        Entity pEntity = event.getSource().getEntity();
        if (pEntity instanceof Apostle apostle && MobUtils.canHurt(hurt, apostle)) {
            apostle.setCancelHealTick(Math.max(apostle.getCancelHealTick(), 30));
        }
        if (!VillagerFangs.canDamage(hurt, pEntity)) {
            event.setAmount(0);
            event.setCanceled(true);
            return;
        }
        List<Healing> healings = healings(hurt);
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
        if (death instanceof Player && NoixmodAPIMainConfig.HorrorMode.get()) {
            var human = NoixmodAPIEntities.THE_HUMAN.get().create(level);
            if (human == null) return;
            human.moveTo(death.position());
            level.addFreshEntity(human);
            return;
        }
        List<Mourner> mourners = death.level().getEntitiesOfClass(Mourner.class, death.getBoundingBox()
                .inflate(8));
        if (!mourners.isEmpty()) {
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

    @SubscribeEvent
    public static void onLivingTarget(LivingChangeTargetEvent event) {
        LivingEntity target =  event.getNewTarget();
        LivingEntity entity = event.getEntity();
        List<VillagerFighter> fighters = entity.level().getEntitiesOfClass(VillagerFighter.class, entity.getBoundingBox()
                .inflate(19));
        if (!fighters.isEmpty() && target instanceof AbstractVillager) {
            for (VillagerFighter fighter : fighters) {
                if (fighter.getTarget() == null || !fighter.isAggressive()) {
                    fighter.setTarget(entity);
                }
            }
        }
    }

    private static List<Healing> healings(LivingEntity entity) {
        return entity.level().getEntitiesOfClass(Healing.class, entity.getBoundingBox().inflate(30),
                healing -> ObjectUtil.nonnullEquals(healing.getOwner(), entity));
    }
}
