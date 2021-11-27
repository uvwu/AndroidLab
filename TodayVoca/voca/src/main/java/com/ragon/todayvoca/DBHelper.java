package com.ragon.todayvoca;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 8;

    public DBHelper(Context context) {
        super(context, "datadb", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String driverTable="create table tb_drive ("+
                "_id integer primary key autoincrement, " +
                "title)";

        db.execSQL(driverTable);
        db.execSQL("insert into tb_drive (title) values ('안드로이드112')");
        db.execSQL("insert into tb_drive (title) values ('db.zip')");
        db.execSQL("insert into tb_drive (title) values ('하이브리드')");
        db.execSQL("insert into tb_drive (title) values ('이미지1')");
        db.execSQL("insert into tb_drive (title) values ('Part4')");
        db.execSQL("insert into tb_drive (title) values ('Angular')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == DATABASE_VERSION) {
            db.execSQL("drop table tb_drive");
            onCreate(db);
        }
    }
}