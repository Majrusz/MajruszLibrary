package com.mlib.temp;

import com.mlib.annotations.AutoInstance;
import com.mlib.contexts.OnLootGenerated;
import com.mlib.contexts.base.Condition;
import com.mlib.effects.ParticleEmitter;
import com.mlib.effects.SoundEmitter;
import com.mlib.items.ItemHelper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

@AutoInstance
public class Smelter {
	public Smelter() {
		OnLootGenerated.listen( this::smelt )
			.addCondition( Condition.predicate( OnLootGenerated::hasBlockState ) )
			.addCondition( Condition.predicate( OnLootGenerated::hasOrigin ) )
			.addCondition( Condition.predicate( OnLootGenerated::hasTool ) )
			.addCondition( Condition.hasAuthority() )
			.addCondition( Condition.hasLevel() )
			.addCondition( Condition.predicate( data->data.entity instanceof Player player && !player.isCrouching() ) );
	}

	private void smelt( OnLootGenerated data ) {
		ObjectArrayList< ItemStack > smeltedLoot = new ObjectArrayList<>();
		float experience = 0.0f;
		for( ItemStack itemStack : data.generatedLoot ) {
			Optional< ItemHelper.SmeltResult > result = ItemHelper.tryToSmelt( data.getLevel(), itemStack );
			if( result.isPresent() ) {
				smeltedLoot.add( result.get().itemStack() );
				experience += result.get().experience();
			} else {
				smeltedLoot.add( itemStack );
			}
		}
		if( experience > 0.0f ) {
			SoundEmitter.of( SoundEvents.PLAYER_HURT_ON_FIRE )
				.volume( SoundEmitter.randomized( 0.25f ) )
				.pitch( SoundEmitter.randomized( 0.5f ) )
				.emit( data.getServerLevel(), data.origin );
			ParticleEmitter.of( ParticleTypes.FLAME )
				.count( 4 )
				.emit( data.getServerLevel(), data.origin );
		}

		data.generatedLoot.clear();
		data.generatedLoot.addAll( smeltedLoot );
	}
}
