package com.mlib.gamemodifiers;

import com.mlib.gamemodifiers.parameters.Parameters;

public interface IParameterizable< Type extends Parameters > {
	Type getParams();
}
