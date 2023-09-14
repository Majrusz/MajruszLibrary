package com.mlib.features;

import com.mlib.Random;
import com.mlib.modhelper.AutoInstance;
import com.mlib.entities.EntityHelper;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Priority;
import com.mlib.contexts.OnExtraFishingLootCheck;
import com.mlib.contexts.OnItemFished;
import com.mlib.math.AnyPos;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@AutoInstance
public class FishingLootIncreaser {
	public FishingLootIncreaser() {
		OnItemFished.listen( this::increaseLoot )
			.priority( Priority.HIGHEST )
			.addCondition( Condition.isServer() );
	}

	private void increaseLoot( OnItemFished.Data data ) {
		OnExtraFishingLootCheck.Data extraData = OnExtraFishingLootCheck.dispatch( data.event );
		if( extraData.extraLoot.isEmpty() ) {
			return;
		}

		this.spawnLoot( data, extraData.extraLoot );
		this.spawnExperience( data, extraData.extraExperience );
		this.damageFishingRod( data, extraData.extraRodDamage );
	}

	private void spawnLoot( OnItemFished.Data data, List< ItemStack > extraLoot ) {
		extraLoot.forEach( itemStack->{
			Vec3 spawnPosition = AnyPos.from( data.hook.position() ).add( Random.nextVector( -0.25, 0.25, 0.125, 0.5, -0.25, 0.25 ) ).vec3();
			ItemEntity itemEntity = new ItemEntity( data.getLevel(), spawnPosition.x, spawnPosition.y, spawnPosition.z, itemStack );
			Vec3 motion = data.player.position().subtract( itemEntity.position() ).multiply( 0.1, 0.1, 0.1 );
			itemEntity.setDeltaMovement( motion.add( 0.0, Math.pow( AnyPos.from( motion ).len().doubleValue(), 0.5 ) * 0.25, 0.0 ) );
			data.getLevel().addFreshEntity( itemEntity );
		} );
	}

	private void spawnExperience( OnItemFished.Data data, int experience ) {
		if( experience > 0 ) {
			EntityHelper.spawnExperience( data.getLevel(), AnyPos.from( data.player.position() ).add( 0.5 ).vec3(), experience );
		}
	}

	private void damageFishingRod( OnItemFished.Data data, int damage ) {
		data.event.damageRodBy( data.event.getRodDamage() + damage );
	}
}
