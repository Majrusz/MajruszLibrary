package com.mlib.gamemodifiers;

import com.mlib.Utility;
import com.mlib.config.ConfigGroup;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class Context extends ConfigGroup {
	final List< Condition > conditions = new ArrayList<>();
	final String configName;
	final String configComment;
	protected GameModifier gameModifier = null;

	public static < DataType extends Context.Data, ContextType extends Context > void handleContexts( Function< Context, DataType > dataProvider,
		List< ContextType > contexts
	) {
		for( Context context : contexts ) {
			Data data = dataProvider.apply( context );
			if( context.check( data ) ) {
				context.gameModifier.execute( data );
			}
		}
	}

	public Context( String configName, String configComment ) {
		this.configName = configName;
		this.configComment = configComment;
	}

	public void setup( GameModifier gameModifier ) {
		this.gameModifier = gameModifier;
		this.configs.addAll( this.conditions );
	}

	public void addCondition( Condition condition ) {
		assert this.gameModifier == null : "context was already set up";
		this.conditions.add( condition );
		this.conditions.sort( Condition.COMPARATOR );
	}

	public void addConditions( Condition... conditions ) {
		for( Condition condition : conditions ) {
			addCondition( condition );
		}
	}

	public boolean check( Data data ) {
		for( Condition condition : this.conditions ) {
			if( !condition.check( this.gameModifier, data ) ) {
				return false;
			}
		}

		return true;
	}

	public GameModifier getGameModifier() {
		return this.gameModifier;
	}

	public static abstract class Data {
		public final Context context;
		@Nullable
		public final LivingEntity entity;
		@Nullable
		public final ServerLevel level;

		public Data( Context context, @Nullable LivingEntity entity ) {
			this.context = context;
			this.entity = entity;
			this.level = this.entity != null ? Utility.castIfPossible( ServerLevel.class, this.entity.level ) : null;
		}
	}
}
