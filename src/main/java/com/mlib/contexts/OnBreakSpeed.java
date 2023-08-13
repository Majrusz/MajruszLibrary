package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnBreakSpeed {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onBreakSpeed( PlayerEvent.BreakSpeed event ) {
		Data data = Contexts.get( Data.class ).dispatch( new Data( event ) );
		event.setNewSpeed( data.newSpeed );
	}

	public static class Data implements IEntityData {
		public final PlayerEvent.BreakSpeed event;
		public final Player player;
		public final float originalSpeed;
		public float newSpeed;

		public Data( PlayerEvent.BreakSpeed event ) {
			this.event = event;
			this.player = event.getPlayer();
			this.originalSpeed = event.getOriginalSpeed();
			this.newSpeed = event.getNewSpeed();
		}

		@Override
		public Entity getEntity() {
			return this.player;
		}
	}
}