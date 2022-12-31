package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class OnFarmlandTillCheck {
	public static final Consumer< Data > INCREASE_AREA = data->++data.area;
	public static final Predicate< Data > IS_NOT_CROUCHING = data->!data.player.isCrouching();

	public static class Context extends ContextBase< Data > {
		static final Contexts< Data, Context > CONTEXTS = new Contexts<>();

		public static Data accept( Data data ) {
			return CONTEXTS.accept( data );
		}

		public Context( Consumer< Data > consumer ) {
			super( consumer );

			CONTEXTS.add( this );
		}
	}

	public static class Data extends ContextData {
		public int area = 0;
		public final ServerLevel level;
		public final Player player;
		public final ItemStack itemStack;

		public Data( OnPlayerInteract.Data data ) {
			super( data.player );

			this.level = data.level;
			this.player = data.player;
			this.itemStack = data.itemStack;
		}
	}
}
