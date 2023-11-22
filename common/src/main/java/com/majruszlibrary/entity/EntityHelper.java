package com.majruszlibrary.entity;

import com.majruszlibrary.MajruszLibrary;
import com.majruszlibrary.animations.IAnimableEntity;
import com.majruszlibrary.annotation.Dist;
import com.majruszlibrary.annotation.OnlyIn;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.data.Serializables;
import com.majruszlibrary.emitter.ParticleEmitter;
import com.majruszlibrary.emitter.SoundEmitter;
import com.majruszlibrary.level.LevelHelper;
import com.majruszlibrary.math.AnyPos;
import com.majruszlibrary.math.AnyRot;
import com.majruszlibrary.mixin.IMixinMob;
import com.majruszlibrary.mixin.IMixinServerLevel;
import com.majruszlibrary.mixininterfaces.IMixinClientLevel;
import com.majruszlibrary.mixininterfaces.IMixinEntity;
import com.majruszlibrary.platform.Side;
import com.majruszlibrary.time.TimeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntityHelper {
	public static void cheatDeath( LivingEntity entity ) {
		entity.setHealth( 1.0f );
		entity.removeAllEffects();
		entity.addEffect( new MobEffectInstance( MobEffects.REGENERATION, 900, 1 ) );
		entity.addEffect( new MobEffectInstance( MobEffects.ABSORPTION, 100, 1 ) );
		entity.addEffect( new MobEffectInstance( MobEffects.FIRE_RESISTANCE, 800, 0 ) );
		if( entity.level() instanceof ServerLevel level ) {
			ParticleEmitter.of( ParticleTypes.TOTEM_OF_UNDYING )
				.sizeBased( entity )
				.count( 32 )
				.offset( new Vec3( 0.25, 0.5, 0.25 ) )
				.emit( level );

			SoundEmitter.of( SoundEvents.TOTEM_USE )
				.position( entity.position() )
				.emit( level );
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
		return entity instanceof Villager
			|| entity instanceof WanderingTrader
			|| entity instanceof Player
			|| entity instanceof Witch
			|| entity instanceof AbstractIllager;
	}

	public static boolean isLoaded( ServerLevel level, UUID uuid ) {
		return ( ( IMixinServerLevel )level ).getEntityManager().isLoaded( uuid );
	}

	public static boolean isOutside( Entity entity ) {
		return entity.level().canSeeSky( entity.blockPosition() );
	}

	public static boolean isIn( Entity entity, ResourceKey< Level > level ) {
		return entity.level().dimension().equals( level );
	}

	public static String getPlayerUUID( Player player ) {
		return String.valueOf( player.getUUID() );
	}

	public static double getHealthRatio( LivingEntity entity ) {
		return Mth.clamp( entity.getHealth() / entity.getMaxHealth(), 0.0, 1.0 );
	}

	public static double getMissingHealthRatio( LivingEntity entity ) {
		return 1.0 - EntityHelper.getHealthRatio( entity );
	}

	public static float getWalkDistanceDelta( LivingEntity entity ) {
		return entity.walkDist - entity.walkDistO; // these values are usually different on client and server sides!
	}

	public static GoalSelector getGoalSelector( Mob mob ) {
		return ( ( IMixinMob )mob ).getGoalSelector();
	}

	public static GoalSelector getTargetSelector( Mob mob ) {
		return ( ( IMixinMob )mob ).getTargetSelector();
	}

	public static @Nullable CompoundTag getExtraTag( Entity entity ) {
		return ( ( IMixinEntity )entity ).majruszlibrary$getExtraTag();
	}

	public static CompoundTag getOrCreateExtraTag( Entity entity ) {
		return ( ( IMixinEntity )entity ).majruszlibrary$getOrCreateExtraTag();
	}

	public static void disableCurrentItem( Player player, double seconds ) {
		player.getCooldowns().addCooldown( player.getUseItem().getItem(), TimeHelper.toTicks( seconds ) );
		player.stopUsingItem();
		player.level().broadcastEntityEvent( player, ( byte )30 );
	}

	public static boolean spawnExperience( Level level, Vec3 position, int experience ) {
		return level.addFreshEntity( new ExperienceOrb( level, position.x, position.y, position.z, experience ) );
	}

	public static < Type extends Entity > Spawner< Type > createSpawner( Supplier< EntityType< Type > > type, Level level ) {
		return new Spawner<>( type.get(), level );
	}

	public static < EntityType extends Entity > List< EntityType > getEntitiesNearby( Class< EntityType > entityClass, ServerLevel level, Vec3 position,
		double radius
	) {
		return level.getEntitiesOfClass( entityClass, new AABB( position, position ).inflate( radius ), entity->AnyPos.from( position )
			.dist( entity.position() )
			.doubleValue() <= radius );
	}

	public static boolean destroyBlocks( Entity entity, AABB aabb, BiPredicate< BlockPos, BlockState > predicate ) {
		if( !LevelHelper.isMobGriefingEnabled( entity.level() ) ) {
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

	@OnlyIn( Dist.CLIENT )
	public static < Type > void applyToClientEntity( int entityId, Class< Type > clazz, Consumer< Type > consumer ) {
		Entity entity = Side.getLocalLevel().getEntity( entityId );
		if( clazz.isInstance( entity ) ) {
			consumer.accept( clazz.cast( entity ) );
		} else {
			// required because the entity can be recreated from client package later during the same tick
			( ( IMixinClientLevel )Side.getLocalLevel() ).majruszlibrary$delayExecution( entityId, clazz, consumer );
		}
	}

	public static AnyRot getLookRotation( Entity entity ) {
		return AnyRot.y( Math.toRadians( -entity.getYRot() ) - Math.PI / 2.0 )
			.rotZ( Math.toRadians( -entity.getXRot() ) );
	}

	public static AnyPos getLookDirection( Entity entity ) {
		return AnyPos.from( 1.0, 0.0, 0.0 ).rot( EntityHelper.getLookRotation( entity ) );
	}

	public static AnyPos getDirection2d( Entity entity ) {
		return AnyPos.from( 1.0, 0.0, 0.0 ).rot( AnyRot.y( Math.toRadians( -entity.getYRot() ) - Math.PI / 2.0 ) );
	}

	public static class Spawner< Type extends Entity > {
		private final EntityType< Type > type;
		private final Level level;
		private MobSpawnType mobSpawnType = MobSpawnType.EVENT;
		private Vec3 position = null;
		private Consumer< Type > beforeEvent = null;

		public Spawner< Type > mobSpawnType( MobSpawnType mobSpawnType ) {
			this.mobSpawnType = mobSpawnType;

			return this;
		}

		public Spawner< Type > position( Vec3 position ) {
			this.position = position;

			return this;
		}

		public Spawner< Type > beforeEvent( Consumer< Type > beforeEvent ) {
			this.beforeEvent = beforeEvent;

			return this;
		}

		@Nullable
		public Type spawn() {
			Type entity = this.type.create( this.level );
			if( entity != null ) {
				Optional.ofNullable( this.position ).ifPresent( entity::moveTo );
				Optional.ofNullable( this.beforeEvent ).ifPresent( beforeEvent->beforeEvent.accept( entity ) );
				if( entity instanceof Mob mob && this.level instanceof ServerLevel level ) {
					mob.finalizeSpawn( level, level.getCurrentDifficultyAt( mob.blockPosition() ), this.mobSpawnType, null, null );
				}

				if( !this.level.addFreshEntity( entity ) ) {
					return null;
				}
			}

			return entity;
		}

		Spawner( EntityType< Type > type, Level level ) {
			this.type = type;
			this.level = level;
		}
	}

	public static class EntityAnimation {
		int entityId;
		String name;
		int trackIdx;

		static {
			Serializables.get( EntityAnimation.class )
				.define( "id", Reader.integer(), s->s.entityId, ( s, v )->s.entityId = v )
				.define( "name", Reader.string(), s->s.name, ( s, v )->s.name = v )
				.define( "trackIdx", Reader.integer(), s->s.trackIdx, ( s, v )->s.trackIdx = v );

			Side.runOnClient( ()->()->MajruszLibrary.ENTITY_ANIMATION.addClientCallback( EntityAnimation::onClient ) );
		}

		public EntityAnimation( int entityId, String name, int trackIdx ) {
			this.entityId = entityId;
			this.name = name;
			this.trackIdx = trackIdx;
		}

		public EntityAnimation( Entity entity, String name, int trackIdx ) {
			this( entity.getId(), name, trackIdx );
		}

		public EntityAnimation() {
			this( 0, "", 0 );
		}

		@OnlyIn( Dist.CLIENT )
		private static void onClient( EntityAnimation data ) {
			EntityHelper.applyToClientEntity( data.entityId, IAnimableEntity.class, entity->entity.playAnimation( data.name, data.trackIdx ) );
		}
	}

	public static class EntityGlow {
		int entityId;
		int ticks;

		static {
			Serializables.get( EntityGlow.class )
				.define( "id", Reader.integer(), s->s.entityId, ( s, v )->s.entityId = v )
				.define( "ticks", Reader.integer(), s->s.ticks, ( s, v )->s.ticks = v );

			Side.runOnClient( ()->()->MajruszLibrary.ENTITY_GLOW.addClientCallback( EntityGlow::onClient ) );
		}

		public EntityGlow( int entityId, int ticks ) {
			this.entityId = entityId;
			this.ticks = ticks;
		}

		public EntityGlow( Entity entity, int ticks ) {
			this( entity.getId(), ticks );
		}

		public EntityGlow() {
			this( 0, 0 );
		}

		@OnlyIn( Dist.CLIENT )
		private static void onClient( EntityGlow data ) {
			EntityHelper.applyToClientEntity( data.entityId, IMixinEntity.class, entity->entity.majruszlibrary$addGlowTicks( data.ticks ) );
		}
	}

	public static class EntityInvisible {
		int entityId;
		int ticks;

		static {
			Serializables.get( EntityInvisible.class )
				.define( "id", Reader.integer(), s->s.entityId, ( s, v )->s.entityId = v )
				.define( "ticks", Reader.integer(), s->s.ticks, ( s, v )->s.ticks = v );

			Side.runOnClient( ()->()->MajruszLibrary.ENTITY_INVISIBLE.addClientCallback( EntityInvisible::onClient ) );
		}

		public EntityInvisible( int entityId, int ticks ) {
			this.entityId = entityId;
			this.ticks = ticks;
		}

		public EntityInvisible( Entity entity, int ticks ) {
			this( entity.getId(), ticks );
		}

		public EntityInvisible() {
			this( 0, 0 );
		}

		@OnlyIn( Dist.CLIENT )
		private static void onClient( EntityInvisible data ) {
			EntityHelper.applyToClientEntity( data.entityId, IMixinEntity.class, entity->entity.majruszlibrary$addInvisibleTicks( data.ticks ) );
		}
	}
}
