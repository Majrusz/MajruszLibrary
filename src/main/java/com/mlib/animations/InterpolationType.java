package com.mlib.animations;

public enum InterpolationType {
	LINEAR( x->x ),
	SQUARE( x->x * x ),
	SQUARE_ROOT( x->( float )Math.sqrt( x ) ),
	CUBE( x->x * x * x ),
	CUBE_ROOT( x->( float )Math.pow( x, 1.0 / 3.0 ) ),
	SMOOTH( x->x * x * ( 3 - 2 * x ) );

	private final IFormula formula;

	InterpolationType( IFormula formula ) {
		this.formula = formula;
	}

	public float apply( float ratio ) {
		return this.formula.apply( ratio );
	}

	interface IFormula {
		float apply( float ratio );
	}
}
