package com.example.simpledatabase3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper{

	public static final String DB_NAME = "test.db";
	public static final int DB_VERSION = 1; // version for migration
	
	public MyDbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// create table
		db.execSQL("CREATE TABLE messages(_id INTEGER PRIMARY KEY AUTOINCREMENT, body VARCHAR)");
		// 初期レコード
		db.execSQL("INSERT INTO messages(body) VALUES('aaa')");
		db.execSQL("INSERT INTO messages(body) VALUES('bbb')");
		db.execSQL("INSERT INTO messages(body) VALUES('ccc')");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// drop old table
		db.execSQL("DROP TABLE IF EXISTS messages");
		// onCreate
		onCreate(db);
	}
}
