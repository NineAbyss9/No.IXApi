
package com.bilibili.player_ix.noixmod_api.register;

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
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@PAMAreNonnullByDefault
@Mod.EventBusSubscriber(modid = NoixmodAPI.MOD_ID)
public class NoixmodAPIEvents {
    private NoixmodAPIEvents() {
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
        if (mob == null)
            return;
        Level level = mob.level();
        if (mob instanceof Villager villager && Math.random() < 0.5) {
            if (!level.isClientSide) {
                ServerLevel serverLevel = (ServerLevel)level;
                int i = serverLevel.random.nextInt(11);
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
    }

    @SubscribeEvent
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        Mob mob = event.getEntity();
        ServerLevel serverLevel = event.getLevel().getLevel();
        if (mob instanceof Villager villager && serverLevel.random.nextInt(3) == 0
                && !mob.isBaby() && event.getSpawnType().equals(MobSpawnType.STRUCTURE)) {
            int i = serverLevel.random.nextInt(11);
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
                villager.discard();
                event.setCanceled(true);
            }
        } else if (mob instanceof WanderingTrader trader && serverLevel.random.nextInt(3) == 0
                && trader.getSpawnType() == MobSpawnType.EVENT && NoixmodAPIMainConfig.IntruderWillSpawn.get()) {
            Intruder intruder = NoixmodAPIEntities.INTRUDER.get().create(serverLevel);
            if (intruder != null) {
                intruder.moveTo(trader.position());
                intruder.setBoss(true);
                intruder.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(trader.blockPosition()),
                        MobSpawnType.EVENT, null, null);
                serverLevel.addFreshEntity(intruder);
                trader.setRemoved(Entity.RemovalReason.KILLED);
                event.setCanceled(true);
            }
        }
    }

    public static void playerInteract(PlayerInteractEvent.EntityInteract event) {
        Level level = event.getLevel();
        ItemStack stack = event.getItemStack();
        Entity entity = event.getTarget();
        if (entity.getType().is(NoixmodAPITags.SILVER_FISHES)
                && stack.is(NoixmodAPIItems.WORM_REAGENT.get())) {
            Worm worm = new Worm(NoixmodAPIEntities.WORM.get(), level);
            worm.moveTo(entity.position());
            level.addFreshEntity(worm);
            entity.discard();
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
        LivingEntity living = event.getEntity();
        DamageSource source = event.getSource();
        Entity entity = source.getEntity();
        List<Mourner> mourners = living.level().getEntitiesOfClass(Mourner.class, living.getBoundingBox()
                .inflate(8));
        if (!mourners.isEmpty()) {
            boolean flag = true;
            for (Mourner mourner : mourners) {
                if (living instanceof AbstractIllager) {
                    mourner.setDeadPlus();
                }
                if (entity instanceof Player player && flag) {
                    player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, Maths.toTick(5) *
                            Math.min(mourner.getDead(), 5), 0));
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

    @SuppressWarnings("all")
    @SubscribeEvent
    public static void onLivingJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        Level level = event.getLevel();
        if (entity instanceof AbstractIllager illager) {
            illager.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(illager,
                    LivingEntity.class, true, livingEntity -> livingEntity instanceof ApiVillager));
        }/* else if (entity instanceof Villager villager && villager.getSpawnType()==MobSpawnType.STRUCTURE
        && villager.tickCount <= 2 && false) {
            if (level.getRandom().nextInt(4) == 0) {

                }
            }
        }*/
    }

    public static final Map<ServerLevel, NihilisticOrderSpawner> ORDER_SPAWNER = new HashMap<>();

    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        if (!event.getLevel().isClientSide()) {
            ORDER_SPAWNER.put((ServerLevel)event.getLevel(), new NihilisticOrderSpawner());
        }
    }

    @SubscribeEvent
    public static void onWorldEnd(LevelEvent.Unload event) {
        if (!event.getLevel().isClientSide()) {
            ORDER_SPAWNER.remove((ServerLevel)event.getLevel());
        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        /*TICKING_BALDLOON.removeIf(Objects::isNull);
        if (!TICKING_BALDLOON.isEmpty()) {
            for (Baldloon baldloon : TICKING_BALDLOON) {
                if (baldloon != null) {
                    baldloon.tick();
                }
            }
        }*/
        if(!event.level.isClientSide){
            ServerLevel serverLevel = (ServerLevel)event.level;
            NihilisticOrderSpawner orderSpawner = ORDER_SPAWNER.get(serverLevel);
            if (orderSpawner != null){
                orderSpawner.tick(serverLevel);
            }
        }
    }

    private static List<Healing> healings(LivingEntity entity) {
        return entity.level().getEntitiesOfClass(Healing.class, entity.getBoundingBox().inflate(30),
                healing -> ObjectUtil.nonnullEquals(healing.getOwner(), entity));
    }
}
