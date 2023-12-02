package com.majruszlibrary.modhelper;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;

public class ResourceForge implements IResourcePlatform {
	@Override
	public void register( ResourceLocation id, ResourceLoader.Server server ) {
		MinecraftForge.EVENT_BUS.addListener( ( AddReloadListenerEvent event )->event.addListener( server ) );
	}
}
