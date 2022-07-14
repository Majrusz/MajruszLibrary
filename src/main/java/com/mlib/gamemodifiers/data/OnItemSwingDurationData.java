package com.mlib.gamemodifiers.data;

import com.mlib.events.ItemSwingDurationEvent;
import com.mlib.gamemodifiers.ContextData;

public class OnItemSwingDurationData extends ContextData.Event< ItemSwingDurationEvent > {
	public OnItemSwingDurationData( ItemSwingDurationEvent event ) {
		super( event.entity, event );
	}
}