/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import java.io.PrintWriter;

public final class Logger {
    private static final boolean SYSTEM_PRINT = false;
    private Logger() {}

    public static void d(String message) {
        if (MainApplication.DEBUG) {
            if (SYSTEM_PRINT) {
                System.out.println(message);
            }
            Log.d(MainApplication.TAG, message);
        }
    }

    public static void d(Class clazz, String message) {
        if (MainApplication.DEBUG) {
            if (SYSTEM_PRINT) {
                System.out.println(clazz.getSimpleName() + ": " + message);
            }
            Log.d(MainApplication.TAG, clazz.getSimpleName() + ": " + message);
        }
    }

    public static void d(Object... objects) {
        String message = "";
        for (Object o : objects) {
            if (o != null) {
                message += o.toString();
            } else {
                message += "null";
            }
        }

        if (MainApplication.DEBUG) {
            if (SYSTEM_PRINT) {
                System.out.println(message);
            }
            Log.d(MainApplication.TAG, message);
        }
    }

    public static void e(String message) {
        if (SYSTEM_PRINT) {
            System.err.println(message);
        }
        Log.e(MainApplication.TAG, message);
    }

    public static void printStackTrace(Exception e) {
        if (MainApplication.DEBUG) {
            e.printStackTrace();
        }
    }

    public static void toast(Context context, String message) {
        if (MainApplication.DEBUG) {
            Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void dumpFragmentManager(FragmentManager fragmentManager) {
        fragmentManager.dump("", null, new PrintWriter(System.out, true), null);
    }
}
