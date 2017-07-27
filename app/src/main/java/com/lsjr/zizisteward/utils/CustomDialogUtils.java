package com.lsjr.zizisteward.utils;

import android.content.Context;

import com.lsjr.zizisteward.coustom.CustomProgressDialog;

public class CustomDialogUtils {

    private static CustomProgressDialog customProgressDialog = null;

    public static void startCustomProgressDialog(final Context context, String msg) {
        if (customProgressDialog == null) {
            customProgressDialog = CustomProgressDialog.createDialog(context);
            customProgressDialog.setCanceledOnTouchOutside(false);
            customProgressDialog.setMessage(msg);
        }
        customProgressDialog.show();
    }

    public static void stopCustomProgressDialog(Context context) {
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
            customProgressDialog = null;
        }
    }
}