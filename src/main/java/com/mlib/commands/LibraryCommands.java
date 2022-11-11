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
			builder3_1.execute( data->this.withRandomPosition( data, data.source.getPosition() ) );

			CommandBuilder builder3_2 = builder3.copy();
			builder3_2.position().execute( data->this.withRandomPosition( data, this.getPosition( data ) ) );

			CommandBuilder builder3_3 = builder3.copy();
			builder3_3.entity().execute( data->this.withRandomPosition( data, this.getEntity( data ).position() ) );

			CommandBuilder builder4 = new CommandBuilder();
			builder4.literal( "mlib" ).hasPermission( 4 ).literal( "mods4" ).anyPosition().execute( this::withAnyPosition );

			CommandBuilder builder5 = new CommandBuilder();
			builder5.literal( "mlib" )
				.hasPermission( 4 )
				.literal( "mods5" )
				.execute( this::withOptional )
				.integer()
				.execute( this::withOptional )
				.position()
				.execute( this::withOptional );

			this.addBuilders( builder1, builder2, builder3_1, builder3_2, builder3_3, builder4, builder5 );
		}

		private int empty( CommandData data ) {
			MajruszLibrary.log( "test" );

			return 1;
		}

		private int withInteger( CommandData data ) {
			MajruszLibrary.log( "test %d", this.getInteger( data ) );

			return 1;
		}

		private int withEnum( CommandData data ) {
			MajruszLibrary.log( "test %d %s", this.getInteger( data ), this.getEnumeration( data, Enumeration.class ).name() );

			return 1;
		}

		private int withPosition( CommandData data ) {
			MajruszLibrary.log( "test %s", this.getPosition( data ).toString() );

			return 1;
		}

		private int withEntity( CommandData data ) {
			MajruszLibrary.log( "test %s %s", this.getPosition( data ).toString(), this.getEntity( data ).toString() );

			return 1;
		}

		private int withRandomPosition( CommandData data, Vec3 position ) {
			MajruszLibrary.log( "test %s", position.toString() );

			return 1;
		}

		private int withAnyPosition( CommandData data ) {
			MajruszLibrary.log( "test %s", this.getAnyPositions( data ).toString() );

			return 1;
		}

		private int withOptional( CommandData data ) {
			MajruszLibrary.log( "test %d %s", this.getOptionalInteger( data ).orElse( -1 ), this.getOptionalPosition( data )
				.orElse( Vec3.ZERO )
				.toString() );

			return 1;
		}

		public enum Enumeration {
			ABC, DEF, GHI
		}
	}
}
