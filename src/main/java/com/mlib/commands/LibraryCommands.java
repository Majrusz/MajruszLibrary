package com.mlib.commands;

import com.mlib.MajruszLibrary;
import com.mlib.annotations.AutoInstance;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

public class LibraryCommands {
	@AutoInstance
	public static class GetModList extends Command {
		public GetModList() {
			CommandBuilder builder = this.newBuilder();
			builder.literal( "mlib" )
				.hasPermission( 4 )
				.literal( "mods1" )
				.execute( this::empty )
				.integer()
				.execute( this::withInteger )
				.enumeration( Enumeration.class )
				.execute( this::withEnum );
		}

		private int empty( CommandContext< CommandSourceStack > context ) {
			MajruszLibrary.log( "test" );

			return 1;
		}

		private int withInteger( CommandContext< CommandSourceStack > context ) {
			MajruszLibrary.log( "test %d", this.getInteger( context ) );

			return 1;
		}

		private int withEnum( CommandContext< CommandSourceStack > context ) {
			MajruszLibrary.log( "test %d %s", this.getInteger( context ), this.getEnumeration( context, Enumeration.class ).name() );

			return 1;
		}

		public enum Enumeration {
			ABC, DEF, GHI
		}
	}
}
