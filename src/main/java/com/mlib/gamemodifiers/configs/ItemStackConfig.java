package com.mlib.gamemodifiers.configs;

import com.mlib.Random;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.items.ItemHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.Item;

import java.util.Optional;
import java.util.function.Supplier;

public class ItemStackConfig extends ConfigGroup {
	final Supplier< Item > item;
	final EquipmentSlot equipmentSlot;
	final DoubleConfig chance;
	final DoubleConfig dropChance;
	final Optional< DoubleConfig > enchantChance;

	@Deprecated
	public ItemStackConfig( String groupName, String groupComment, Supplier< Item > item, EquipmentSlot equipmentSlot, double chance, double dropChance,
		Optional< Double > enchantChance
	) {
		super( groupName, groupComment );
		this.item = item;
		this.equipmentSlot = equipmentSlot;
		this.chance = new DoubleConfig( "chance", "Chance that a mob will get the item.", false, chance, 0.0, 1.0 );
		this.dropChance = new DoubleConfig( "drop_chance", "Chance for item to drop.", false, dropChance, 0.0, 1.0 );
		this.enchantChance = enchantChance.map( value->new DoubleConfig( "enchant_chance", "Chance for item to be randomly enchanted (enchants depend on Clamped Regional Difficulty).", false, value, 0.0, 1.0 ) );
		this.addConfigs( this.chance, this.dropChance );
		this.enchantChance.ifPresent( this::addConfig );
	}

	public ItemStackConfig( String groupName, String groupComment, Supplier< Item > item, EquipmentSlot equipmentSlot, double chance, double dropChance,
		double enchantChance
	) {
		this( groupName, groupComment, item, equipmentSlot, chance, dropChance, Optional.of( enchantChance ) );
	}

	public ItemStackConfig( String groupName, String groupComment, Supplier< Item > item, EquipmentSlot equipmentSlot, double chance, double dropChance ) {
		this( groupName, groupComment, item, equipmentSlot, chance, dropChance, Optional.empty() );
	}

	public ItemStackConfig( String groupName, Supplier< Item > item, EquipmentSlot equipmentSlot, double chance, double dropChance, double enchantChance
	) {
		this( groupName, "", item, equipmentSlot, chance, dropChance, enchantChance );
	}

	public ItemStackConfig( String groupName, Supplier< Item > item, EquipmentSlot equipmentSlot, double chance, double dropChance ) {
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
}
