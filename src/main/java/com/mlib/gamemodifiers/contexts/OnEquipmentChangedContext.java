package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnEquipmentChangedData;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnEquipmentChangedContext extends Context< OnEquipmentChangedData > {
	static final List< OnEquipmentChangedContext > CONTEXTS = new ArrayList<>();

	public OnEquipmentChangedContext( Consumer< OnEquipmentChangedData > consumer, String configName, String configComment ) {
		super( OnEquipmentChangedData.class, consumer, configName, configComment );
		Context.addSorted( CONTEXTS, this );
	}

	public OnEquipmentChangedContext( Consumer< OnEquipmentChangedData > consumer ) {
		this( consumer, "OnBreakSpeed", "" );
	}

	@SubscribeEvent
	public static void onEquipmentChanged( LivingEquipmentChangeEvent event ) {
		Context.accept( CONTEXTS, new OnEquipmentChangedData( event ) );
	}
}
