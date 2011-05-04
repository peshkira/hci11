package com.questo.android.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class HomeIcon extends View {

	//private final Drawable icon;

	public HomeIcon(Context context) {
		super(context);
		/*this.icon = icon;
		setBackgroundDrawable(icon);*/
	}

	public HomeIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		/*this.icon = icon;
		setBackgroundDrawable(icon);*/
	}

	public HomeIcon(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		/*this.icon = icon;
		setBackgroundDrawable(icon);*/
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = width * getBackground().getIntrinsicHeight() / getBackground().getIntrinsicWidth();
		setMeasuredDimension(width, height);
	}
}