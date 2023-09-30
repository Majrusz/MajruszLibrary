package net.mlib.mixin.fabric;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.mlib.mixininterfaces.fabric.IMixinLootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin( LootDataType.class )
public abstract class MixinLootDataType< T > {
	@Inject(
		at = @At( "RETURN" ),
		method = "deserialize (Lnet/minecraft/resources/ResourceLocation;Lcom/google/gson/JsonElement;)Ljava/util/Optional;"
	)
	private void deserialize( ResourceLocation id, JsonElement json, CallbackInfoReturnable< Optional< T > > callback ) {
		Optional< T > optional = callback.getReturnValue();
		if( optional.isPresent() && optional.get() instanceof IMixinLootTable lootTable ) {
			lootTable.set( id );
		}
	}
}
