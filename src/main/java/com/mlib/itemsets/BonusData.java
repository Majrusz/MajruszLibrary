package com.mlib.itemsets;

import com.mlib.text.FormattedTranslatable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Arrays;
import java.util.function.BiFunction;

public class BonusData {
	final ICondition condition;
	final String keyId;
	final IRequirementFormat requirementFormat;
	final Object[] parameters;

	public static ICondition requiredItems( int count ) {
		return ( itemSet, entity )->itemSet.countEquippedItems( entity ) >= count;
	}

	public static IRequirementFormat requiredItemsFormat( int count ) {
		return ( itemSet, entity )->Component.translatable( "%1$s/%2$s", count, itemSet.getTotalItemsCount() );
	}

	public BonusData( ICondition condition, String keyId, IRequirementFormat requirementFormat, Object... parameters ) {
		this.condition = condition;
		this.keyId = keyId;
		this.requirementFormat = requirementFormat;
		this.parameters = parameters;
	}

	public BonusData( int requiredItems, String keyId, Object... parameters ) {
		this( requiredItems( requiredItems ), keyId, requiredItemsFormat( requiredItems ), parameters );
	}

	public boolean isConditionMet( ItemSet itemSet, LivingEntity entity ) {
		return this.condition.check( itemSet, entity );
	}

	public MutableComponent buildTranslatedRequirements( ItemSet itemSet, boolean isConditionMet ) {
		ChatFormatting chatFormatting = isConditionMet ? itemSet.getChatFormatting() : ChatFormatting.DARK_GRAY;

		return this.requirementFormat.build( itemSet, this ).withStyle( chatFormatting );
	}

	public MutableComponent buildTranslatedName( ItemSet itemSet, boolean isConditionMet ) {
		ChatFormatting chatFormatting = isConditionMet ? itemSet.getChatFormatting() : ChatFormatting.DARK_GRAY;
		FormattedTranslatable component = new FormattedTranslatable( this.keyId, isConditionMet ? ChatFormatting.GRAY : chatFormatting );
		Arrays.stream( this.parameters ).forEach( parameter->component.addParameter( parameter, chatFormatting ) );

		return component.create();
	}

	private ChatFormatting getChatFormatting( ItemSet itemSet, LivingEntity entity ) {
		return this.isConditionMet( itemSet, entity ) ? itemSet.getChatFormatting() : ChatFormatting.GRAY;
	}

	@FunctionalInterface
	public interface ICondition {
		boolean check( ItemSet itemSet, LivingEntity player );
	}

	@FunctionalInterface
	public interface IRequirementFormat {
		MutableComponent build( ItemSet itemSet, BonusData bonusData );
	}
}