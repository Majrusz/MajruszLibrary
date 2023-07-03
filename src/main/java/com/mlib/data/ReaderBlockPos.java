package com.mlib.data;

import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ReaderBlockPos extends ReaderStringCustom< BlockPos > {
	@Override
	public BlockPos convert( String position ) {
		Pattern pattern = Pattern.compile( "(-?\\d+),(-?\\d+),(-?\\d+)" );
		Matcher matcher = pattern.matcher( position );

		return matcher.find()
			? new BlockPos( Integer.parseInt( matcher.group( 1 ) ), Integer.parseInt( matcher.group( 2 ) ), Integer.parseInt( matcher.group( 3 ) ) )
			: BlockPos.ZERO;
	}

	@Override
	public String convert( BlockPos position ) {
		return String.format( "%d,%d,%d", position.getX(), position.getY(), position.getZ() );
	}
}
