package com.tallesperozzo.agileprocesschallenge.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Objects;

import static com.tallesperozzo.agileprocesschallenge.data.FavoriteBeersContract.FavoriteBeersEntry.TABLE_NAME;

public class FavoriteBeersContentProvider extends ContentProvider {

    private static final int FAVORITE_BEERS = 100;
    private static final int FAVORITE_BEERS_WITH_ID = 101;

    private FavoriteBeersDbHelper favoriteBeersDbHelper;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavoriteBeersContract.AUTHORITY, FavoriteBeersContract.PATH_FAVORITE_BEERS, FAVORITE_BEERS);
        uriMatcher.addURI(FavoriteBeersContract.AUTHORITY, FavoriteBeersContract.PATH_FAVORITE_BEERS + "/#", FAVORITE_BEERS_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        favoriteBeersDbHelper = new FavoriteBeersDbHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = favoriteBeersDbHelper.getReadableDatabase();

        int match =  uriMatcher.match(uri);

        Cursor retCursor;

        switch (match){
            case FAVORITE_BEERS:
                retCursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case FAVORITE_BEERS_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "id_beer";
                String[] mSelectionArgs = new String[]{id};
                retCursor = db.query(TABLE_NAME, projection, mSelection, mSelectionArgs, null, null, sortOrder);

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = favoriteBeersDbHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        Uri returnUri = null;

        switch (match){
            case FAVORITE_BEERS:

                long id = db.insert(TABLE_NAME, null, values);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(FavoriteBeersContract.FavoriteBeersEntry.CONTENT_URI, id);
                }
                else{
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }

                break;
            case FAVORITE_BEERS_WITH_ID:
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = favoriteBeersDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);

        int beersDeleted; // starts as 0

        switch (match) {
            case FAVORITE_BEERS_WITH_ID:
                String id = uri.getPathSegments().get(1);
                beersDeleted = db.delete(TABLE_NAME, "id_beer=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (beersDeleted != 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }

        return beersDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
