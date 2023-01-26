package com.mlib.data;

import net.minecraft.core.BlockPos;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DataBlockPos extends Data< BlockPos > {
	@Override
	protected JsonReader< BlockPos > getJsonReader() {
		return element->fromString( element.getAsString() );
	}

	@Override
	protected BufferWriter< BlockPos > getBufferWriter() {
		return ( buffer, value )->buffer.writeUtf( toString( value ) );
	}

	@Override
	protected BufferReader< BlockPos > getBufferReader() {
		return buffer->fromString( buffer.readUtf() );
	}

	@Override
	protected TagWriter< BlockPos > getTagWriter() {
		return ( tag, key, value )->tag.putString( key, toString( value ) );
	}

	@Override
	protected TagReader< BlockPos > getTagReader() {
		return ( tag, key )->fromString( tag.getString( key ) );
	}

	private static BlockPos fromString( String position ) {
		Pattern pattern = Pattern.compile( "(-?\\d+),(-?\\d+),(-?\\d+)" );
		Matcher matcher = pattern.matcher( position );

		return matcher.find()
			? new BlockPos( Integer.parseInt( matcher.group( 1 ) ), Integer.parseInt( matcher.group( 2 ) ), Integer.parseInt( matcher.group( 3 ) ) )
			: BlockPos.ZERO;
	}

	private static String toString( BlockPos position ) {
		return String.format( "%d,%d,%d", position.getX(), position.getY(), position.getZ() );
	}
}
