package com.mlib.features;

import com.google.gson.JsonObject;
import com.mlib.events.AnyLootModificationEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

/** Loot modifier for all situations to handle . */
public class AnyModification extends LootModifier {
	public AnyModification( LootItemCondition[] conditionsIn ) {
		super( conditionsIn );
	}

	@Nonnull
	@Override
	public List< ItemStack > doApply( List< ItemStack > generatedLoot, LootContext context ) {
		MinecraftForge.EVENT_BUS.post( new AnyLootModificationEvent( generatedLoot, context ) );
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer< AnyModification > {
		@Override
		public AnyModification read( ResourceLocation name, JsonObject object, LootItemCondition[] conditions ) {
			return new AnyModification( conditions );
		}

		@Override
		public JsonObject write( AnyModification instance ) {
			return makeConditions( instance.conditions );
		}
	}
}
