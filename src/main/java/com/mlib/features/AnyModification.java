package com.mlib.features;

import com.google.common.base.Suppliers;
import com.mlib.contexts.OnLoot;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/** Loot modifier for all situations to handle . */
public class AnyModification extends LootModifier {
	public static final Supplier< Codec< AnyModification > > CODEC = Suppliers.memoize( ()->RecordCodecBuilder.create( inst->codecStart( inst ).apply( inst, AnyModification::new ) ) );

	public AnyModification( LootItemCondition[] conditionsIn ) {
		super( conditionsIn );
	}

	@Nonnull
	@Override
	public ObjectArrayList< ItemStack > doApply( ObjectArrayList< ItemStack > generatedLoot, LootContext context ) {
		return OnLoot.dispatch( generatedLoot, context ).generatedLoot;
	}

	@Override
	public Codec< ? extends IGlobalLootModifier > codec() {
		return CODEC.get();
	}
}
