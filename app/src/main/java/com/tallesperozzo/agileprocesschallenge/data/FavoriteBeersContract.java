package com.tallesperozzo.agileprocesschallenge.data;

import android.net.Uri;
import android.provider.BaseColumns;

@SuppressWarnings("ALL")
public class FavoriteBeersContract {

    public static final String AUTHORITY = "com.tallesperozzo.agileprocesschallenge";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITE_BEERS = "favorite_beers";

    public static final class FavoriteBeersEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_BEERS).build();
        public static final String TABLE_NAME = "favorite_beers";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_ID_BEER = "id_beer";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TAGLINE = "tagline";
        public static final String COLUMN_FIRST_BREWED = "first_brewed";
        public static final String COLUMN_IMAGE_URL = "image_url";
        public static final String COLUMN_ABV = "abv";
        public static final String COLUMN_CONTRIBUTED_BY = "contributed_by";

    }

}
