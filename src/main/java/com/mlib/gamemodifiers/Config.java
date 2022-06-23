package com.mlib.gamemodifiers;

import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.config.IntegerConfig;

public abstract class Config {
	final String groupName;
	final String groupComment;

	public Config( String groupName, String groupComment ) {
		this.groupName = groupName;
		this.groupComment = groupComment;
	}

	public abstract void setup( ConfigGroup group );

	public ConfigGroup addNewGroup( ConfigGroup group ) {
		return group.addNewGroup( this.groupName, this.groupComment );
	}

	public static class Effect extends Config {
		static final int MIN_AMPLIFIER = 0, MAX_AMPLIFIER = 10;
		static final double MIN_DURATION = 1.0, MAX_DURATION = 99.0;
		final IntegerConfig amplifier;
		final DoubleConfig duration;

		public Effect( String groupName, int amplifier, double duration ) {
			super( groupName, "" );
			this.amplifier = new IntegerConfig( "amplifier", "Level of the effect to apply.", false, amplifier, MIN_AMPLIFIER, MAX_AMPLIFIER );
			this.duration = new DoubleConfig( "duration", "Duration in seconds.", false, duration, MIN_DURATION, MAX_DURATION );
		}

		public int getAmplifier() {
			return this.amplifier.get();
		}

		public int getDuration() {
			return this.duration.asTicks();
		}

		@Override
		public void setup( ConfigGroup group ) {
			group.addConfigs( this.amplifier, this.duration );
		}
	}
}
