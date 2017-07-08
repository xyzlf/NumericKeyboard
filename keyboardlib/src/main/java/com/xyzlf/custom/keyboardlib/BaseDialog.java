package com.xyzlf.custom.keyboardlib;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;

public class BaseDialog extends Dialog {

    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

    protected void setDialogSize(int width, int height) {
        if (this.getWindow() != null) {
            WindowManager.LayoutParams lp = this.getWindow().getAttributes();
            lp.width = width;
            if (height > 0) {
                lp.height = height;
            }
            this.getWindow().setAttributes(lp);
        }
    }

    @Override
    public void show() {
        try {
            Context context = getContext();
            if (context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
                    return;
                }
            }
            if (isShowing()) {
                return;
            }
            super.show();
        } catch (Throwable ignore) {
        }
    }

    @Override
    public void dismiss() {
        try {
            Context context = getContext();
            if (context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
                    return;
                }
            }
            if (isShowing()) {
                super.dismiss();
            }
        } catch (Throwable ignore) {
        }
    }
}
