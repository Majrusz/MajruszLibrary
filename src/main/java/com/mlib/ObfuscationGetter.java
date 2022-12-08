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
			try {
				if( this.field != null ) {
					return ( ReturnType )this.field.get( instance );
				}
			} catch( Exception ignored ) {}

			return null;
		}

		public void set( Type instance, ReturnType value ) {
			if( this.field == null )
				return;

			try {
				this.field.set( instance, value );
			} catch( Exception ignored ) {}
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
			try {
				if( this.method != null ) {
					return this.method.invoke( instance, args );
				}
			} catch( Exception ignored ) {}

			return null;
		}
	}
}

