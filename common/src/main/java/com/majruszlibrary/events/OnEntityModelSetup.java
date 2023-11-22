package com.majruszlibrary.events;

import com.majruszlibrary.annotation.Dist;
import com.majruszlibrary.annotation.OnlyIn;
import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

@OnlyIn( Dist.CLIENT )
public class OnEntityModelSetup implements IEntityEvent {
	public final LivingEntity entity;
	public final HumanoidModel< ? > model;

	public static Event< OnEntityModelSetup > listen( Consumer< OnEntityModelSetup > consumer ) {
		return Events.get( OnEntityModelSetup.class ).add( consumer );
	}

	public OnEntityModelSetup( LivingEntity entity, HumanoidModel< ? > model ) {
		this.entity = entity;
		this.model = model;
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}
}
