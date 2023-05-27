package com.mlib.mixin;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mlib.gamemodifiers.contexts.OnLootTableCustomLoad;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Type;

@Mixin( LootTable.Serializer.class )
public abstract class MixinLootTableSerializer {
	@Shadow( aliases = { "this$0" } )
	@Inject( method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/world/level/storage/loot/LootTable;", at = @At( "RETURN" ) )
	private void deserialize( JsonElement jsonElement, Type type, JsonDeserializationContext context, CallbackInfoReturnable< LootTable > callback ) {
		JsonObject jsonObject = GsonHelper.convertToJsonObject( jsonElement, "loot table" );
		if( jsonObject.has( "mlib" ) ) {
			OnLootTableCustomLoad.dispatch( new ResourceLocation( jsonObject.get( "mlib" ).getAsString() ), callback.getReturnValue(), jsonObject );
		}
	}
}
