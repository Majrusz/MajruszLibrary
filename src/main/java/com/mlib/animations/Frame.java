package com.mlib.animations;

import net.minecraft.util.Mth;

public abstract class Frame< Type > {
	public final float startDuration;
	public final Type value;
	public final InterpolationType interpolationType;

	public Frame( float startDuration, Type value, InterpolationType interpolationType ) {
		this.startDuration = startDuration;
		this.value = value;
		this.interpolationType = interpolationType;
	}

	public Frame( float startDuration, Type value ) {
		this( startDuration, value, InterpolationType.LINEAR );
	}

	public abstract Type interpolate( float ratio, Frame< Type > nextFrame );

	public Type getValue() {
		return this.value;
	}

	public enum InterpolationType {
		LINEAR( x->x ), SQUARE( x->x * x ), SQUARE_ROOT( Mth::sqrt ), CUBE( x->x * x * x );

		private final IFormula formula;

		InterpolationType( IFormula formula ) {
			this.formula = formula;
		}

		public float apply( float ratio ) {
			return this.formula.apply( ratio );
		}

		protected interface IFormula {
			float apply( float ratio );
		}
	}
}
