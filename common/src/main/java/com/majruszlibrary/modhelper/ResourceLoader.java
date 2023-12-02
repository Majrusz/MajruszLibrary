package com.majruszlibrary.modhelper;

import com.google.gson.*;
import com.majruszlibrary.animations.AnimationsDef;
import com.majruszlibrary.animations.ModelDef;
import com.majruszlibrary.annotation.Dist;
import com.majruszlibrary.annotation.OnlyIn;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.data.Serializables;
import com.majruszlibrary.events.OnPlayerLoggedIn;
import com.majruszlibrary.events.OnResourcesReloaded;
import com.majruszlibrary.network.NetworkObject;
import com.majruszlibrary.platform.Services;
import com.majruszlibrary.platform.Side;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.storage.loot.Deserializers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class ResourceLoader {
	static final Map< String, ResourceLoader > LOADERS = Collections.synchronizedMap( new HashMap<>() );
	static final IResourcePlatform PLATFORM = Services.load( IResourcePlatform.class );
	final ModHelper helper;
	final Gson gson;
	final NetworkObject< Message > network;
	final String name = "custom";
	Server server;
	@OnlyIn( Dist.CLIENT )
	Client client;

	static {
		Serializables.get( Message.class )
			.define( "mod_id", Reader.string(), s->s.modId, ( s, v )->s.modId = v )
			.define( "animations", Reader.map( Reader.custom( AnimationsDef::new ) ), s->s.animations, ( s, v )->s.animations = v );
	}

	public ResourceLoader( ModHelper helper ) {
		this.helper = helper;
		this.gson = Deserializers.createFunctionSerializer()
			.registerTypeAdapter( AnimationsDef.class, new AnimationAdapter() )
			.registerTypeAdapter( ModelDef.class, new ModelAdapter() )
			.create();
		this.network = this.helper.create( "custom_data", Message.class );
		this.server = new Server( this );
		PLATFORM.register( this.helper.getLocation( this.name ), this.server );

		OnResourcesReloaded.listen( data->{
			if( Side.isDedicatedServer() && this.network != null ) {
				this.network.sendToClients( this.getMessage() );
			}
		} );

		OnPlayerLoggedIn.listen( data->{
			if( Side.isDedicatedServer() && this.network != null ) {
				this.network.sendToClient( data.player, this.getMessage() );
			}
		} );

		Side.runOnClient( ()->()->{
			this.client = new Client( this );
			this.network.addClientCallback( ResourceLoader::onClient );
		} );

		LOADERS.put( this.helper.getModId(), this );
	}

	public < Type > Resource< Type > load( ResourceLocation id, Class< Type > clazz ) {
		Resource< Type > resource = new Resource<>( clazz );
		this.server.resources.put( id.toString(), resource );

		return resource;
	}

	@OnlyIn( Dist.CLIENT )
	public < Type > Resource< Type > loadClient( ResourceLocation id, Class< Type > clazz ) {
		Resource< Type > resource = new Resource.Lazy<>( clazz, ()->this.client.loadResources( Side.getMinecraft().getResourceManager() ) );
		this.client.resources.put( id.toString(), resource );

		return resource;
	}

	private Message getMessage() {
		Message message = new Message();
		message.modId = this.helper.getModId();
		for( String id : this.server.resources.keySet() ) {
			Resource< ? > resource = this.server.resources.get( id );
			if( resource.value instanceof AnimationsDef animationsDef ) {
				message.animations.put( id, animationsDef );
			}
		}

		return message;
	}

	@OnlyIn( Dist.CLIENT )
	private static void onClient( Message message ) {
		Server server = LOADERS.get( message.modId ).server;
		for( Map.Entry< String, AnimationsDef > entry : message.animations.entrySet() ) {
			( ( Resource< AnimationsDef > )server.resources.get( entry.getKey() ) ).value = entry.getValue();
		}
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

	static class Server extends SimpleJsonResourceReloadListener {
		final ResourceLoader loader;
		final Map< String, Resource< ? > > resources = new HashMap<>();

		public Server( ResourceLoader loader ) {
			super( loader.gson, loader.name );

			this.loader = loader;
		}

		@Override
		protected void apply( Map< ResourceLocation, JsonElement > jsons, ResourceManager resourceManager, ProfilerFiller profilerFiller ) {
			for( String id : this.resources.keySet() ) {
				this.resources.get( id ).load( this.loader.gson, jsons.get( new ResourceLocation( id ) ) );
			}
		}
	}

	static class Message {
		String modId;
		Map< String, AnimationsDef > animations = new HashMap<>();

		public Message() {}
	}

	@OnlyIn( Dist.CLIENT )
	static class Client {
		final ResourceLoader loader;
		final Map< String, Resource< ? > > resources = new HashMap<>();

		public Client( ResourceLoader loader ) {
			this.loader = loader;
		}

		public void loadResources( ResourceManager resourceManager ) {
			var resources = resourceManager.listResources( this.loader.name, file->file.toString().endsWith( ".json" ) );
			for( ResourceLocation id : resources.keySet() ) {
				try {
					this.resources.get( id.toString() ).load( this.loader.gson, resources.get( id ).openAsReader() );
				} catch( Exception exception ) {
					this.loader.helper.logError( exception.toString() );
				}
			}
		}
	}
}
