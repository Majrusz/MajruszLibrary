package com.mlib.contexts.base;

import com.mlib.Random;
import com.mlib.Utility;
import com.mlib.config.*;
import com.mlib.entities.EntityHelper;
import com.mlib.contexts.data.ILevelData;
import com.mlib.contexts.data.IPositionData;
import com.mlib.contexts.data.ITickData;
import com.mlib.levels.LevelHelper;
import com.mlib.math.AnyPos;
import com.mlib.math.Range;
import com.mlib.text.RegexString;
import com.mlib.time.TimeHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Condition< DataType > extends ConfigGroup {
	final Predicate< DataType > predicate;
	Priority priority = Priority.NORMAL;
	boolean isNegated = false;
	boolean isConfigurable = false;

	public static < DataType > Condition< DataType > excludable( BooleanConfig availability ) {
		return new Condition< DataType >( data->availability.getOrDefault() )
			.priority( Priority.HIGHEST )
			.configurable( true )
			.addConfig( availability );
	}

	public static < DataType > Condition< DataType > excludable( boolean defaultValue ) {
		return excludable( DefaultConfigs.excludable( defaultValue ) );
	}

	public static < DataType > Condition< DataType > excludable() {
		return excludable( true );
	}

	public static < DataType > Condition< DataType > chance( DoubleConfig chance ) {
		return new Condition< DataType >( data->Random.tryChance( chance.getOrDefault() ) )
			.priority( Priority.HIGH )
			.configurable( true )
			.addConfig( chance );
	}

	public static < DataType > Condition< DataType > chance( double defaultChance ) {
		return chance( DefaultConfigs.chance( defaultChance ) );
	}

	/** WARNING: This condition cannot be used with OnSpawned.listen! (use OnSpawned.listenSafe) */
	public static < DataType extends ILevelData & IPositionData > Condition< DataType > chanceCRD( DoubleConfig chance, boolean defaultScaledByCRD ) {
		BooleanConfig scaledByCRD = new BooleanConfig( defaultScaledByCRD );
		Predicate< DataType > predicate = data->{
			double multiplier = 1.0;
			if( scaledByCRD.isEnabled() && data.getLevel() != null ) {
				multiplier *= LevelHelper.getClampedRegionalDifficultyAt( data.getLevel(), AnyPos.from( data.getPosition() ).block() );
			}

			return Random.tryChance( multiplier * chance.getOrDefault() );
		};

		return new Condition< DataType >( predicate )
			.priority( Priority.HIGH )
			.configurable( true )
			.addConfig( chance )
			.addConfig( scaledByCRD.name( "scaled_by_crd" ).comment( "Specifies whether the chance should be scaled by Clamped Regional Difficulty." ) );
	}

	/** WARNING: This condition cannot be used with OnSpawned.listen! (use OnSpawned.listenSafe) */
	public static < DataType extends ILevelData & IPositionData > Condition< DataType > chanceCRD( double defaultChance, boolean defaultScaledByCRD ) {
		return chanceCRD( DefaultConfigs.chance( defaultChance ), defaultScaledByCRD );
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

	public static < DataType > Condition< DataType > cooldown( DoubleConfig cooldown, Dist distribution ) {
		Predicate< Double > predicate = distribution == Dist.CLIENT ? TimeHelper::hasClientSecondsPassed : TimeHelper::hasServerSecondsPassed;

		return new Condition< DataType >( data->predicate.test( cooldown.getOrDefault() ) )
			.priority( Priority.HIGH )
			.configurable( true )
			.addConfig( cooldown );
	}

	public static < DataType > Condition< DataType > cooldown( double defaultSeconds, Dist distribution ) {
		return cooldown( DefaultConfigs.cooldown( defaultSeconds ), distribution );
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

	public static < DataType > Condition< DataType > isCooldownOver( Function< DataType, Player > player, Supplier< Item > item ) {
		return new Condition< DataType >( data->player.apply( data ) != null && !player.apply( data ).getCooldowns().isOnCooldown( item.get() ) );
	}

	public static < DataType extends ILevelData > Condition< DataType > isLevel( StringListConfig levels ) {
		return new Condition< DataType >( data->levels.contains( data.getLevel().dimension().location().toString() ) )
			.priority( Priority.HIGH )
			.configurable( true )
			.addConfig( levels );
	}

	public static < DataType extends ILevelData > Condition< DataType > isLevel( String... levelIds ) {
		return Condition.isLevel( DefaultConfigs.isLevel( levelIds ) );
	}

	@SafeVarargs
	public static < DataType extends ILevelData > Condition< DataType > isLevel( ResourceKey< Level >... levels ) {
		return Condition.isLevel( Arrays.stream( levels ).map( level->level.location().toString() ).toArray( String[]::new ) );
	}

	public static < DataType extends ILevelData > Condition< DataType > isAnyLevel() {
		return Condition.isLevel( "%s.*".formatted( RegexString.REGEX_PREFIX ) );
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

	public static class DefaultConfigs {
		public static BooleanConfig excludable( boolean defaultValue ) {
			BooleanConfig availability = new BooleanConfig( defaultValue );
			availability.name( "is_enabled" ).comment( "Specifies whether this is enabled." );

			return availability;
		}

		public static DoubleConfig chance( double defaultChance ) {
			DoubleConfig chance = new DoubleConfig( defaultChance, Range.CHANCE );
			chance.name( "chance" ).comment( "Chance for this to happen." );

			return chance;
		}

		public static DoubleConfig cooldown( double defaultSeconds ) {
			DoubleConfig cooldown = new DoubleConfig( defaultSeconds, new Range<>( 0.1, 300.0 ) );
			cooldown.name( "cooldown" ).comment( "Cooldown in seconds before it happens." );

			return cooldown;
		}

		public static StringListConfig isLevel( String... levelIds ) {
			StringListConfig dimensions = new StringListConfig( levelIds );
			dimensions.name( "dimensions" ).comment( "Determines in which dimensions it should work." );

			return dimensions;
		}
	}
}
