package com.mlib.data;

import com.google.gson.*;
import com.mlib.MajruszLibrary;
import com.mlib.contexts.OnPlayerLoggedIn;
import com.mlib.contexts.OnResourcesReloaded;
import com.mlib.modhelper.ModHelper;
import com.mlib.network.NetworkObject;
import com.mlib.platform.Platform;
import com.mlib.registry.Registries;
import net.minecraft.world.level.storage.loot.Deserializers;

import java.io.*;
import java.util.List;
import java.util.function.Supplier;

public class Config extends SerializableStructure {
	private final File file;
	private final Gson gson;

	public void save() {
		try {
			if( this.file.exists() || this.file.createNewFile() ) {
				FileOutputStream stream = new FileOutputStream( this.file );
				stream.write( this.gson.toJson( this ).getBytes() );
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
				this.gson.fromJson( reader, Config.class );
			}
		} catch( Exception exception ) {
			MajruszLibrary.HELPER.logError( exception.toString() );
		}
	}

	private Config( String name ) {
		this.file = Registries.getConfigPath().resolve( "%s.json".formatted( name ) ).toFile();
		this.gson = Deserializers.createFunctionSerializer()
			.registerTypeAdapter( Config.class, new TypeAdapter<>( ()->this ) )
			.setPrettyPrinting()
			.create();
	}

	public static class Builder {
		private final ModHelper helper;
		private String name;
		private boolean isAutoSyncEnabled;

		public Builder( ModHelper helper ) {
			this.helper = helper;
		}

		public Builder named( String name ) {
			this.name = name;

			return this;
		}

		public Builder autoSync() {
			this.isAutoSyncEnabled = true;

			return this;
		}

		public Config create() {
			String name = this.name != null ? this.name : this.helper.getModId();
			Config config = new Config( name );
			NetworkObject< Config > network = this.isAutoSyncEnabled ? this.helper.create( name.replace( "-", "_" ), Config.class, ()->config ) : null;

			this.helper.onRegister( ()->{
				config.load();
				config.save();
			} );

			OnResourcesReloaded.listen( data->{
				if( Platform.isAuthority() ) {
					config.load();
					config.save();
				}
				if( Platform.isDedicatedServer() && network != null ) {
					network.sendToClients( config );
				}
			} );

			OnPlayerLoggedIn.listen( data->{
				if( Platform.isAuthority() ) {
					config.load();
					config.save();
				}
				if( Platform.isDedicatedServer() && network != null ) {
					network.sendToClients( List.of( data.player ), config );
				}
			} );

			return config;
		}
	}

	private static class TypeAdapter< Type extends ISerializable > implements JsonDeserializer< Type >, JsonSerializer< Type > {
		final Supplier< Type > instance;

		public TypeAdapter( Supplier< Type > instance ) {
			this.instance = instance;
		}

		@Override
		public Type deserialize( JsonElement element, java.lang.reflect.Type type, JsonDeserializationContext context ) throws JsonParseException {
			return SerializableHelper.read( this.instance, element );
		}

		@Override
		public JsonElement serialize( Type value, java.lang.reflect.Type type, JsonSerializationContext context ) {
			return SerializableHelper.write( ()->value, new JsonObject() );
		}
	}
}
