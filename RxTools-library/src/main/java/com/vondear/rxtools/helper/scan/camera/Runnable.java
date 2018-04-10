package com.vondear.rxtools.helper.scan.camera;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

/**
 * 兼容低版本的子线程开启任务
 */
public class Runnable {

    @SuppressLint("NewApi")
    @SuppressWarnings("unchecked")
    public static void execAsync(AsyncTask<?, ?, ?> task) {
//		if (Build.VERSION.SDK_INT >= 11) {
//			task.executeOnExecutor(Executors.newCachedThreadPool());
//		}
//		else {
//			task.execute(null);
//		}

    }

}
