package com.molinari.androidstructure.data.db.migration;

import java.util.ArrayList;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//definition of database adapter class. used to access and maintain database for application
public class DBAdapter {
	//Adapter Tools
	private final Context Cxt;
	public static final String ACTIVITY_TAG = "DBAdapter";
	
	//DB Tools
	private DataBaseHelper DBHelper;
	private SQLiteDatabase DB;
	
	//DB Details
    private static final String DATABASE_NAME = "Data";
    private static final int DATABASE_VERSION = 5;
    public static final ArrayList<DBTable> TABLELIST = new ArrayList<DBTable>();
    
    //Constructor
	public DBAdapter(Context _Context){
		//Column Holder used to hold and pass columns into the table
		ArrayList<DBColumn> ColumnHolder = new ArrayList<DBColumn>(); 
		
		Cxt = _Context;
		
		//define columns for the entries table
		ColumnHolder.add(new DBColumn("_id", DATA_TYPE.INTEGER, 0, DATABASE_VERSION, "primary key autoincrement"));
		ColumnHolder.add(new DBColumn("Label", DATA_TYPE.TEXT, 1, DATABASE_VERSION, "default 'Label' not null"));
		ColumnHolder.add(new DBColumn("Content", DATA_TYPE.TEXT, 2, DATABASE_VERSION, "default 'No Details' not null"));
		ColumnHolder.add(new DBColumn("CreateDate", DATA_TYPE.TEXT, 3, DATABASE_VERSION, "default '' not null")); //TODO CREATE DEFAULT DATE
		ColumnHolder.add(new DBColumn("EditDate", DATA_TYPE.TEXT, 3, DATABASE_VERSION, "default '' not null")); //TODO CREATE DEFAULT DATE
		ColumnHolder.add(new DBColumn("Hidden", DATA_TYPE.INTEGER, 4, DATABASE_VERSION, "default 0 not null"));
		ColumnHolder.add(new DBColumn("NoteOrder", DATA_TYPE.INTEGER, 5, DATABASE_VERSION, "default 0 not null"));
		ColumnHolder.add(new DBColumn("newColumn", DATA_TYPE.TEXT, 6, DATABASE_VERSION, "defailt 0 not null"));
		
		//create entries table definition in code
		TABLELIST.add(new DBTable("Entries", ColumnHolder, 0, DATABASE_VERSION));
		
		//clear the columns so we can add a new set
		ColumnHolder.removeAll(ColumnHolder);
		
		//define columns for the settings table
		ColumnHolder.add(new DBColumn("_id", DATA_TYPE.INTEGER, 0, DATABASE_VERSION, "primary key autoincrement"));
		ColumnHolder.add(new DBColumn("Label", DATA_TYPE.TEXT, 1, DATABASE_VERSION, "default 'Setting' not null"));
		ColumnHolder.add(new DBColumn("Details", DATA_TYPE.TEXT, 1, DATABASE_VERSION, "default 'Details about Setting' not null"));
		ColumnHolder.add(new DBColumn("Enabled", DATA_TYPE.INTEGER, 1, DATABASE_VERSION, "default 0 not null"));
		
		//create settings table definition in code
		TABLELIST.add(new DBTable("Settings", ColumnHolder, 0, DATABASE_VERSION));
	}
	
	//open
    public DBAdapter open() throws SQLException {
    	DBHelper = new DataBaseHelper(Cxt);
    	   	
		DB = DBHelper.getWritableDatabase();
        return this;
    }
    
    //close
    public void close() {
    	DBHelper.close();
    	DB.close();
    }
	
    //fetchAllNotes
    public Cursor fetchAllNotes(){
    	return DB.query(TABLELIST.get(0).TableName, TABLELIST.get(0).getColumnNameList(DATABASE_VERSION), null, null, null, null, TABLELIST.get(0).Columns.get(0).Col_Name);
    	//return null;
    }
    
    //DATA_TYPE
	public static enum DATA_TYPE {
		INTEGER, TEXT, REAL, BLOB
	}
	
	//DBTable
	public static class DBTable {
		private String TableName;
		private static final String TempExt = "TEMP_";
		public ArrayList<DBColumn>Columns = new ArrayList<DBColumn>();
		private Cursor DBCursor;
		private int LiveVersion;
		private int DieVersion;
		
		//Contructor
		public DBTable(String name, ArrayList<DBColumn>column, int live, int die){
			this.TableName = name;
			this.Columns = column;
			this.LiveVersion = live;
			this.DieVersion = die;
		}
		
		//buildTable
		public void create(SQLiteDatabase db){
			//Only run a build for this table it its versioning allows for it
			if(this.LiveVersion <= DATABASE_VERSION && DATABASE_VERSION <= this.DieVersion){
				//db.execSQL(String.format("CREATE TABLE %s (%s)", this.TableName, buildColumns(DATABASE_VERSION)));
				this.createTable(db, this.TableName, DATABASE_VERSION);
			}
		}
		
		//create table with specific columns based on the version received
		protected void createTable(SQLiteDatabase db, String tableName, int version){
			db.execSQL(String.format("CREATE TABLE %s (%s)", tableName, buildColumns(version)));
		}
		
