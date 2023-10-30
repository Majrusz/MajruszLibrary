package com.mlib.platform;

import java.util.Optional;
import java.util.ServiceLoader;

public class Services {
	public static < Type > Type load( Class< Type > clazz ) {
		return ServiceLoader.load( clazz ).findFirst().orElseThrow();
	}

	public static < Type > Optional< Type > loadOptional( Class< Type > clazz ) {
		return ServiceLoader.load( clazz ).findFirst();
	}
}
