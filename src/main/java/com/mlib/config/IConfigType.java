package com.mlib.config;

public interface IConfigType< Type > extends IConfig {
	/** Returns value directly stored in a config. */
	Type get();
}
