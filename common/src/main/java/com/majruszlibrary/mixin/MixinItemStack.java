package com.majruszlibrary.mixin;

import com.majruszlibrary.contexts.OnItemAttributeTooltip;
import com.majruszlibrary.contexts.OnItemDamaged;
import com.majruszlibrary.contexts.OnItemTooltip;
import com.majruszlibrary.contexts.base.Contexts;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin( value = ItemStack.class, priority = 1100 )
public abstract class MixinItemStack {
	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "hurt (ILnet/minecraft/util/RandomSource;Lnet/minecraft/server/level/ServerPlayer;)Z"
	)
	private void hurt( int damage, RandomSource source, ServerPlayer player, CallbackInfoReturnable< Boolean > callback ) {
		ItemStack itemStack = ( ItemStack )( Object )this;
		itemStack.setDamageValue( itemStack.getDamageValue() + Contexts.dispatch( new OnItemDamaged( player, itemStack, damage ) ).getExtraDamage() );

		callback.setReturnValue( itemStack.getDamageValue() >= itemStack.getMaxDamage() );
	}

	@Inject(
		at = @At(
			ordinal = 4,
			shift = At.Shift.BEFORE,
			target = "Lnet/minecraft/world/item/ItemStack;shouldShowInTooltip (ILnet/minecraft/world/item/ItemStack$TooltipPart;)Z",
			value = "INVOKE"
		),
		locals = LocalCapture.CAPTURE_FAILHARD,
		method = "getTooltipLines (Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;"
	)
	private void getTooltipLines( Player player, TooltipFlag flag, CallbackInfoReturnable< List< Component > > callback, List< Component > components ) {
		Contexts.dispatch( new OnItemTooltip( ( ItemStack )( Object )this, components, flag, player ) );
	}

	@ModifyVariable(
		at = @At(
			ordinal = 1,
			target = "Lnet/minecraft/world/item/ItemStack;hasTag()Z",
			value = "INVOKE"
		),
		method = "getTooltipLines(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;"
	)
	private List< Component > getTooltipLines( List< Component > components ) {
		OnItemAttributeTooltip data = Contexts.dispatch( new OnItemAttributeTooltip( ( ItemStack )( Object )this ) );
		for( EquipmentSlot slot : EquipmentSlot.values() ) {
			List< Component > slotComponents = data.components.get( slot );
			if( slotComponents.isEmpty() ) {
				continue;
			}

			int insertIdx = this.majruszlibrary$getInsertIdx( components, slot );
			if( insertIdx == -1 ) {
				components.add( CommonComponents.EMPTY );
				components.add( Component.translatable( this.majruszlibrary$getModifierId( slot ) ).withStyle( ChatFormatting.GRAY ) );
				components.addAll( slotComponents );
			} else {
				components.addAll( insertIdx, slotComponents );
			}
		}

		return components;
	}

	private int majruszlibrary$getInsertIdx( List< Component > components, EquipmentSlot slot ) {
		for( int idx = 0; idx < components.size(); ++idx ) {
			if( !components.get( idx ).toString().contains( this.majruszlibrary$getModifierId( slot ) ) ) {
				continue;
			}

			for( int subIdx = idx + 1; subIdx < components.size(); ++subIdx ) {
				if( components.get( subIdx ).toString().contains( "item.modifiers" ) ) {
					return subIdx + 1;
				}
			}

			return components.size();
		}

		return -1;
	}

	private String majruszlibrary$getModifierId( EquipmentSlot slot ) {
		return String.format( "item.modifiers.%s", slot.getName() );
	}
}
