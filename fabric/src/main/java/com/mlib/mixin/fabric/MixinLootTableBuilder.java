package com.mlib.mixin.fabric;

import com.mlib.mixininterfaces.fabric.IMixinLootTable;
import com.mlib.mixininterfaces.fabric.IMixinLootTableBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( LootTable.Builder.class )
public abstract class MixinLootTableBuilder implements IMixinLootTableBuilder {
	ResourceLocation mlib$id = null;

	@Override
	public void mlib$set( ResourceLocation id ) {
		this.mlib$id = id;
	}

	@Inject(
		at = @At( "RETURN" ),
		method = "build ()Lnet/minecraft/world/level/storage/loot/LootTable;"
	)
	private void build( CallbackInfoReturnable< LootTable > callbackInfo ) {
		( ( IMixinLootTable )callbackInfo.getReturnValue() ).mlib$set( this.mlib$id );
	}
}
