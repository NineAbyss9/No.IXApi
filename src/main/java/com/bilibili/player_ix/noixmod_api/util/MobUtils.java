
package com.bilibili.player_ix.noixmod_api.util;

import org.NineAbyss9.annotation.PAMAreNonnullByDefault;
import com.github.NineAbyss9.ix_api.api.mobs.IShieldUser;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import com.github.NineAbyss9.ix_api.api.mobs.ApiVillager;
import com.github.NineAbyss9.ix_api.api.mobs.Ownable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import org.NineAbyss9.math.AbyssMath;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

@PAMAreNonnullByDefault
public record MobUtils(Entity entity) {
    public static final Predicate<LivingEntity> avoidEntityPredicate_RangedEnemy;

    public void floatOnLava() {
        floatOnLava(this.entity);
    }

    public static void floatOnLava(Entity entity) {
        floatFluid(entity, 1);
    }

    public static void floatFluid(Entity entity, int i) {
        if (i == 0) {
            if (entity.isInWater()) {
                entity.getDeltaMovement().scale(0.5).add(0, 0.05, 0);
            }
        }
        if (i == 1) {
            if (entity.isInLava()) {
                entity.getDeltaMovement().scale(0.5).add(0, 0.1, 0);
            }
        }
    }

    public static void forceLook(Mob pLooker, Entity pTarget) {
        pLooker.getLookControl().setLookAt(pTarget, 200F, pLooker.getMaxHeadXRot());
        Vec3 pos = pTarget.position();
        double d2 = pos.x - pLooker.getX();
        double d1 = pos.z - pTarget.getZ();
        float rotate = -((float) Mth.atan2(d2, d1)) * (180F / Maths.PI);
        pLooker.setYRot(rotate);
        pLooker.yBodyRot = rotate;
        pLooker.yHeadRot = rotate;
    }

    @SuppressWarnings("deprecation")
    public static <T extends Mob> void registerSpawn(EntityType<T> p_21755_, SpawnPlacements.Type p_21756_,
                                                     Heightmap.Types p_21757_, SpawnPlacements.SpawnPredicate<T> p_21758_) {
        SpawnPlacements.register(p_21755_, p_21756_, p_21757_, p_21758_);
    }

    public static boolean illagerSpawnPredicate(EntityType<? extends AbstractIllager> entityType, ServerLevelAccessor
            world, MobSpawnType reason, BlockPos blockPos, RandomSource random) {
        return Mob.checkMobSpawnRules(entityType, world, reason, blockPos, random) && blockPos.asLong() > 60;
    }

