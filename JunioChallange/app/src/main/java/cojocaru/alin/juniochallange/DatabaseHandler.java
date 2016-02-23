package cojocaru.alin.juniochallange;

/**
 * Created by Alin on 022 22 02 2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "fieldsManager";
    private static final String TABLE_FIELDS = "fields";

    // FIELDS Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_PROTEIN = "protein";
    private static final String KEY_SUGAR = "sugar";
    private static final String KEY_CALORIE = "calorie";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FIELDS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FIELDS + "(" +
                KEY_ID + " TEXT PRIMARY KEY," +
                KEY_NAME + " TEXT NOT NULL," +
                KEY_BRAND + " TEXT NOT NULL," +
                KEY_PROTEIN + " LONG NOT NULL," +
                KEY_SUGAR + " LONG NOT NULL," +
                KEY_CALORIE + " LONG NOT NULL);";
        db.execSQL(CREATE_FIELDS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELDS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new field
    void addField(Field field) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, field.getId());
        values.put(KEY_NAME, field.getName());
        values.put(KEY_BRAND, field.getBrand());
        values.put(KEY_PROTEIN, field.getProteins());
        values.put(KEY_SUGAR, field.getSurgars());
        values.put(KEY_CALORIE, field.getCalories());

        // Inserting Row
        db.insert(TABLE_FIELDS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single field
    Field getField(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FIELDS, new String[]{KEY_ID,
                        KEY_NAME, KEY_BRAND, KEY_PROTEIN, KEY_SUGAR, KEY_CALORIE}, KEY_ID + " LIKE ?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Field field = new Field(cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                Double.parseDouble(cursor.getString(3)),
                Double.parseDouble(cursor.getString(4)),
                Double.parseDouble(cursor.getString(5)));
        return field;
    }

    // Getting All Fields
    public List<Field> getAllField() {
        List<Field> fieldList = new ArrayList<Field>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FIELDS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Field field = new Field();
                field.setId(cursor.getString(0));
                field.setName(cursor.getString(1));
                field.setBrand(cursor.getString(2));
                field.setProteins(Double.parseDouble(cursor.getString(3)));
                field.setSurgars(Double.parseDouble(cursor.getString(4)));
                field.setCalories(Double.parseDouble(cursor.getString(5)));
                fieldList.add(field);
            } while (cursor.moveToNext());
        }
        return fieldList;
    }

    // Updating single field
    public int updateField(Field field) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, field.getName());
        values.put(KEY_BRAND, field.getBrand());
        values.put(KEY_PROTEIN, field.getProteins());
        values.put(KEY_SUGAR, field.getSurgars());
        values.put(KEY_CALORIE, field.getCalories());

        // updating row
        return db.update(TABLE_FIELDS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(field.getId())});
    }

    // Deleting single field
    public void deleteField(Field field) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FIELDS, KEY_ID + " = ?",
                new String[]{String.valueOf(field.getId())});
        db.close();
    }


    // Getting fields Count
    public int getFieldsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FIELDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public boolean itemExist(Field field) {
        String keyId = String.valueOf(field.getId());
        String query = "SELECT * FROM " + TABLE_FIELDS + " WHERE " + KEY_ID + " LIKE \'" + keyId + "\'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return true;
            // record exist
        } else {
            return false;
            //record not exist
        }
    }

}