		//upgradeTable
		public void upgrade(SQLiteDatabase db, int newVersion, int oldVersion){
			if(this.LiveVersion <= newVersion && newVersion <= this.DieVersion){
				//Query the database for values
				this.DBCursor = db.query(this.TableName, getColumnNameList(oldVersion), null, null, null, null, this.Columns.get(0).Col_Name);
				
				//Create the temp table of the old table
				this.createTable(db, (TempExt + this.TableName), oldVersion);
				
				//Load the temp table with the values queried
				this.loadTable(db, this.DBCursor, (TempExt + this.TableName));
				
				//drop original table
				this.dropTable(db, this.TableName);
				
				//Query the database for values
				this.DBCursor = db.query((TempExt + this.TableName), getColumnNameList(newVersion), null, null, null, null, this.Columns.get(0).Col_Name);
				
				//Create the temp table of the old table
				this.createTable(db, this.TableName, newVersion);
				
				//drop temp table now that the data is no longer needed
				this.loadTable(db, this.DBCursor, this.TableName);
				
				//drop temp table
				this.dropTable(db, (TempExt + this.TableName));
			}else{
				//drop the current table 
				this.dropTable(db, this.TableName);
			}
		}

		//loadTable
		protected void loadTable(SQLiteDatabase db, Cursor data, String table) {
			//prep values for reinsertion
			ContentValues oldValues = new ContentValues();
			
			//move to first guarentee
			data.moveToFirst();
			if(!data.isAfterLast()){//check to find empty set
				do{//start looping over the cursor rows
					for(int i = 0; i < this.Columns.size(); i++){//start looping over cursor columns
						if(data.getColumnIndex(this.Columns.get(i).Col_Name) > -1){//check to see if the column exists
							switch (this.Columns.get(i).Col_Type) {//collect the values from the cursor with the correct data get method
								case TEXT:
									oldValues.put(data.getColumnName(i), data.getString(i));
									break;
	
								case INTEGER:
									oldValues.put(data.getColumnName(i), data.getInt(i));
									break;
	
								case REAL:
									oldValues.put(data.getColumnName(i), data.getDouble(i));								
									break;
	
								case BLOB:
									oldValues.put(data.getColumnName(i), data.getBlob(i));
									break;
									
								default:
									break;
							}
						}
					}
					//insert the collected values into the table
					db.insert(table, null, oldValues);
				}while(data.moveToNext());
			}
		}
		
		//dropTable
		protected void dropTable(SQLiteDatabase db, String TableLabel){
			//drop old table
			db.execSQL(String.format("DROP TABLE IF EXISTS %s", TableLabel));
		}
		
		//buildColumns
		protected String buildColumns(int buildVersion){
			String out = "";
			String append = ", ";
			
			//loop that builds the column declarations for the creating the table
			for(int i = 0; i < this.Columns.size(); i++){
				//only put the current column into the list if it is living
				if(this.Columns.get(i).LiveVersion <= buildVersion && buildVersion <= this.Columns.get(i).DieVersion){
					//append a new column declaration to the current declaration string
					out = out + this.Columns.get(i).getDeclaration() + append;
				}
			}
			
			//Return complete string of column declarations without the ending appended character 
			return out.substring(0, (out.length() - append.length()));
		}
		
		//getColumnNameList
		protected String[] getColumnNameList(int buildVersion){
			ArrayList<String> out = new ArrayList<String>();

			//loop that builds the column list collection
			for(int i = 0; i < this.Columns.size(); i++){
				//only put the current column into the list if it is living
				if(this.Columns.get(i).LiveVersion <= buildVersion && buildVersion <= this.Columns.get(i).DieVersion){
					out.add(this.Columns.get(i).Col_Name);
				}
			}
			
			//convert the collection into the expected datatype
			String[] output = new String[out.size()];
			out.toArray(output);
			
			//return the final adjusted data type values
			return output;
		}
	}
	
	//definition class for a column
	
	//DBColumn
	public static class DBColumn {
		public String Col_Name = ""; //Name of the column
		public DATA_TYPE Col_Type = DATA_TYPE.TEXT; //Column Data Type
		public Integer LiveVersion = 0; //When the column begins to exist in the table
		public Integer DieVersion = 0; //When the column no longer exists in the table
		public String Parameters = ""; //Meta parameters for column create declaration
		
		//constructor
		public DBColumn(String Name, DATA_TYPE Type, int Live, int Die, String Extra){
			this.Col_Name = Name;
			this.Col_Type = Type;
			this.LiveVersion = Live;
			this.DieVersion = Die;
			this.Parameters = Extra;
		}
		
		//GetVersion
		public int getVersion(){
			return this.LiveVersion;
		}
		
		//GetDeclaration
		public String getDeclaration(){
			return String.format("%s %s %s", Col_Name, Col_Type.toString().toLowerCase(Locale.getDefault()), Parameters);
		}
	}
	
	//DatabaseHelper::SQLiteOpenHelper
	public static class DataBaseHelper extends SQLiteOpenHelper{
		//Constructor
		public DataBaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		//loadTable
		protected void loadTableTemp(SQLiteDatabase db, String tableLabel){
			ContentValues values = new ContentValues();

			values.put("Label", "First Entry");
			db.insert(tableLabel, null, values);
			
			values.clear();
			values.put("Label", "Second Entry");
			db.insert(tableLabel, null, values);
			
			values.clear();
			values.put("Label", "Third Entry");
			db.insert(tableLabel, null, values);
			
			values.clear();
			values.put("Label", "Fourth Entry");
			db.insert(tableLabel, null, values);
		}
		
		//[version 1]onCreate
		@Override
		public void onCreate(SQLiteDatabase db) {
			//iterate through all the tables specified
			for (DBTable table : TABLELIST) {
				table.create(db);//run the create method for each table
			}
			
			//test data insertion
//			this.loadTableTemp(db, TABLELIST.get(0).TableName); //TODO: remove this for release
		}
		
		//[version >1]onUpgrade
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			//iterate through all the tables specified
			for (DBTable table : TABLELIST) {
				table.upgrade(db, newVersion, oldVersion); // run the update method for each table
			}
		}
	}
}
