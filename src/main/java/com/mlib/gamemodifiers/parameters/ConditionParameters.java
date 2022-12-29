package com.mlib.gamemodifiers.parameters;

public class ConditionParameters extends Parameters {
	boolean isNegated = false;
	boolean isConfigurable = false;

	public ConditionParameters negated( boolean isNegated ) {
		this.isNegated = isNegated;

		return this;
	}

	public boolean isNegated() {
		return this.isNegated;
	}

	public ConditionParameters configurable( boolean isConfigurable ) {
		this.isConfigurable = isConfigurable;

		return this;
	}

	public boolean isConfigurable() {
		return this.isConfigurable;
	}
}
