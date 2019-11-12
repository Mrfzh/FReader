package com.feng.freader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.feng.freader.constant.Constant;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/11
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "fzh";

    private static final String CREATE_TABLE_HISTORY = "create table " + Constant.TABLE_HISTORY
            + " (" + Constant.TABLE_HISTORY_ID + " integer primary key autoincrement, "
            + Constant.TABLE_HISTORY_WORD + " text)";

    DatabaseHelper(Context context, String name,
                   SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
