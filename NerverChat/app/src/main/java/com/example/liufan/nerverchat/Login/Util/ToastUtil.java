package com.example.liufan.nerverchat.Login.Util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by LIUFAN on 2017/4/23.
 */

public class ToastUtil {
    public static void showInfo(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }
}
