package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.ICancellableData;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OnPlayerInteracted implements ICancellableData, IEntityData {
	public final Player player;
	public final ItemStack itemStack;
	public final InteractionHand hand;
	public final @Nullable Entity entity;
	public final @Nullable BlockHitResult blockResult;
	private InteractionResult result = null;

	public static Context< OnPlayerInteracted > listen( Consumer< OnPlayerInteracted > consumer ) {
		return Contexts.get( OnPlayerInteracted.class ).add( consumer );
	}

	public OnPlayerInteracted( Player player, InteractionHand hand ) {
		this( player, hand, null, null );
	}

	public OnPlayerInteracted( Player player, InteractionHand hand, Entity entity ) {
		this( player, hand, entity, null );
	}

	public OnPlayerInteracted( Player player, InteractionHand hand, BlockHitResult blockResult ) {
		this( player, hand, null, blockResult );
	}

	@Override
	public boolean isExecutionStopped() {
		return this.hasResult();
	}

	@Override
	public Entity getEntity() {
		return this.player;
	}

	public void setResult( InteractionResult result ) {
		this.result = result;
	}

	public void finish() {
		this.setResult( InteractionResult.sidedSuccess( this.getLevel().isClientSide ) );
	}

	public boolean hasResult() {
		return this.result != null;
	}

	public InteractionResult getResult() {
		return this.result;
	}

	private OnPlayerInteracted( Player player, InteractionHand hand, @Nullable Entity entity, @Nullable BlockHitResult blockResult ) {
		this.player = player;
		this.itemStack = player.getItemInHand( hand );
		this.hand = hand;
		this.entity = entity;
		this.blockResult = blockResult;
	}
}