    public static <T extends Mob> SpawnPlacements.SpawnPredicate<T> monsterSpawnPredicate() {
        return (entityType, world, reason, pos, random) ->
                (world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random)
                        && Mob.checkMobSpawnRules(entityType, world, reason, pos, random));
    }

    public boolean isEasy() {
        return isEasy(this.entity);
    }

    public static boolean isEasy(Entity entity) {
        return entity.level().getDifficulty().equals(Difficulty.EASY);
    }

    public boolean isNormal() {
        return isNormal(this.entity);
    }

    public static boolean isNormal(Entity entity) {
        return entity.level().getDifficulty().equals(Difficulty.NORMAL);
    }

    public boolean isHard() {
        return isHard(this.entity);
    }

    public static boolean isHard(Entity entity) {
        return entity.level().getDifficulty().equals(Difficulty.HARD);
    }

    public static void healLiving(LivingEntity living, float amount) {
        if (living.isAlive()) {
            if (amount > 0) {
                float var = amount;
                var = Math.min(living.getMaxHealth() - living.getHealth(), var);
                living.setHealth(living.getHealth() + var);
            }
        }
    }

    @SuppressWarnings("all")
    public static boolean actuallyHurt(LivingEntity living, DamageSource source, float amount) {
        /*if (living.getHealth() - amount <= 0) {
            living.die(source);
            return;
        }*/
        try {
            living.walkAnimation.setSpeed(1.5F);
            Method actuallyHurt = LivingEntity.class.getDeclaredMethod("m_6475_", DamageSource.class, float.class);
            actuallyHurt.setAccessible(true);
            actuallyHurt.invoke(living, source, amount);
            living.hurtTime = 10;
            living.hurtDuration = 10;
            living.hurtMarked = true;
            if (living.isDeadOrDying()) {
                living.die(source);
                return false;
            }
        } catch (Exception ignore) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("all")
    public static EntityDataAccessor<Float> getHealth() {
        try {
            Field field = LivingEntity.class.getDeclaredField("f_20961_");
            field.setAccessible(true);
            Object value = field.get(null);
            if (value instanceof EntityDataAccessor<?>) {
                return (EntityDataAccessor<Float>)value;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isDead(LivingEntity living) {
        return living.getHealth() <= 0;
    }

    public static boolean isHalfHealth(LivingEntity living) {
        return living.getHealth() <= living.getMaxHealth() / 2;
    }

    @Nullable
    public static Entity hurtSource(DamageSource source) {
        return source.getDirectEntity() == null ? source.getEntity() : source.getDirectEntity();
    }

    public static boolean areAllies(Entity entity, Entity entity1) {
        if (entity instanceof Mob mob) {
            if (entity1 instanceof LivingEntity livingEntity) {
                if (mob.getTarget() == livingEntity) {
                    return false;
                }
            }
        }
        if (entity1 instanceof Mob mob) {
            if (entity instanceof LivingEntity livingEntity) {
                if (mob.getTarget() == livingEntity) {
                    return false;
                }
            }
        }
        return entity.isAlliedTo(entity1) || entity1.isAlliedTo(entity) || entity == entity1 ||
                entity.getTeam() == entity1.getTeam();
    }

    public static boolean ownableCanHurt(LivingEntity living, @Nullable Entity entity) {
        if (entity instanceof Ownable own) {
            if (own.isHostile()) {
                return living instanceof AbstractVillager || living instanceof AbstractGolem || living instanceof Player
                        || living instanceof ApiVillager;
            } else {
                return living instanceof Enemy || living instanceof Ownable ownable && ownable.isHostile();
            }
        } else {
            return MobUtils.canHurt(living, entity);
        }
    }

    public static boolean canHurt(@Nonnull LivingEntity entity, @Nullable Entity sourceMob) {
        if (sourceMob == entity) {
            return false;
        }
        if (sourceMob instanceof Ownable ownableMob) {
            if (entity.equals(ownableMob.getOwner())) {
                return false;
            }
            if (entity instanceof Ownable ownable && ObjectUtil.nonnullEquals(ownable.getOwner(),
                    ownableMob.getOwner())) {
                return false;
            }
        }
        if (entity instanceof Ownable ownableMob) {
            if (sourceMob != null && sourceMob.equals(ownableMob.getOwner())) {
                return false;
            }
            if (sourceMob instanceof Ownable ownable && ObjectUtil.nonnullEquals(ownableMob.getOwner(),
                    ownable.getOwner())) {
                return false;
            }
        }
        if (sourceMob instanceof TraceableEntity ownableMob) {
            if (ownableMob.getOwner() == entity) {
                return false;
            }
            if (entity instanceof TraceableEntity ownable && ObjectUtil.nonnullEquals(ownable.getOwner(),
                    ownableMob.getOwner())) {
                return false;
            }
        }
        if (entity instanceof TraceableEntity ownableMob) {
            if (sourceMob != null && ownableMob.getOwner() == sourceMob) {
                return false;
            }
            if (sourceMob instanceof TraceableEntity ownable && ObjectUtil.nonnullEquals(ownableMob.getOwner(),
                    ownable.getOwner())) {
                return false;
            }
        }
        if (entity instanceof OwnableEntity ownableEntity) {
            if (sourceMob != null && ObjectUtil.nonnullEquals(sourceMob.getUUID(), ownableEntity.getOwnerUUID())) {
                return false;
            }
            if (sourceMob instanceof OwnableEntity ownable && ObjectUtil.nonnullEquals(ownable.getOwnerUUID(),
                    ownable.getOwnerUUID())) {
                return false;
            }
        }
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)) {
            return false;
        }
        if (sourceMob != null) {
            CompoundTag targetTag = entity.getPersistentData();
            CompoundTag sourceTag = sourceMob.getPersistentData();
            String OWNER = "Owner";
            String OWNER_UUID = "OwnerUUID";
            String OWNER_Uuid = "OwnerUuid";
            if (targetTag.contains(OWNER)) {
                UUID uuid = targetTag.getUUID(OWNER);
                return sourceTag.contains(OWNER) && uuid.equals(sourceTag.getUUID(OWNER)) ||
                        sourceTag.contains(OWNER_UUID) && uuid
                                .equals(sourceTag.getUUID(OWNER_UUID)) ||
                        sourceTag.contains(OWNER_Uuid) && uuid.equals(sourceTag.getUUID(OWNER_Uuid));
            }
            if (targetTag.contains(OWNER_UUID)) {
                UUID uuid = targetTag.getUUID(OWNER_UUID);
                return sourceTag.contains(OWNER) && uuid.equals(sourceTag.getUUID(OWNER)) ||
                        sourceTag.contains(OWNER_UUID) && uuid
                                .equals(sourceTag.getUUID(OWNER_UUID)) ||
                        sourceTag.contains(OWNER_Uuid) && uuid.equals(sourceTag.getUUID(OWNER_Uuid));
            }
            if (targetTag.contains(OWNER_Uuid)) {
                UUID uuid = targetTag.getUUID(OWNER_Uuid);
                return sourceTag.contains(OWNER) && uuid.equals(sourceTag.getUUID(OWNER)) ||
                        sourceTag.contains(OWNER_UUID) && uuid
                                .equals(sourceTag.getUUID(OWNER_UUID)) ||
                        sourceTag.contains(OWNER_Uuid) && uuid.equals(sourceTag.getUUID(OWNER_Uuid));
            }
            Team team = sourceMob.getTeam();
            Team ea = entity.getTeam();
            if (ea != null && ea.equals(team)) {
                return false;
            }
        }
        return entity.isAlive() && !entity.isInvulnerable();
    }

    public static void push(double x, double y, double z, Entity entity, double xSpeed, double ySpeed, double zSpeed) {
        List<LivingEntity> list = WorldUtil.makeList(LivingEntity.class, entity, x, y, z);
        for (LivingEntity lie : list) {
            if (MobUtils.canHurt(lie, entity)) {
                lie.push(xSpeed, ySpeed, zSpeed);
            }
        }
    }

    public static boolean isAlive(Entity e) {
        if (e instanceof LivingEntity living) {
            return living.getHealth() > 0 && !living.isRemoved();
        } else {
            return e.isAlive();
        }
    }

    public static boolean rangeHurt(double x, double y, double z, Entity mob, DamageSource type, float damage, boolean flag) {
        List<LivingEntity> list = WorldUtil.makeList(LivingEntity.class, mob, x, y, z);
        for (LivingEntity lie : list) {
            if (flag) {
                lie.hurt(type, damage);
            } else {
                if (!canHurt(lie, mob))
                    continue;
                lie.hurt(type, damage);
            }
        }
        return false;
    }

    public static void rangeHurt(double x, double y, double z, Entity entity, DamageSource source,
                                 float damage, Predicate<LivingEntity> predicate) {
        List<LivingEntity> list = entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(x, y, z),
                predicate);
        for (LivingEntity lie : list) {
            if (predicate.test(lie)) {
                lie.hurt(source, damage);
            }
        }
    }

    public static void setAttributeValue(@Nullable AttributeInstance instance, double d) {
        if (instance != null) {
            instance.setBaseValue(d);
        }
    }

    public static boolean rangeHurt(double x, double y, double z, Entity mob, DamageSource type, float damage) {
        return MobUtils.rangeHurt(x, y, z, mob, type, damage, false);
    }

    public static void rangeHurtAndFire(double x, double y, double z, Entity mob, DamageSource type,
                                        float damage, int onFireTime) {
        List<LivingEntity> list = WorldUtil.makeList(LivingEntity.class, mob, x, y, z);
        for (LivingEntity lie : list) {
            if (mob instanceof LivingEntity living) {
                if (MobUtils.canHurt(lie, living)) {
                    lie.hurt(type, damage);
                    if (!lie.fireImmune()) {
                        lie.setSecondsOnFire(onFireTime);
                    }
                }
            } else {
                if (MobUtils.canHurt(lie, mob)) {
                    lie.hurt(type, damage);
                    if (!lie.fireImmune()) {
                        lie.setSecondsOnFire(onFireTime);
                    }
                }
            }
        }
    }

    public static void disableShield(double x, double y, double z, Entity mob) {
        List<Entity> players = mob.level().getEntitiesOfClass(Entity.class, mob.getBoundingBox().inflate(x, y, z));
        if (!players.isEmpty()) {
            for (Entity entityIn : players) {
                if (entityIn instanceof Player player) {
                    player.disableShield(true);
                } else if (entityIn instanceof IShieldUser user) {
                    user.disableShield(true);
                }
            }
        }
    }

    public static void disableShield(Entity pEntity, int pTicks) {
        if (pEntity instanceof Player player && player.isBlocking()) {
            if (!player.level().isClientSide) {
                player.getCooldowns().addCooldown(player.getUseItem().getItem(), pTicks);
                player.stopUsingItem();
                player.level().broadcastEntityEvent(player, AbyssMath.toByte(30));
            }
        } else if (pEntity instanceof IShieldUser user) {
            user.disableShield(true);
        }
    }

    //Based on L_Ender's code
    public static double calculateRange(LivingEntity livingEntity, DamageSource source) {
        return source.getEntity() != null ? livingEntity.distanceToSqr(source.getEntity()) : -1;
    }

    public static List<LivingEntity> getEntityLivingBaseNearby(LivingEntity livingEntity, double distanceX, double distanceY, double distanceZ, double radius) {
        return getEntitiesNearby(livingEntity, LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public static  <T extends Entity> List<T> getEntitiesNearby(LivingEntity livingEntity, Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return livingEntity.level().getEntitiesOfClass(entityClass, livingEntity.getBoundingBox().inflate(dX, dY, dZ), e -> e != livingEntity && livingEntity.distanceTo(e) <= r + e.getBbWidth() / 2.0F && e.getY() <= livingEntity.getY() + dY);
    }

    public static void areaAttack(LivingEntity attacker, float range, float height, float arc, DamageSource source, float damage, boolean act) {
        areaAttack(attacker, range, height, arc, damage, 0, 0, source, true, null, act);
    }

    public static void areaAttack(LivingEntity attacker, float range, float height, float arc, DamageSource source, float damage) {
        areaAttack(attacker, range, height, arc, damage, 0, 0, source, true);
    }

    public static void areaAttack(LivingEntity attacker, float range, float height, float arc, float damage, float hpDamage, int shieldBreak, DamageSource damageSource, boolean knockback) {
        areaAttack(attacker, range, height, arc, damage, hpDamage, shieldBreak, damageSource, knockback, null, false);
    }

    public static void areaAttack(LivingEntity attacker, float range, float height, float arc, float damage, float hpDamage,
                                  int shieldBreak, DamageSource source, boolean knockback, @Nullable Consumer<LivingEntity> attackEffect,
                                  boolean act) {
        List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(attacker, range, height, range, range);
        if (!attacker.level().isClientSide) {
            for (LivingEntity entityHit : entitiesHit) {
                float entityRelativeAngle = getRelativeAngle(attacker, entityHit);
                float entityHitDistance = (float)Math.sqrt((entityHit.getZ() - attacker.getZ()) *
                        (entityHit.getZ() - attacker.getZ()) + (entityHit.getX() - attacker.getX()) * (entityHit.getX() - attacker.getX()));
                if (entityHitDistance <= range && (entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2)
                        || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2)) {
                    if (canHurt(entityHit, attacker)) {
                        boolean flag;
                        if (act) {
                            flag = actuallyHurt(entityHit, source, damage + (entityHit.getMaxHealth() * hpDamage));
                        } else {
                            flag = entityHit.hurt(source, damage + (entityHit.getMaxHealth() * hpDamage));
                        }
                        if (entityHit.isDamageSourceBlocked(source) && shieldBreak > 0) {
                            disableShield(entityHit, shieldBreak);
                        }
                        if (flag) {
                            double d0 = entityHit.getX() - attacker.getX();
                            double d1 = entityHit.getZ() - attacker.getZ();
                            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                            if (knockback) {
                                entityHit.push(d0 / d2 * 2.0D, 0.18D, d1 / d2 * 2.0D);
                            }
                            if (attackEffect != null) {
                                attackEffect.accept(entityHit);
                            }
                        } else if (act) {
                            if (entityHit.hurt(source, damage + (entityHit.getMaxHealth() * hpDamage))) {
                                double d0 = entityHit.getX() - attacker.getX();
                                double d1 = entityHit.getZ() - attacker.getZ();
                                double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                                if (knockback) {
                                    entityHit.push(d0 / d2 * 2.5D, 0.18D, d1 / d2 * 2.2D);
                                }
                                if (attackEffect != null) {
                                    attackEffect.accept(entityHit);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static float getRelativeAngle(LivingEntity attacker, LivingEntity entityHit) {
        float entityHitAngle = (float)((Math.atan2(entityHit.getZ() - attacker.getZ(), entityHit.getX() - attacker.getX()) * (180 / Math.PI) - 90) % 360);
        float entityAttackingAngle = attacker.yBodyRot % 360;
        if (entityHitAngle < 0) {
            entityHitAngle += 360;
        }
        if (entityAttackingAngle < 0) {
            entityAttackingAngle += 360;
        }
        return entityHitAngle - entityAttackingAngle;
    }
    //To here

    public static void sweepAttack(LivingEntity attacker, Entity target, DamageSource damageSource, float damage){
        sweepAttack(attacker, target, damageSource, damage, 1.0D);
    }

    public static void sweepAttack(LivingEntity attacker, Entity target, DamageSource damageSource,
                                   float damage, double radius){
        for(LivingEntity livingentity : attacker.level().getEntitiesOfClass(LivingEntity.class,
                target.getBoundingBox().inflate(radius, 0.25D, radius))) {
            if (livingentity != attacker && livingentity != target && !attacker.isAlliedTo(livingentity) &&
                    (!(livingentity instanceof ArmorStand) || !((ArmorStand)livingentity).isMarker()) &&
                    attacker.canAttack(livingentity)) {
                livingentity.knockback(0.4F, Mth.sin(attacker.getYRot() *
                        (Maths.CLOSER_PI / 180F)), -Mth.cos(attacker.getYRot() * ((float)Math.PI / 180F)));
                livingentity.hurt(damageSource, damage);
            }
        }
    }

    public static AABB getRange(Mob mob, double dv, double x, double y, double z, double x1, double y1, double z1) {
        float bodyYawRad = mob.yBodyRot * Mth.DEG_TO_RAD;
        double dx = -Mth.sin(bodyYawRad) * dv;
        double dz = Mth.cos(bodyYawRad) * dv;
        Vec3 center = new Vec3(mob.getX() + dx, mob.getY() + mob.getBbHeight() * 0.5f,
                mob.getZ() + dz);
        return new AABB(center.x - x / 2, center.y - y / 2, center.z - z / 2,
                center.x + x1 / 2, center.y + y1 / 2, center.z + z1 / 2);
    }

    public static void burnInTheSun(boolean flag, Mob mob, int onFireTime) {
        if (MobUtils.isSunBurnTick(mob) && flag) {
            ItemStack itemstack = mob.getItemBySlot(EquipmentSlot.HEAD);
            if (itemstack.isEmpty()) {
                mob.setSecondsOnFire(onFireTime);
            } else {
                if (itemstack.isDamageableItem()) {
                    itemstack.setDamageValue(itemstack.getDamageValue() + mob.getRandom().nextInt(2));
                    if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                        mob.broadcastBreakEvent(EquipmentSlot.HEAD);
                        mob.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static boolean isSunBurnTick(Mob mob) {
        if (mob.level().isDay() && !mob.level().isClientSide) {
            float f = mob.getLightLevelDependentMagicValue();
            BlockPos blockpos = BlockPos.containing(mob.getX(), mob.getEyeY(), mob.getZ());
            boolean flag = mob.isInWaterRainOrBubble() || mob.isInPowderSnow || mob.wasInPowderSnow
                    || mob.hasEffect(NoixmodAPIMobEffects.WET.get());
            return f > 0.5F && mob.getRandom().nextFloat() * 30.0F < (f - 0.4F) * 2.0F && !flag
                    && mob.level().canSeeSky(blockpos);
        }
        return false;
    }

    //Based on Polarice's MobUtil
    public static void moveToGround(Entity entity) {
        HitResult rayTrace = rayTrace(entity);
        if (rayTrace.getType() == HitResult.Type.BLOCK) {
            BlockHitResult hitResult = (BlockHitResult) rayTrace;
            if (hitResult.getDirection() == Direction.UP) {
                BlockState hitBlock = entity.level().getBlockState(hitResult.getBlockPos());
                if (hitBlock.getBlock() instanceof SlabBlock && hitBlock.getValue(BlockStateProperties.SLAB_TYPE)
                        == SlabType.BOTTOM) {
                    entity.setPos(entity.getX(), hitResult.getBlockPos().getY() + 1.0625F - 0.5f, entity.getZ());
                } else {
                    entity.setPos(entity.getX(), hitResult.getBlockPos().getY() + 1.0625F, entity.getZ());
                }
            }
        }
    }

    public static HitResult rayTrace(Entity entity) {
        Vec3 startPos = new Vec3(entity.getX(), entity.getY(), entity.getZ());
        Vec3 endPos = new Vec3(entity.getX(), entity.level().getMinBuildHeight(), entity.getZ());
        return entity.level().clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE, entity));
    }

    public static BlockHitResult rayTrace(Entity mob, double distance, boolean fluids) {
        return (BlockHitResult)mob.pick(distance, 1.0F, fluids);
    }

    public static boolean hasLineOfSight(Entity looker, Entity target) {
        if (looker.level() != target.level()) {
            return false;
        } else {
            if (looker instanceof LivingEntity living) {
                return living.hasLineOfSight(target);
            }
            Vec3 vec3 = new Vec3(looker.getX(), looker.getEyeY(), looker.getZ());
            Vec3 vec31 = new Vec3(target.getX(), target.getEyeY(), target.getZ());
            if (vec31.distanceTo(vec3) > 128.0D) {
                return false;
            } else {
                return looker.level().clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE, looker)).getType() == HitResult.Type.MISS;
            }
        }
    }

    //ToHere

    public static class HostileNearestAttackableTargetGoal
            extends NearestAttackableTargetGoal<LivingEntity> {
        public HostileNearestAttackableTargetGoal(Mob p_26060_, boolean p_26062_, Predicate<LivingEntity> predicate) {
            super(p_26060_, LivingEntity.class, p_26062_, predicate);
        }

        public HostileNearestAttackableTargetGoal(Mob p, boolean b) {
            this(p, b, lie -> lie instanceof AbstractVillager || lie instanceof AbstractGolem || lie instanceof Player
                    || lie instanceof ApiVillager);
        }
    }

    static {
        avoidEntityPredicate_RangedEnemy = lie -> (lie instanceof ApiVillager || lie instanceof AbstractGolem);
    }
}
