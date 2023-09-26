package net.mlib.data;

import net.minecraft.world.item.enchantment.Enchantment;

class ReaderEnchantment extends ReaderStringCustom< Enchantment > {
	@Override
	public Enchantment convert( String id ) {
		return null; // TODO: Utility.getEnchantment( id );
	}

	@Override
	public String convert( Enchantment enchantment ) {
		return null; // TODO: Utility.getRegistryString( enchantment );
	}
}
