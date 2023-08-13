package com.mlib.data;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.mlib.modhelper.ModHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class JsonListener {
	final static List< ISerializable > STRUCTURES = Collections.synchronizedList( new ArrayList<>() );

	public static < Type extends ISerializable > Holder< Type > add( String data, ResourceLocation id, Class< Type > clazz, Supplier< Type > newInstance ) {
		Holder< Type > holder = new Holder<>( STRUCTURES.size() );
		STRUCTURES.add( newInstance.get() );
		var gson = Deserializers.createFunctionSerializer()
			.registerTypeAdapter( clazz, ( JsonDeserializer< Type > )( element, type, context )->SerializableHelper.read( newInstance, element ) )
			.create();
		var listener = new SimpleJsonResourceReloadListener( gson, data ) {
			@Override
			protected void apply( Map< ResourceLocation, JsonElement > elements, ResourceManager manager, ProfilerFiller filler ) {
				STRUCTURES.set( holder.idx, gson.fromJson( elements.get( id ), clazz ) );
			}
		};
		MinecraftForge.EVENT_BUS.addListener( ( AddReloadListenerEvent event )->event.addListener( listener ) );

		return holder;
	}

	public static class Holder< Type extends ISerializable > implements Supplier< Type > {
		final int idx;

		Holder( int idx ) {
			this.idx = idx;
		}

		public Holder< Type > syncWithClients( ModHelper helper ) {
			MinecraftForge.EVENT_BUS.addListener( ( OnDatapackSyncEvent event )->{
				if( event.getPlayer() != null ) {
					helper.sendMessage( PacketDistributor.PLAYER.with( event::getPlayer ), this.get() );
				}
			} );

			return this;
		}

		public void onSync( Type instance ) {
			STRUCTURES.set( this.idx, instance );
		}

		@Override
		public Type get() {
			return ( Type )STRUCTURES.get( this.idx );
		}

		public boolean isPresent() {
			return this.get() != null;
		}
	}
}
