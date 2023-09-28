package net.mlib.temp;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.level.ServerPlayer;
import net.mlib.MajruszLibrary;
import net.mlib.annotations.AutoInstance;
import net.mlib.commands.Command;
import net.mlib.commands.CommandData;
import net.mlib.data.SerializableStructure;

@AutoInstance
public class TestCommand {
	public TestCommand() {
		Command.create()
			.literal( "mysend" )
			.hasPermission( 4 )
			.integer()
			.execute( this::handleMessage )
			.register();
	}

	private int handleMessage( CommandData data ) throws CommandSyntaxException {
		int value = data.getInteger();
		MajruszLibrary.HELPER.log( "[SENDER] %d", value );
		if( data.getOptionalEntityOrPlayer() instanceof ServerPlayer player ) {
			MajruszLibrary.MESSAGE.sendToClients( new Message( value + 1 ) );
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
		public void onClient() {
			MajruszLibrary.HELPER.log( "[CLIENT] %d", this.value );
		}
	}
}
