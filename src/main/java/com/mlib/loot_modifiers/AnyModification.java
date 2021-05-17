package com.mlib.loot_modifiers;

import com.google.gson.JsonObject;
import com.mlib.events.AnyLootModificationEvent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

/** Loot modifier for all situations. */
public class AnyModification extends LootModifier {
	public AnyModification( ILootCondition[] conditionsIn ) {
		super( conditionsIn );
	}

	@Nonnull
	@Override
	public List< ItemStack > doApply( List< ItemStack > generatedLoot, LootContext context ) {
		BlockState blockState = context.get( LootParameters.BLOCK_STATE );
		DamageSource damageSource = context.get( LootParameters.DAMAGE_SOURCE );
		Entity killer = context.get( LootParameters.KILLER_ENTITY );
		Entity entity = context.get( LootParameters.THIS_ENTITY );
		ItemStack tool = context.get( LootParameters.TOOL );
		Vector3d origin = context.get( LootParameters.field_237457_g_ );

		MinecraftForge.EVENT_BUS.post( new AnyLootModificationEvent( generatedLoot, blockState, damageSource, killer, entity, tool, origin ) );
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer< AnyModification > {
		@Override
		public AnyModification read( ResourceLocation name, JsonObject object, ILootCondition[] conditions ) {
			return new AnyModification( conditions );
		}

		@Override
		public JsonObject write( AnyModification instance ) {
			return null;
		}
	}
}
