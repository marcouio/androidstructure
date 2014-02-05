package com.molinari.androidstructure.hardware.camera;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * The PreviewCamera class is the bulk of the recipe. It handle the Surface
 * where the pixels are drawn and the Camera object.
 * 
 * We define a ClickListener in the constructor so the user can take a picture
 * by just tapping once on the screen. Once we get the notification of the click
 * we take a picture passing as parameters four (all optional) callbacks.
 * 
 * The PreviewCamera class implement the SurfaceHolder.Callback interface in
 * order to be notified when underlying surface is created, changed and
 * destroyed. We'll use these callbacks to properly handle the Camera object.
 * 
 */
public class PreviewCamera extends SurfaceView implements
		SurfaceHolder.Callback, PictureCallback {

	private SurfaceHolder mHolder;
	private Camera mCamera;
	private RawCallback mRawCallback;

	/**
	 * @param context
	 */
	public PreviewCamera(Context context) {
		super(context);

		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mRawCallback = new RawCallback();

		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCamera.takePicture(mRawCallback, mRawCallback, null,
						PreviewCamera.this);
			}
		});
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setPreviewSize(width, height);
		mCamera.setParameters(parameters);

		mCamera.startPreview();

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mCamera = Camera.open();

		configure(mCamera);

		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException exception) {
			closeCamera();
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	/**
	 * Finally we implement the ShutterCallback and again PictureCallback to receive 
	 * the uncompressed raw image data.
	 *
	 */
	class RawCallback implements ShutterCallback, PictureCallback {

		@Override
		public void onShutter() {
			// notify the user, normally with a sound, that the picture has
			// been taken
		}

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// manipulate uncompressed image data
		}
	}

	/**
	 * As soon as the camera is created we call configure in order to set the
	 * parameters the camera will use to take a picture. Things like flash mode,
	 * effects, picture format, picture size, scene mode and so on. Since not
	 * all devices support all kind of features always ask which features are
	 * supported before setting them.
	 * 
	 * @param camera
	 */
	private void configure(Camera camera) {
		Camera.Parameters params = camera.getParameters();

		// Configure image format. RGB_565 is the most common format.
		List<Integer> formats = params.getSupportedPictureFormats();
		if (formats.contains(PixelFormat.RGB_565))
			params.setPictureFormat(PixelFormat.RGB_565);
		else
			params.setPictureFormat(PixelFormat.JPEG);

		// Choose the biggest picture size supported by the hardware
		List<Size> sizes = params.getSupportedPictureSizes();
		Camera.Size size = sizes.get(sizes.size() - 1);
		params.setPictureSize(size.width, size.height);

		List<String> flashModes = params.getSupportedFlashModes();
		if (flashModes.size() > 0)
			params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);

		// Action mode take pictures of fast moving objects
		List<String> sceneModes = params.getSupportedSceneModes();
		if (sceneModes.contains(Camera.Parameters.SCENE_MODE_ACTION))
			params.setSceneMode(Camera.Parameters.SCENE_MODE_ACTION);
		else
			params.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);

		// if you choose FOCUS_MODE_AUTO remember to call autoFocus() on
		// the Camera object before taking a picture
		params.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);

		camera.setParameters(params);
	}

	/**
	 * When the surface is destroyed we close the camera and free its resources
	 */
	private void closeCamera() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

}
