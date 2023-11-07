package com.mlib.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IParameter< Type > {
	CommandBuilder apply( CommandBuilder builder );

	Type get( CommandContext< CommandSourceStack > context );

	class Named< Type > implements IParameter< Type > {
		Consumer< CommandBuilder > applier;
		Function< CommandContext< CommandSourceStack >, Type > getter;
		String name;

		@Override
		public CommandBuilder apply( CommandBuilder builder ) {
			this.applier.accept( builder );

			return builder;
		}

		@Override
		public Type get( CommandContext< CommandSourceStack > context ) {
			return this.getter.apply( context );
		}

		public Named< Type > named( String name ) {
			this.name = name;

			return this;
		}

		Named< Type > applier( Consumer< CommandBuilder > applier ) {
			this.applier = applier;

			return this;
		}

		Named< Type > getter( Function< CommandContext< CommandSourceStack >, Type > getter ) {
			this.getter = getter;

			return this;
		}
	}

	class Hinted< Type > extends Named< Type > {
		Supplier< List< Type > > suggestions = null;

		@Override
		public Hinted< Type > named( String name ) {
			super.named( name );

			return this;
		}

		public Hinted< Type > suggests( Supplier< List< Type > > suggestions ) {
			this.suggestions = suggestions;

			return this;
		}
	}
}
