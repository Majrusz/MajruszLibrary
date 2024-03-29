package com.majruszlibrary.data;

import com.majruszlibrary.registry.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

class ReaderMobEffect extends ReaderStringCustom< MobEffect > {
	@Override
	public MobEffect convert( String id ) {
		return Registries.EFFECTS.get( new ResourceLocation( id ) );
	}

	@Override
	public String convert( MobEffect effect ) {
		return Registries.EFFECTS.getId( effect ).toString();
	}
}
