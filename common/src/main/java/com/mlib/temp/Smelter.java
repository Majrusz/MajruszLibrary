package com.mlib.temp;

import com.mlib.MajruszLibrary;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnLootGenerated;
import com.mlib.contexts.base.Condition;
import com.mlib.emitter.ParticleEmitter;
import com.mlib.emitter.SoundEmitter;
import com.mlib.item.ItemHelper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

@AutoInstance
public class Smelter {
	int dmg = 1;

	public Smelter() {
		OnLootGenerated.listen( this::smelt )
			.addCondition( Condition.hasAuthority() )
			.addCondition( Condition.hasLevel() )
			.addCondition( data->data.blockState != null )
			.addCondition( data->data.origin != null )
			.addCondition( data->data.tool != null )
			.addCondition( data->data.entity instanceof Player player && !player.isCrouching() );

		MajruszLibrary.CONFIG.extend( DamageIncreaser.Test.class, subconfig->{
			subconfig.defineInteger( "dmg", ()->this.dmg, x->this.dmg = x );
		} );
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
