package com.mlib.data;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.mlib.contexts.OnResourceListenersGet;
import com.mlib.network.NetworkObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.storage.loot.Deserializers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class JsonListener {
	final static List< ISerializable > STRUCTURES = Collections.synchronizedList( new ArrayList<>() );

	public static < Type extends ISerializable > Holder< Type > add( String directory, ResourceLocation id, Class< Type > clazz, Supplier< Type > instance ) {
		Holder< Type > holder = new Holder<>();
		STRUCTURES.add( instance.get() );

		Gson gson = Deserializers.createFunctionSerializer()
			.registerTypeAdapter( clazz, ( JsonDeserializer< Type > )( element, type, context )->SerializableHelper.read( instance, element ) )
			.create();

		SimpleJsonResourceReloadListener listener = new SimpleJsonResourceReloadListener( gson, directory ) {
			@Override
			protected void apply( Map< ResourceLocation, JsonElement > elements, ResourceManager manager, ProfilerFiller filler ) {
				holder.set( gson.fromJson( elements.get( id ), clazz ) );
			}
		};

		OnResourceListenersGet.listen( data->data.listeners.add( listener ) );

		return holder;
	}

	public static class Holder< Type extends ISerializable > implements Supplier< Type > {
		final int idx = STRUCTURES.size();

		@Override
		public Type get() {
			return ( Type )STRUCTURES.get( this.idx );
		}

		public Holder< Type > enableClientSync( NetworkObject< Type > network ) {
			OnResourceListenersGet.listen( data->network.sendToClients( this.get() ) );

			return this;
		}

		public void set( Type instance ) {
			STRUCTURES.set( this.idx, instance );
		}

		public boolean isPresent() {
			return this.get() != null;
		}
	}
}
