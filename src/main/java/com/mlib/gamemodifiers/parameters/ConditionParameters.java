package com.mlib.gamemodifiers.parameters;

public class ConditionParameters extends Parameters {
	boolean isNegated = false;
	boolean isConfigurable = false;

	public ConditionParameters setNegated( boolean isNegated ) {
		this.isNegated = isNegated;

		return this;
	}

	public boolean isNegated() {
		return this.isNegated;
	}

	public ConditionParameters setConfigurable( boolean isConfigurable ) {
		this.isConfigurable = isConfigurable;

		return this;
	}

	public boolean isConfigurable() {
		return this.isConfigurable;
	}
}
