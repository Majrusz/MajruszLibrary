package com.majruszlibrary.modhelper;

import net.minecraft.resources.ResourceLocation;

public interface IResourcePlatform {
	void register( ResourceLocation id, ResourceLoader.Server server );
}
