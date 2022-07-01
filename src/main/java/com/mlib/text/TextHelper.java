package com.mlib.text;

import com.mlib.ObfuscationGetter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.*;

public class TextHelper {
	static final ObfuscationGetter.Field< TranslatableContents, List< FormattedText > > DECOMPOSED_PARTS = new ObfuscationGetter.Field<>( TranslatableContents.class, "f_237500_" );
	static final ObfuscationGetter.Method< TranslatableContents > DECOMPOSE = new ObfuscationGetter.Method<>( TranslatableContents.class, "m_237524_" );
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
		TranslatableContents translatableContents = new TranslatableContents( key, params );
		DECOMPOSE.invoke( translatableContents );
		return DECOMPOSED_PARTS.get( translatableContents );
	}

	public static MutableComponent createColoredTranslatable( List< FormattedText > decomposedText, ChatFormatting defaultFormatting, FormattingData... formattingList ) {
		MutableComponent component = Component.literal( "" ).withStyle( defaultFormatting );
		for( int idx = 0; idx < decomposedText.size(); ++idx ) {
			int finalIdx = idx;
			Optional< FormattingData > formattingData = Arrays.stream( formattingList ).filter( data->data.idx == finalIdx ).findFirst();
			ChatFormatting chatFormatting = formattingData.orElse( new FormattingData( idx, defaultFormatting ) ).formatting;
			component.append( Component.literal( decomposedText.get( idx ).getString() ).withStyle( chatFormatting ) );
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
