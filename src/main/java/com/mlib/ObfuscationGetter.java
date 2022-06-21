package com.mlib;

import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nullable;

/** Class for easier getting private and protected members and methods from Minecraft/Minecraft Forge classes. */
public class ObfuscationGetter {
	public static class Field< Type, ReturnType > {
		private final java.lang.reflect.Field field;

		public Field( Class< ? super Type > classToAccess, String mcpName ) {
			java.lang.reflect.Field field;
			try {
				field = ObfuscationReflectionHelper.findField( classToAccess, mcpName );
			} catch( Exception exception2 ) {
				field = null;
				assert false;
			}
			this.field = field;
		}

		@Nullable
		public ReturnType get( Type instance ) {
			if( this.field == null )
				return null;

			try {
				return ( ReturnType )this.field.get( instance );
			} catch( Exception exception ) {
				return null;
			}
		}
	}

	public static class Method< Type > {
		private final java.lang.reflect.Method method;

		public Method( Class< ? super Type > classToAccess, String mcpName, Class< ? >... parameterTypes ) {
			java.lang.reflect.Method method;
			try {
				method = ObfuscationReflectionHelper.findMethod( classToAccess, mcpName, parameterTypes );
			} catch( Exception exception2 ) {
				method = null;
				assert false;
			}
			this.method = method;
		}

		@Nullable
		public Object invoke( Type instance, Object... args ) {
			if( this.method == null )
				return null;

			try {
				return this.method.invoke( instance, args );
			} catch( Exception exception ) {
				return null;
			}
		}
	}
}

