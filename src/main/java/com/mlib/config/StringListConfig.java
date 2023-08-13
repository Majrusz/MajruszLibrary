package com.mlib.config;

import com.mlib.text.RegexString;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringListConfig extends ValueConfig< List< ? extends String > > {
	protected final List< RegexString > strings = new ArrayList<>();

	public StringListConfig( List< ? extends String > defaultValue ) {
		super( defaultValue );
	}

	public StringListConfig( String... values ) {
		super( Arrays.asList( values ) );
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		super.build( builder );

		builder.comment( "Supports 'regular expressions' when text starts with %s prefix.".formatted( RegexString.REGEX_PREFIX ) );
		this.config = builder.defineList( this.name, this.defaultValue, list->true );
	}

	@Override
	public void onReload() {
		super.onReload();

		this.strings.clear();
		this.getOrDefault().forEach( value->this.strings.add( new RegexString( value ) ) );
	}

	public boolean contains( String value ) {
		return this.strings.stream().anyMatch( subvalue->subvalue.matches( value ) );
	}
}
