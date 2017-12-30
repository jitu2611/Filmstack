package org.jitu.filmstack;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by JITU on 10/11/2016.
 */
public class AlarmDatabase extends ContentProvider {
    public static String Providername = "org.jitu.filmstack.droidrush1";
    public static String url = "content://" + Providername + "/alarm";
    public static Uri Contenturi = Uri.parse(url);

    interface alarm {
        String _ID = "_id";
        String AID = "aid";
        String TITLE = "title";

        String ICON = "icon";
        String TIME = "time";
        String DATE = "date" ;


    }

    HashMap<String, String> pro;

    static final int all = 1;
    static final int spe = 2;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Providername, "alarm", all);
        uriMatcher.addURI(Providername, "alarm/#", spe);
    }

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "droidrush1";
    static final String TABLE_NAME = "alarm";
    static final int DATABASE_VERSION = 2;

    static final String CreateTable = "CREATE TABLE "
            + TABLE_NAME
            + "( _id INTEGER PRIMARY KEY AUTOINCREMENT , aid TEXT NOT NULL , title TEXT NOT NULL , icon BLOB NOT NULL , time TEXT NOT NULL , date TEXT NOT NULL ) ;";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CreateTable);
        }

        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub
            int version = arg1;
            if (version == 1) {
                version = 2;

            }
            if (version != DATABASE_VERSION) {
                arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                onCreate(arg0);
            }
        }

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case all:
                count = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case spe:
                String id = uri.getPathSegments().get(1);
                count = db.delete(TABLE_NAME, alarm._ID
                        + " = "
                        + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                        + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long rowID = db.insert(TABLE_NAME, "", values);

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(Contenturi, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        db = dbHelper.getWritableDatabase();
        return (db == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        switch (uriMatcher.match(uri)) {
            case all:
                qb.setProjectionMap(pro);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Cursor c = qb.query(db, projection, selection, selectionArgs, null,
                null, null);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case all:
                count = db.update(TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case spe:
                count = db.update(
                        TABLE_NAME,
                        values,
                        AlarmDatabase.alarm._ID
                                + " = "
                                + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND ("
                                + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
