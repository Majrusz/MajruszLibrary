package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.EnderManAngerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnEnderManAnger {
	public static ContextBase< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onAnger( EnderManAngerEvent event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data implements ILevelData {
		public final EnderManAngerEvent event;
		public final EnderMan enderMan;
		public final Player player;

		public Data( EnderManAngerEvent event ) {
			this.event = event;
			this.enderMan = event.getEntity();
			this.player = event.getPlayer();
		}

		@Override
		public Level getLevel() {
			return this.enderMan.getLevel();
		}
	}
}
