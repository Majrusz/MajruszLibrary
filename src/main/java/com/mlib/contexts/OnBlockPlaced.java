package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.ILevelData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnBlockPlaced {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onPlace( BlockEvent.EntityPlaceEvent event ) {
		Contexts.get( Data.class ).dispatch( new Data( event, List.of( event.getBlockSnapshot() ) ) );
	}

	@SubscribeEvent
	public static void onMultiPlace( BlockEvent.EntityMultiPlaceEvent event ) {
		Contexts.get( Data.class ).dispatch( new Data( event, event.getReplacedBlockSnapshots() ) );
	}

	public static class Data implements ILevelData {
		private final Entity entity;
		private final List< BlockSnapshot > blockSnapshots;
		private final BlockState placedBlock;
		private final BlockState placedAgainst;

		public Data( BlockEvent.EntityPlaceEvent event, List< BlockSnapshot > blockSnapshots ) {
			this.entity = event.getEntity();
			this.blockSnapshots = blockSnapshots;
			this.placedBlock = event.getPlacedBlock();
			this.placedAgainst = event.getPlacedAgainst();
		}

		@Override
		public Level getLevel() {
			return this.entity != null ? this.entity.level : null;
		}

		public List< BlockSnapshot > getBlockSnapshots() {
			return this.blockSnapshots;
		}

		public BlockState getPlacedBlock() {
			return placedBlock;
		}

		public BlockState getPlacedAgainst() {
			return placedAgainst;
		}
	}
}