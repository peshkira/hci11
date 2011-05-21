package com.questo.android.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;

public class DisplayHelper {

	private static Float scale;
	
	public static int dpToPixel(int dp, Context context) {
		if(scale == null)
			scale = context.getResources().getDisplayMetrics().density;
		return (int)((float)dp * scale);
	} 
	
	public static BitmapDrawable bitmapWithConstraints(int bitmapResource, Context ctx, int dpConstraintWidthAndHeight, int padding) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;
		Bitmap bitmapOrg = BitmapFactory.decodeResource(ctx.getResources(), bitmapResource, options);

		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();
		int newWidth = dpToPixel(dpConstraintWidthAndHeight, ctx) - 2*dpToPixel(padding, ctx);
		int newHeight = newWidth;
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);
		return new BitmapDrawable(resizedBitmap);
	}
	
}
