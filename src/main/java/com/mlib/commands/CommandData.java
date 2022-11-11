package com.mlib.commands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

public class CommandData {
	public final CommandContext< CommandSourceStack > context;
	public final CommandSourceStack source;

	public CommandData( CommandContext< CommandSourceStack > context ) {
		this.context = context;
		this.source = context.getSource();
	}
}
