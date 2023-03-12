package com.mlib.gamemodifiers.parameters;

import com.mlib.gamemodifiers.IParameterizable;
import com.mlib.gamemodifiers.Priority;
import net.minecraft.util.Mth;

import java.util.Comparator;

public class Parameters {
	public static final Comparator< IParameterizable< ? > > COMPARATOR = ( left, right )->Mth.sign( getPriorityAsInt( left ) - getPriorityAsInt( right ) );
	Priority priority = Priority.NORMAL;

	public Parameters priority( Priority priority ) {
		this.priority = priority;

		return this;
	}

	public Priority getPriority() {
		return this.priority;
	}

	public int getPriorityAsInt() {
		return this.priority.ordinal();
	}

	private static int getPriorityAsInt( IParameterizable< ? > parameterizable ) {
		return parameterizable.getParams().getPriorityAsInt();
	}
}
