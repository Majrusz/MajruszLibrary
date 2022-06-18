package com.mlib.loot_modifiers;

import com.google.gson.JsonObject;
import com.mlib.events.AnyLootModificationEvent;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
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

/** Loot modifier for all situations. */
public class AnyModification extends LootModifier {
	public AnyModification( LootItemCondition[] conditionsIn ) {
		super( conditionsIn );
	}

	@Nonnull
	@Override
	public ObjectArrayList< ItemStack > doApply( ObjectArrayList< ItemStack > generatedLoot, LootContext context ) {
		BlockState blockState = LootHelper.getParameter( context, LootContextParams.BLOCK_STATE );
		DamageSource damageSource = LootHelper.getParameter( context, LootContextParams.DAMAGE_SOURCE );
		Entity killer = LootHelper.getParameter( context, LootContextParams.KILLER_ENTITY );
		Entity entity = LootHelper.getParameter( context, LootContextParams.THIS_ENTITY );
		ItemStack tool = LootHelper.getParameter( context, LootContextParams.TOOL );
		Vec3 origin = LootHelper.getParameter( context, LootContextParams.ORIGIN );

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
			return makeConditions( instance.conditions );
		}
	}
}
