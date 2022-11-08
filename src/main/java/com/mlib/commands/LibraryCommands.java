package com.mlib.commands;

import com.mlib.MajruszLibrary;
import com.mlib.annotations.AutoInstance;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.phys.Vec3;

public class LibraryCommands {
	@AutoInstance
	public static class GetModList extends Command {
		public GetModList() {
			CommandBuilder builder1 = new CommandBuilder();
			builder1.literal( "mlib" )
				.hasPermission( 4 )
				.literal( "mods1" )
				.execute( this::empty )
				.integer()
				.execute( this::withInteger )
				.enumeration( Enumeration.class )
				.execute( this::withEnum );

			CommandBuilder builder2 = new CommandBuilder();
			builder2.literal( "mlib" )
				.hasPermission( 4 )
				.literal( "mods2" )
				.execute( this::empty )
				.position()
				.execute( this::withPosition )
				.entity()
				.execute( this::withEntity );

			CommandBuilder builder3 = new CommandBuilder();
			builder3.literal( "mlib" ).hasPermission( 4 ).literal( "mods3" );

			CommandBuilder builder3_1 = builder3.copy();
			builder3_1.execute( context->this.withRandomPosition( context, context.getSource().getPosition() ) );

			CommandBuilder builder3_2 = builder3.copy();
			builder3_2.position().execute( context->this.withRandomPosition( context, this.getPosition( context ) ) );

			CommandBuilder builder3_3 = builder3.copy();
			builder3_3.entity().execute( context->this.withRandomPosition( context, this.getEntity( context ).position() ) );

			this.addBuilders( builder1, builder2, builder3_1, builder3_2, builder3_3 );
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

		private int withPosition( CommandContext< CommandSourceStack > context ) {
			MajruszLibrary.log( "test %s", this.getPosition( context ).toString() );

			return 1;
		}

		private int withEntity( CommandContext< CommandSourceStack > context ) {
			MajruszLibrary.log( "test %s %s", this.getPosition( context ).toString(), this.getEntity( context ).toString() );

			return 1;
		}

		private int withRandomPosition( CommandContext< CommandSourceStack > context, Vec3 position ) {
			MajruszLibrary.log( "test %s", position.toString() );

			return 1;
		}

		public enum Enumeration {
			ABC, DEF, GHI
		}
	}
}
