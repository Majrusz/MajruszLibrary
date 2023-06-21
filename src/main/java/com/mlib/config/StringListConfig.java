package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class StringListConfig extends ValueConfig< List< ? extends String > > {
	public static final String REGEX_PREFIX = "{regex}";
	protected final List< Predicate< String > > predicates = new ArrayList<>();

	public StringListConfig( List< ? extends String > defaultValue ) {
		super( defaultValue );
	}

	public StringListConfig( String... values ) {
		super( Arrays.asList( values ) );
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		super.build( builder );

		this.config = builder.defineList( this.name, this.defaultValue, list->true );
	}

	@Override
	public void onReload() {
		super.onReload();

		this.predicates.clear();
		this.getOrDefault()
			.forEach( value->{
				if( value.startsWith( REGEX_PREFIX ) ) {
					this.predicates.add( subvalue->subvalue.matches( value.substring( REGEX_PREFIX.length() ) ) );
				} else {
					this.predicates.add( subvalue->subvalue.equals( value ) );
				}
			} );
	}

	public boolean contains( String value ) {
		for( Predicate< String > predicate : this.predicates ) {
			if( predicate.test( value ) ) {
				return true;
			}
		}

		return false;
	}
}
