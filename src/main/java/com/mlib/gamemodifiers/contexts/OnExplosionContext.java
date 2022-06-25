package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.gamemodifiers.Context;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class OnExplosionContext extends Context {
	static final List< OnExplosionContext > CONTEXTS = new ArrayList<>();

	public OnExplosionContext( String configName, String configComment ) {
		super( configName, configComment );
		CONTEXTS.add( this );
	}

	public OnExplosionContext() {
		this( "OnExplosion", "" );
	}

	@SubscribeEvent
	public static void onExplosion( ExplosionEvent.Detonate event ) {
		Data data = new Data( event );
		for( OnExplosionContext context : CONTEXTS ) {
			if( context.check( data ) ) {
				context.gameModifier.execute( data );
			}
		}
	}

	public static class Data extends Context.Data {
		public final ExplosionEvent.Detonate event;
		public final Explosion explosion;
		@Nullable
		public final LivingEntity sourceMob;
		@Nullable
		public final ServerLevel level;

		public Data( ExplosionEvent.Detonate event ) {
			super( event.getExplosion().getSourceMob() );
			this.event = event;
			this.explosion = event.getExplosion();
			this.sourceMob = this.explosion.getSourceMob();
			this.level = Utility.castIfPossible( ServerLevel.class, event.getWorld() );
		}
	}
}
