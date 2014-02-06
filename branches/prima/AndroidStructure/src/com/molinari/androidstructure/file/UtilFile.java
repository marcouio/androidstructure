package com.molinari.androidstructure.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.util.Log;

public class UtilFile {

	/**
	 * Very simple code to copy a picture from the application's
	 * resource into the external file.  Note that this code does
     * no error checking, and assumes the picture is small (does not
	 * try to copy it in chunks).  Note that if external storage is
     * not currently mounted this will silently fail.
	 * 
	 * @param context
	 * @param data
	 */
	public static void saveFile(Context context, byte[] data, String fileName) {
		File file = new File(context.getExternalFilesDir(null), fileName);
		try {
	        OutputStream os = new FileOutputStream(file);
	        os.write(data);
	        os.close();
	    } catch (IOException e) {
	        // Unable to create file, likely because external storage is
	        // not currently mounted.
	        Log.w("ExternalStorage", "Error writing " + file, e);
	    }
	}
	
	public static void getFile(Context context, String fileType){
		final String absolutePath = context.getExternalFilesDir(fileType).getAbsolutePath();
		
	}
	
	/**
	 * Create a path where we will place our file in the user's
	 * public files directory and check if the file exists.  If
	 * external storage is not currently mounted this will think the
	 * file doesn't exist.
	 * 
	 * @param context
	 * @param fileType
	 * @param fileName
	 * @return
	 */
	public static boolean hasExternalStoragePrivateFile(Context context, String fileType, String fileName) {

	    File path = context.getExternalFilesDir(fileType);
	    if (path != null) {
	        File file = new File(path, fileName);
	        return file.exists();
	    }
	    return false;
	}
	
	/**
	 * Create a path where we will place our file in the user's
	 * public file directory and delete the file.  If external
     * storage is not currently mounted this will fail.
     * 
	 * @param context
	 * @param fileType for example Environment.DIRECTORY_PICTURES
	 * @param pictureName
	 */
	public static void deleteExternalStoragePrivateFile(Context context, String fileType, String pictureName) {
	     
	    File path = context.getExternalFilesDir(fileType);
	    if (path != null) {
	        File file = new File(path, pictureName);
	        file.delete();
	    }
	}
}
