package com.mlib.data;

import com.google.gson.*;
import com.mlib.MajruszLibrary;
import com.mlib.registry.Registries;
import net.minecraft.world.level.storage.loot.Deserializers;

import java.io.*;
import java.util.function.Supplier;

public class Config extends SerializableStructure {
	private final File file;
	private final Gson gson;

	public static Config create( String name ) {
		return new Config( name );
	}

	public void reload() {
		this.load();
		this.save();
	}

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
