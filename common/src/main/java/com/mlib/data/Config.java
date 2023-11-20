package com.mlib.data;

import com.google.gson.*;
import com.mlib.MajruszLibrary;
import com.mlib.contexts.OnPlayerLoggedIn;
import com.mlib.contexts.OnResourcesReloaded;
import com.mlib.modhelper.ModHelper;
import com.mlib.network.NetworkObject;
import com.mlib.platform.Side;
import com.mlib.registry.Registries;
import net.minecraft.world.level.storage.loot.Deserializers;

import java.io.Reader;
import java.io.*;
import java.util.List;
import java.util.function.Supplier;

public final class Config {
	private final File file;
	private final Gson gson;
	private final Class< ? > clazz;
	private final Object instance;

	public void save() {
		try {
			if( this.file.exists() || this.file.createNewFile() ) {
				FileOutputStream stream = new FileOutputStream( this.file );
				stream.write( this.gson.toJson( this.instance ).getBytes() );
				stream.flush();
				stream.close();
			}
		} catch( Exception exception ) {
			MajruszLibrary.HELPER.logError( exception.toString() );
		}
	}

	public void load() {
		try {
			if( this.file.exists() ) {
				Reader reader = new InputStreamReader( new FileInputStream( this.file ) );
				this.gson.fromJson( reader, this.clazz );
			}
		} catch( Exception exception ) {
			MajruszLibrary.HELPER.logError( exception.toString() );
		}
	}

	private Config( String name, Class< ? > clazz, Object instance ) {
		this.file = Registries.getConfigPath().resolve( "%s.json".formatted( name ) ).toFile();
		this.gson = Deserializers.createFunctionSerializer()
			.registerTypeAdapter( clazz, new TypeAdapter<>( ()->instance ) )
			.setPrettyPrinting()
			.create();
		this.clazz = clazz;
		this.instance = instance;
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

			this.helper.onRegister( ()->{
				config.load();
				config.save();
			} );

			OnResourcesReloaded.listen( data->{
				if( Side.isLogicalServer() ) {
					long start = System.currentTimeMillis();
					config.load();
					config.save();
					long end = System.currentTimeMillis();
					MajruszLibrary.HELPER.log( "Reloading %s.json took %dms".formatted( name, end - start ) );
				}
				if( Side.isDedicatedServer() && network != null ) {
					network.sendToClients();
				}
			} );

			OnPlayerLoggedIn.listen( data->{
				if( Side.isLogicalServer() ) {
					config.load();
					config.save();
				}
				if( Side.isDedicatedServer() && network != null ) {
					network.sendToClients( List.of( data.player ) );
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
