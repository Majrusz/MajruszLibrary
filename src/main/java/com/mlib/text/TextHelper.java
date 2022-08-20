package com.mlib.text;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
			return String.format( "%.0f", number );
		} else {
			return new BigDecimal( number ).setScale( 2, RoundingMode.HALF_EVEN ).stripTrailingZeros().toPlainString();
		}
	}

	public static String minPrecision( float number ) {
		if( Math.abs( number - ( int )number ) < 0.001f ) {
			return String.format( "%.0f", number );
		} else {
			return new BigDecimal( number ).setScale( 2, RoundingMode.HALF_EVEN ).stripTrailingZeros().toPlainString();
		}
	}

	public static String signed( float number ) {
		return String.format( "%s%s", number > 0.0f ? "+" : "", minPrecision( number ) );
	}

	public static String signed( int number ) {
		return String.format( "%s%d", number > 0 ? "+" : "", number );
	}

	public static String signedPercent( float number ) {
		return String.format( "%s%s%%", number > 0.0f ? "+" : "", minPrecision( number * 100.0f ) );
	}

	public static String signedPercent( int number ) {
		return String.format( "%s%d%%", number > 0 ? "+" : "", number * 100 );
	}

	public static String percent( float number ) {
		return String.format( "%s%%", minPrecision( number * 100.0f ) );
	}

	public static String percent( int number ) {
		return String.format( "%d%%", number * 100 );
	}
}
