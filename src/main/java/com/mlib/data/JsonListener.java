package com.mlib.data;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class JsonListener {
	final static List< SerializableStructure > STRUCTURES = Collections.synchronizedList( new ArrayList<>() );

	public static < Type extends SerializableStructure > Supplier< Type > add( String data, ResourceLocation location, Class< Type > clazz,
		Supplier< Type > newInstance
	) {
		int idx = STRUCTURES.size();
		STRUCTURES.add( null );
		var gson = Deserializers.createFunctionSerializer()
			.registerTypeAdapter( clazz, ( JsonDeserializer< Type > )( element, type, context )->{
				Type messages = newInstance.get();
				messages.read( element );

				return messages;
			} ).create();
		var listener = new SimpleJsonResourceReloadListener( gson, data ) {
			@Override
			protected void apply( Map< ResourceLocation, JsonElement > elements, ResourceManager manager, ProfilerFiller filler ) {
				STRUCTURES.set( idx, gson.fromJson( elements.get( location ), clazz ) );
			}
		};
		MinecraftForge.EVENT_BUS.addListener( ( AddReloadListenerEvent event )->event.addListener( listener ) );

		return ()->( Type )STRUCTURES.get( idx );
	}
}
