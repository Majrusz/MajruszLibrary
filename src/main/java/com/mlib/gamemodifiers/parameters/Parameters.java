package com.mlib.gamemodifiers.parameters;

import com.mlib.Utility;
import com.mlib.gamemodifiers.IParameterizable;
import net.minecraft.util.Mth;

import java.util.Comparator;

public class Parameters {
	public static final Comparator< IParameterizable > COMPARATOR = ( left, right )->Mth.sign( priorityAsInt( left ) - priorityAsInt( right ) );
	final Priority priority;

	public static int priorityAsInt( IParameterizable parameterizable ) {
		return parameterizable.getParams().priority.ordinal();
	}

	public Parameters( Priority priority ) {
		this.priority = Utility.or( priority, Priority.NORMAL );
	}
}
