package com.majruszlibrary.item;

import com.majruszlibrary.platform.Side;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class LootHelper {
	public static LootTable getLootTable( ResourceLocation id ) {
		return Side.getServer()
			.getLootData()
			.getLootTable( id );
	}

	public static LootParams toGiftParams( Entity entity ) {
		return new LootParams.Builder( ( ServerLevel )entity.level() )
			.withParameter( LootContextParams.ORIGIN, entity.position() )
			.withParameter( LootContextParams.THIS_ENTITY, entity )
			.withLuck( entity instanceof Player player ? player.getLuck() : 0.0f )
			.create( LootContextParamSets.GIFT );
	}

	public static LootParams toChestParams( ServerLevel level, Vec3 origin ) {
		return new LootParams.Builder( level )
			.withParameter( LootContextParams.ORIGIN, origin )
			.create( LootContextParamSets.CHEST );
	}
}
