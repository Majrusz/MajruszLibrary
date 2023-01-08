package com.mlib.gamemodifiers;

import com.mlib.EquipmentSlots;
import com.mlib.Random;
import com.mlib.Utility;
import com.mlib.config.BooleanConfig;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.entities.EntityHelper;
import com.mlib.gamemodifiers.parameters.ConditionParameters;
import com.mlib.gamemodifiers.parameters.Priority;
import com.mlib.math.Range;
import com.mlib.time.TimeHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class Condition< DataType extends ContextData > extends ConfigGroup implements IParameterizable< ConditionParameters > {
	protected final ConditionParameters params = new ConditionParameters();

	@Override
	public ConditionParameters getParams() {
		return this.params;
	}

	public boolean isMet( GameModifier feature, DataType data ) {
		return this.params.isNegated() ^ this.check( feature, data );
	}

	public Condition< DataType > apply( Consumer< ConditionParameters > consumer ) {
		consumer.accept( this.params );

		return this;
	}

	public Condition< DataType > negate() {
		return this.apply( params->params.negated( !params.isNegated() ) );
	}

	public Condition< DataType > configurable( boolean isConfigurable ) {
		return this.apply( params->params.configurable( isConfigurable ) );
	}

	public Condition< DataType > priority( Priority priority ) {
		return this.apply( params->params.priority( priority ) );
	}

	protected abstract boolean check( GameModifier feature, DataType data );

	public static class Excludable< DataType extends ContextData > extends Condition< DataType > {
		protected final BooleanConfig availability;

		public Excludable( boolean defaultValue ) {
			this.availability = new BooleanConfig( defaultValue );

			this.priority( Priority.HIGHEST )
				.configurable( true )
				.addConfig( this.availability.name( "is_enabled" ).comment( "Specifies whether this is enabled." ) );
		}

		public Excludable() {
			this( true );
		}

		@Override
		protected boolean check( GameModifier gameModifier, DataType data ) {
			return this.availability.getOrDefault();
		}
	}

	public static class Chance< DataType extends ContextData > extends Condition< DataType > {
		protected final DoubleConfig chance;

		public Chance( double chance ) {
			this.chance = new DoubleConfig( chance, Range.CHANCE );

			this.priority( Priority.HIGH )
				.configurable( true )
				.addConfig( this.chance.name( "chance" ).comment( "Chance for this to happen." ) );
		}

		@Override
		protected boolean check( GameModifier gameModifier, DataType data ) {
			return Random.tryChance( this.chance.getOrDefault() );
		}
	}

	public static class IsLivingBeing< DataType extends ContextData > extends Condition< DataType > {
		@Override
		protected boolean check( GameModifier feature, DataType data ) {
			return EntityHelper.isAnimal( data.entity ) || EntityHelper.isHuman( data.entity );
		}
	}

	public static class ArmorDependentChance< DataType extends ContextData > extends Condition< DataType > {
		static protected final Function< EquipmentSlot, String > SLOT_FORMAT = slot->String.format( "%s_multiplier", slot.getName() );
		protected final Map< EquipmentSlot, DoubleConfig > multipliers = new HashMap<>();
		protected final ConfigGroup group = new ConfigGroup();

		public ArmorDependentChance( Map< EquipmentSlot, Double > chances ) {
			for( EquipmentSlot slot : EquipmentSlots.ARMOR ) {
				DoubleConfig config = new DoubleConfig( chances.get( slot ), Range.CHANCE );
				this.multipliers.put( slot, config );
				this.group.addConfig( config.name( SLOT_FORMAT.apply( slot ) ) );
			}

			this.configurable( true )
				.addConfig( this.group
					.name( "ArmorChanceMultipliers" )
					.comment( "Chance multipliers for each armor piece.\nFor instance 'head_multiplier = 0.8' makes the final chance 30% lower if the mob has any helmet." )
				);
		}

		public ArmorDependentChance( double headChance, double chestChance, double legsChance, double feetChance ) {
			this( Map.of(
				EquipmentSlot.HEAD, headChance,
				EquipmentSlot.CHEST, chestChance,
				EquipmentSlot.LEGS, legsChance,
				EquipmentSlot.FEET, feetChance
			) );
		}

		public ArmorDependentChance( double chance ) {
			this( chance, chance, chance, chance );
		}

		public ArmorDependentChance() {
			this( 0.7 );
		}

		@Override
		protected boolean check( GameModifier feature, DataType data ) {
			double chance = 1.0;
			if( data.entity instanceof LivingEntity entity ) {
				for( EquipmentSlot slot : this.multipliers.keySet() ) {
					DoubleConfig config = this.multipliers.get( slot );
					ItemStack itemStack = entity.getItemBySlot( slot );
					if( !itemStack.isEmpty() && itemStack.getAttributeModifiers( slot ).containsKey( Attributes.ARMOR ) ) {
						chance *= config.getOrDefault();
					}
				}
			}

			return Random.tryChance( chance );
		}
	}

	public static class Custom< DataType extends ContextData > extends Condition< DataType > {
		protected final Predicate< DataType > predicate;

		public Custom( Predicate< DataType > predicate ) {
			this.predicate = predicate;

			this.priority( Priority.LOW );
		}

		@Override
		protected boolean check( GameModifier gameModifier, DataType data ) {
			return this.predicate.test( data );
		}
	}

	public static class Cooldown< DataType extends ContextData > extends Condition< DataType > {
		protected final Predicate< Double > distribution;
		protected final DoubleConfig cooldown;

		public Cooldown( double seconds, Dist distribution ) {
			this.distribution = distribution == Dist.CLIENT ? TimeHelper::hasClientSecondsPassed : TimeHelper::hasServerSecondsPassed;
			this.cooldown = new DoubleConfig( seconds, new Range<>( 0.1, 300.0 ) );

			this.priority( Priority.HIGH )
				.configurable( true )
				.addConfig( this.cooldown.name( "cooldown" ).comment( "Cooldown in seconds before it happens." ) );
		}

		public Cooldown( int ticks, Dist distribution ) {
			this( Utility.ticksToSeconds( ticks ), distribution );
		}

		@Override
		protected boolean check( GameModifier feature, DataType data ) {
			return this.distribution.test( this.cooldown.getOrDefault() );
		}
	}

	public static class HasEnchantment< DataType extends ContextData > extends Condition< DataType > {
		protected final Supplier< Enchantment > enchantment;
		protected final Function< DataType, LivingEntity > entity;

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
		protected boolean check( GameModifier feature, DataType data ) {
			LivingEntity entity = this.entity.apply( data );

			return entity != null && EnchantmentHelper.getEnchantmentLevel( this.enchantment.get(), entity ) > 0;
		}
	}

	public static class HasEffect< DataType extends ContextData > extends Condition< DataType > {
		protected final Supplier< MobEffect > effect;
		protected final Function< DataType, LivingEntity > entity;

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
		protected boolean check( GameModifier feature, DataType data ) {
			LivingEntity entity = this.entity.apply( data );

			return entity != null && entity.hasEffect( this.effect.get() );
		}
	}

	public static class IsServer< DataType extends ContextData > extends Condition< DataType > {
		public IsServer() {
			this.priority( Priority.HIGH );
		}

		@Override
		protected boolean check( GameModifier feature, DataType data ) {
			return data.level != null;
		}
	}

	public static class IsShiftKeyDown< DataType extends ContextData > extends Condition< DataType > {
		protected final Function< DataType, Player > player;

		public IsShiftKeyDown( Function< DataType, Player > player ) {
			this.player = player;

			this.priority( Priority.HIGH );
		}

		public IsShiftKeyDown() {
			this( data->( Player )data.entity );
		}

		@Override
		protected boolean check( GameModifier feature, DataType data ) {
			Player player = this.player.apply( data );

			return player != null && player.isShiftKeyDown();
		}
	}

	public static class IsOnGround< DataType extends ContextData > extends Condition< DataType > {
		protected final Function< DataType, Entity > entity;

		public IsOnGround( Function< DataType, Entity > entity ) {
			this.entity = entity;

			this.priority( Priority.HIGH );
		}

		public IsOnGround() {
			this( data->data.entity );
		}

		@Override
		protected boolean check( GameModifier feature, DataType data ) {
			Entity entity = this.entity.apply( data );

			return entity != null && entity.isOnGround();
		}
	}
}
