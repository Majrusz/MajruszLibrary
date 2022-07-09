package com.mlib.gamemodifiers.parameters;

import com.mlib.Utility;

public class ContextParameters extends Parameters {
	final String configName;
	final String configComment;

	public ContextParameters( Priority priority, String configName, String configComment ) {
		super( priority );
		this.configName = Utility.or( configName, "" );
		this.configComment = Utility.or( configComment, "" );
	}

	public ContextParameters() {
		this( null, null, null );
	}

	public String getConfigName() {
		return this.configName;
	}

	public String getConfigComment() {
		return this.configComment;
	}
}
