package com.mlib.gamemodifiers;

public interface IRegistrable {
	void setHolder( GameModifiersHolder< ? > holder );

	GameModifiersHolder< ? > getHolder();
}
