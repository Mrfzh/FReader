package com.feng.freader.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.feng.freader.app.App;
import com.feng.freader.constant.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/11
 */
public class DatabaseManager {
    private static final String TAG = "DatabaseManager";

    private static DatabaseManager mManager;
    private SQLiteDatabase mDb;

    private DatabaseManager() {
        SQLiteOpenHelper helper = new DatabaseHelper(
                App.getContext(), Constant.DB_NAME, null, 1);
        mDb = helper.getWritableDatabase();
    }

    public static DatabaseManager getInstance() {
        if (mManager == null) {
            mManager = new DatabaseManager();
        }
        return mManager;
    }

    /**
     * 插入一条新的历史记录
     */
    public void insertHistory(String content) {
        ContentValues values = new ContentValues();
        values.put(Constant.TABLE_HISTORY_WORD, content);
        mDb.insert(Constant.TABLE_HISTORY, null, values);
    }

    /**
     * 删除一条历史记录
     */
    public void deleteHistory(String content) {
        mDb.delete(Constant.TABLE_HISTORY,
                Constant.TABLE_HISTORY_WORD + " = ?", new String[]{content});
    }

    /**
     * 查询所有历史记录（较新的记录排前面）
     */
    public List<String> searchAllHistory() {
        List<String> res = new ArrayList<>();
        Cursor cursor = mDb.query(Constant.TABLE_HISTORY, null, null,
                null, null, null,null);
        if (cursor.moveToLast()) {
            do {
                res.add(cursor.getString(cursor.getColumnIndex(Constant.TABLE_HISTORY_WORD)));
            } while (cursor.moveToPrevious());
        }
        cursor.close();

        return res;
    }

    /**
     * 删除前 n 条历史记录
     */
    public void deleteHistories(int n) {
        String sql = "delete from " + Constant.TABLE_HISTORY +
                " where " + Constant.TABLE_HISTORY_ID + " in(" +
                "select " + Constant.TABLE_HISTORY_ID + " from " + Constant.TABLE_HISTORY +
                " order by " + Constant.TABLE_HISTORY_ID +
                " limit " + n + ")";
        mDb.execSQL(sql);
    }

    /**
     * 删除所有历史记录
     */
    public void deleteAllHistories() {
        mDb.delete(Constant.TABLE_HISTORY, null, null);
    }
}
