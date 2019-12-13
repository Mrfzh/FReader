package com.feng.freader.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.feng.freader.app.App;
import com.feng.freader.constant.Constant;
import com.feng.freader.entity.data.BookshelfNovelDbData;

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
    public List<String> queryAllHistory() {
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

    /**
     * 插入一条书架书籍数据
     */
    public void insertBookshelfNovel(BookshelfNovelDbData dbData) {
        ContentValues values = new ContentValues();
        values.put(Constant.TABLE_BOOKSHELF_NOVEL_NOVEL_URL, dbData.getNovelUrl());
        values.put(Constant.TABLE_BOOKSHELF_NOVEL_NAME, dbData.getName());
        values.put(Constant.TABLE_BOOKSHELF_NOVEL_COVER, dbData.getCover());
        values.put(Constant.TABLE_BOOKSHELF_NOVEL_CHAPTER_INDEX, dbData.getChapterIndex());
        values.put(Constant.TABLE_BOOKSHELF_NOVEL_POSITION, dbData.getPosition());
        values.put(Constant.TABLE_BOOKSHELF_NOVEL_TYPE, dbData.getType());
        values.put(Constant.TABLE_BOOKSHELF_NOVEL_SECOND_POSITION, dbData.getSecondPosition());
        mDb.insert(Constant.TABLE_BOOKSHELF_NOVEL, null, values);
    }

    /**
     * 查询所有书架书籍信息
     */
    public List<BookshelfNovelDbData> queryAllBookshelfNovel() {
        // 查询表中所有数据
        Cursor cursor = mDb.query(Constant.TABLE_BOOKSHELF_NOVEL, null, null, null,
                null, null ,null);
        List<BookshelfNovelDbData> res = new ArrayList<>();
        if (cursor.moveToLast()) {
            do {
                String novelUrl = cursor.getString(
                        cursor.getColumnIndex(Constant.TABLE_BOOKSHELF_NOVEL_NOVEL_URL));
                String name = cursor.getString(
                        cursor.getColumnIndex(Constant.TABLE_BOOKSHELF_NOVEL_NAME));
                String cover = cursor.getString(
                        cursor.getColumnIndex(Constant.TABLE_BOOKSHELF_NOVEL_COVER));
                int chapterIndex = cursor.getInt(
                        cursor.getColumnIndex(Constant.TABLE_BOOKSHELF_NOVEL_CHAPTER_INDEX));
                int position = cursor.getInt(
                        cursor.getColumnIndex(Constant.TABLE_BOOKSHELF_NOVEL_POSITION));
                int type = cursor.getInt(
                        cursor.getColumnIndex(Constant.TABLE_BOOKSHELF_NOVEL_TYPE));
                int secondPosition = cursor.getInt(
                        cursor.getColumnIndex(Constant.TABLE_BOOKSHELF_NOVEL_SECOND_POSITION));
                res.add(new BookshelfNovelDbData(novelUrl, name, cover,
                        chapterIndex, position, type, secondPosition));
            } while (cursor.moveToPrevious());
        }
        cursor.close();

        return res;
    }

    /**
     * 根据小说 url 删除一条书架书籍数据集
     */
    public void deleteBookshelfNovel(String novelUrl) {
        mDb.delete(Constant.TABLE_BOOKSHELF_NOVEL,
                Constant.TABLE_BOOKSHELF_NOVEL_NOVEL_URL + " = ?",
                new String[]{novelUrl});
    }

    /**
     * 查询 Bookshelf 表是否存在主键为 novelUrl 的记录
     */
    public boolean isExistInBookshelfNovel(String novelUrl) {
        Cursor cursor = mDb.query(Constant.TABLE_BOOKSHELF_NOVEL, null,
                Constant.TABLE_BOOKSHELF_NOVEL_NOVEL_URL+ " = ?", new String[]{novelUrl},
                null,null, null ,null);
        boolean res = false;
        if (cursor.moveToLast()) {
            do {
                res = true;
            } while (cursor.moveToPrevious());
        }
        cursor.close();

        return res;
    }
}
