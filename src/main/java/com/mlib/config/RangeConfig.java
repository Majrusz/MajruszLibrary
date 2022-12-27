package com.mlib.config;

import com.mlib.math.Range;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Supplier;

public abstract class RangeConfig< Type extends Number > extends UserConfig implements Supplier< Range< Type > > {
	protected final Range< Type > defaultValue;
	protected final Predicate predicateFrom;
	protected final Predicate predicateTo;
	protected ForgeConfigSpec.ConfigValue< Type > configFrom = null;
	protected ForgeConfigSpec.ConfigValue< Type > configTo = null;

	public RangeConfig( String name, String comment, boolean worldRestartRequired, Range< Type > defaultValue, Type min,
		Type max
	) {
		super( name, addRangeInfo( comment, min, max ), worldRestartRequired );
		this.defaultValue = defaultValue;
		this.predicateFrom = new Predicate( min, max, value->value.doubleValue() <= this.configTo.get().doubleValue() );
		this.predicateTo = new Predicate( min, max, value->value.doubleValue() >= this.configFrom.get().doubleValue() );
	}

	/** Returns stored (or cached) value. */
	@Override
	public Range< Type > get() {
		assert this.isBuilt() : "Config has not been initialized yet!";

		return new Range<>( this.configFrom.get(), this.configTo.get() );
	}

	@Override
	public boolean isBuilt() {
		return this.configFrom != null && this.configTo != null;
	}

	public Range< Type > getOrDefault() {
		return this.isBuilt() ? this.get() : this.defaultValue;
	}

	protected class Predicate implements java.util.function.Predicate< Object > {
		final Type min;
		final Type max;
		final java.util.function.Predicate< Number > condition;

		public Predicate( Type min, Type max, java.util.function.Predicate< Number > condition ) {
			this.min = min;
			this.max = max;
			this.condition = condition;
		}

		@Override
		public boolean test( Object value ) {
			return value instanceof Number number
				&& this.min.doubleValue() < number.doubleValue() && number.doubleValue() < this.max.doubleValue()
				&& this.condition.test( number );
		}
	}

	private static < Type extends Number > String addRangeInfo( String comment, Type min, Type max ) {
		return String.format( "%s\nRange: %s ~ %s (from < to)", comment, min, max );
	}
}
