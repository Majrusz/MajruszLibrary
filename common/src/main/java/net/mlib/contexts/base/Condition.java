package net.mlib.contexts.base;

import java.util.function.Predicate;

public class Condition< DataType > {
	final Predicate< DataType > predicate;
	boolean isNegated = false;

	public Condition( Predicate< DataType > predicate ) {
		this.predicate = predicate;
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
}
