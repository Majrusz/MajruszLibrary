package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnEquipmentChangedData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnEquipmentChangedContext extends ContextBase< OnEquipmentChangedData > {
	static final List< OnEquipmentChangedContext > CONTEXTS = Collections.synchronizedList( new ArrayList<>() );

	public OnEquipmentChangedContext( Consumer< OnEquipmentChangedData > consumer, ContextParameters params ) {
		super( OnEquipmentChangedData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnEquipmentChangedContext( Consumer< OnEquipmentChangedData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onEquipmentChanged( LivingEquipmentChangeEvent event ) {
		ContextBase.accept( CONTEXTS, new OnEquipmentChangedData( event ) );
	}
}
