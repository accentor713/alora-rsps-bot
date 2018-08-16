package org.iyamjeremy.alorarspsbot.api.ui;

import java.lang.reflect.InvocationTargetException;

public class UIUtil {
	
	private static int TRANSLATION_X = 0;
	private static int TRANSLATION_Y = 0;
	
	public static void fillRect(int x, int y, int width, int height, int color, int opacity) {
		try {
			Class.forName("NS").getDeclaredMethod("fillRect", new Class<?>[]{int.class, int.class, int.class, int.class, int.class, int.class}).invoke(null, new Object[]{TRANSLATION_X+x, TRANSLATION_Y+y, width, height, color, opacity});
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void drawText(String s, int x, int y) {
		try {
			Class.forName("NS").getDeclaredMethod("drawText", new Class<?>[]{String.class, int.class, int.class}).invoke(null, new Object[]{s, TRANSLATION_X+x, TRANSLATION_Y+y});
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void translateBy(int x, int y) {
		TRANSLATION_X += x;
		TRANSLATION_Y += y;
	}

}
