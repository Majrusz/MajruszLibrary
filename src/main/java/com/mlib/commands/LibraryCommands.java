package com.mlib.commands;

import com.mlib.MajruszLibrary;
import com.mlib.annotations.AutoInstance;

public class LibraryCommands {
	@AutoInstance
	public static class GetModList extends Command {
		public GetModList() {
			CommandBuilder builder = this.newBuilder();
			builder.literal( "mlib" ).hasPermission( 4 );
			builder.literal( "mods1", "mods2" ).execute( context -> {
				MajruszLibrary.log( "test" );

				return 1;
			} );
		}
	}
}
