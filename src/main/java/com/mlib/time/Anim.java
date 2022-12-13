package com.mlib.time;

import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnServerTick;
import com.mlib.gamemodifiers.parameters.Priority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class Anim {
	static final List< IAnimation > PENDING_ANIMS = Collections.synchronizedList( new ArrayList<>() );
	static final List< IAnimation > ANIMS = Collections.synchronizedList( new ArrayList<>() );

	public static Delay delay( Consumer< Delay > callback, int ticks ) {
		return setup( new Delay( callback, ticks ) );
	}

	public static Delay nextTick( Runnable callback ) {
		return setup( new Delay( delay->callback.run(), 1 ) );
	}

	public static Slider slider( Consumer< Slider > callback, int ticks ) {
		return setup( new Slider( callback, ticks ) );
	}

	public static < Type extends IAnimation > Type setup( Type anim ) {
		PENDING_ANIMS.add( anim );

		return anim;
	}

	@AutoInstance
	public static class Updater extends GameModifier {
		public Updater() {
			new OnServerTick.Context( this::updateAnims )
				.priority( Priority.HIGHEST )
				.addCondition( TimeHelper::isEndPhase );
		}

		private void updateAnims( OnServerTick.Data data ) {
			synchronized( PENDING_ANIMS ) {
				PENDING_ANIMS.forEach( IAnimation::onStart );
				ANIMS.addAll( PENDING_ANIMS );
				PENDING_ANIMS.clear();
			}
			for( Iterator< IAnimation > iterator = ANIMS.iterator(); iterator.hasNext(); ) {
				IAnimation anim = iterator.next();
				anim.onTick();
				if( anim.isFinished() ) {
					anim.onEnd();
					iterator.remove();
				}
			}
		}
	}
}
