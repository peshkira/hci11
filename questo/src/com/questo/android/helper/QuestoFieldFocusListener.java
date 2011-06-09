package com.questo.android.helper;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;

public class QuestoFieldFocusListener implements OnTouchListener {

    private InputMethodManager imm;

    public QuestoFieldFocusListener(InputMethodManager imm) {
        this.imm = imm;
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        return false;
    }

}
