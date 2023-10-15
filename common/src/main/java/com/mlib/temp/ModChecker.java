package com.mlib.temp;

import com.mlib.annotation.AutoInstance;
import com.mlib.command.Command;
import com.mlib.command.CommandData;
import com.mlib.command.IParameter;
import com.mlib.platform.Integration;
import com.mlib.text.TextHelper;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.entity.player.Player;

@AutoInstance
public class ModChecker {
	static final IParameter< String > ID = Command.string().named( "mod_id" );

	public ModChecker() {
		Command.create()
			.literal( "mymodcheck" )
			.hasPermission( 4 )
			.parameter( ID )
			.execute( this::sendStatus )
			.register();
	}

	private int sendStatus( CommandData data ) throws CommandSyntaxException {
		if( data.getCaller() instanceof Player player ) {
			String id = data.get( ID );
			player.sendSystemMessage( TextHelper.literal( "%s - %s", id, Integration.isLoaded( id ) ? "true" : "false" ) );
		}

		return 0;
	}
}
