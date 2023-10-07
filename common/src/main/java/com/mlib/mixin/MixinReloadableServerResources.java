package com.mlib.mixin;

import com.mlib.contexts.OnResourceListenersGet;
import com.mlib.contexts.base.Contexts;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin( ReloadableServerResources.class )
public abstract class MixinReloadableServerResources {
	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "listeners ()Ljava/util/List;"
	)
	private void listeners( CallbackInfoReturnable< List< PreparableReloadListener > > callback ) {
		List< PreparableReloadListener > listeners = new ArrayList<>( callback.getReturnValue() );
		listeners.addAll( Contexts.dispatch( new OnResourceListenersGet() ).listeners );

		callback.setReturnValue( listeners );
	}
}
