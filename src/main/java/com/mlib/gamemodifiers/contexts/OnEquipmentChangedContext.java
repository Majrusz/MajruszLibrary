package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnEquipmentChangedData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnEquipmentChangedContext extends Context< OnEquipmentChangedData > {
	static final List< OnEquipmentChangedContext > CONTEXTS = new ArrayList<>();

	public OnEquipmentChangedContext( Consumer< OnEquipmentChangedData > consumer, ContextParameters params ) {
		super( OnEquipmentChangedData.class, consumer, params );
		Context.addSorted( CONTEXTS, this );
	}

	public OnEquipmentChangedContext( Consumer< OnEquipmentChangedData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onEquipmentChanged( LivingEquipmentChangeEvent event ) {
		Context.accept( CONTEXTS, new OnEquipmentChangedData( event ) );
	}
}
