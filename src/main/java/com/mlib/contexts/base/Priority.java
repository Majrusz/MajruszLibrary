package com.mlib.contexts.base;

import net.minecraft.util.Mth;

import java.util.Comparator;

public enum Priority {
	HIGHEST, HIGH, NORMAL, LOW, LOWEST;

	public static final Comparator< Priority > COMPARATOR = ( left, right )->Mth.sign( left.ordinal() - right.ordinal() );
}
