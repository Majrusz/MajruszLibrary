package com.mlib.text;

import com.mlib.ObfuscationGetter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

public class TextHelper {
	static final ObfuscationGetter.Field< TranslatableComponent, List< FormattedText > > DECOMPOSED_PARTS = new ObfuscationGetter.Field<>( TranslatableComponent.class, "f_131301_" );
	static final ObfuscationGetter.Method< TranslatableComponent > DECOMPOSE = new ObfuscationGetter.Method<>( TranslatableComponent.class, "m_131330_" );
	static final TreeMap< Integer, String > ROMAN_LETTERS = new TreeMap<>();

	static {
		ROMAN_LETTERS.put( 100, "C" );
		ROMAN_LETTERS.put( 90, "XC" );
		ROMAN_LETTERS.put( 50, "L" );
		ROMAN_LETTERS.put( 40, "XL" );
		ROMAN_LETTERS.put( 10, "X" );
		ROMAN_LETTERS.put( 9, "IX" );
		ROMAN_LETTERS.put( 5, "V" );
		ROMAN_LETTERS.put( 4, "IV" );
		ROMAN_LETTERS.put( 1, "I" );
	}

	public static List< FormattedText > decomposeTranslatableText( String key, Object... params ) {
		TranslatableComponent TranslatableComponent = new TranslatableComponent( key, params );
		DECOMPOSE.invoke( TranslatableComponent );
		return DECOMPOSED_PARTS.get( TranslatableComponent );
	}

	public static MutableComponent createColoredTranslatable( List< FormattedText > decomposedText, ChatFormatting defaultFormatting,
		FormattingData... formattingList
	) {
		MutableComponent component = new TextComponent( "" ).withStyle( defaultFormatting );
		for( int idx = 0; idx < decomposedText.size(); ++idx ) {
			int finalIdx = idx;
			Optional< FormattingData > formattingData = Arrays.stream( formattingList ).filter( data->data.idx == finalIdx ).findFirst();
			ChatFormatting chatFormatting = formattingData.orElse( new FormattingData( idx, defaultFormatting ) ).formatting;
			component.append( new TextComponent( decomposedText.get( idx ).getString() ).withStyle( chatFormatting ) );
		}

		return component;
	}

	public static String toRoman( int number ) {
		int nearestKey = ROMAN_LETTERS.floorKey( number );

		return number == nearestKey ? ROMAN_LETTERS.get( number ) : ROMAN_LETTERS.get( nearestKey ) + toRoman( number - nearestKey );
	}

	public static String minPrecision( double number ) {
		if( number == ( long )number ) {
			return String.format( "%.0f", number );
		} else {
			return Double.toString( number );
		}
	}

	public record FormattingData( int idx, ChatFormatting formatting ) {}
}
