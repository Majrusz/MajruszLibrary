package com.mlib.temp;

import com.mlib.annotation.AutoInstance;
import com.mlib.command.Command;
import com.mlib.command.CommandData;
import com.mlib.item.EnchantmentHelper;
import com.mlib.text.TextHelper;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.entity.player.Player;

@AutoInstance
public class EnchantmentsLister {
	public EnchantmentsLister() {
		Command.create()
			.literal( "myenchantments" )
			.hasPermission( 4 )
			.execute( this::listEnchantments )
			.register();
	}

	private int listEnchantments( CommandData data ) throws CommandSyntaxException {
		if( data.getCaller() instanceof Player player ) {
			EnchantmentHelper.EnchantmentsDef enchantments = EnchantmentHelper.read( player.getMainHandItem() );
			player.sendSystemMessage( TextHelper.literal( "ENCHANTMENTS %d", enchantments.enchantments.size() ) );
			for( EnchantmentHelper.EnchantmentDef enchantment : enchantments.enchantments ) {
				player.sendSystemMessage( TextHelper.literal( " - %s %d", enchantment.id, enchantment.level ) );
			}
		}

		return 0;
	}
}
