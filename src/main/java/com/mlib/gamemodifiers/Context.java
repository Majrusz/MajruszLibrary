package com.mlib.gamemodifiers;

import com.mlib.config.ConfigGroup;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class Context extends ConfigGroup {
	final List< Condition > conditions = new ArrayList<>();
	final String configName;
	final String configComment;
	protected GameModifier gameModifier = null;

	public static < DataType extends Context.Data, ContextType extends Context > void handleContexts( DataType data, List< ContextType > contexts ) {
		for( Context context : contexts ) {
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

	public static abstract class Data {
		@Nullable
		public final LivingEntity entity;

		public Data( @Nullable LivingEntity entity ) {
			this.entity = entity;
		}
	}
}
