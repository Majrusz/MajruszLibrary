package com.mlib.temp;

import com.mlib.annotation.AutoInstance;
import com.mlib.command.Command;
import com.mlib.command.CommandData;
import com.mlib.command.IParameter;
import com.mlib.contexts.OnMobSpawnLimitGet;
import com.mlib.contexts.OnMobSpawnRateGet;
import com.mlib.math.Range;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

@AutoInstance
public class SpawnRateChanger {
	static final IParameter< Float > SPAWN_RATE = Command.number( Range.of( 0.0f, 10.0f ) ).named( "spawn_rate" );
	float spawnRate = 1.0f;

	public SpawnRateChanger() {
		Command.create()
			.literal( "myspawnrate" )
			.hasPermission( 4 )
			.parameter( SPAWN_RATE )
			.execute( this::updateSpawnRate )
			.register();

		OnMobSpawnRateGet.listen( this::increaseSpawnRate );

		OnMobSpawnLimitGet.listen( this::increaseSpawnLimit );
	}

	private int updateSpawnRate( CommandData data ) throws CommandSyntaxException {
		this.spawnRate = data.get( SPAWN_RATE );

		return 0;
	}

	private void increaseSpawnRate( OnMobSpawnRateGet data ) {
		data.value *= this.spawnRate;
	}

	private void increaseSpawnLimit( OnMobSpawnLimitGet data ) {
		data.value *= this.spawnRate;
	}
}
