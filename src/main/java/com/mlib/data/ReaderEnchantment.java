package com.mlib.data;

import com.mlib.Utility;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
