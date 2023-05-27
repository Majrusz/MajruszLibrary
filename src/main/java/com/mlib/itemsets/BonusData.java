package com.mlib.itemsets;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;

import java.util.Arrays;

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
		Object[] params = Arrays.stream( this.parameters )
			.map( parameter -> {
				if( parameter instanceof MutableComponent component ) {
					return component.withStyle( chatFormatting );
				}

				return Component.literal( parameter.toString() ).withStyle( chatFormatting );
			} ).toArray();

		return Component.translatable( this.keyId, params )
			.withStyle( isConditionMet ? ChatFormatting.GRAY : ChatFormatting.DARK_GRAY );
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