package com.majruszlibrary.data;

import com.majruszlibrary.events.OnLevelsLoaded;
import com.majruszlibrary.events.OnLevelsSaved;
import com.majruszlibrary.events.OnPlayerLoggedIn;
import com.majruszlibrary.events.base.Priority;
import com.majruszlibrary.modhelper.ModHelper;
import com.majruszlibrary.network.NetworkObject;
import com.majruszlibrary.platform.Side;
import net.minecraft.nbt.CompoundTag;

public class WorldData {
	private final SavedData savedData;
	private final NetworkObject< ? > networkObject;
	private final String name;
	private final Runnable setupDefaultValues;

	public WorldData setDirty() {
		this.savedData.setDirty();
		if( Side.isDedicatedServer() ) {
			this.networkObject.sendToClients();
		}

		return this;
	}

	private WorldData( ModHelper helper, Class< ? > serverClass, Class< ? > clientClass, Runnable setupDefaultValues ) {
		this.savedData = new SavedData( serverClass );
		this.networkObject = helper.create( "world_data", clientClass );
		this.setupDefaultValues = setupDefaultValues;
		this.name = helper.getModId();

		OnLevelsLoaded.listen( this::setup )
			.priority( Priority.LOW );

		OnLevelsSaved.listen( this::save )
			.priority( Priority.LOW );

		OnPlayerLoggedIn.listen( this::sendToClient )
			.addCondition( data->Side.isDedicatedServer() );
	}

	private void setup( OnLevelsLoaded data ) {
		this.setupDefaultValues.run();

		data.server.overworld()
			.getDataStorage()
			.computeIfAbsent( this.savedData::load, ()->this.savedData, this.name );
	}

	private void save( OnLevelsSaved data ) {
		this.savedData.setDirty();
	}

	private void sendToClient( OnPlayerLoggedIn data ) {
		this.networkObject.sendToClient( data.player );
	}

	public static class Builder {
		private final ModHelper helper;
		private final Class< ? > serverClass;
		private Class< ? > clientClass = null;
		private Runnable setupDefaultValues = ()->{};

		public Builder( ModHelper helper, Class< ? > serverClass ) {
			this.helper = helper;
			this.serverClass = serverClass;
		}

		public Builder client( Class< ? > clientClass ) {
			this.clientClass = clientClass;

			return this;
		}

		public Builder setupDefaultValues( Runnable setupDefaultValues ) {
			this.setupDefaultValues = setupDefaultValues;

			return this;
		}

		public WorldData create() {
			return new WorldData( this.helper, this.serverClass, this.clientClass != null ? this.clientClass : this.serverClass, this.setupDefaultValues );
		}
	}

	private static class SavedData extends net.minecraft.world.level.saveddata.SavedData {
		final Class< ? > clazz;

		public SavedData( Class< ? > clazz ) {
			this.clazz = clazz;
		}

		@Override
		public CompoundTag save( CompoundTag tag ) {
			return Serializables.write( this.clazz, tag );
		}

		public SavedData load( CompoundTag tag ) {
			Serializables.read( this.clazz, tag );

			return this;
		}
	}
}
