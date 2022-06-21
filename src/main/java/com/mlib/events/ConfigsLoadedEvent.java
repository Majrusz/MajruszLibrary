package com.mlib.events;

import com.mlib.config.ConfigHandler;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

/** Event called when custom configs are loaded. */
public class ConfigsLoadedEvent extends Event implements IModBusEvent {
	public final ConfigHandler configHandler;

	public ConfigsLoadedEvent( ConfigHandler configHandler ) {
		this.configHandler = configHandler;
	}
}
