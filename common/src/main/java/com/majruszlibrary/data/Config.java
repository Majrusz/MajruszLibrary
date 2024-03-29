package com.majruszlibrary.data;

import com.google.gson.*;
import com.majruszlibrary.MajruszLibrary;
import com.majruszlibrary.events.OnGameInitialized;
import com.majruszlibrary.events.OnPlayerLoggedIn;
import com.majruszlibrary.events.OnResourcesReloaded;
import com.majruszlibrary.modhelper.ModHelper;
import com.majruszlibrary.network.NetworkObject;
import com.majruszlibrary.platform.Side;
import com.majruszlibrary.registry.Registries;
import net.minecraft.world.level.storage.loot.Deserializers;

import java.io.Reader;
import java.io.*;
import java.util.function.Supplier;

public final class Config {
	private final String name;
	private final File file;
	private final Gson gson;
	private final Class< ? > clazz;
	private final Object instance;
	boolean isLoaded = false;

	public void save() {
		try {
			if( this.file.exists() || this.file.createNewFile() ) {
				FileOutputStream stream = new FileOutputStream( this.file );
				stream.write( this.gson.toJson( this.instance ).getBytes() );
				stream.flush();
				stream.close();
			}
		} catch( Exception exception ) {
			MajruszLibrary.HELPER.logError( "[%s] %s".formatted( this.name, exception.toString() ) );
		}
	}

	public void load() {
		try {
			if( this.file.exists() ) {
				Reader reader = new InputStreamReader( new FileInputStream( this.file ) );
				this.gson.fromJson( reader, this.clazz );
				this.isLoaded = true;
			}
		} catch( Exception exception ) {
			MajruszLibrary.HELPER.logError( "[%s] %s".formatted( this.name, exception.toString() ) );
		}
	}

	private Config( String name, Class< ? > clazz, Object instance ) {
		this.name = "%s.json".formatted( name );
		this.file = Registries.getConfigPath().resolve( this.name ).toFile();
		this.gson = Deserializers.createFunctionSerializer()
			.registerTypeAdapter( clazz, new TypeAdapter<>( ()->instance ) )
			.setPrettyPrinting()
			.create();
		this.clazz = clazz;
		this.instance = instance;
	}

	private void reload() {
		long start = System.currentTimeMillis();
		this.load();
		this.save();
		long end = System.currentTimeMillis();
		MajruszLibrary.HELPER.log( "[%s] Reloading configuration file took %d milliseconds".formatted( this.name, end - start ) );
	}

	public static class Builder< Type > {
		private final ModHelper helper;
		private final Class< Type > clazz;
		private String name;
		private boolean isAutoSyncEnabled;

		public Builder( ModHelper helper, Class< Type > clazz ) {
			this.helper = helper;
			this.clazz = clazz;
		}

		public Builder< Type > named( String name ) {
			this.name = name;

			return this;
		}

		public Builder< Type > autoSync() {
			this.isAutoSyncEnabled = true;

			return this;
		}

		public void create() {
			String name = this.name != null ? this.name : this.helper.getModId();
			Type instance;
			try {
				instance = this.clazz.getConstructor().newInstance();
			} catch( Exception exception ) {
				instance = null;
			}
			Config config = new Config( name, this.clazz, instance );
			NetworkObject< Type > network = this.isAutoSyncEnabled ? this.helper.create( name.replace( "-", "_" ), this.clazz ) : null;

			OnResourcesReloaded.listen( data->{
				config.reload();
				if( Side.isDedicatedServer() && network != null ) {
					network.sendToClients();
				}
			} );

			OnGameInitialized.listen( data->{
				if( !config.isLoaded ) {
					config.reload();
				}
			} );

			OnPlayerLoggedIn.listen( data->{
				if( Side.isDedicatedServer() && network != null ) {
					network.sendToClient( data.player );
				}
			} );
		}
	}

	private record TypeAdapter< Type >( Supplier< Type > instance ) implements JsonDeserializer< Type >, JsonSerializer< Type > {
		@Override
		public Type deserialize( JsonElement json, java.lang.reflect.Type type, JsonDeserializationContext context ) throws JsonParseException {
			Serializables.read( this.instance.get().getClass(), json );

			return this.instance.get();
		}

		@Override
		public JsonElement serialize( Type value, java.lang.reflect.Type type, JsonSerializationContext context ) {
			return Serializables.write( value, new JsonObject() );
		}
	}
}
