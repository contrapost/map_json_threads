package no.westerdals.shiale14.pikachucatcher.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 27.05.2016.
 *
 */
public class PikachuDataSource {

    private SQLiteDatabase db;
    private DbHelper dbHelper;

    public PikachuDataSource(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void savePikachu(Pikachu pikachu) {
        ContentValues contentValues = new ContentValues();
        // table -> value
        contentValues.put("_id", pikachu.get_id());
        contentValues.put("pikachuId", pikachu.getPikachuId());
        contentValues.put("name", pikachu.getName());
        contentValues.put("urlImage", pikachu.getImageUrl());

        db.insert("pikachu", null, contentValues);
    }

    public List<Pikachu> getPikachus() {
        List<Pikachu> pikachus = new ArrayList<>();

        String[] columns = { "id", "_id", "pikachuId", "name", "urlImage" };

        Cursor cursor = db.query("pikachu", columns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            pikachus.add(pikachuFromCursor(cursor));
            cursor.moveToNext();
        }

        return pikachus;
    }

    private Pikachu pikachuFromCursor(final Cursor cursor) {
        Pikachu pikachu = new Pikachu();

        pikachu.setId(cursor.getInt(0));
        pikachu.set_id(cursor.getString(1));
        pikachu.setPikachuId(cursor.getString(2));
        pikachu.setName(cursor.getString(3));
        pikachu.setImageUrl(cursor.getString(4));

        return pikachu;
    }

}
