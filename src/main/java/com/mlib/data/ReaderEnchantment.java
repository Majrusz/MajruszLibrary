package com.mlib.data;

import com.mlib.Utility;
import net.minecraft.world.item.enchantment.Enchantment;

class ReaderEnchantment extends ReaderStringCustom< Enchantment > {
	@Override
	public Enchantment convert( String id ) {
		return Utility.getEnchantment( id );
	}

	@Override
	public String convert( Enchantment enchantment ) {
		return Utility.getRegistryString( enchantment );
	}
}
