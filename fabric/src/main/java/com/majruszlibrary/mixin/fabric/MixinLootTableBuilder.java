package com.majruszlibrary.mixin.fabric;

import com.majruszlibrary.mixininterfaces.fabric.IMixinLootTable;
import com.majruszlibrary.mixininterfaces.fabric.IMixinLootTableBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( LootTable.Builder.class )
public abstract class MixinLootTableBuilder implements IMixinLootTableBuilder {
	ResourceLocation majruszlibrary$id = null;

	@Override
	public void majruszlibrary$set( ResourceLocation id ) {
		this.majruszlibrary$id = id;
	}

	@Inject(
		at = @At( "RETURN" ),
		method = "build ()Lnet/minecraft/world/level/storage/loot/LootTable;"
	)
	private void build( CallbackInfoReturnable< LootTable > callbackInfo ) {
		( ( IMixinLootTable )callbackInfo.getReturnValue() ).majruszlibrary$set( this.majruszlibrary$id );
	}
}
