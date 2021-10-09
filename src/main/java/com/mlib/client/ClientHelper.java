package com.mlib.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn( Dist.CLIENT )
@Mod.EventBusSubscriber( value = Dist.CLIENT )
public class ClientHelper {
	protected static int shiftLastAction = 0;
	protected static int ctrlLastAction = 0;
	protected static int leftAltLastAction = 0;

	@SubscribeEvent
	public static void onKeyInput( InputEvent.KeyInputEvent event ) {
		switch( event.getKey() ) {
			case 340 -> shiftLastAction = event.getAction();
			case 341 -> ctrlLastAction = event.getAction();
			case 342 -> leftAltLastAction = event.getAction();
		}
	}

	/** Checks whether SHIFT was pressed. (only once) */
	public static boolean wasShiftPressed() {
		return shiftLastAction == 1;
	}

	public static boolean isShiftDown() {
		return shiftLastAction >= 1;
	}

	/** Checks whether CTRL was pressed. (only once) */
	public static boolean wasCtrlPressed() {
		return ctrlLastAction == 1;
	}

	public static boolean isCtrlDown() {
		return ctrlLastAction >= 1;
	}

	/** Checks whether left ALT was pressed. (only once) */
	public static boolean wasLeftAltPressed() {
		return leftAltLastAction == 1;
	}

	public static boolean isLeftAltDown() {
		return leftAltLastAction >= 1;
	}
}
