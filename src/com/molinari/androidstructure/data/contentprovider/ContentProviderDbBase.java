package com.molinari.androidstructure.data.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.molinari.androidstructure.data.db.DatabaseAndroid;

public abstract class ContentProviderDbBase extends ContentProvider {

	private DatabaseAndroid dbAndroid = null;
	
	private SQLiteDatabase db;
	
	@Override
	public boolean onCreate() {
		Context context = getContext();
		  DatabaseAndroid dbHelper = getDatabaseHelper(context);
	      
		  /**
	       * Create a write able database which will trigger its 
	       * creation if it doesn't already exist.
	       */
	      db = dbHelper.getWritableDatabase();
	      return (db == null)? false:true;
	}
	
	public abstract String getProviderName();
	public abstract String getUrl();
	
	public Uri getContentUri(){
		return Uri.parse(getUrl());
	}
	
	public abstract UriMatcher getUriMatcher();
	
	public abstract String getDatabaseName();
	public abstract int getDatabaseVersion();
	public abstract String[] queryUpdateDbProvider();
	public abstract String[] queryCreateDbProvider();
	
	private DatabaseAndroid getDatabaseHelper(Context context){
		synchronized (dbAndroid) {
			if(dbAndroid == null){
				dbAndroid = new DatabaseAndroid(context, getDatabaseName(), getDatabaseVersion()) {
					
					@Override
					public String[] queryUpdateDb() {
						return queryUpdateDbProvider();
					}
					
					@Override
					public String[] queryCreateDb() {
						return queryCreateDbProvider();
					}
				};
			}
		}
		return dbAndroid;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;

		if(getUriMatcher().match(uri) == -1){
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		count = customDelete(uri, selection, selectionArgs);

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	protected abstract int customDelete(Uri uri, String selection, String[] selectionArgs);
	
	@Override
	public String getType(Uri uri) {
		if(getUriMatcher().match(uri) == -1){
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		return getCustomType(uri);
	}

	protected abstract String getCustomType(Uri uri);

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		  /**
	       * Add a new record
	       */
	      long rowID = customInsert(uri, values);
	      /** 
	       * If record is added successfully
	       */
	      if (rowID > 0)
	      {
	         Uri _uri = ContentUris.withAppendedId(getContentUri(), rowID);
	         getContext().getContentResolver().notifyChange(_uri, null);
	         return _uri;
	      }
	      throw new SQLException("Failed to add a record into " + uri);
	}

	protected abstract long customInsert(Uri uri, ContentValues values);

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		if(getUriMatcher().match(uri) == -1){
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		Cursor c = customQuery(uri, projection, selection, selectionArgs, sortOrder);
		/** 
		 * register to watch a content URI for changes
		 */
		c.setNotificationUri(getContext().getContentResolver(), uri);

		return c;
	}

	protected abstract Cursor customQuery(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder);

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int count = 0;

		if(getUriMatcher().match(uri) == -1){
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		count = customUpdate(uri, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	protected abstract int customUpdate(Uri uri, ContentValues values, String selection, String[] selectionArgs);
}
