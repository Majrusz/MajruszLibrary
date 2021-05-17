package com.mlib;

import com.mlib.loot_modifiers.AnyModification;
import com.mlib.loot_modifiers.HarvestCrop;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

/** Loot modifiers responsible for adding, removing or changing loot. */
@Mod.EventBusSubscriber( modid = MajruszLibrary.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD )
public class LootModifiers {
	@SubscribeEvent
	public static void registerModifierSerializers( final RegistryEvent.Register< GlobalLootModifierSerializer< ? > > event ) {
		IForgeRegistry< GlobalLootModifierSerializer< ? > > registry = event.getRegistry();

		registerSingleModifier( registry, new HarvestCrop.Serializer(), "harvest_crop" );
		registerSingleModifier( registry, new AnyModification.Serializer(), "any_situation" );
	}

	/** Adding to registry single loot modifier. */
	protected static void registerSingleModifier( IForgeRegistry< GlobalLootModifierSerializer< ? > > registry,
		GlobalLootModifierSerializer< ? > serializer, String registerName
	) {
		registry.register( serializer.setRegistryName( MajruszLibrary.getLocation( registerName ) ) );
	}
}
