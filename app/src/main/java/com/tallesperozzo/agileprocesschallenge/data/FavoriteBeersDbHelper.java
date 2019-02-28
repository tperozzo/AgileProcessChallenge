package com.tallesperozzo.agileprocesschallenge.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class FavoriteBeersDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favoritebeers.db";

    private static final int DATABASE_VERSION = 1;

    public FavoriteBeersDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_BEERS_TABLE = "CREATE TABLE " + FavoriteBeersContract.FavoriteBeersEntry.TABLE_NAME
                + " (" + FavoriteBeersContract.FavoriteBeersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FavoriteBeersContract.FavoriteBeersEntry.COLUMN_ID_BEER + " TEXT NOT NULL,"
                + FavoriteBeersContract.FavoriteBeersEntry.COLUMN_NAME + " TEXT NOT NULL,"
                + FavoriteBeersContract.FavoriteBeersEntry.COLUMN_TAGLINE + " TEXT,"
                + FavoriteBeersContract.FavoriteBeersEntry.COLUMN_ABV + " TEXT,"
                + FavoriteBeersContract.FavoriteBeersEntry.COLUMN_IMAGE_URL + " TEXT,"
                + FavoriteBeersContract.FavoriteBeersEntry.COLUMN_FIRST_BREWED + " TEXT,"
                + FavoriteBeersContract.FavoriteBeersEntry.COLUMN_CONTRIBUTED_BY + " TEXT);";

        db.execSQL(SQL_CREATE_FAVORITE_BEERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteBeersContract.FavoriteBeersEntry.TABLE_NAME);
        onCreate(db);
    }
}
