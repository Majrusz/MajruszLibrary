package com.mlib.mixin;

import com.mlib.contexts.OnCommandsInitialized;
import com.mlib.contexts.base.Contexts;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( Commands.class )
public abstract class MixinCommands {
	@Shadow private CommandDispatcher< CommandSourceStack > dispatcher;

	@Inject(
		at = @At( "RETURN" ),
		method = "<init> (Lnet/minecraft/commands/Commands$CommandSelection;Lnet/minecraft/commands/CommandBuildContext;)V"
	)
	private void constructor( Commands.CommandSelection commandSelection, CommandBuildContext context, CallbackInfo callback ) {
		Contexts.dispatch( new OnCommandsInitialized( this.dispatcher ) );
	}
}
