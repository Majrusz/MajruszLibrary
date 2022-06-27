package com.mlib.gamemodifiers;

import com.mlib.Random;
import com.mlib.Utility;
import com.mlib.config.BooleanConfig;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.entities.EntityHelper;
import com.mlib.gamemodifiers.contexts.OnCheckSpawnContext;
import com.mlib.gamemodifiers.contexts.OnPlayerInteractContext;
import com.mlib.gamemodifiers.contexts.OnPlayerLoggedContext;
import com.mlib.gamemodifiers.contexts.OnPlayerTickContext;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.function.Predicate;

public abstract class Condition extends ConfigGroup {
	public static final Comparator< Condition > COMPARATOR = ( left, right )->Mth.sign( left.priority.ordinal() - right.priority.ordinal() );

	public enum Priority {
		HIGHEST, HIGH, NORMAL, LOW, LOWEST
	}

	final Priority priority;

	public Condition( Priority priority ) {
		this.priority = priority;
	}

	public Condition() {
		this( Priority.NORMAL );
	}

	public abstract boolean check( GameModifier feature, com.mlib.gamemodifiers.Context.Data data );

	public static class Excludable extends Condition {
		final BooleanConfig availability;

		public Excludable() {
			super( Priority.HIGHEST );
			this.availability = new BooleanConfig( "is_enabled", "Specifies whether this game modifier is enabled.", false, true );
			this.addConfig( this.availability );
		}

		@Override
		public boolean check( GameModifier gameModifier, com.mlib.gamemodifiers.Context.Data data ) {
			return this.availability.isEnabled();
		}
	}

	public static class Chance extends Condition {
		final DoubleConfig chance;

		public Chance( double defaultChance ) {
			super( Priority.HIGH );
			this.chance = new DoubleConfig( "chance", "Chance of this to happen.", false, defaultChance, 0.0, 1.0 );
			this.addConfig( this.chance );
		}

		@Override
		public boolean check( GameModifier gameModifier, com.mlib.gamemodifiers.Context.Data data ) {
			return Random.tryChance( this.chance.get() );
		}
	}

	public static class IsLivingBeing extends Condition {
		@Override
		public boolean check( GameModifier feature, com.mlib.gamemodifiers.Context.Data data ) {
			return EntityHelper.isAnimal( data.entity ) || EntityHelper.isHuman( data.entity );
		}
	}

	public static class ArmorDependentChance extends Condition {
		@Override
		public boolean check( GameModifier feature, com.mlib.gamemodifiers.Context.Data data ) {
			return Random.tryChance( getChance( data.entity ) );
		}

		private double getChance( @Nullable LivingEntity entity ) {
			if( entity == null )
				return 1.0;

			MutableInt armorCount = new MutableInt( 0 );
			entity.getArmorSlots().forEach( itemStack->{
				if( !itemStack.isEmpty() )
					armorCount.add( 1 );
			} );
			return switch( armorCount.getValue() ) {
				default -> 1.0;
				case 1 -> 0.7;
				case 2 -> 0.49;
				case 3 -> 0.34;
				case 4 -> 0.24;
			};
		}
	}


	public static class Context< DataType extends com.mlib.gamemodifiers.Context.Data > extends Condition {
		final Class< DataType > dataClass;
		final Predicate< DataType > predicate;

		public Context( Class< DataType > dataClass, Predicate< DataType > predicate ) {
			super( Priority.LOW );
			this.dataClass = dataClass;
			this.predicate = predicate;
		}

		@Override
		public boolean check( GameModifier gameModifier, com.mlib.gamemodifiers.Context.Data data ) {
			DataType contextData = Utility.castIfPossible( this.dataClass, data );
			assert contextData != null;

			return this.predicate.test( contextData );
		}
	}

	public static class ContextOnCheckSpawn extends Context< OnCheckSpawnContext.Data > {
		public ContextOnCheckSpawn( Predicate< OnCheckSpawnContext.Data > predicate ) {
			super( OnCheckSpawnContext.Data.class, predicate );
		}
	}

	public static class ContextOnDamaged extends Context< com.mlib.gamemodifiers.contexts.OnDamagedContext.Data > {
		public ContextOnDamaged( Predicate< com.mlib.gamemodifiers.contexts.OnDamagedContext.Data > predicate ) {
			super( com.mlib.gamemodifiers.contexts.OnDamagedContext.Data.class, predicate );
		}
	}

	public static class ContextOnDeath extends Context< com.mlib.gamemodifiers.contexts.OnDeathContext.Data > {
		public ContextOnDeath( Predicate< com.mlib.gamemodifiers.contexts.OnDeathContext.Data > predicate ) {
			super( com.mlib.gamemodifiers.contexts.OnDeathContext.Data.class, predicate );
		}
	}

	public static class ContextOnDimensionChanged extends Context< com.mlib.gamemodifiers.contexts.OnDimensionChangedContext.Data > {
		public ContextOnDimensionChanged( Predicate< com.mlib.gamemodifiers.contexts.OnDimensionChangedContext.Data > predicate ) {
			super( com.mlib.gamemodifiers.contexts.OnDimensionChangedContext.Data.class, predicate );
		}
	}

	public static class ContextOnExplosion extends Context< com.mlib.gamemodifiers.contexts.OnExplosionContext.Data > {
		public ContextOnExplosion( Predicate< com.mlib.gamemodifiers.contexts.OnExplosionContext.Data > predicate ) {
			super( com.mlib.gamemodifiers.contexts.OnExplosionContext.Data.class, predicate );
		}
	}

	public static class ContextOnItemFished extends Context< com.mlib.gamemodifiers.contexts.OnItemFishedContext.Data > {
		public ContextOnItemFished( Predicate< com.mlib.gamemodifiers.contexts.OnItemFishedContext.Data > predicate ) {
			super( com.mlib.gamemodifiers.contexts.OnItemFishedContext.Data.class, predicate );
		}
	}

	public static class ContextOnPickupXp extends Context< com.mlib.gamemodifiers.contexts.OnPickupXpContext.Data > {
		public ContextOnPickupXp( Predicate< com.mlib.gamemodifiers.contexts.OnPickupXpContext.Data > predicate ) {
			super( com.mlib.gamemodifiers.contexts.OnPickupXpContext.Data.class, predicate );
		}
	}

	public static class ContextOnPlayerInteract extends Context< OnPlayerInteractContext.Data > {
		public ContextOnPlayerInteract( Predicate< OnPlayerInteractContext.Data > predicate ) {
			super( OnPlayerInteractContext.Data.class, predicate );
		}
	}

	public static class ContextOnPlayerLogged extends Context< OnPlayerLoggedContext.Data > {
		public ContextOnPlayerLogged( Predicate< OnPlayerLoggedContext.Data > predicate ) {
			super( OnPlayerLoggedContext.Data.class, predicate );
		}
	}

	public static class ContextOnPlayerTick extends Context< OnPlayerTickContext.Data > {
		public ContextOnPlayerTick( Predicate< OnPlayerTickContext.Data > predicate ) {
			super( OnPlayerTickContext.Data.class, predicate );
		}
	}

	public static class ContextOnSpawned extends Context< com.mlib.gamemodifiers.contexts.OnSpawnedContext.Data > {
		public ContextOnSpawned( Predicate< com.mlib.gamemodifiers.contexts.OnSpawnedContext.Data > predicate ) {
			super( com.mlib.gamemodifiers.contexts.OnSpawnedContext.Data.class, predicate );
		}
	}
}
