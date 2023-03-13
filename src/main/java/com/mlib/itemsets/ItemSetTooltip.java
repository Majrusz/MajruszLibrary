package com.mlib.itemsets;

import com.mlib.client.ClientHelper;
import com.mlib.text.TextHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@OnlyIn( Dist.CLIENT )
@Mod.EventBusSubscriber( value = Dist.CLIENT )
public class ItemSetTooltip {
	static final ChatFormatting DISABLED_FORMAT = ChatFormatting.DARK_GRAY;
	static final ChatFormatting HINT_FORMAT = ChatFormatting.GRAY;
	static final String ITEM_TITLE_KEY = "mlib.item_sets.item_title";
	static final String ITEM_KEY = "mlib.item_sets.item";
	static final String BONUS_TITLE_KEY = "mlib.item_sets.bonus_title";
	static final String BONUS_KEY = "mlib.item_sets.bonus";

	@SubscribeEvent
	public static void onItemTooltip( ItemTooltipEvent event ) {
		@Nullable Player player = event.getEntity();
		ItemStack itemStack = event.getItemStack();
		List< Component > tooltip = event.getToolTip();
		if( player == null || isNotPartOfAnySet( itemStack ) )
			return;

		if( !ClientHelper.isShiftDown() ) {
			TextHelper.addEmptyLine( tooltip );
			TextHelper.addMoreDetailsText( tooltip );
			return;
		}

		for( ItemSet itemSet : ItemSet.ITEM_SETS ) {
			if( !itemSet.isPartOfSet( itemStack ) )
				continue;

			TextHelper.addEmptyLine( tooltip );
			addItemList( tooltip, itemSet, player );

			TextHelper.addEmptyLine( tooltip );
			addBonusList( tooltip, itemSet, player );
		}
	}

	private static boolean isNotPartOfAnySet( ItemStack itemStack ) {
		return ItemSet.ITEM_SETS.stream().noneMatch( set->set.isPartOfSet( itemStack ) );
	}

	private static void addItemList( List< Component > tooltip, ItemSet itemSet, Player player ) {
		MutableComponent title = Component.translatable( ITEM_TITLE_KEY, itemSet.getTranslatedName(), itemSet.countEquippedItems( player ), itemSet.getTotalItemsCount() );
		tooltip.add( title.withStyle( HINT_FORMAT ) );
		itemSet.getItems().forEach( item->{
			ChatFormatting chatFormatting = item.isEquipped( player ) ? itemSet.getChatFormatting() : DISABLED_FORMAT;

			tooltip.add( Component.translatable( ITEM_KEY, item.getTranslatedName() ).withStyle( chatFormatting ) );
		} );
	}

	private static void addBonusList( List< Component > tooltip, ItemSet itemSet, Player player ) {
		MutableComponent title = Component.translatable( BONUS_TITLE_KEY, itemSet.getTranslatedName() );
		tooltip.add( title.withStyle( HINT_FORMAT ) );
		itemSet.getBonuses().forEach( bonus->{
			boolean isConditionMet = bonus.isConditionMet( itemSet, player );
			ChatFormatting chatFormatting = isConditionMet ? itemSet.getChatFormatting() : ChatFormatting.DARK_GRAY;
			MutableComponent component = Component.translatable( BONUS_KEY )
				.withStyle( chatFormatting )
				.append( bonus.buildTranslatedRequirements( itemSet, isConditionMet ) )
				.append( bonus.buildTranslatedName( itemSet, isConditionMet ) );

			tooltip.add( component );
		} );
	}
}