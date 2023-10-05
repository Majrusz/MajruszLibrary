package com.mlib.mixin;

import com.mlib.contexts.OnItemAttributeTooltip;
import com.mlib.contexts.base.Contexts;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin( ItemStack.class )
public abstract class MixinItemStack {
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
			if( !components.get( idx ).toString().contains( this.getModifierId( slot ) ) ) {
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

	private String getModifierId( EquipmentSlot slot ) {
		return String.format( "item.modifiers.%s", slot.getName() );
	}
}
