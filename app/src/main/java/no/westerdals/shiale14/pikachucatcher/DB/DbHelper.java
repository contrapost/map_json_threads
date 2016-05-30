package no.westerdals.shiale14.pikachucatcher.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alexander on 26.05.2016.
 *
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "pikachu_catcher.db";
    private static final int DB_VERSION = 1;

    public DbHelper(final Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        String createLocationTableSQL =
                "create table location ("
                        + "id integer primary key autoincrement,"
                        + "locationId text,"
                        + "name text,"
                        + "lat real,"
                        + "lng real,"
                        + "isCought boolean);";

        db.execSQL(createLocationTableSQL);

        String createPikachuTableSQL =
                "create table pikachu ("
                        + "id integer primary key autoincrement,"
                        + "_id text,"
                        + "pikachuId text,"
                        + "name text,"
                        + "urlImage text);";

        db.execSQL(createPikachuTableSQL);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        // do nothing
    }

}
