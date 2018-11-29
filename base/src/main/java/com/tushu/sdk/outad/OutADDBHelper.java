package com.tushu.sdk.outad;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 通知栏数据库管理
 */
public class OutADDBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase sdb;

    public OutADDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public OutADDBHelper(Context context) {
        super(context, "outad.db3", null, 1);
        sdb = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table outadshowtype(_id integer PRIMARY KEY AUTOINCREMENT,showtype text)");
        db.execSQL("create table outadshownum(_id integer PRIMARY KEY AUTOINCREMENT,shownum text)");

        db.execSQL("insert into outadshowtype(showtype) values('facebook')");
        db.execSQL("insert into outadshownum(shownum) values('0')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String getShowType() {
        String type = "facebook";
        Cursor cursor = sdb.rawQuery("select * from outadshowtype", new String[]{});
        while (cursor.moveToNext()) {
            type = cursor.getString(cursor.getColumnIndex("showtype"));
        }
        cursor.close();
        return type;
    }

    public void saveShowType(String showType) {
        sdb.execSQL("insert into outadshowtype(showtype) values('" + showType + "')");
    }

    public void deleteShowType(String showType) {
        sdb.execSQL("delete from outadshowtype where showtype = ?", new String[]{showType});
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public String getShowNum() {
        String num = "0";
        Cursor cursor = sdb.rawQuery("select * from outadshownum", new String[]{});
        while (cursor.moveToNext()) {
            int shownumid = cursor.getColumnIndex("shownum");
            num = cursor.getString(shownumid);
        }
        cursor.close();
        return num;
    }

    public void saveShowNum(String shownum) {
        sdb.execSQL("insert into outadshownum(shownum) values('" + shownum + "')");
    }

    public void deleteShowNum(String shownum) {
        sdb.execSQL("delete from outadshownum where shownum = ?", new String[]{shownum});
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void startTransaction() {
        sdb.beginTransaction();
    }

    public void endTransaction() {
        sdb.setTransactionSuccessful();
        sdb.endTransaction();
    }

    public void destroy() {
        sdb.close();
    }
}
