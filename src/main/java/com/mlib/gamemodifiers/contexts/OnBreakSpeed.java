package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnBreakSpeed {
	public static ContextBase< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onBreakSpeed( PlayerEvent.BreakSpeed event ) {
		Data data = Contexts.get( Data.class ).dispatch( new Data( event ) );
		event.setNewSpeed( data.newSpeed );
	}

	public static class Data implements ILevelData {
		public final PlayerEvent.BreakSpeed event;
		public final Player player;
		public final float originalSpeed;
		public float newSpeed;

		public Data( PlayerEvent.BreakSpeed event ) {
			this.event = event;
			this.player = event.getEntity();
			this.originalSpeed = event.getOriginalSpeed();
			this.newSpeed = event.getNewSpeed();
		}

		@Override
		public Level getLevel() {
			return this.player.getLevel();
		}
	}
}