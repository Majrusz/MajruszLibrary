package net.mlib.temp;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.mlib.MajruszLibrary;
import net.mlib.annotations.AutoInstance;
import net.mlib.commands.Command;
import net.mlib.commands.CommandData;
import net.mlib.data.SerializableStructure;

@AutoInstance
public class TestCommand {
	public TestCommand() {
		Command.create()
			.literal( "mycommand" )
			.execute( this::handle )
			.enumeration( SomeType.class )
			.hasPermission( 4 )
			.execute( this::handle )
			.register();

		Command.create()
			.literal( "mysend" )
			.hasPermission( 4 )
			.integer()
			.execute( this::handleMessage )
			.register();
	}

	private int handle( CommandData data ) {
		MajruszLibrary.HELPER.log( "%s :D", data.getOptionalEnumeration( SomeType.class ).orElse( SomeType.NORMAL ) );

		return 0;
	}

	private int handleMessage( CommandData data ) throws CommandSyntaxException {
		int value = data.getInteger();
		MajruszLibrary.HELPER.log( "[SENDER] %d", value );
		if( data.getOptionalEntityOrPlayer() instanceof Player player ) {
			MajruszLibrary.MESSAGE.sendToServer( new Message( value ) );
		}

		return 0;
	}

	public enum SomeType {
		SMALL,
		NORMAL,
		BIG
	}

	public static class Message extends SerializableStructure {
		int value;

		public Message() {
			this.defineInteger( "value", ()->this.value, x->this.value = x );
		}

		public Message( int value ) {
			this();

			this.value = value;
		}

		@Override
		public void onServer( ServerPlayer player ) {
			MajruszLibrary.HELPER.log( "[SERVER] %d %s", this.value, player.toString() );
			this.value++;
			MajruszLibrary.MESSAGE.sendToClients( this );
		}

		@Override
		public void onClient() {
			MajruszLibrary.HELPER.log( "[CLIENT] %d", this.value );
		}
	}
}
