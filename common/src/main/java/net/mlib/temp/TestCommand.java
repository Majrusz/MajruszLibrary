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
			.execute( this::handle )
			.enumeration( SomeType.class )
			.hasPermission( 4 )
			.execute( this::handle )
			.register();
	}

	private int handle( CommandData data ) {
		MajruszLibrary.HELPER.log( "%s :D", data.getOptionalEnumeration( SomeType.class ).orElse( SomeType.NORMAL ) );

		return 0;
	}

	public enum SomeType {
		SMALL,
		NORMAL,
		BIG
	}
}
