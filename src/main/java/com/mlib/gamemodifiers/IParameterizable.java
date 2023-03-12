package com.mlib.gamemodifiers;

import com.mlib.gamemodifiers.parameters.Parameters;

@Deprecated( forRemoval = true )
public interface IParameterizable< Type extends Parameters > {
	Type getParams();
}
