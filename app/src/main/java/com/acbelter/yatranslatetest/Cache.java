/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest;

import android.content.Context;

import java.io.File;

public class Cache {
    private static File sCacheDir;

    public static void init(Context context) {
        sCacheDir = context.getCacheDir();
    }

    public static File provideCacheDir() {
        return sCacheDir;
    }
}
