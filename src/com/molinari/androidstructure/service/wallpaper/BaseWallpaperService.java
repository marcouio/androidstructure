package com.molinari.androidstructure.service.wallpaper;

import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public abstract class BaseWallpaperService extends WallpaperService {

	// variabile locale dell'engine per capire se il wallpaper è visibile o meno
	private boolean mVisible;

	// handler che ci consentirà di interagire con l'interfaccia grafica
	private Handler mHandler = new Handler();
	
	private Engine customEngine = null;

	@Override
	public Engine onCreateEngine() {
		return getEngine();
	}

	protected Engine getEngine() {
		if(customEngine == null){
			customEngine = new CustomEngine();
		}
		return customEngine;
	}

	protected abstract void draw();

	private class CustomEngine extends Engine {

		// runnable da mandare all'handler per disegnare sull'interfaccia grafica
		private final Runnable mDraw = new Runnable() {
			public void run() {
				drawFrame();
			}
		};

		public void onDestroy() {
			super.onDestroy();
			mHandler.removeCallbacks(mDraw);
			// Se c'è qualcosa da eliminare quando distruggo l'engine..
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			// la visibilità è cambiata, aggiorno la variabile locale
			mVisible = visible;
			if (visible) {
				drawFrame();
			} else {
				mHandler.removeCallbacks(mDraw);
			}
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			// chiamato quando la surface cambia dimensione, probabilmente
			// quando cambia la orientation anche
			super.onSurfaceChanged(holder, format, width, height);
			drawFrame();
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			// non è più visibile, quindi non vogliamo più disegnare per non
			// sprecare risorse
			mVisible = false;
			mHandler.removeCallbacks(mDraw);
		}

		protected void drawFrame() {
			draw();

			// Rischedula il prossimo redraw , sempre se lo sfondo è visibile
			mHandler.removeCallbacks(mDraw);
			if (mVisible) {
				mHandler.postDelayed(mDraw, 1000/25); //25 fps
			}
		}
	}
	
	public boolean isVisible() {
		return mVisible;
	}

	public void setVisible(boolean mVisible) {
		this.mVisible = mVisible;
	}

	public Handler getHandler() {
		return mHandler;
	}

	public void setHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}
}
