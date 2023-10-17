package com.mlib.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootDataType;

import java.util.List;
import java.util.function.Supplier;

public interface IParameter< Type > {
	CommandBuilder apply( CommandBuilder builder );

	Type get( CommandContext< CommandSourceStack > context );

	abstract class Named< Type > implements IParameter< Type > {
		String name;

		public Named< Type > named( String name ) {
			this.name = name;

			return this;
		}
	}

	abstract class Hinted< Type > implements IParameter< Type > {
		String name;
		SuggestionProvider< CommandSourceStack > suggestions = null;

		public Hinted< Type > named( String name ) {
			this.name = name;

			return this;
		}

		public Hinted< Type > suggests( Supplier< List< ResourceLocation > > suggestions ) {
			this.suggestions = ( context, builder )->SharedSuggestionProvider.suggestResource( suggestions.get(), builder );

			return this;
		}
	}
}
