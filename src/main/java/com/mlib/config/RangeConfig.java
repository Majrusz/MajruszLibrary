package com.mlib.config;

import com.mlib.math.Range;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Supplier;

@Deprecated( since = "5.1.0 use ValueRangeConfig instead" )
public abstract class RangeConfig< Type extends Number & Comparable< Type > > extends UserConfig implements Supplier< Range< Type > > {
	protected final Range< Type > defaultValue;
	protected final Predicate predicateFrom;
	protected final Predicate predicateTo;
	protected ForgeConfigSpec.ConfigValue< Type > configFrom = null;
	protected ForgeConfigSpec.ConfigValue< Type > configTo = null;

	public RangeConfig( Range< Type > defaultValue, Range< Type > range ) {
		this.defaultValue = defaultValue;
		this.predicateFrom = new Predicate( range, this.lessThanMax() );
		this.predicateTo = new Predicate( range, this.moreThanMin() );
	}

	@Override
	public UserConfig comment( String comment ) {
		return super.comment( String.format( "%s\n%s (from < to)", comment, this.predicateFrom ) );
	}

	@Override
	public boolean isBuilt() {
		return this.configFrom != null && this.configTo != null;
	}

	/** Returns stored (or cached) value. */
	@Override
	public Range< Type > get() {
		assert this.isBuilt() : "Config has not been initialized yet!";

		return new Range<>( this.configFrom.get(), this.configTo.get() );
	}

	public Range< Type > getOrDefault() {
		return this.isBuilt() ? this.get() : this.defaultValue;
	}

	protected class Predicate implements java.util.function.Predicate< Object > {
		final Range< Type > range;
		final java.util.function.Predicate< Number > condition;

		public Predicate( Range< Type > range, java.util.function.Predicate< Number > condition ) {
			this.range = range;
			this.condition = condition;
		}

		@Override
		public boolean test( Object value ) {
			return value instanceof Number number
				&& this.range.from.doubleValue() < number.doubleValue() && number.doubleValue() < this.range.to.doubleValue()
				&& this.condition.test( number );
		}

		@Override
		public String toString() {
			return String.format( "Range: %s ~ %s", this.range.from, this.range.to );
		}
	}

	private java.util.function.Predicate< Number > lessThanMax() {
		return value->value.doubleValue() <= this.configTo.get().doubleValue();
	}

	private java.util.function.Predicate< Number > moreThanMin() {
		return value->value.doubleValue() >= this.configFrom.get().doubleValue();
	}
}
