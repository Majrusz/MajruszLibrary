package com.mlib.gamemodifiers.data;

import com.mlib.events.ItemSwingDurationEvent;
import com.mlib.gamemodifiers.ContextData;

public class OnItemSwingDurationData extends ContextData {
	public final ItemSwingDurationEvent event;

	public OnItemSwingDurationData( ItemSwingDurationEvent event ) {
		super( event.entity );
		this.event = event;
	}
}