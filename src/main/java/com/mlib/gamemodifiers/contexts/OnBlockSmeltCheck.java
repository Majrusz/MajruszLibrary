package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

public class OnBlockSmeltCheck {
	public static final Consumer< Data > ENABLE_SMELT = data->data.shouldSmelt = true;

	@Mod.EventBusSubscriber
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
		public boolean shouldSmelt;
		public final ItemStack tool;
		public final BlockState blockState;
		public final Player player;

		public Data( OnLoot.Data data ) {
			super( data.entity );

			this.shouldSmelt = false;
			this.tool = data.tool;
			this.blockState = data.blockState;
			this.player = ( Player )data.entity;
		}
	}
}
