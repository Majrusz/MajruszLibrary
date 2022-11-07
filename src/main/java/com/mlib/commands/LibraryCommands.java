package com.mlib.commands;

import com.mlib.MajruszLibrary;
import com.mlib.annotations.AutoInstance;

public class LibraryCommands {
	@AutoInstance
	public static class GetModList extends Command {
		public GetModList() {
			CommandBuilder builder = this.newBuilder();
			builder.literal( "mlib" ).hasPermission( 4 ).literal( "mods1" ).integer().execute( context -> {
				MajruszLibrary.log( "test %d", this.getInteger( context ) );

				return 1;
			} );
		}
	}
}
