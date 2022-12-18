package com.mlib.mixin;

import com.mlib.events.ItemHurtEvent;
import com.mlib.gamemodifiers.contexts.OnItemAttributeTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin( ItemStack.class )
public abstract class MixinItemStack {
	@Shadow( aliases = { "this$0" } )
	@Inject( method = "hurt(ILnet/minecraft/util/RandomSource;Lnet/minecraft/server/level/ServerPlayer;)Z", at = @At( "RETURN" ), cancellable = true )
	private void hurt( int damage, RandomSource source, @Nullable ServerPlayer player,
		CallbackInfoReturnable< Boolean > callback
	) {
		ItemStack itemStack = ( ItemStack )( Object )this;
		ItemHurtEvent itemHurtEvent = new ItemHurtEvent( player, itemStack, damage );
		MinecraftForge.EVENT_BUS.post( itemHurtEvent );
		if( itemHurtEvent.extraDamage != 0 ) {
			itemStack.setDamageValue( itemStack.getDamageValue() + itemHurtEvent.extraDamage );
		}

		callback.setReturnValue( itemHurtEvent.hasBeenBroken() );
	}

	@Shadow( aliases = { "this$0" } )
	@ModifyVariable( method = "getTooltipLines(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;", at = @At( value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hasTag()Z", ordinal = 1 ) )
	private List< Component > getTooltipLines( List< Component > components ) {
		OnItemAttributeTooltip.Data data = new OnItemAttributeTooltip.Data( ( ItemStack )( Object )this );
		OnItemAttributeTooltip.Context.accept( data );
		for( EquipmentSlot slot : EquipmentSlot.values() ) {
			List< Component > slotComponents = data.components.get( slot );
			if( slotComponents.isEmpty() )
				continue;

			int insertIdx = this.getInsertIdx( components, slot );
			if( insertIdx == -1 ) {
				components.add( CommonComponents.EMPTY );
				components.add( Component.translatable( this.getModifierId( slot ) ).withStyle( ChatFormatting.GRAY ) );
				components.addAll( slotComponents );
			} else {
				components.addAll( insertIdx, slotComponents );
			}
		}

		return components;
	}

	private int getInsertIdx( List< Component > components, EquipmentSlot slot ) {
		for( int idx = 0; idx < components.size(); ++idx ) {
			if( !components.get( idx ).toString().contains( this.getModifierId( slot ) ) )
				continue;

			for( int subIdx = idx + 1; subIdx < components.size(); ++subIdx ) {
				if( components.get( subIdx ).toString().contains( "item.modifiers" ) )
					return subIdx + 1;
			}

			return components.size();
		}

		return -1;
	}

	private String getModifierId( EquipmentSlot slot ) {
		return String.format( "item.modifiers.%s", slot.getName() );
	}
}
