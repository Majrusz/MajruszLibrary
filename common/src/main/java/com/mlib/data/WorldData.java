package com.mlib.data;

import com.mlib.contexts.OnLevelsLoaded;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class WorldData extends SavedData {
	private String name;

	public WorldData() {
		OnLevelsLoaded.listen( this::setup );
	}

	@Override
	public CompoundTag save( CompoundTag tag ) {
		return Serializables.write( this, tag );
	}

	public WorldData load( CompoundTag tag ) {
		Serializables.read( this, tag );

		return this;
	}

	public WorldData named( String name ) {
		this.name = name;

		return this;
	}

	protected void setupDefaultValues() {}

	private void setup( OnLevelsLoaded data ) {
		this.setupDefaultValues();

		data.server.overworld()
			.getDataStorage()
			.computeIfAbsent( this::load, ()->this, this.name );
	}
}
