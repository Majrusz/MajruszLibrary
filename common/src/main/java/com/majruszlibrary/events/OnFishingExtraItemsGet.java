package com.majruszlibrary.events;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.entity.EntityHelper;
import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.base.Priority;
import com.majruszlibrary.events.type.IEntityEvent;
import com.majruszlibrary.math.AnyPos;
import com.majruszlibrary.math.Random;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OnFishingExtraItemsGet implements IEntityEvent {
	public final Player player;
	public final FishingHook hook;
	public final ItemStack fishingRod;
	public final List< ItemStack > items;
	public final List< ItemStack > extraItems = new ArrayList<>();
	public int extraExperience = 0;

	public static Event< OnFishingExtraItemsGet > listen( Consumer< OnFishingExtraItemsGet > consumer ) {
		return Events.get( OnFishingExtraItemsGet.class ).add( consumer );
	}

	public OnFishingExtraItemsGet( OnItemFished data ) {
		this.player = data.player;
		this.hook = data.hook;
		this.fishingRod = data.fishingRod;
		this.items = data.items;
	}

	@Override
	public Entity getEntity() {
		return this.player;
	}

	@AutoInstance
	public static class Increaser {
		public Increaser() {
			OnItemFished.listen( this::increaseLoot )
				.priority( Priority.HIGHEST )
				.addCondition( Condition.isLogicalServer() );
		}

		private void increaseLoot( OnItemFished data ) {
			OnFishingExtraItemsGet subdata = Events.dispatch( new OnFishingExtraItemsGet( data ) );
			if( !subdata.extraItems.isEmpty() ) {
				this.spawnLoot( subdata );
				this.spawnExperience( subdata );
			}
		}

		private void spawnLoot( OnFishingExtraItemsGet data ) {
			data.extraItems.forEach( itemStack->{
				Vec3 spawnPosition = AnyPos.from( data.hook.position() ).add( Random.nextVector( -0.25, 0.25, 0.125, 0.5, -0.25, 0.25 ) ).vec3();
				ItemEntity itemEntity = new ItemEntity( data.getLevel(), spawnPosition.x, spawnPosition.y, spawnPosition.z, itemStack );
				Vec3 motion = data.player.position().subtract( itemEntity.position() ).multiply( 0.1, 0.1, 0.1 );
				itemEntity.setDeltaMovement( motion.add( 0.0, Math.pow( AnyPos.from( motion ).len().doubleValue(), 0.5 ) * 0.25, 0.0 ) );
				data.getLevel().addFreshEntity( itemEntity );
			} );
		}

		private void spawnExperience( OnFishingExtraItemsGet data ) {
			if( data.extraExperience > 0 ) {
				EntityHelper.spawnExperience( data.getLevel(), AnyPos.from( data.player.position() ).add( 0.5 ).vec3(), data.extraExperience );
			}
		}
	}
}
