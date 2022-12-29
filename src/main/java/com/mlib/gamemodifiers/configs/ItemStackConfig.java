package com.mlib.gamemodifiers.configs;

import com.mlib.Random;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.items.ItemHelper;
import com.mlib.math.Range;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ItemStackConfig extends ConfigGroup {
	final Supplier< ? extends Item > item;
	final EquipmentSlot equipmentSlot;
	final DoubleConfig chance;
	final DoubleConfig dropChance;

	public ItemStackConfig( Supplier< ? extends Item > item, EquipmentSlot equipmentSlot, double chance, double dropChance ) {
		this.item = item;
		this.equipmentSlot = equipmentSlot;
		this.chance = new DoubleConfig( chance, Range.CHANCE );
		this.dropChance = new DoubleConfig( dropChance, Range.CHANCE );

		this.addConfig( this.chance.name( "chance" ).comment( "Chance that a mob will get the item." ) )
			.addConfig( this.dropChance.name( "drop_chance" ).comment( "Chance for item to drop." ) );
	}

	public ItemStackConfig( Item item, EquipmentSlot equipmentSlot, double chance, double dropChance ) {
		this( ()->item, equipmentSlot, chance, dropChance );
	}

	public ItemStackConfig( RegistryObject< ? extends Item > item, EquipmentSlot equipmentSlot, double chance, double dropChance
	) {
		this( item::get, equipmentSlot, chance, dropChance );
	}

	public void tryToEquip( PathfinderMob mob, double clampedRegionalDifficulty ) {
		if( !Random.tryChance( this.getChance() ) )
			return;

		ItemStack itemStack = this.buildItemStack( clampedRegionalDifficulty );
		mob.setItemSlot( this.equipmentSlot, itemStack );
		mob.setDropChance( this.equipmentSlot, ( float )this.getDropChance() );
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

	protected ItemStack buildItemStack( double clampedRegionalDifficulty ) {
		ItemStack itemStack = new ItemStack( this.getItem() );
		if( itemStack.isDamageableItem() ) {
			return ItemHelper.damageItem( itemStack, 0.5 );
		} else {
			return itemStack;
		}
	}
}
