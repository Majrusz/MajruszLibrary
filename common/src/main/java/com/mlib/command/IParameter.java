package com.mlib.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

public interface IParameter< Type > {
	CommandBuilder apply( CommandBuilder builder );

	Type get( CommandContext< CommandSourceStack > context );

	abstract class Named< Type > implements IParameter< Type > {
		String name;

		public Named< Type > named( String name ) {
			this.name = name;

			return this;
		}
	}
}
