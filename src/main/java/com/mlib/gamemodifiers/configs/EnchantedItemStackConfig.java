package com.mlib.gamemodifiers.configs;

import com.mlib.Random;
import com.mlib.config.DoubleConfig;
import com.mlib.items.ItemHelper;
import com.mlib.math.Range;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class EnchantedItemStackConfig extends ItemStackConfig {
	final DoubleConfig enchantChance;

	public EnchantedItemStackConfig( Supplier< ? extends Item > item, EquipmentSlot equipmentSlot, double chance,
		double dropChance, double enchantChance
	) {
		super( item, equipmentSlot, chance, dropChance );

		this.enchantChance = new DoubleConfig( enchantChance, Range.CHANCE );

		this.addConfig( this.chance.name( "enchant_chance" )
			.name( "Chance for item to be randomly enchanted (enchants depend on Clamped Regional Difficulty)." ) );
	}

	public EnchantedItemStackConfig( Item item, EquipmentSlot equipmentSlot, double chance, double dropChance,
		double enchantChance
	) {
		this( ()->item, equipmentSlot, chance, dropChance, enchantChance );
	}

	public EnchantedItemStackConfig( RegistryObject< ? extends Item > item, EquipmentSlot equipmentSlot, double chance,
		double dropChance,
		double enchantChance
	) {
		this( item::get, equipmentSlot, chance, dropChance, enchantChance );
	}

	public double getEnchantChance() {
		return this.enchantChance.get();
	}

	@Override
	protected ItemStack buildItemStack( double clampedRegionalDifficulty ) {
		ItemStack itemStack = super.buildItemStack( clampedRegionalDifficulty );
		if( Random.tryChance( this.getEnchantChance() ) ) {
			return ItemHelper.enchantItem( itemStack, clampedRegionalDifficulty, true );
		} else {
			return itemStack;
		}
	}
}
