package com.molinari.androidstructure.data.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class DatabaseAndroid extends SQLiteOpenHelper implements IDatabaseAndroid{
	
	private int databaseVersion = 1;

	public DatabaseAndroid(final Context context, final String name, final CursorFactory cursorFactory , final int version) {
		super(context, name, cursorFactory, version);
		setDatabaseVersion(version);
	}
	
	public DatabaseAndroid(final Context context, final String name, final int version) {
		super(context, name, null, version);
		setDatabaseVersion(version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			generaDB(db);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public abstract String[] queryUpdateDb();
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String[] sql = queryUpdateDb();
		if(sql != null){
			db.beginTransaction();
			try{
				execMultipleSQL(db, sql);
                db.setTransactionSuccessful();
                
			}catch (android.database.SQLException e) {
				Log.e("Error updating tables and debug data", e.toString());
                throw e;
			}finally{
				db.endTransaction();
			}
		}
		//db.close();
	}
	
	public abstract String[] queryCreateDb();
	
	private void execMultipleSQL(SQLiteDatabase db, String[] sql) {

        for(String s : sql){

            if(s.trim().length() > 0){

                db.execSQL(s);
            }
        }

    }
	
	public void generaDB(final SQLiteDatabase db) throws SQLException {
		final String[] sql = queryCreateDb();
		if(sql != null){
			db.beginTransaction();
			try{
				execMultipleSQL(db, sql);
                db.setTransactionSuccessful();
                
			}catch (android.database.SQLException e) {
				Log.e("Error creating tables and debug data", e.toString());
                throw e;
			}finally{
				db.endTransaction();
			}
		}
		
		//db.close();
	}

	public void setDatabaseVersion(int newVersion){
		databaseVersion = newVersion;
	}
	public int getDatabaseVersion() {
		return databaseVersion;
	}
	
}
