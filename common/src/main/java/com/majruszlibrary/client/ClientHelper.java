package com.majruszlibrary.client;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.annotation.Dist;
import com.majruszlibrary.annotation.OnlyIn;
import com.majruszlibrary.events.OnKeyStateChanged;

@OnlyIn( Dist.CLIENT )
public class ClientHelper {
	private static int SHIFT_ACTION = 0;
	private static int CTRL_ACTION = 0;
	private static int LEFT_ALT_ACTION = 0;

	public static boolean wasShiftPressed() {
		return SHIFT_ACTION == 1;
	}

	public static boolean isShiftDown() {
		return SHIFT_ACTION >= 1;
	}

	public static boolean wasCtrlPressed() {
		return CTRL_ACTION == 1;
	}

	public static boolean isCtrlDown() {
		return CTRL_ACTION >= 1;
	}

	public static boolean wasLeftAltPressed() {
		return LEFT_ALT_ACTION == 1;
	}

	public static boolean isLeftAltDown() {
		return LEFT_ALT_ACTION >= 1;
	}

	@AutoInstance
	@OnlyIn( Dist.CLIENT )
	public static class Updater {
		public Updater() {
			OnKeyStateChanged.listen( data->{
				switch( data.key ) {
					case 340 -> SHIFT_ACTION = data.action;
					case 341 -> CTRL_ACTION = data.action;
					case 342 -> LEFT_ALT_ACTION = data.action;
				}
			} );
		}
	}
}
