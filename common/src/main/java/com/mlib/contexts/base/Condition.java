package com.mlib.contexts.base;

import com.mlib.contexts.data.ILevelData;
import com.mlib.MajruszLibrary;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class Condition< DataType > {
	final Predicate< DataType > predicate;
	boolean isNegated = false;

	public static < DataType > Condition< DataType > hasAuthority() {
		return new Condition<>( data->MajruszLibrary.SIDE.isAuthority() );
	}

	public static < DataType extends ILevelData > Condition< DataType > hasLevel() {
		return new Condition<>( data->data.getLevel() != null );
	}

	public static < DataType > Condition< DataType > predicate( Predicate< DataType > predicate ) {
		return new Condition<>( predicate );
	}

	public static < DataType > Condition< DataType > predicate( Supplier< Boolean > check ) {
		return new Condition<>( data->check.get() );
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
