package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnPreDamagedData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnPreDamagedContext extends ContextBase< OnPreDamagedData > {
	static final List< OnPreDamagedContext > CONTEXTS = new ArrayList<>();

	public OnPreDamagedContext( Consumer< OnPreDamagedData > consumer, ContextParameters params ) {
		super( OnPreDamagedData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnPreDamagedContext( Consumer< OnPreDamagedData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onPreDamaged( LivingAttackEvent event ) {
		ContextBase.accept( CONTEXTS, new OnPreDamagedData( event ) );
	}
}
