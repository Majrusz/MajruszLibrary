package net.mlib.contexts.base;

import net.mlib.MajruszLibrary;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class Condition< DataType > {
	final Predicate< DataType > predicate;
	boolean isNegated = false;

	public static < DataType > Condition< DataType > isServer() {
		return new Condition< DataType >( data->MajruszLibrary.SIDE.isServer() );
	}

	public static < DataType > Condition< DataType > predicate( Predicate< DataType > predicate ) {
		return new Condition< DataType >( predicate );
	}

	public static < DataType > Condition< DataType > predicate( Supplier< Boolean > check ) {
		return new Condition< DataType >( data->check.get() );
	}

	public Condition< DataType > negate() {
		this.isNegated = !this.isNegated;

		return this;
	}

	public boolean isNegated() {
		return this.isNegated;
	}

	public boolean check( DataType data ) {
		return this.isNegated ^ this.predicate.test( data );
	}

	private Condition( Predicate< DataType > predicate ) {
		this.predicate = predicate;
	}
}
