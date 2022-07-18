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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

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

		public Excludable() {
			super( Priority.HIGHEST );
			this.availability = new BooleanConfig( "is_enabled", "Specifies whether this game modifier is enabled.", false, true );
			this.addConfig( this.availability );
		}

		@Override
		public boolean check( GameModifier gameModifier, ContextData data ) {
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
		final DoubleConfig cooldown;
		final Predicate< DoubleConfig > condition;

		public Cooldown( double seconds, Dist distribution ) {
			super( Priority.HIGH );
			this.cooldown = new DoubleConfig( "cooldown", "Cooldown in seconds before this happens.", false, seconds, 0.1, 300.0 );
			this.condition = distribution == Dist.CLIENT ? TimeHelper::hasClientSecondsPassed : TimeHelper::hasServerSecondsPassed;
			this.addConfig( this.cooldown );
		}

		public Cooldown( int ticks, Dist distribution ) {
			this( Utility.ticksToSeconds( ticks ), distribution );
		}

		@Override
		public boolean check( GameModifier feature, ContextData data ) {
			return this.condition.test( this.cooldown );
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

	public static class IsServer extends Condition {
		@Override
		public boolean check( GameModifier feature, ContextData data ) {
			return data.level != null;
		}
	}
}
