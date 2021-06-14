package com.example.hw3_sql;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class MyContentProvider extends ContentProvider {

    public static final String DBNAME = "TickersDatabase";

    protected static final class MainDatabaseHelper extends SQLiteOpenHelper {

        MainDatabaseHelper(Context context)
        {
            super(context, DBNAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(SQL_CREATE_MAIN);
        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
        {}

        public void clearDatabase()
        {
            String clearDBQuery = "DELETE FROM "+ TICKERS_TABLE;
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(clearDBQuery);
        }

        public boolean checkConfirmationCodeExists(String confCode)
        {
            SQLiteDatabase db = getWritableDatabase();
            String query = "SELECT " + COL_CONFCODE + " FROM " + TICKERS_TABLE + " WHERE " + COL_CONFCODE + " =?";
            Cursor cursor = db.rawQuery(query, new String[]{confCode});
            if (cursor.getCount() > 0)
            {
                return false;
            }
            return true;
        }


        public ArrayList<Tickers> getData()
        {
            SQLiteDatabase db = getWritableDatabase();
            String query = "SELECT * FROM " + TICKERS_TABLE;
            Cursor cursor = db.rawQuery(query, null);
            ArrayList<Tickers> tickers = new ArrayList<Tickers>();
            while (cursor.moveToNext())
            {
                Tickers ticker = new Tickers();
                ticker.setTickerName(cursor.getString(1));
                ticker.setCompanyName(cursor.getString(2));
                ticker.setTransType(cursor.getString(3));
                ticker.setTransactionAmount(cursor.getString(5));
                ticker.setPPS(cursor.getString(4));
                ticker.setOrderType(cursor.getString(6));
                ticker.setConfirmationCode(cursor.getString(7));
                tickers.add(ticker);
            }
            cursor.close();
            db.close();
            return tickers;
        }


    }


    // defining authority so that other application can access it
    static final String AUTHORITY = "com.hw3.cp";

    public static final String TICKERS_TABLE = "Tickers"; // table name of Database

    public static final String COL_TICKER = "ticker";
    public static final String COL_COMPANY = "companyname";
    public static final String COL_TRANSTYPE = "transactiontype";
    public static final String COL_PPS = "pricepershare";
    public static final String COL_TRANSAMOUNT = "transactionamount";
    public static final String COL_ORDERTYPE = "ordertype";
    public static final String COL_CONFCODE = "confcode";

    // defining content URI
    // content://com.hw3.cp/Tickers
    // table called Tickers
    static final String URL = "content://" + AUTHORITY + "/" + TICKERS_TABLE;

    // parsing the content URI
    static final Uri CONTENT_URI = Uri.parse(URL);

    private MainDatabaseHelper mOpenHelper;

    private static final String SQL_CREATE_MAIN = " CREATE TABLE " +
            TICKERS_TABLE +
            "(" +
            " _ID INTEGER PRIMARY KEY, " +
            COL_TICKER +
            " TEXT," +
            COL_COMPANY +
            " TEXT," +
            COL_TRANSTYPE +
            " TEXT," +
            COL_PPS +
            " TEXT," +
            COL_TRANSAMOUNT +
            " TEXT," +
            COL_ORDERTYPE +
            " TEXT, " +
            COL_CONFCODE +
            " TEXT)";
    public static final int TICKERS = 1;
    public static final int TICKERS_ID = 2;
    
    static final UriMatcher uriMatcher;



    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, TICKERS_TABLE, TICKERS);
        uriMatcher.addURI(AUTHORITY, TICKERS_TABLE + "/*", TICKERS_ID);
    }


    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.

        return mOpenHelper.getWritableDatabase().delete(TICKERS_TABLE, selection, selectionArgs);

    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (uriMatcher.match(uri))
        {
            case TICKERS:
                return "vnd.android.cursor.dir/Tickers";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.

        String tempTicker = values.getAsString(COL_TICKER).trim();
        String tempCompany = values.getAsString(COL_COMPANY).trim();
        String tempPPS = values.getAsString(COL_PPS).trim();
        String tempTA = values.getAsString(COL_TRANSAMOUNT).trim();
        String tempOrder = values.getAsString(COL_ORDERTYPE).trim();
        String tempCode = values.getAsString(COL_CONFCODE).trim();

        if (tempTicker.equals("") || tempCompany.equals("") || tempPPS.equals("")
        || tempTA.equals("") || tempOrder.equals("") || tempCode.equals(""))
        {
            Toast.makeText(getContext(), "Error in CP Insert", Toast.LENGTH_SHORT).show();
            return null;
        }

        long id = mOpenHelper.getWritableDatabase().insert(TICKERS_TABLE, null, values);
        return Uri.withAppendedPath(CONTENT_URI, "" + id);

    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.

        mOpenHelper = new MainDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.

        return mOpenHelper.getReadableDatabase().query(TICKERS_TABLE, projection, selection, selectionArgs, null, null, sortOrder);

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        return mOpenHelper.getWritableDatabase().update(TICKERS_TABLE, values, selection, selectionArgs);

    }



}
