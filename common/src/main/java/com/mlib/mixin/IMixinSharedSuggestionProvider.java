package com.mlib.mixin;

import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.function.Consumer;
import java.util.function.Function;

@Mixin( SharedSuggestionProvider.class )
public interface IMixinSharedSuggestionProvider {
	@Overwrite
	static < Type > void filterResources( Iterable< Type > registries, String command, Function< Type, ResourceLocation > mapper,
		Consumer< Type > suggestions
	) {
		boolean containsColon = command.indexOf( 58 ) > -1;
		for( Type registry : registries ) {
			ResourceLocation id = mapper.apply( registry );
			if( containsColon ) {
				if( SharedSuggestionProvider.matchesSubStr( command, id.toString() ) ) {
					suggestions.accept( registry );
				}
			} else if( SharedSuggestionProvider.matchesSubStr( command, id.getNamespace() ) || SharedSuggestionProvider.matchesSubStr( command, id.getPath() ) ) {
				suggestions.accept( registry );
			}
		}
	}
}
