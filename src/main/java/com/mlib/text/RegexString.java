package com.mlib.text;

import java.util.function.Predicate;

public class RegexString {
	public static final String REGEX_PREFIX = "{regex}";
	String value;
	Predicate< String > predicate = value->value.equals( this.value );

	public RegexString() {}

	public RegexString( String value ) {
		this.set( value );
	}

	public boolean matches( String value ) {
		return this.predicate.test( value );
	}

	public void set( String value ) {
		this.value = value;
		if( this.value.startsWith( REGEX_PREFIX ) ) {
			String pattern = this.value.substring( REGEX_PREFIX.length() );
			this.predicate = subvalue->subvalue.matches( pattern );
		} else {
			this.predicate = subvalue->subvalue.equals( this.value );
		}
	}

	public String get() {
		return this.value;
	}
}
