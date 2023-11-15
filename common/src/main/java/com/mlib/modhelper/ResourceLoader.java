package com.mlib.modhelper;

import com.google.gson.*;
import com.mlib.animations.AnimationsDef;
import com.mlib.animations.ModelDef;
import com.mlib.data.Serializables;
import com.mlib.platform.Side;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.storage.loot.Deserializers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ResourceLoader {
	final ModHelper helper;
	final Gson gson;
	List< Cache > caches = List.of( new Cache(), new Cache() );

	public ResourceLoader( ModHelper helper ) {
		this.helper = helper;
		this.gson = Deserializers.createFunctionSerializer()
			.registerTypeAdapter( AnimationsDef.class, new AnimationAdapter() )
			.registerTypeAdapter( ModelDef.class, new ModelAdapter() )
			.create();
	}

	public < Type > Type load( ResourceLocation id, Class< Type > clazz, PackType packType ) {
		Cache cache = this.caches.get( packType.ordinal() );
		if( cache.manager == null ) {
			if( packType.equals( PackType.CLIENT_RESOURCES ) ) {
				Side.runOnClient( ()->()->cache.manager = Side.getMinecraft().getResourceManager() );
			} else {
				cache.manager = Side.getServer().getResourceManager();
			}
			cache.resources = cache.manager.listResources( "custom", file->file.toString().endsWith( ".json" ) );
		}

		try {
			return this.gson.fromJson( cache.resources.get( id ).openAsReader(), clazz );
		} catch( Exception exception ) {
			this.helper.logError( exception.toString() );
		}

		return null;
	}

	private static class Cache {
		ResourceManager manager = null;
		Map< ResourceLocation, Resource > resources = new HashMap<>();
	}

	private static class AnimationAdapter implements JsonDeserializer< AnimationsDef > {
		@Override
		public AnimationsDef deserialize( JsonElement json, java.lang.reflect.Type type, JsonDeserializationContext context ) throws JsonParseException {
			return Serializables.read( new AnimationsDef(), json );
		}
	}

	private static class ModelAdapter implements JsonDeserializer< ModelDef > {
		@Override
		public ModelDef deserialize( JsonElement json, java.lang.reflect.Type type, JsonDeserializationContext context ) throws JsonParseException {
			return Serializables.read( new ModelDef(), json );
		}
	}
}
