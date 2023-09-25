package net.mlib.temp;

import net.mlib.MajruszLibrary;
import net.mlib.annotations.AutoInstance;
import net.mlib.commands.Command;
import net.mlib.commands.CommandData;

@AutoInstance
public class TestCommand {
	public TestCommand() {
		Command.create()
			.literal( "mycommand" )
			.hasPermission( 4 )
			.execute( this::handle )
			.register();
	}

	private int handle( CommandData data ) {
		MajruszLibrary.HELPER.log( ":D" );

		return 0;
	}
}
