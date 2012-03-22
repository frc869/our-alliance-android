package com.mechinn.android.myalliance.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @deprecated
 * @author mechinn
 *
 */
public abstract class GeneralDB extends SQLiteOpenHelper {
    public static final String DBName = "myAlliance";
    
    public static final String keyRowID = "_id";
    public static final String keyLastMod = "_lastMod";
    
    protected SQLiteDatabase mDB;

	public GeneralDB(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	public void close() {
		if (mDB != null && mDB.isOpen()) {
			mDB.close();
			mDB = null;
        }
	}
	
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new SQLiteException("Can't downgrade database from version " +
                oldVersion + " to " + newVersion);
    }
	
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

	public abstract void onCreate(SQLiteDatabase db);
	public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

}