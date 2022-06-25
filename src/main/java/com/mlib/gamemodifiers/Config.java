package com.mlib.gamemodifiers;

import com.mlib.Random;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.config.IntegerConfig;
import com.mlib.effects.EffectHelper;
import com.mlib.items.ItemHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.Item;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class Config {
	final String groupName;
	final String groupComment;

	public Config( String groupName, String groupComment ) {
		this.groupName = groupName;
		this.groupComment = groupComment;
	}

	public abstract void setup( ConfigGroup group );

	public ConfigGroup addNewGroup( ConfigGroup group ) {
		return group.addNewGroup( this.groupName, this.groupComment );
	}

	public static class Effect extends Config {
		static final int MIN_AMPLIFIER = 0, MAX_AMPLIFIER = 9;
		static final double MIN_DURATION = 1.0, MAX_DURATION = 999.0;
		static final double MIN_LIMIT = 5.0, MAX_LIMIT = 999.0;
		final Supplier< MobEffect > effect;
		final IntegerConfig amplifier;
		final DoubleConfig duration;
		final Optional< DoubleConfig > maxDuration;

		public Effect( String groupName, Supplier< MobEffect > effect, int amplifier, double duration, Optional< Double > maxDuration ) {
			super( groupName, "" );
			this.effect = effect;
			this.amplifier = new IntegerConfig( "amplifier", "Level of the effect to apply.", false, amplifier, MIN_AMPLIFIER, MAX_AMPLIFIER );
			this.duration = new DoubleConfig( "duration", "Duration in seconds.", false, duration, MIN_DURATION, MAX_DURATION );
			this.maxDuration = maxDuration.map( value->new DoubleConfig( "maximum_duration", "Maximum duration in seconds it can reach.", false, value, MIN_LIMIT, MAX_LIMIT ) );
		}

		public Effect( String groupName, Supplier< MobEffect > effect, int amplifier, double duration, double maxDuration ) {
			this( groupName, effect, amplifier, duration, Optional.of( maxDuration ) );
		}

		public Effect( String groupName, Supplier< MobEffect > effect, int amplifier, double duration ) {
			this( groupName, effect, amplifier, duration, Optional.empty() );
		}

		@Override
		public void setup( ConfigGroup group ) {
			group.addConfigs( this.amplifier, this.duration );
			this.maxDuration.ifPresent( group::addConfig );
		}

		public void apply( LivingEntity entity ) {
			if( this.maxDuration.isPresent() ) {
				EffectHelper.stackEffectIfPossible( entity, getEffect(), getDuration(), getAmplifier(), getMaxDuration() );
			} else {
				EffectHelper.applyEffectIfPossible( entity, getEffect(), getDuration(), getAmplifier() );
			}
		}

		public MobEffect getEffect() {
			return this.effect.get();
		}

		public int getAmplifier() {
			return this.amplifier.get();
		}

		public int getDuration() {
			return this.duration.asTicks();
		}

		public int getMaxDuration() {
			assert this.maxDuration.isPresent();

			return this.maxDuration.get().asTicks();
		}
	}

	public static class ItemStack extends Config {
		final Supplier< Item > item;
		final EquipmentSlot equipmentSlot;
		final DoubleConfig chance;
		final DoubleConfig dropChance;
		final Optional< DoubleConfig > enchantChance;

		public ItemStack( String groupName, String groupComment, Supplier< Item > item, EquipmentSlot equipmentSlot, double chance, double dropChance,
			Optional< Double > enchantChance
		) {
			super( groupName, groupComment );
			this.item = item;
			this.equipmentSlot = equipmentSlot;
			this.chance = new DoubleConfig( "chance", "Chance that a mob will get the item.", false, chance, 0.0, 1.0 );
			this.dropChance = new DoubleConfig( "drop_chance", "Chance for item to drop.", false, dropChance, 0.0, 1.0 );
			this.enchantChance = enchantChance.map( value->new DoubleConfig( "enchant_chance", "Chance for item to be randomly enchanted (enchants depend on Clamped Regional Difficulty).", false, value, 0.0, 1.0 ) );
		}

		public ItemStack( String groupName, String groupComment, Supplier< Item > item, EquipmentSlot equipmentSlot, double chance, double dropChance,
			double enchantChance
		) {
			this( groupName, groupComment, item, equipmentSlot, chance, dropChance, Optional.of( enchantChance ) );
		}

		public ItemStack( String groupName, String groupComment, Supplier< Item > item, EquipmentSlot equipmentSlot, double chance, double dropChance ) {
			this( groupName, groupComment, item, equipmentSlot, chance, dropChance, Optional.empty() );
		}

		public ItemStack( String groupName, Supplier< Item > item, EquipmentSlot equipmentSlot, double chance, double dropChance, double enchantChance ) {
			this( groupName, "", item, equipmentSlot, chance, dropChance, enchantChance );
		}

		public ItemStack( String groupName, Supplier< Item > item, EquipmentSlot equipmentSlot, double chance, double dropChance ) {
			this( groupName, "", item, equipmentSlot, chance, dropChance );
		}

		public void tryToEquip( PathfinderMob mob, double clampedRegionalDifficulty ) {
			if( !Random.tryChance( getChance() ) )
				return;

			net.minecraft.world.item.ItemStack itemStack = new net.minecraft.world.item.ItemStack( getItem() );
			if( itemStack.isDamageableItem() )
				itemStack = ItemHelper.damageItem( new net.minecraft.world.item.ItemStack( getItem() ), 0.5 );
			if( this.enchantChance.isPresent() && Random.tryChance( getEnchantChance() ) )
				itemStack = ItemHelper.enchantItem( itemStack, clampedRegionalDifficulty, true );
			mob.setItemSlot( this.equipmentSlot, itemStack );
			mob.setDropChance( this.equipmentSlot, ( float )getDropChance() );
		}

		public Item getItem() {
			return this.item.get();
		}

		public double getChance() {
			return this.chance.get();
		}

		public double getDropChance() {
			return this.dropChance.get();
		}

		public double getEnchantChance() {
			assert this.enchantChance.isPresent();

			return this.enchantChance.get().get();
		}

		@Override
		public void setup( ConfigGroup group ) {
			group.addConfigs( this.chance, this.dropChance );
			this.enchantChance.ifPresent( group::addConfigs );
		}
	}
}
