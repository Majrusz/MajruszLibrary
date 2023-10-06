package com.mlib.temp;

import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnEntityNoiseCheck;
import com.mlib.contexts.base.Condition;
import com.mlib.entity.EntityNoiseListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

@AutoInstance
public class SignalListener {
	public SignalListener() {
		EntityNoiseListener.add( ServerPlayer.class );

		OnEntityNoiseCheck.listen( OnEntityNoiseCheck::makeAudible )
			.addCondition( Condition.predicate( data->data.listener instanceof Player player && player.isCrouching() ) );
	}
}
