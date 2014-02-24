package com.android.testapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

public class DatabaseContentProvider extends ContentProvider {

    private SQLiteDatabase db;
    private Context context;

    public static final String DATABASE_NAME = "testapp";
    public static final int DATABASE_VERSION = 1;

    public static final String USERS_TABLE = "users";
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String SECOND_NAME = "secondname";

    public static final String AUTHORITY = "com.android.testapp.DatabaseContentProvider";
    public static final int URI_USERS = 1;
    public static final int URI_USERS_ID = 2;
    public static final Uri USERS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/"
            + USERS_TABLE);
    static final String USERS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "."
            + USERS_TABLE;
    static final String USERS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "."
            + USERS_TABLE;

    private static final UriMatcher uriMatcher;
    static {

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, USERS_TABLE, URI_USERS);
        uriMatcher.addURI(AUTHORITY, USERS_TABLE + "/#", URI_USERS_ID);

    }

    @Override
    public boolean onCreate() {

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        this.context = this.getContext();
        this.db = dbHelper.getWritableDatabase();
        return this.db != null;

    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings2, String s2) {

        switch (uriMatcher.match(uri)) {
            case URI_USERS :
                if (TextUtils.isEmpty(s2)) {
                    s2 = _ID + " ASC";
                }
                break;
            case URI_USERS_ID :
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)) {
                    s = _ID + " = " + id;
                } else {
                    s = s + " AND " + _ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        Cursor cursor = this.db.query(USERS_TABLE, strings, s, strings2, null, null, s2);
        if (this.context != null)
            cursor.setNotificationUri(this.context.getContentResolver(), USERS_CONTENT_URI);
        return cursor;

    }

    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)) {
            case URI_USERS:
                return USERS_CONTENT_TYPE;
            case URI_USERS_ID:
                return USERS_CONTENT_ITEM_TYPE;
        }
        return null;

    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        if (uriMatcher.match(uri) != URI_USERS)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        long rowID = db.insert(USERS_TABLE, null, contentValues);
        Uri resultUri = ContentUris.withAppendedId(USERS_CONTENT_URI, rowID);

        if (this.context != null)
            this.context.getContentResolver().notifyChange(resultUri, null);
        return resultUri;

    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {

        switch (uriMatcher.match(uri)) {
            case URI_USERS :
                break;
            case URI_USERS_ID :
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)) {
                    s = _ID + " = " + id;
                } else {
                    s = s + " AND " + _ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        int cnt = this.db.delete(USERS_TABLE, s, strings);
        if (this.context != null)
            this.context.getContentResolver().notifyChange(uri, null);
        return cnt;

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {

        switch (uriMatcher.match(uri)) {
            case URI_USERS :
                break;
            case URI_USERS_ID :
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)) {
                    s = _ID + " = " + id;
                } else {
                    s = s + " AND " + _ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        int cnt = this.db.update(USERS_TABLE, contentValues, s, strings);
        if (this.context != null)
            this.context.getContentResolver().notifyChange(uri, null);
        return cnt;

    }

    @Override
    @SuppressWarnings("NullableProblems")
    public int bulkInsert(Uri uri, ContentValues[] contentValues) {

        switch(uriMatcher.match(uri)){
            case URI_USERS :
                int numInserted= 0;
                db.beginTransaction();
                try {
                    for (ContentValues value : contentValues){
                        this.insert(uri, value);
                    }
                    db.setTransactionSuccessful();
                    numInserted = contentValues.length;
                } finally {
                    db.endTransaction();
                }
                return numInserted;

            default:
                throw new UnsupportedOperationException("Wrong URI: " + uri);
        }

    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + USERS_TABLE +
                    " (" + _ID + " INTEGER PRIMARY KEY, " + NAME + " TEXT, "
                    + SECOND_NAME + " TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
            onCreate(db);
        }

    }
}
