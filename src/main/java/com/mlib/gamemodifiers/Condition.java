package com.mlib.gamemodifiers;

import com.mlib.Random;
import com.mlib.Utility;
import com.mlib.config.BooleanConfig;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleArrayConfig;
import com.mlib.config.DoubleConfig;
import com.mlib.entities.EntityHelper;
import com.mlib.gamemodifiers.parameters.ConditionParameters;
import com.mlib.gamemodifiers.parameters.Priority;
import com.mlib.time.TimeHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class Condition< DataType extends ContextData > extends ConfigGroup implements IParameterizable< ConditionParameters > {
	protected final ConditionParameters params = new ConditionParameters();

	@Override
	public ConditionParameters getParams() {
		return this.params;
	}

	public abstract boolean check( GameModifier feature, DataType data );

	public Condition< DataType > apply( Consumer< ConditionParameters > consumer ) {
		consumer.accept( this.params );

		return this;
	}

	public static class Excludable< DataType extends ContextData > extends Condition< DataType > {
		final BooleanConfig availability;

		public Excludable( BooleanConfig config ) {
			this.availability = config;

			this.addConfig( this.availability );
			this.apply( params->params.setConfigurable( true ).setPriority( Priority.HIGHEST ) );
		}

		public Excludable( boolean defaultValue, String name, String comment ) {
			this( new BooleanConfig( name, comment, false, defaultValue ) );
		}

		public Excludable() {
			this( true, "is_enabled", "Specifies whether this is enabled." );
		}

		@Override
		public boolean check( GameModifier gameModifier, DataType data ) {
			return this.availability.getOrDefault();
		}
	}

	public static class Chance< DataType extends ContextData > extends Condition< DataType > {
		final DoubleConfig chance;

		public Chance( DoubleConfig config ) {
			this.chance = config;

			this.addConfig( this.chance );
			this.apply( params->params.setConfigurable( true ).setPriority( Priority.HIGH ) );
		}

		public Chance( double chance, String name, String comment ) {
			this( new DoubleConfig( name, comment, false, chance, 0.0, 1.0 ) );
		}

		public Chance( double chance ) {
			this( chance, "chance", "Chance for this to happen." );
		}

		@Override
		public boolean check( GameModifier gameModifier, DataType data ) {
			return Random.tryChance( this.chance.getOrDefault() );
		}
	}

	public static class IsLivingBeing< DataType extends ContextData > extends Condition< DataType > {
		@Override
		public boolean check( GameModifier feature, DataType data ) {
			return EntityHelper.isAnimal( data.entity ) || EntityHelper.isHuman( data.entity );
		}
	}

	public static class ArmorDependentChance< DataType extends ContextData > extends Condition< DataType > {
		static final Function< Integer, String > CONFIG_FORMAT = idx->String.format( "equipped_%d", idx );
		final DoubleArrayConfig chances;

		public ArmorDependentChance() {
			this.chances = new DoubleArrayConfig( "chances", "Chances which depend on amount of equipped armor pieces.", CONFIG_FORMAT, false, 0.0, 1.0, 1.0, 0.7, 0.49, 0.34, 0.24 );

			this.addConfig( this.chances );
			this.apply( params->params.setConfigurable( true ) );
		}

		@Override
		public boolean check( GameModifier feature, DataType data ) {
			return Random.tryChance( this.getChance( data.entity ) );
		}

		private double getChance( @Nullable Entity entity ) {
			if( entity == null )
				return this.chances.getOrDefault( 0 );

			int equippedArmorPieces = ( int )StreamSupport.stream( entity.getArmorSlots().spliterator(), false )
				.filter( itemStack->!itemStack.isEmpty() )
				.count();
			return this.chances.getOrDefault( equippedArmorPieces );
		}
	}

	public static class Context< DataType extends ContextData > extends Condition< DataType > {
		final Predicate< DataType > predicate;

		public Context( Predicate< DataType > predicate ) {
			this.predicate = predicate;

			this.apply( params->params.setPriority( Priority.LOW ) );
		}

		@Override
		public boolean check( GameModifier gameModifier, DataType data ) {
			return this.predicate.test( data );
		}
	}

	public static class Cooldown< DataType extends ContextData > extends Condition< DataType > {
		final Predicate< Double > distribution;
		final DoubleConfig cooldown;

		public Cooldown( DoubleConfig cooldown, Dist distribution ) {
			this.distribution = distribution == Dist.CLIENT ? TimeHelper::hasClientSecondsPassed : TimeHelper::hasServerSecondsPassed;
			this.cooldown = cooldown;

			this.addConfig( cooldown );
			this.apply( params->params.setConfigurable( true ).setPriority( Priority.HIGH ) );
		}

		public Cooldown( double seconds, Dist distribution ) {
			this( new DoubleConfig( "cooldown", "Cooldown in seconds before it happens.", false, seconds, 0.1, 300.0 ), distribution );
		}

		public Cooldown( int ticks, Dist distribution ) {
			this( Utility.ticksToSeconds( ticks ), distribution );
		}

		@Override
		public boolean check( GameModifier feature, DataType data ) {
			return this.distribution.test( this.cooldown.getOrDefault() );
		}
	}

	public static class HasEnchantment< DataType extends ContextData > extends Condition< DataType > {
		final Supplier< Enchantment > enchantment;
		final Function< DataType, LivingEntity > entity;

		public HasEnchantment( RegistryObject< ? extends Enchantment > enchantment, Function< DataType, LivingEntity > entity ) {
			this.enchantment = enchantment::get;
			this.entity = entity;
		}

		public HasEnchantment( RegistryObject< ? extends Enchantment > enchantment ) {
			this( enchantment, data->( LivingEntity )data.entity );
		}

		public HasEnchantment( Enchantment enchantment, Function< DataType, LivingEntity > entity ) {
			this.enchantment = ()->enchantment;
			this.entity = entity;
		}

		public HasEnchantment( Enchantment enchantment ) {
			this( enchantment, data->( LivingEntity )data.entity );
		}

		@Override
		public boolean check( GameModifier feature, DataType data ) {
			LivingEntity entity = this.entity.apply( data );

			return entity != null && EnchantmentHelper.getEnchantmentLevel( this.enchantment.get(), entity ) > 0;
		}
	}

	public static class HasEffect< DataType extends ContextData > extends Condition< DataType > {
		final Supplier< MobEffect > effect;
		final Function< DataType, LivingEntity > entity;

		public HasEffect( RegistryObject< ? extends MobEffect > effect, Function< DataType, LivingEntity > entity ) {
			this.effect = effect::get;
			this.entity = entity;
		}

		public HasEffect( RegistryObject< ? extends MobEffect > effect ) {
			this( effect, data->( LivingEntity )data.entity );
		}

		public HasEffect( MobEffect effect, Function< DataType, LivingEntity > entity ) {
			this.effect = ()->effect;
			this.entity = entity;
		}

		public HasEffect( MobEffect effect ) {
			this( effect, data->( LivingEntity )data.entity );
		}

		@Override
		public boolean check( GameModifier feature, DataType data ) {
			LivingEntity entity = this.entity.apply( data );

			return entity != null && entity.hasEffect( this.effect.get() );
		}
	}

	public static class IsServer< DataType extends ContextData > extends Condition< DataType > {
		public IsServer() {
			this.apply( params->params.setPriority( Priority.HIGH ) );
		}

		@Override
		public boolean check( GameModifier feature, DataType data ) {
			return data.level != null;
		}
	}

	public static class IsShiftKeyDown< DataType extends ContextData > extends Condition< DataType > {
		final Function< DataType, Player > player;

		public IsShiftKeyDown( Function< DataType, Player > player ) {
			this.player = player;

			this.apply( params->params.setPriority( Priority.HIGH ) );
		}

		public IsShiftKeyDown() {
			this( data->( Player )data.entity );
		}

		@Override
		public boolean check( GameModifier feature, DataType data ) {
			Player player = this.player.apply( data );

			return player != null && player.isShiftKeyDown();
		}
	}
}
