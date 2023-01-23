package com.mlib.text;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.TreeMap;

public class TextHelper {
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

	public static String toRoman( int number ) {
		int nearestKey = ROMAN_LETTERS.floorKey( number );

		return number == nearestKey ? ROMAN_LETTERS.get( number ) : ROMAN_LETTERS.get( nearestKey ) + toRoman( number - nearestKey );
	}

	public static String minPrecision( double number ) {
		if( Math.abs( number - ( long )number ) < 0.001 ) {
			return "%.0f".formatted( number );
		} else {
			return new BigDecimal( number ).setScale( 2, RoundingMode.HALF_EVEN ).stripTrailingZeros().toPlainString();
		}
	}

	public static String minPrecision( float number ) {
		if( Math.abs( number - ( int )number ) < 0.001f ) {
			return "%.0f".formatted( number );
		} else {
			return new BigDecimal( number ).setScale( 2, RoundingMode.HALF_EVEN ).stripTrailingZeros().toPlainString();
		}
	}

	public static String signed( float number ) {
		return "%s%s".formatted( number >= 0.0f ? "+" : "", minPrecision( number ) );
	}

	public static String signed( int number ) {
		return "%s%d".formatted( number >= 0 ? "+" : "", number );
	}

	public static String signedPercent( float number ) {
		return "%s%%".formatted( signed( number * 100.0f ) );
	}

	public static String signedPercent( int number ) {
		return "%s%%".formatted( signed( number * 100 ) );
	}

	public static String percent( float number ) {
		return "%s%%".formatted( minPrecision( number * 100.0f ) );
	}

	public static String percent( int number ) {
		return "%d%%".formatted( number * 100 );
	}

	public static void addEmptyLine( List< Component > components ) {
		components.add( new TextComponent( " " ) );
	}

	public static void addMoreDetailsText( List< Component > components ) {
		components.add( new TranslatableComponent( "mlib.items.advanced_hint" ).withStyle( ChatFormatting.GRAY ) );
	}
}
