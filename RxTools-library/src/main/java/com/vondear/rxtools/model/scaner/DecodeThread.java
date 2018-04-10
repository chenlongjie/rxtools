package com.vondear.rxtools.model.scaner;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.vondear.rxtools.R;
import com.vondear.rxtools.activity.ActivityScanerCode;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;

/**
 *
 * 描述: 解码线程
 */
final class DecodeThread extends Thread {

	WeakReference<ActivityScanerCode>  activity;
	private Handler handler;
	private final CountDownLatch handlerInitLatch;

	DecodeThread(WeakReference<ActivityScanerCode>  activity) {
		this.activity = activity;
		handlerInitLatch = new CountDownLatch(1);
	}

	Handler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return handler;
	}

	@Override
	public void run() {
		Looper.prepare();
		handler = new DecodeHandler();
		handlerInitLatch.countDown();
		Looper.loop();
	}
	class DecodeHandler extends Handler {
		private boolean running = true;
		private ImageScanner scanner;

		public DecodeHandler() {
			scanner = new ImageScanner();
			scanner.setConfig(0, Config.X_DENSITY, 3);
			scanner.setConfig(0, Config.Y_DENSITY, 3);
		}

		@Override
		public void handleMessage(Message message) {
			if (!running) {
				return;
			}
			if (message.what == R.id.decode) {
				decode((byte[]) message.obj, message.arg1, message.arg2);

			} else if (message.what == R.id.quit) {
				Looper.myLooper().quit();

			}
		}

		private void decode(byte[] data, int width, int height) {
			Image barcode = new Image(width, height, "Y800");
			barcode.setData(data);
			int result = scanner.scanImage(barcode);
			if (activity.get() == null) {
				return;
			}
			Handler handler = activity.get().getHandler();
			String scanValue = null;
			if (result != 0) {
				SymbolSet syms = scanner.getResults();
				if (!syms.isEmpty()) {
					for (Symbol sym : syms) {
						scanValue = sym.getData();
						if (scanValue != null) {
							break;
						}
					}
				}
			}
			// 扫描到数据
			if (scanValue != null) {
				if (handler != null) {
					Message message = Message.obtain(handler,
							R.id.decode_succeeded, scanValue);

					message.sendToTarget();
				}
			} else {
				if (handler != null) {
					Message message = Message.obtain(handler,
							R.id.decode_failed);
					message.sendToTarget();
				}
			}
		}
	}
}
