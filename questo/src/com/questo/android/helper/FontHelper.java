package com.questo.android.helper;

import android.content.Context;
import android.graphics.Typeface;

public class FontHelper {

	private static Typeface medievalFont;
	private static final Object lock = new Object();

	public static Typeface getMedievalFont(Context context) {
		synchronized (lock) {
			if (medievalFont == null)
				medievalFont = Typeface.createFromAsset(context.getAssets(), "fonts/Cimbrian.ttf");
		}
		return medievalFont;
	}
}
