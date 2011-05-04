package com.questo.android.helper;

import android.content.Context;

public class DisplayHelper {

	private static Float scale;
	
	public static int dpToPixel(int dp, Context context) {
		if(scale == null)
			scale = context.getResources().getDisplayMetrics().density;
		return (int)((float)dp * scale);
	} 
	
}
