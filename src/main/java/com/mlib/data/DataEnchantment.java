package com.mlib.data;

import com.mlib.Utility;
import net.minecraft.world.item.enchantment.Enchantment;

class DataEnchantment extends Data< Enchantment > {
	@Override
	protected JsonReader< Enchantment > getJsonReader() {
		return element->Utility.getEnchantment( element.getAsString() );
	}

	@Override
	protected BufferWriter< Enchantment > getBufferWriter() {
		return ( buffer, value )->buffer.writeUtf( Utility.getRegistryString( value ) );
	}

	@Override
	protected BufferReader< Enchantment > getBufferReader() {
		return buffer->Utility.getEnchantment( buffer.readUtf() );
	}

	@Override
	protected TagWriter< Enchantment > getTagWriter() {
		return ( tag, key, value )->tag.putString( key, Utility.getRegistryString( value ) );
	}

	@Override
	protected TagReader< Enchantment > getTagReader() {
		return ( tag, key )->Utility.getEnchantment( tag.getString( key ) );
	}
}
