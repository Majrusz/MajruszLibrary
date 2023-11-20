package com.majruszlibrary.data;

import com.majruszlibrary.registry.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

class ReaderEnchantment extends ReaderStringCustom< Enchantment > {
	@Override
	public Enchantment convert( String id ) {
		return Registries.getEnchantment( new ResourceLocation( id ) );
	}

	@Override
	public String convert( Enchantment enchantment ) {
		return Registries.get( enchantment ).toString();
	}
}