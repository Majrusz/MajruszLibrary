package com.mlib.features;

import com.mlib.Random;
import com.mlib.annotations.AutoInstance;
import com.mlib.entities.EntityHelper;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnExtraFishingLootCheck;
import com.mlib.gamemodifiers.contexts.OnItemFished;
import com.mlib.gamemodifiers.parameters.Priority;
import com.mlib.math.VectorHelper;
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
public class FishingLootIncreaser extends GameModifier {
	public FishingLootIncreaser() {
		new OnItemFished.Context( this::increaseLoot )
			.priority( Priority.HIGHEST )
			.addCondition( new Condition.IsServer<>() )
			.insertTo( this );
	}

	private void increaseLoot( OnItemFished.Data data ) {
		OnExtraFishingLootCheck.Data extraData = this.dispatchContext( data );
		if( extraData.isExtraLootEmpty() )
			return;

		this.spawnLoot( data, extraData.extraLoot );
		this.spawnExperience( data, extraData.extraExperience );
		this.damageFishingRod( data, extraData.extraRodDamage );
		this.sendMessage( data, extraData.extraLoot );
	}

	private OnExtraFishingLootCheck.Data dispatchContext( OnItemFished.Data data ) {
		return OnExtraFishingLootCheck.Context.accept( new OnExtraFishingLootCheck.Data( data ) );
	}

	private void spawnLoot( OnItemFished.Data data, List< ItemStack > extraLoot ) {
		extraLoot.forEach( itemStack->{
			Vec3 offset = Random.getRandomVector3d( -0.25, 0.25, 0.125, 0.5, -0.25, 0.25 );
			Vec3 spawnPosition = data.hook.position().add( offset );
			ItemEntity itemEntity = new ItemEntity( data.level, spawnPosition.x, spawnPosition.y, spawnPosition.z, itemStack );
			Vec3 motion = data.player.position().subtract( itemEntity.position() ).multiply( 0.1, 0.1, 0.1 );
			itemEntity.setDeltaMovement( motion.add( 0.0, Math.pow( VectorHelper.length( motion ), 0.5 ) * 0.25, 0.0 ) );
			data.level.addFreshEntity( itemEntity );
		} );
	}

	private void spawnExperience( OnItemFished.Data data, int experience ) {
		if( experience > 0 ) {
			EntityHelper.spawnExperience( data.level, VectorHelper.add( data.player.position(), 0.5 ), experience );
		}
	}

	private void damageFishingRod( OnItemFished.Data data, int damage ) {
		data.event.damageRodBy( data.event.getRodDamage() + damage );
	}

	private void sendMessage( OnItemFished.Data data, List< ItemStack > extraLoot ) {
		List< FishedItem > fishedItems = this.buildFishedItemList( data, extraLoot );
		MutableComponent message = this.buildMessage( fishedItems );

		data.player.displayClientMessage( message, true );
	}

	private List< FishedItem > buildFishedItemList( OnItemFished.Data data, List< ItemStack > extraLoot ) {
		List< FishedItem > fishedItems = new ArrayList<>();
		BiConsumer< ItemStack, Boolean > addItem = ( ItemStack itemStack, Boolean isExtra )->{
			for( FishedItem fishedItem : fishedItems ) {
				if( fishedItem.is( itemStack ) ) {
					fishedItem.increase( itemStack, isExtra );
					return;
				}
			}

			fishedItems.add( new FishedItem( itemStack, isExtra ) );
		};

		data.drops.forEach( itemStack->addItem.accept( itemStack, false ) );
		extraLoot.forEach( itemStack->addItem.accept( itemStack, true ) );

		return fishedItems;
	}

	private MutableComponent buildMessage( List< FishedItem > fishedItems ) {
		MutableComponent message = Component.literal( "(" ).withStyle( ChatFormatting.WHITE );
		for( int idx = 0; idx < fishedItems.size(); ++idx ) {
			if( idx > 0 ) {
				message.append( ", " );
			}
			message.append( fishedItems.get( idx ).build() );
		}

		return message.append( Component.literal( ")" ) );
	}

	private static class FishedItem {
		public final ItemStack itemStack;
		public final ChatFormatting itemFormatting;
		public int count;
		public ChatFormatting countFormatting;

		public FishedItem( ItemStack itemStack, boolean isExtra ) {
			ChatFormatting formatting = isExtra ? ChatFormatting.GOLD : ChatFormatting.WHITE;
			this.itemStack = itemStack;
			this.itemFormatting = formatting;
			this.count = itemStack.getCount();
			this.countFormatting = formatting;
		}

		public MutableComponent build() {
			return this.itemStack.getItem()
				.getName( this.itemStack )
				.copy()
				.withStyle( this.itemFormatting )
				.append( Component.literal( String.format( " x%d", this.count ) ).withStyle( this.countFormatting ) );
		}

		public void increase( ItemStack itemStack, boolean isExtra ) {
			this.count += itemStack.getCount();
			if( isExtra ) {
				this.countFormatting = ChatFormatting.GOLD;
			}
		}

		public boolean is( ItemStack itemStack ) {
			return this.itemStack.getItem().equals( itemStack.getItem() );
		}
	}
}
