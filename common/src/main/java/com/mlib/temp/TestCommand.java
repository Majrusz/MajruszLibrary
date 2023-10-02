package com.mlib.temp;

import com.mlib.MajruszLibrary;
import com.mlib.annotations.AutoInstance;
import com.mlib.commands.Command;
import com.mlib.commands.CommandData;
import com.mlib.contexts.OnChorusFruitEaten;
import com.mlib.contexts.OnEntitySpawned;
import com.mlib.contexts.base.Condition;
import com.mlib.data.SerializableStructure;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;

@AutoInstance
public class TestCommand {
	int spawnValue = 0;
	int chorusValue = 0;

	public TestCommand() {
		Command.create()
			.literal( "mysend" )
			.hasPermission( 4 )
			.integer()
			.execute( this::handleMessage )
			.register();

		Command.create()
			.literal( "myadvancement" )
			.hasPermission( 4 )
			.execute( this::handleAdvancement )
			.register();

		Command.create()
			.literal( "myblock" )
			.hasPermission( 4 )
			.integer()
			.execute( this::handleBlock )
			.register();

		OnEntitySpawned.listen( OnEntitySpawned.Data::cancelSpawn )
			.addCondition( Condition.predicate( data->{
				if( this.spawnValue == 0 ) {
					return false;
				} else if( this.spawnValue == 1 ) {
					return data.entity.getType().equals( EntityType.ZOMBIFIED_PIGLIN );
				} else if( this.spawnValue == 2 ) {
					return data.position.getY() < 50;
				} else {
					return true;
				}
			} ) );

		Command.create()
			.literal( "mychorus" )
			.hasPermission( 4 )
			.integer()
			.execute( this::handleChorus )
			.register();

		OnChorusFruitEaten.listen( OnChorusFruitEaten.Data::cancelTeleport )
			.addCondition( Condition.predicate( data->this.chorusValue != 0 ) );
	}

	private int handleMessage( CommandData data ) throws CommandSyntaxException {
		int value = data.getInteger();
		MajruszLibrary.HELPER.log( "[SENDER] %d", value );
		if( data.getOptionalEntityOrPlayer() instanceof ServerPlayer player ) {
			MajruszLibrary.MESSAGE.sendToClients( new Message( value + 1 ) );
		}

		return 0;
	}

	private int handleAdvancement( CommandData data ) throws CommandSyntaxException {
		if( data.getOptionalEntityOrPlayer() instanceof ServerPlayer player ) {
			MajruszLibrary.HELPER.triggerAchievement( player, "apple" );
		}

		return 0;
	}

	private int handleBlock( CommandData data ) throws CommandSyntaxException {
		this.spawnValue = data.getInteger();

		return 0;
	}

	private int handleChorus( CommandData data ) throws CommandSyntaxException {
		this.chorusValue = data.getInteger();

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
