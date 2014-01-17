package com.molinari.androidstructure.data.db;

import java.sql.SQLException;

import android.database.sqlite.SQLiteDatabase;

public interface IDatabaseAndroid {

	public abstract String[] queryUpdateDb();
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
	
	public abstract String[] queryCreateDb();
	
	public void generaDB(final SQLiteDatabase db) throws SQLException;

	public SQLiteDatabase getWritableDatabase();
	
	public SQLiteDatabase getReadableDatabase();
	
}
