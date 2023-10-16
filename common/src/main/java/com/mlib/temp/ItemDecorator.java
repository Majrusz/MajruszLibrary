package com.mlib.temp;

import com.mlib.annotation.AutoInstance;
import com.mlib.command.Command;
import com.mlib.command.CommandData;
import com.mlib.command.IParameter;
import com.mlib.item.ItemHelper;
import com.mlib.math.Range;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

@AutoInstance
public class ItemDecorator {
	static final IParameter< Boolean > VANILLA_CHANCE = Command.bool().named( "vanilla_chance" );
	static final IParameter< Boolean > TREASURE = Command.bool().named( "is_treasure" );
	static final IParameter< Float > DAMAGE_RATIO = Command.number( Range.of( 0.0f, 1.0f ) ).named( "damage_ratio" );
	static final IParameter< Float > CRD = Command.number( Range.of( 0.0f, 1.0f ) ).named( "crd" );
	static final IParameter< String > MESSAGE = Command.string().named( "message" );

	public ItemDecorator() {
		Command.create()
			.literal( "mydecorate" )
			.hasPermission( 4 )
			.execute( this::decorateItem )
			.parameter( VANILLA_CHANCE )
			.execute( this::decorateItem )
			.parameter( TREASURE )
			.execute( this::decorateItem )
			.parameter( DAMAGE_RATIO )
			.execute( this::decorateItem )
			.parameter( CRD )
			.execute( this::decorateItem )
			.register();

		Command.create()
			.literal( "mystring" )
			.hasPermission( 4 )
			.parameter( MESSAGE )
			.execute( this::sendMessage )
			.register();
	}

	private int decorateItem( CommandData data ) throws CommandSyntaxException {
		if( data.getCaller() instanceof Player player ) {
			ItemHelper.Decorator decorator = ItemHelper.decorate( player.getMainHandItem() );
			data.getOptional( VANILLA_CHANCE ).ifPresent( value->{if( value ) {decorator.withVanillaEnchantmentChance();}} );
			data.getOptional( TREASURE ).ifPresent( value->{if( value ) {decorator.allowTreasureEnchantments();}} );
			data.getOptional( DAMAGE_RATIO ).ifPresent( decorator::damage );
			data.getOptional( CRD ).ifPresent( decorator::enchant );
			decorator.apply();
		}

		return 0;
	}

	private int sendMessage( CommandData data ) throws CommandSyntaxException {
		if( data.getCaller() instanceof Player player ) {
			player.sendSystemMessage( Component.literal( data.get( MESSAGE ) ) );
		}

		return 0;
	}
}
