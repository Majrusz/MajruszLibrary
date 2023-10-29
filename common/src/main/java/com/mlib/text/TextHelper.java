package com.mlib.text;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.TreeMap;
import java.util.function.UnaryOperator;

public class TextHelper {
	static final int DEFAULT_SCALE = 2;
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

	public static String minPrecision( double number, int scale ) {
		if( Math.abs( number - ( long )number ) < Math.pow( 0.1, scale + 1 ) ) {
			return "%.0f".formatted( number );
		} else {
			return new BigDecimal( number ).setScale( scale, RoundingMode.HALF_EVEN ).stripTrailingZeros().toPlainString();
		}
	}

	public static String minPrecision( double number ) {
		return TextHelper.minPrecision( number, DEFAULT_SCALE );
	}

	public static String minPrecision( float number, int scale ) {
		if( Math.abs( number - ( int )number ) < Math.pow( 0.1f, scale + 1 ) ) {
			return "%.0f".formatted( number );
		} else {
			return new BigDecimal( number ).setScale( scale, RoundingMode.HALF_EVEN ).stripTrailingZeros().toPlainString();
		}
	}

	public static String minPrecision( float number ) {
		return TextHelper.minPrecision( number, DEFAULT_SCALE );
	}

	public static String signed( float number, int scale ) {
		return "%s%s".formatted( number >= 0.0f ? "+" : "", TextHelper.minPrecision( number, scale ) );
	}

	public static String signed( float number ) {
		return TextHelper.signed( number, DEFAULT_SCALE );
	}

	public static String signed( int number ) {
		return "%s%d".formatted( number >= 0 ? "+" : "", number );
	}

	public static String signedPercent( float number, int scale ) {
		return "%s%%".formatted( TextHelper.signed( number * 100.0f, scale ) );
	}

	public static String signedPercent( float number ) {
		return TextHelper.signedPercent( number, DEFAULT_SCALE );
	}

	public static String signedPercent( int number ) {
		return "%s%%".formatted( TextHelper.signed( number * 100 ) );
	}

	public static String percent( float number, int scale ) {
		return "%s%%".formatted( TextHelper.minPrecision( number * 100.0f, scale ) );
	}

	public static String percent( float number ) {
		return TextHelper.percent( number, DEFAULT_SCALE );
	}

	public static String percent( int number ) {
		return "%d%%".formatted( number * 100 );
	}

	public static MutableComponent literal( String text ) {
		return Component.literal( text );
	}

	public static MutableComponent literal( String text, Object... arguments ) {
		return Component.literal( text.formatted( arguments ) );
	}

	public static MutableComponent translatable( String id, Object... arguments ) {
		return Component.translatable( id, arguments );
	}

	public static MutableComponent empty() {
		return Component.empty();
	}

	public static MutableComponent moreDetailsComponent() {
		return Component.translatable( "mlib.advanced_hint" ).withStyle( ChatFormatting.GRAY );
	}

	public static UnaryOperator< Style > createURL( String url ) {
		return style->style.withClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, url ) )
			.withHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, TextHelper.literal( url ).withStyle( ChatFormatting.GRAY ) ) );
	}
}
