package com.mlib.loot_modifiers;

import com.google.gson.JsonObject;
import com.mlib.events.AnyLootModificationEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

/** Loot modifier for all situations. */
public class AnyModification extends LootModifier {
	public AnyModification( LootItemCondition[] conditionsIn ) {
		super( conditionsIn );
	}

	@Nonnull
	@Override
	public List< ItemStack > doApply( List< ItemStack > generatedLoot, LootContext context ) {
		BlockState blockState = context.getParam( LootContextParams.BLOCK_STATE );
		DamageSource damageSource = context.getParam( LootContextParams.DAMAGE_SOURCE );
		Entity killer = context.getParam( LootContextParams.KILLER_ENTITY );
		Entity entity = context.getParam( LootContextParams.THIS_ENTITY );
		ItemStack tool = context.getParam( LootContextParams.TOOL );
		Vec3 origin = context.getParam( LootContextParams.ORIGIN );

		MinecraftForge.EVENT_BUS.post( new AnyLootModificationEvent( generatedLoot, blockState, damageSource, killer, entity, tool, origin ) );
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer< AnyModification > {
		@Override
		public AnyModification read( ResourceLocation name, JsonObject object, LootItemCondition[] conditions ) {
			return new AnyModification( conditions );
		}

		@Override
		public JsonObject write( AnyModification instance ) {
			return null;
		}
	}
}
