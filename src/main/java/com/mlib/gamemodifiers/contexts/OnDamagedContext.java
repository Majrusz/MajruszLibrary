package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnDamagedData;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnDamagedContext extends Context< OnDamagedData > {
	static final List< OnDamagedContext > CONTEXTS = new ArrayList<>();

	public OnDamagedContext( Consumer< OnDamagedData > consumer, String configName, String configComment ) {
		super( consumer, configName, configComment );
		CONTEXTS.add( this );
	}

	public OnDamagedContext( Consumer< OnDamagedData > consumer ) {
		this( consumer, "OnDamaged", "" );
	}

	@SubscribeEvent public static void onDamaged( LivingHurtEvent event ) {
		handleContexts( new OnDamagedData( event ), CONTEXTS );
	}

	public static class DirectDamage extends Condition.ContextOnDamaged {
		public DirectDamage() {
			super( data->data.source.getDirectEntity() == data.attacker );
		}
	}
}
