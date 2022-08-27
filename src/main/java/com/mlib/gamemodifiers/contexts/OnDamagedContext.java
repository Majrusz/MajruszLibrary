package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnDamagedData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mod.EventBusSubscriber
public class OnDamagedContext extends ContextBase< OnDamagedData > {
	public static final Predicate< OnDamagedData > DIRECT_DAMAGE = data->data.source.getDirectEntity() == data.attacker;
	static final List< OnDamagedContext > CONTEXTS = Collections.synchronizedList( new ArrayList<>() );

	public OnDamagedContext( Consumer< OnDamagedData > consumer, ContextParameters params ) {
		super( OnDamagedData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnDamagedContext( Consumer< OnDamagedData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onDamaged( LivingHurtEvent event ) {
		ContextBase.accept( CONTEXTS, new OnDamagedData( event ) );
	}
}
