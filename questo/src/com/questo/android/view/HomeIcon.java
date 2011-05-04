package com.questo.android.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class HomeIcon extends View {

	public HomeIcon(Context context) {
		super(context);
	}

	public HomeIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HomeIcon(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		if (getBackground() != null) {
			int height = width * getBackground().getIntrinsicHeight() / getBackground().getIntrinsicWidth();
			setMeasuredDimension(width, height);
		}
		else
			setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
	}
}