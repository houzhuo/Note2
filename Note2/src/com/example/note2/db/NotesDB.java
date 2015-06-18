package com.example.note2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotesDB extends SQLiteOpenHelper {

	public NotesDB(Context context) {
		super(context, "notes", null, 1);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "+TABLE_NAME_NOTES+"(" +
				COLUMN_NAME_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
				COLUMN_NAME_NOTE_NAME+" TEXT NOT NULL DEFAULT \"\"," +
				COLUMN_NAME_NOTE_TOPIC+" TEXT NOT NULL DEFAULT \"\"," +
				COLUMN_NAME_NOTE_DATE+" TEXT NOT NULL DEFAULT \"\"" +
				")");
		db.execSQL("CREATE TABLE "+TABLE_NAME_MEDIA+"(" +
				COLUMN_NAME_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
				COLUMN_NAME_MEDIA_PATH+" TEXT NOT NULL DEFAULT \"\"," +
				COLUMN_NAME_MEDIA_TOPIC+" TEXT NOT NULL DEFAULT \"\","+
				COLUMN_NAME_MEDIA_CONTENT+" TEXT NOT NULL DEFAULT \"\","+
				COLUMN_NAME_MEDIA_DATE+" TEXT NOT NULL DEFAULT \"\"," +
				COLUMN_NAME_MEDIA_OWNER_NOTE_ID+" INTEGER NOT NULL DEFAULT 0" +
				")");
		db.execSQL("CREATE TABLE "+TABLE_NAME_DOWNLOAD+"(" +
				COLUMN_NAME_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
				COLUMN_NAME_DOWNLOAD_PATH+" TEXT NOT NULL DEFAULT \"\"," +
				COLUMN_NAME_DOWNLOAD_NAME+" TEXT NOT NULL DEFAULT \"\"" +
				")");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	
	public static final String TABLE_NAME_NOTES = "notes";
	public static final String TABLE_NAME_MEDIA = "media";
	public static final String TABLE_NAME_DOWNLOAD = "download";
	
	public static final String COLUMN_NAME_ID = "_id";
	public static final String COLUMN_NAME_NOTE_NAME = "name";
	public static final String COLUMN_NAME_NOTE_TOPIC = "topic";
	public static final String COLUMN_NAME_NOTE_DATE = "date";
	public static final String COLUMN_NAME_MEDIA_PATH = "path";
	public static final String COLUMN_NAME_MEDIA_TOPIC = "mediatopic";
	public static final String COLUMN_NAME_MEDIA_CONTENT = "real_mediacontent";
	public static final String COLUMN_NAME_MEDIA_DATE = "mediadate";
	public static final String COLUMN_NAME_MEDIA_OWNER_NOTE_ID = "note_id";
	
	public static final String COLUMN_NAME_DOWNLOAD_NAME = "dowload_name";
	public static final String COLUMN_NAME_DOWNLOAD_PATH = "dowload_path";


}
