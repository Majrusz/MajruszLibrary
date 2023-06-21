package com.mlib.entities;

import com.mlib.ObfuscationGetter;
import com.mlib.Registries;
import com.mlib.Utility;
import com.mlib.data.SerializableStructure;
import com.mlib.math.AABBHelper;
import com.mlib.math.AnyPos;
import com.mlib.mixininterfaces.IMixinEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EntityHelper {
	static final ObfuscationGetter.Field< ServerLevel, PersistentEntitySectionManager< Entity > > ENTITY_MANAGER = new ObfuscationGetter.Field<>( ServerLevel.class, "f_143244_" );

	public static void cheatDeath( LivingEntity entity, float healthRatio, boolean shouldPlayEffects ) {
		entity.setHealth( entity.getMaxHealth() * healthRatio );

		if( shouldPlayEffects && entity.level() instanceof ServerLevel level ) {
			level.sendParticles( ParticleTypes.TOTEM_OF_UNDYING, entity.getX(), entity.getY( 0.75 ), entity.getZ(), 64, 0.25, 0.5, 0.25, 0.5 );
			level.playSound( null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, SoundSource.AMBIENT, 1.0f, 1.0f );
		}
	}

	public static boolean isOnCreativeMode( Player player ) {
		return player.getAbilities().instabuild;
	}

	public static boolean isOnSpectatorMode( Player player ) {
		return player.isSpectator();
	}

	public static boolean isAnimal( @Nullable Entity entity ) {
		return entity instanceof Animal;
	}

	public static boolean isHuman( Entity entity ) {
		return ( entity instanceof Villager || entity instanceof WanderingTrader || entity instanceof Player || entity instanceof Witch || entity instanceof Pillager );
	}

	public static boolean isLoaded( ServerLevel level, UUID uuid ) {
		return ENTITY_MANAGER.get( level ).isLoaded( uuid );
	}

	public static double getHealthRatio( LivingEntity entity ) {
		return Mth.clamp( entity.getHealth() / entity.getMaxHealth(), 0.0, 1.0 );
	}

	public static double getMissingHealthRatio( LivingEntity entity ) {
		return 1.0 - getHealthRatio( entity );
	}

	public static float getWalkDistanceDelta( LivingEntity entity ) {
		return entity.walkDist - entity.walkDistO; // these values are usually different on client and server sides!
	}

	public static void disableCurrentItem( Player player, double seconds ) {
		player.getCooldowns().addCooldown( player.getUseItem().getItem(), Utility.secondsToTicks( seconds ) );
		player.stopUsingItem();
		player.level().broadcastEntityEvent( player, ( byte )30 );
	}

	public static boolean spawnExperience( Level level, Vec3 position, int experience ) {
		return level.addFreshEntity( new ExperienceOrb( level, position.x, position.y, position.z, experience ) );
	}

	@Nullable
	public static < Type extends Entity > Type spawn( EntityType< Type > entityType, Level level, Consumer< Type > beforeEvent ) {
		Type entity = entityType.create( level );
		if( entity != null ) {
			beforeEvent.accept( entity );
			if( !level.addFreshEntity( entity ) )
				return null;
		}

		return entity;
	}

	@Nullable
	public static < Type extends Entity > Type spawn( RegistryObject< EntityType< Type > > entityType, Level level, Consumer< Type > beforeEvent ) {
		return spawn( entityType.get(), level, beforeEvent );
	}

	@Nullable
	public static < Type extends Entity > Type spawn( EntityType< Type > entityType, Level level, Vec3 position ) {
		return spawn( entityType, level, entity->entity.moveTo( position ) );
	}

	@Nullable
	public static < Type extends Entity > Type spawn( RegistryObject< EntityType< Type > > entityType, Level level,
		Vec3 position
	) {
		return spawn( entityType.get(), level, position );
	}

	public static < EntityType extends Entity > List< EntityType > getEntitiesInBox( Class< EntityType > entityClass,
		ServerLevel level, Vec3 position, double sideLength, Predicate< EntityType > extraPredicate
	) {
		AABB axisAligned = AABBHelper.createInflatedAABB( position, sideLength / 2.0 );

		return level.getEntitiesOfClass( entityClass, axisAligned, extraPredicate );
	}

	public static < EntityType extends Entity > List< EntityType > getEntitiesInBox( Class< EntityType > entityClass,
		ServerLevel level, BlockPos blockPosition, double sideLength, Predicate< EntityType > extraPredicate
	) {
		return getEntitiesInBox( entityClass, level, AnyPos.from( blockPosition ).vec3(), sideLength, extraPredicate );
	}

	public static < EntityType extends Entity > List< EntityType > getEntitiesInBox( Class< EntityType > entityClass,
		ServerLevel level, Entity entity, double sideLength, Predicate< EntityType > extraPredicate
	) {
		return getEntitiesInBox( entityClass, level, entity.position(), sideLength, extraPredicate );
	}

	public static < EntityType extends Entity > List< EntityType > getEntitiesInSphere( Class< EntityType > entityClass,
		ServerLevel level, Vec3 position, double radius, Predicate< EntityType > extraPredicate
	) {
		Predicate< EntityType > distancePredicate = entity->AnyPos.from( position ).dist( entity.position() ).doubleValue() <= radius;

		return getEntitiesInBox( entityClass, level, position, radius * 2.0, distancePredicate.and( extraPredicate ) );
	}

	public static < EntityType extends Entity > List< EntityType > getEntitiesInSphere( Class< EntityType > entityClass,
		ServerLevel level, BlockPos blockPosition, double radius, Predicate< EntityType > extraPredicate
	) {
		return getEntitiesInSphere( entityClass, level, AnyPos.from( blockPosition ).vec3(), radius, extraPredicate );
	}

	public static < EntityType extends Entity > List< EntityType > getEntitiesInSphere( Class< EntityType > entityClass,
		ServerLevel level, Entity entity, double radius, Predicate< EntityType > extraPredicate
	) {
		return getEntitiesInSphere( entityClass, level, entity.position(), radius, extraPredicate );
	}

	public static void sendExtraClientGlowTicks( ServerPlayer player, Entity target, int ticks ) {
		Registries.HELPER.sendMessage( PacketDistributor.PLAYER.with( ()->player ), new EntityGlow( target, ticks ) );
	}

	public static boolean destroyBlocks( Entity entity, AABB aabb, BiPredicate< BlockPos, BlockState > predicate ) {
		if( !ForgeEventFactory.getMobGriefingEvent( entity.level(), entity ) ) {
			return false;
		}

		boolean destroyedAnyBlock = false;
		for( BlockPos blockPos : BlockPos.betweenClosed( Mth.floor( aabb.minX ), Mth.floor( aabb.minY ), Mth.floor( aabb.minZ ), Mth.floor( aabb.maxX ), Mth.floor( aabb.maxY ), Mth.floor( aabb.maxZ ) ) ) {
			if( predicate.test( blockPos, entity.level().getBlockState( blockPos ) ) ) {
				destroyedAnyBlock |= entity.level().destroyBlock( blockPos, true, entity );
			}
		}

		return destroyedAnyBlock;
	}

	public static class EntityGlow extends SerializableStructure {
		int entityId = 0;
		int ticks = 0;

		public EntityGlow( Entity entity, int ticks ) {
			this();

			this.entityId = entity.getId();
			this.ticks = ticks;
		}

		public EntityGlow() {
			this.define( null, ()->this.entityId, x->this.entityId = x );
			this.define( null, ()->this.ticks, x->this.ticks = x );
		}

		@Override
		@OnlyIn( Dist.CLIENT )
		public void onClient( NetworkEvent.Context context ) {
			Level level = Minecraft.getInstance().level;
			if( level != null ) {
				IMixinEntity.addGlowTicks( level.getEntity( this.entityId ), this.ticks );
			}
		}
	}
}
