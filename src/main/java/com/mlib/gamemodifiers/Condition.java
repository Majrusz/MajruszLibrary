package com.mlib.gamemodifiers;

import com.mlib.EquipmentSlots;
import com.mlib.Random;
import com.mlib.Utility;
import com.mlib.config.BooleanConfig;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.config.IConfigurable;
import com.mlib.entities.EntityHelper;
import com.mlib.gamemodifiers.data.ILevelData;
import com.mlib.gamemodifiers.data.IPositionData;
import com.mlib.gamemodifiers.data.ITickData;
import com.mlib.levels.LevelHelper;
import com.mlib.math.AnyPos;
import com.mlib.math.Range;
import com.mlib.time.TimeHelper;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraftforge.event.TickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Condition< DataType > extends ConfigGroup {
	final Predicate< DataType > predicate;
	Priority priority = Priority.NORMAL;
	boolean isNegated = false;
	boolean isConfigurable = false;

	public static < DataType > Condition< DataType > excludable( boolean defaultValue ) {
		BooleanConfig availability = new BooleanConfig( defaultValue );

		return new Condition< DataType >( data->availability.getOrDefault() )
			.priority( Priority.HIGHEST )
			.configurable( true )
			.addConfig( availability.name( "is_enabled" ).comment( "Specifies whether this is enabled." ) );
	}

	public static < DataType > Condition< DataType > excludable() {
		return excludable( true );
	}

	public static < DataType > Condition< DataType > chance( double defaultChance ) {
		DoubleConfig chance = new DoubleConfig( defaultChance, Range.CHANCE );

		return new Condition< DataType >( data->Random.tryChance( chance.getOrDefault() ) )
			.priority( Priority.HIGH )
			.configurable( true )
			.addConfig( chance.name( "chance" ).comment( "Chance for this to happen." ) );
	}

	/** WARNING: This condition cannot be used with OnSpawned.listen! (use OnSpawned.listenSafe) */
	public static < DataType extends ILevelData & IPositionData > Condition< DataType > chanceCRD( double defaultChance, boolean defaultScaledByCRD ) {
		DoubleConfig chance = new DoubleConfig( defaultChance, Range.CHANCE );
		BooleanConfig scaledByCRD = new BooleanConfig( defaultScaledByCRD );
		Predicate< DataType > predicate = data->{
			double multiplier = scaledByCRD.isEnabled() ? LevelHelper.getRegionalDifficultyAt( data.getLevel(), AnyPos.from( data.getPosition() ).block() ) : 1.0;

			return Random.tryChance( multiplier * chance.getOrDefault() );
		};

		return new Condition< DataType >( predicate )
			.priority( Priority.HIGH )
			.configurable( true )
			.addConfig( chance.name( "chance" ).comment( "Chance for this to happen." ) )
			.addConfig( scaledByCRD.name( "scaled_by_crd" ).comment( "Specifies whether the chance should be scaled by Clamped Regional Difficulty." ) );
	}

	public static < DataType > Condition< DataType > isLivingBeing( Function< DataType, Entity > entity ) {
		return new Condition< DataType >( data->EntityHelper.isAnimal( entity.apply( data ) ) || EntityHelper.isHuman( entity.apply( data ) ) );
	}

	public static < DataType > Condition< DataType > predicate( Predicate< DataType > predicate ) {
		return new Condition< DataType >( predicate )
			.priority( Priority.LOW );
	}

	public static < DataType > Condition< DataType > predicate( Supplier< Boolean > check ) {
		return new Condition< DataType >( data->check.get() )
			.priority( Priority.LOW );
	}

	public static < DataType > Condition< DataType > cooldown( double defaultSeconds, Dist distribution ) {
		Predicate< Double > predicate = distribution == Dist.CLIENT ? TimeHelper::hasClientSecondsPassed : TimeHelper::hasServerSecondsPassed;
		DoubleConfig cooldown = new DoubleConfig( defaultSeconds, new Range<>( 0.1, 300.0 ) );

		return new Condition< DataType >( data->predicate.test( cooldown.getOrDefault() ) )
			.priority( Priority.HIGH )
			.configurable( true )
			.addConfig( cooldown.name( "cooldown" ).comment( "Cooldown in seconds before it happens." ) );
	}

	public static < DataType > Condition< DataType > cooldown( int defaultTicks, Dist distribution ) {
		return cooldown( Utility.ticksToSeconds( defaultTicks ), distribution );
	}

	public static < DataType > Condition< DataType > hasEnchantment( Supplier< ? extends Enchantment > enchantment,
		Function< DataType, LivingEntity > entity
	) {
		return new Condition< DataType >( data->entity.apply( data ) != null && EnchantmentHelper.getEnchantmentLevel( enchantment.get(), entity.apply( data ) ) > 0 );
	}

	public static < DataType > Condition< DataType > hasEnchantment( Enchantment enchantment, Function< DataType, LivingEntity > entity ) {
		return hasEnchantment( ()->enchantment, entity );
	}

	public static < DataType > Condition< DataType > hasEffect( Supplier< ? extends MobEffect > effect,
		Function< DataType, LivingEntity > entity
	) {
		return new Condition< DataType >( data->entity.apply( data ) != null && entity.apply( data ).hasEffect( effect.get() ) );
	}

	public static < DataType > Condition< DataType > hasEffect( MobEffect effect, Function< DataType, LivingEntity > entity ) {
		return hasEffect( ()->effect, entity );
	}

	public static < DataType extends ILevelData > Condition< DataType > isServer() {
		return new Condition< DataType >( data->data.getLevel() instanceof ServerLevel )
			.priority( Priority.HIGH );
	}

	public static < DataType > Condition< DataType > isShiftKeyDown( Function< DataType, Player > player ) {
		return new Condition< DataType >( data->player.apply( data ) != null && player.apply( data ).isShiftKeyDown() )
			.priority( Priority.HIGH );
	}

	public static < DataType > Condition< DataType > isOnGround( Function< DataType, Entity > entity ) {
		return new Condition< DataType >( data->entity.apply( data ) != null && entity.apply( data ).isOnGround() )
			.priority( Priority.HIGH );
	}

	public static < DataType extends ITickData > Condition< DataType > isEndPhase() {
		return new Condition< DataType >( data->data.getPhase() == TickEvent.Phase.END );
	}

	public static < DataType > Condition< DataType > armorDependentChance( Map< EquipmentSlot, Double > chances, Function< DataType, LivingEntity > entity ) {
		Map< EquipmentSlot, DoubleConfig > multipliers = new HashMap<>();
		ConfigGroup group = new ConfigGroup();

		for( EquipmentSlot slot : EquipmentSlots.ARMOR ) {
			DoubleConfig config = new DoubleConfig( chances.get( slot ), Range.CHANCE );
			multipliers.put( slot, config );
			group.addConfig( config.name( String.format( "%s_multiplier", slot.getName() ) ) );
		}

		return new Condition< DataType >( data->{
			double chance = 1.0;
			for( EquipmentSlot slot : multipliers.keySet() ) {
				DoubleConfig config = multipliers.get( slot );
				ItemStack itemStack = entity.apply( data ).getItemBySlot( slot );
				if( !itemStack.isEmpty() && itemStack.getAttributeModifiers( slot ).containsKey( Attributes.ARMOR ) ) {
					chance *= config.getOrDefault();
				}
			}

			return Random.tryChance( chance );
		} ).configurable( true )
			.addConfig( group
				.name( "ArmorChanceMultipliers" )
				.comment( "Chance multipliers for each armor piece.\nFor instance 'head_multiplier = 0.8' makes the final chance 20% lower if mob has any helmet." )
			);
	}

	public static < DataType > Condition< DataType > armorDependentChance( double headChance, double chestChance, double legsChance, double feetChance,
		Function< DataType, LivingEntity > entity
	) {
		return armorDependentChance( Map.of( EquipmentSlot.HEAD, headChance, EquipmentSlot.CHEST, chestChance, EquipmentSlot.LEGS, legsChance, EquipmentSlot.FEET, feetChance ), entity );
	}

	public static < DataType > Condition< DataType > armorDependentChance( double chance, Function< DataType, LivingEntity > entity ) {
		return armorDependentChance( chance, chance, chance, chance, entity );
	}

	public Condition( Predicate< DataType > predicate ) {
		this.predicate = predicate;
	}

	@Override
	public Condition< DataType > addConfig( IConfigurable config ) {
		super.addConfig( config );

		return this;
	}

	@Override
	public Condition< DataType > addConfigs( IConfigurable... configs ) {
		super.addConfigs( configs );

		return this;
	}

	public Condition< DataType > configurable( boolean isConfigurable ) {
		this.isConfigurable = isConfigurable;

		return this;
	}

	public Condition< DataType > negate() {
		this.isNegated = !this.isNegated;

		return this;
	}

	public Condition< DataType > priority( Priority priority ) {
		this.priority = priority;

		return this;
	}

	public Priority getPriority() {
		return this.priority;
	}

	public boolean isNegated() {
		return this.isNegated;
	}

	public boolean isConfigurable() {
		return this.isConfigurable;
	}

	public boolean check( DataType data ) {
		return this.isNegated ^ this.predicate.test( data );
	}
}
