package com.mlib.mixin.fabric;

import com.mlib.contexts.OnEnderManAngered;
import com.mlib.contexts.base.Contexts;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( EnderMan.class )
public abstract class MixinEnderMan {
	private Player mlib$lastPlayer = null;

	@Inject(
		at = @At( "HEAD" ),
		method = "isLookingAtMe (Lnet/minecraft/world/entity/player/Player;)Z"
	)
	private void isLookingAtMe( Player player, CallbackInfoReturnable< Boolean > callback ) {
		this.mlib$lastPlayer = player;
	}

	@Redirect(
		at = @At(
			target = "Lnet/minecraft/world/item/ItemStack;is (Lnet/minecraft/world/item/Item;)Z",
			value = "INVOKE"
		),
		method = "isLookingAtMe (Lnet/minecraft/world/entity/player/Player;)Z"
	)
	private boolean is( ItemStack itemStack, Item item ) {
		EnderMan enderMan = ( EnderMan )( Object )this;

		return itemStack.is( item )
			|| Contexts.dispatch( new OnEnderManAngered( enderMan, this.mlib$lastPlayer ) ).isAngerCancelled();
	}
}
