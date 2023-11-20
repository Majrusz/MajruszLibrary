package com.majruszlibrary.command;

import com.google.gson.JsonObject;
import com.majruszlibrary.mixin.IMixinArgumentTypeInfos;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class EnumArgument< T extends Enum< T > > implements ArgumentType< T > {
	private static final Dynamic2CommandExceptionType INVALID_ENUM = new Dynamic2CommandExceptionType( ( found, constants )->Component.translatable( "commands.majruszlibrary.arguments.enum.invalid", constants, found ) );
	private final Class< T > enumClass;
	private final List< String > examples;

	public static < R extends Enum< R > > EnumArgument< R > enumArgument( Class< R > enumClass ) {
		return new EnumArgument<>( enumClass );
	}

	public static ArgumentTypeInfo< ?, ? > register() {
		ArgumentTypeInfo< ?, ? > argument = new EnumArgument.Info<>();
		IMixinArgumentTypeInfos.getByClass().put( EnumArgument.class, new EnumArgument.Info<>() );

		return argument;
	}

	private EnumArgument( Class< T > enumClass ) {
		this.enumClass = enumClass;
		this.examples = Stream.of( this.enumClass.getEnumConstants() ).map( Enum::name ).toList();
	}

	@Override
	public T parse( final StringReader reader ) throws CommandSyntaxException {
		String name = reader.readUnquotedString();
		try {
			return Enum.valueOf( this.enumClass, name );
		} catch( IllegalArgumentException e ) {
			throw INVALID_ENUM.createWithContext( reader, name, Arrays.toString( this.examples.toArray() ) );
		}
	}

	@Override
	public < S > CompletableFuture< Suggestions > listSuggestions( final CommandContext< S > context, final SuggestionsBuilder builder ) {
		return SharedSuggestionProvider.suggest( this.examples.stream(), builder );
	}

	@Override
	public Collection< String > getExamples() {
		return this.examples;
	}

	public static class Info< T extends Enum< T > > implements ArgumentTypeInfo< EnumArgument< T >, Info< T >.Template > {
		@Override
		public void serializeToNetwork( Template template, FriendlyByteBuf buffer ) {
			buffer.writeUtf( template.enumClass.getName() );
		}

		@Override
		public Template deserializeFromNetwork( FriendlyByteBuf buffer ) {
			try {
				return new Template( ( Class< T > )Class.forName( buffer.readUtf() ) );
			} catch( ClassNotFoundException exception ) {
				return null;
			}
		}

		@Override
		public void serializeToJson( Template template, JsonObject json ) {
			json.addProperty( "enum", template.enumClass.getName() );
		}

		@Override
		public Template unpack( EnumArgument< T > argument ) {
			return new Template( argument.enumClass );
		}

		public class Template implements ArgumentTypeInfo.Template< EnumArgument< T > > {
			final Class< T > enumClass;

			Template( Class< T > enumClass ) {
				this.enumClass = enumClass;
			}

			@Override
			public EnumArgument< T > instantiate( CommandBuildContext p_223435_ ) {
				return new EnumArgument<>( this.enumClass );
			}

			@Override
			public ArgumentTypeInfo< EnumArgument< T >, ? > type() {
				return Info.this;
			}
		}
	}
}

