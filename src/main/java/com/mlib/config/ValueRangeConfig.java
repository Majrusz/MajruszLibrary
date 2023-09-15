package com.mlib.config;

import com.mlib.math.Range;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class ValueRangeConfig< Type extends Number & Comparable< Type > > extends UserConfig implements Supplier< Range< Type > > {
	protected final Range< Type > defaultValue;
	protected final Range< Type > range;
	protected ForgeConfigSpec.ConfigValue< Type > from = null;
	protected ForgeConfigSpec.ConfigValue< Type > to = null;

	public ValueRangeConfig( Range< Type > defaultValue, Range< Type > range ) {
		this.defaultValue = defaultValue;
		this.range = range;
	}

	@Override
	public UserConfig comment( String comment ) {
		return super.comment( "%s\nRange: %s (from <= to)".formatted( comment, this.defaultValue ) );
	}

	@Override
	public boolean isBuilt() {
		return this.from != null && this.to != null;
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		super.build( builder );

		builder.push( this.name );
		this.from = builder.define( "from", this.defaultValue.from, this.test( value->value.compareTo( this.defaultValue.to ) <= 0 ) );
		this.to = builder.define( "to", this.defaultValue.to, this.test( value->value.compareTo( this.defaultValue.from ) >= 0 ) );
		builder.pop();
	}

	@Override
	public Range< Type > get() {
		return new Range<>( this.from.get(), this.to.get() );
	}

	public Range< Type > getOrDefault() {
		return this.isBuilt() ? this.get() : this.defaultValue;
	}

	private Predicate< Object > test( Predicate< Type > predicate ) {
		return obj->{
			try {
				Type value = ( Type )obj;

				return value.compareTo( this.defaultValue.from ) >= 0
					&& value.compareTo( this.defaultValue.to ) <= 0
					&& predicate.test( value );
			} catch( Exception e ) {
				return false;
			}
		};
	}
}
