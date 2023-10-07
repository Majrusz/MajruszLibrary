package com.mlib.temp;

import com.mlib.MajruszLibrary;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnEntityNoiseCheck;
import com.mlib.contexts.OnEntityNoiseReceived;
import com.mlib.contexts.base.Condition;
import com.mlib.entity.EntityHelper;
import com.mlib.entity.EntityNoiseListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.List;

@AutoInstance
public class SignalListener {
	public SignalListener() {
		EntityNoiseListener.add( ServerPlayer.class );

		OnEntityNoiseCheck.listen( OnEntityNoiseCheck::makeAudible )
			.addCondition( Condition.predicate( data->data.listener instanceof Player player && player.isCrouching() ) );

		OnEntityNoiseReceived.listen( this::glowEntity )
			.addCondition( Condition.predicate( data->data.listener instanceof ServerPlayer ) );
	}

	private void glowEntity( OnEntityNoiseReceived data ) {
		MajruszLibrary.ENTITY_GLOW.sendToClients( List.of( ( ServerPlayer )data.listener ), new EntityHelper.EntityGlow( data.emitter, 100 ) );
	}
}
