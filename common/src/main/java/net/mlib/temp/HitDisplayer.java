package net.mlib.temp;

import net.mlib.MajruszLibrary;
import net.mlib.annotations.AutoInstance;
import net.mlib.contexts.OnLoot;
import net.mlib.registries.Registries;

@AutoInstance
public class HitDisplayer {
	public HitDisplayer() {
		OnLoot.listen( this::display );
	}

	private void display( OnLoot.Data data ) {
		MajruszLibrary.HELPER.log( "TARGET %s", data.entity != null ? Registries.get( data.entity.getType() ) : '-' );
		MajruszLibrary.HELPER.log( "ATTACKER %s", data.killer != null ? Registries.get( data.killer.getType() ) : '-' );
		MajruszLibrary.HELPER.log( "TOOL %s", data.tool != null ? Registries.get( data.tool.getItem() ) : '-' );
	}
}
