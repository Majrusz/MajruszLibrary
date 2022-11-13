package com.mlib.gamemodifiers;

import com.mlib.Random;
import com.mlib.Utility;
import com.mlib.config.BooleanConfig;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.entities.EntityHelper;
import com.mlib.gamemodifiers.parameters.ConditionParameters;
import com.mlib.gamemodifiers.parameters.Parameters;
import com.mlib.gamemodifiers.parameters.Priority;
import com.mlib.time.TimeHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class Condition extends ConfigGroup implements IParameterizable {
	final ConditionParameters params;

	public Condition( ConditionParameters params ) {
		this.params = params;
	}

	public Condition( Priority priority ) {
		this( new ConditionParameters( priority ) );
	}

	public Condition() {
		this( new ConditionParameters() );
	}

	@Override
	public Parameters getParams() {
		return this.params;
	}

	public abstract boolean check( GameModifier feature, ContextData data );

	public static class Excludable extends Condition {
		final BooleanConfig availability;
		final Function< BooleanConfig, Boolean > check;

		public Excludable( BooleanConfig config, Function< BooleanConfig, Boolean > check ) {
			super( Priority.HIGHEST );
			this.availability = config;
			this.check = check;

			this.addConfig( this.availability );
		}

		public Excludable( boolean defaultValue, String name, String comment, Function< BooleanConfig, Boolean > check ) {
			this( new BooleanConfig( name, comment, false, defaultValue ), check );
		}

		public Excludable( boolean defaultValue, String name, String comment ) {
			this( defaultValue, name, comment, BooleanConfig::isEnabled );
		}

		public Excludable() {
			this( true, "is_enabled", "Specifies whether this feature is enabled." );
		}

		@Override
		public boolean check( GameModifier gameModifier, ContextData data ) {
			return this.check.get();
		}
	}

	public static class Chance extends Condition {
		final DoubleConfig chance;

		public Chance( DoubleConfig config ) {
			super( Priority.HIGH );
			this.chance = config;
			this.addConfig( this.chance );
		}

		public Chance( double defaultChance, String name, String comment ) {
			this( new DoubleConfig( name, comment, false, defaultChance, 0.0, 1.0 ) );
		}

		public Chance( double defaultChance ) {
			this( defaultChance, "chance", "Chance of this to happen." );
		}

		@Override
		public boolean check( GameModifier gameModifier, ContextData data ) {
			return Random.tryChance( this.chance.get() );
		}
	}

	public static class IsLivingBeing extends Condition {
		@Override
		public boolean check( GameModifier feature, ContextData data ) {
			return EntityHelper.isAnimal( data.entity ) || EntityHelper.isHuman( data.entity );
		}
	}

	public static class ArmorDependentChance extends Condition {
		@Override
		public boolean check( GameModifier feature, ContextData data ) {
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

	public static class Context< DataType extends ContextData > extends Condition {
		final Class< DataType > dataClass;
		final Predicate< DataType > predicate;

		public Context( Class< DataType > dataClass, Predicate< DataType > predicate ) {
			super( Priority.LOW );
			this.dataClass = dataClass;
			this.predicate = predicate;
		}

		@Override
		public boolean check( GameModifier gameModifier, ContextData data ) {
			DataType contextData = Utility.castIfPossible( this.dataClass, data );
			assert contextData != null;

			return this.predicate.test( contextData );
		}
	}

	public static class Cooldown extends Condition {
		final Supplier< Boolean > test;

		public Cooldown( double seconds, Dist distribution, boolean isConfigurable ) {
			super( Priority.HIGH );
			Predicate< Double > predicate = distribution == Dist.CLIENT ? TimeHelper::hasClientSecondsPassed : TimeHelper::hasServerSecondsPassed;
			if( isConfigurable ) {
				DoubleConfig config = new DoubleConfig( "cooldown", "Cooldown in seconds before this happens.", false, seconds, 0.1, 300.0 );
				this.test = ()->predicate.test( config.get() );
				this.addConfig( config );
			} else {
				this.test = ()->predicate.test( seconds );
			}
		}

		public Cooldown( double seconds, Dist distribution ) {
			this( seconds, distribution, true );
		}

		public Cooldown( int ticks, Dist distribution, boolean isConfigurable ) {
			this( Utility.ticksToSeconds( ticks ), distribution, isConfigurable );
		}

		public Cooldown( int ticks, Dist distribution ) {
			this( ticks, distribution, true );
		}

		@Override
		public boolean check( GameModifier feature, ContextData data ) {
			return this.test.get();
		}
	}

	public static class HasEnchantment extends Condition {
		final Enchantment enchantment;

		public HasEnchantment( Enchantment enchantment ) {
			this.enchantment = enchantment;
		}

		@Override
		public boolean check( GameModifier feature, ContextData data ) {
			return data.entity != null && EnchantmentHelper.getEnchantmentLevel( this.enchantment, data.entity ) > 0;
		}
	}

	public static class HasEffect extends Condition {
		final Supplier< MobEffect > effect;

		public HasEffect( RegistryObject< ? extends MobEffect > effect ) {
			this.effect = effect::get;
		}

		public HasEffect( MobEffect effect ) {
			this.effect = ()->effect;
		}

		@Override
		public boolean check( GameModifier feature, ContextData data ) {
			return data.entity != null && data.entity.hasEffect( this.effect.get() );
		}
	}

	public static class IsServer extends Condition {
		public IsServer() {
			super( Priority.HIGH );
		}

		@Override
		public boolean check( GameModifier feature, ContextData data ) {
			return data.level != null;
		}
	}
}
