package com.mlib.gamemodifiers.parameters;

public class ContextParameters extends Parameters {
	final String configName;
	final String configComment;

	public ContextParameters( String configName, String configComment ) {
		this.configName = configName;
		this.configComment = configComment;
	}

	public ContextParameters() {
		this( "", "" );
	}

	public String getConfigName() {
		return this.configName;
	}

	public String getConfigComment() {
		return this.configComment;
	}
}
