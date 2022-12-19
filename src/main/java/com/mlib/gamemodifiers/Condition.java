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
import com.mlib.time.TimeHelper;
import net.minecraft.world.effect.MobEffect;
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
		return this.apply( params->params.setNegated( !params.isNegated() ) );
	}

	public Condition< DataType > setConfigurable( boolean isConfigurable ) {
		return this.apply( params->params.setConfigurable( isConfigurable ) );
	}

	public Condition< DataType > priority( Priority priority ) {
		return this.apply( params->params.setPriority( priority ) );
	}

	public < ConditionType extends Condition< ? > > Condition< DataType > save( Ref< ConditionType > ref ) {
		ref.condition = ( ConditionType )this;

		return this;
	}

	protected abstract boolean check( GameModifier feature, DataType data );

	public static class Ref< ConditionType extends Condition< ? > > {
		ConditionType condition = null;

		public ConditionType get() {
			return this.condition;
		}
	}

	public static class Excludable< DataType extends ContextData > extends Condition< DataType > {
		protected final BooleanConfig availability;

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

		public BooleanConfig getConfig() {
			return this.availability;
		}

		@Override
		protected boolean check( GameModifier gameModifier, DataType data ) {
			return this.availability.getOrDefault();
		}
	}

	public static class Chance< DataType extends ContextData > extends Condition< DataType > {
		protected final DoubleConfig chance;

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

		public DoubleConfig getConfig() {
			return this.chance;
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
		static final Function< EquipmentSlot, String > SLOT_FORMAT = slot->String.format( "%s_multiplier", slot.getName() );
		final Map< EquipmentSlot, DoubleConfig > multipliers = new HashMap<>();
		final ConfigGroup group = new ConfigGroup( "ArmorChanceMultipliers", "Chance multipliers for each armor piece.\nFor instance 'head_multiplier = 0.8' makes the final chance 30% lower if the mob has any helmet." );

		public ArmorDependentChance( Map< EquipmentSlot, Double > chances ) {
			for( EquipmentSlot slot : EquipmentSlots.ARMOR ) {
				this.multipliers.put( slot, new DoubleConfig( SLOT_FORMAT.apply( slot ), "", false, chances.get( slot ), 0.0, 1.0 ) );
			}

			this.multipliers.forEach( ( slot, config )->this.group.addConfig( config ) );
			this.addConfig( group );
			this.apply( params->params.setConfigurable( true ) );
		}

		public ArmorDependentChance( double headChance, double chestChance, double legChance, double feetChance ) {
			this( Map.of(
				EquipmentSlot.HEAD, headChance,
				EquipmentSlot.CHEST, chestChance,
				EquipmentSlot.LEGS, legChance,
				EquipmentSlot.FEET, feetChance
			) );
		}

		public ArmorDependentChance( double chance ) {
			this( chance, chance, chance, chance );
		}

		public ArmorDependentChance() {
			this( 0.7 );
		}

		public DoubleConfig getConfig( EquipmentSlot slot ) {
			return this.multipliers.get( slot );
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

	public static class Context< DataType extends ContextData > extends Condition< DataType > {
		protected final Predicate< DataType > predicate;

		public Context( Predicate< DataType > predicate ) {
			this.predicate = predicate;

			this.apply( params->params.setPriority( Priority.LOW ) );
		}

		@Override
		protected boolean check( GameModifier gameModifier, DataType data ) {
			return this.predicate.test( data );
		}
	}

	public static class Cooldown< DataType extends ContextData > extends Condition< DataType > {
		protected final Predicate< Double > distribution;
		protected final DoubleConfig cooldown;

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

		public DoubleConfig getConfig() {
			return this.cooldown;
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
			this.apply( params->params.setPriority( Priority.HIGH ) );
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

			this.apply( params->params.setPriority( Priority.HIGH ) );
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
}
