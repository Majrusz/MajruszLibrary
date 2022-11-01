package com.mlib.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

@Deprecated
public interface IRegistrableCommand {
	/** Registers this command. */
	void register( CommandDispatcher< CommandSourceStack > commandDispatcher );
}
