package com.mlib.commands;

import com.mlib.annotations.AutoInstance;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class LibCommands {
	@AutoInstance
	public static class RuntimeTests extends Command {
		public RuntimeTests() {
			this.newBuilder()
				.literal( "mlib" )
				.literal( "test" )
				.isDevelopmentBuild()
				.execute( this::handle )
				.enumeration( Type.class )
				.execute( this::handle );
		}

		private int handle( CommandData data ) {
			this.getOptionalEnumeration( data, Type.class )
				.ifPresentOrElse( Type.accept( data ), ()->Stream.of( Type.values() ).forEach( Type.accept( data ) ) );

			return 0;
		}

		private static void testContext( CommandData data ) {
			data.source.sendSystemMessage( Component.literal( "abc" ) );
		}

		private enum Type {
			CONTEXT( RuntimeTests::testContext );

			final Consumer< CommandData > consumer;

			Type( Consumer< CommandData > consumer ) {
				this.consumer = consumer;
			}

			static Consumer< Type > accept( CommandData data ) {
				return type->type.consumer.accept( data );
			}
		}
	}
}
