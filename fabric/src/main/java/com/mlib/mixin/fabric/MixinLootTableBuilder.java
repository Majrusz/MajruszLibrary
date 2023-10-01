package com.mlib.mixin.fabric;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import com.mlib.mixininterfaces.fabric.IMixinLootTable;
import com.mlib.mixininterfaces.fabric.IMixinLootTableBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( LootTable.Builder.class )
public abstract class MixinLootTableBuilder implements IMixinLootTableBuilder {
	ResourceLocation mlibId = null;

	@Override
	public void set( ResourceLocation id ) {
		this.mlibId = id;
	}

	@Inject(
		at = @At( "RETURN" ),
		method = "build ()Lnet/minecraft/world/level/storage/loot/LootTable;"
	)
	public void build( CallbackInfoReturnable< LootTable > callbackInfo ) {
		( ( IMixinLootTable )callbackInfo.getReturnValue() ).set( this.mlibId );
	}
}
