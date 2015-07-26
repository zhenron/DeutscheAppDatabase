package xingke.deutscheappdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xingke on 26/07/2015.
 *
 C:\Users\Xingke\AppData\Local\Android\sdk\platform-tools
 adb shell
 run-as com.your.packagename
 run-as xingke.deutscheappdatabase;
 cp /data/data/com.your.pacakagename/
 cd /data/data/xingke.deutscheappdatabase;

 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "DeutscheAppDatabase";

    // Table Names
    private static final String TABLE_ADJEKTIV = "adjektiven";
    private static final String TABLE_SUBSTANTIV = "substantiv";

    // Common column names
    private static final String KEY_ID = "id";

    // ADJEKTIVEN Table - column names
    private static final String KEY_ADJEKTIV = "adjektiv";

    // SUBSTANTIVEN Table - column nmaes
    private static final String KEY_ARTIKEL = "artikel";
    private static final String KEY_SUBSTANTIV = "substantiv";

    // Table Create Statements
    // Adjektiv table create statement
    private static final String CREATE_TABLE_ADJEKTIV = "CREATE TABLE "
            + TABLE_ADJEKTIV + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ADJEKTIV + " TEXT" + ")";

    // Substantiv table create statement
    private static final String CREATE_TABLE_SUBSTANTIV = "CREATE TABLE "
            + TABLE_SUBSTANTIV + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ARTIKEL + " TEXT,"
            + KEY_SUBSTANTIV + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_ADJEKTIV);
        db.execSQL(CREATE_TABLE_SUBSTANTIV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADJEKTIV);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBSTANTIV);

        // create new tables
        onCreate(db);
    }

    /*
    * Creating an Adjektiv
    */
    public long createAdjektiv(Adjketiv adjketiv){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ADJEKTIV, adjketiv.getAdjektiv());

        // insert row
        long todo_id = db.insert(TABLE_ADJEKTIV, null, values);

        //db.close(); // Closing database connection *ADDED

        return todo_id; //-1 if error, id if success
    }

    /*
    * get single Adjektiv
    */
    public Adjketiv getAdjektiv(long adjektiv_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_ADJEKTIV + " WHERE "
                + KEY_ID + " = " + adjektiv_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Adjketiv adjketiv = new Adjketiv();
        adjketiv.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        adjketiv.setAdjektiv(c.getString(c.getColumnIndex(KEY_ADJEKTIV)));

        return adjketiv;
    }

    /*
    * getting all Adjektiven
    */
    public List<Adjketiv> getAllAdjektiven() {
        List<Adjketiv> adjketiven = new ArrayList<Adjketiv>();
        String selectQuery = "SELECT  * FROM " + TABLE_ADJEKTIV;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Adjketiv adjketiv = new Adjketiv();
                adjketiv.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                adjketiv.setAdjektiv(c.getString(c.getColumnIndex(KEY_ADJEKTIV)));

                // adding to todo list
                adjketiven.add(adjketiv);
            } while (c.moveToNext());
        }

        return adjketiven;
    }

    /*
    * Updating an Adjektiv
    */
    public int updateAdjektiv(Adjketiv adjketiv) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ADJEKTIV, adjketiv.getAdjektiv());

        // Close the database
        //db.close(); //*ADDED

        // updating row
        return db.update(TABLE_ADJEKTIV, values, KEY_ID + " = ?",
                new String[] { String.valueOf(adjketiv.getId()) });
    }

    /*
    * Deleting an Adjektiv
    */
    public void deleteAdjektiv(long adjektiv_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADJEKTIV, KEY_ID + " = ?",
                new String[]{String.valueOf(adjektiv_id)});

        // Close the database
        //db.close(); //*ADDED
    }

    /*
    * Deleting All Adjektiven
    */
    public void deleteAllAdjektiven() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADJEKTIV, null, null);

        // Close the database
        //db.close(); //*ADDED
    }

    /*
    * * getting Adjektiv count
    */
    public int getAdjektivCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ADJEKTIV;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /*
    * Creating a Substantiv
    */
    public long createSubstantiv(Substantiv substantiv){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ARTIKEL, substantiv.getArtikel());
        values.put(KEY_SUBSTANTIV, substantiv.getSubstantiv());

        // insert row
        long substantiv_id = db.insert(TABLE_SUBSTANTIV, null, values);

        return substantiv_id; //-1 if error, id if success
    }

    /*
    * get single Substantiv
    */
    public Substantiv getSubstantiv(long substantiv_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_SUBSTANTIV + " WHERE "
                + KEY_ID + " = " + substantiv_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Substantiv substantiv = new Substantiv();
        substantiv.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        substantiv.setArtikel(c.getString(c.getColumnIndex(KEY_ARTIKEL)));
        substantiv.setSubstantiv(c.getString(c.getColumnIndex(KEY_SUBSTANTIV)));

        return substantiv;
    }

    /*
    * getting all Substantiven
    */
    public List<Substantiv> getAllSunstantiven() {
        List<Substantiv> substantiven = new ArrayList<Substantiv>();
        String selectQuery = "SELECT  * FROM " + TABLE_SUBSTANTIV;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Substantiv substantiv = new Substantiv();
                substantiv.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                substantiv.setArtikel(c.getString(c.getColumnIndex(KEY_ARTIKEL)));
                substantiv.setSubstantiv(c.getString(c.getColumnIndex(KEY_SUBSTANTIV)));

                // adding to substantiven list
                substantiven.add(substantiv);
            } while (c.moveToNext());
        }

        return substantiven;
    }

    /*
    * Updating a Substantiv
    */
    public int updateSubstantiv(Substantiv substantiv) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ARTIKEL, substantiv.getArtikel());
        values.put(KEY_SUBSTANTIV, substantiv.getSubstantiv());

        // updating row
        return db.update(TABLE_SUBSTANTIV, values, KEY_ID + " = ?",
                new String[] { String.valueOf(substantiv.getId()) });
    }

    /*
    * Deleting a Substantiv
    */
    public void deleteSubstantiv(long substantiv_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SUBSTANTIV, KEY_ID + " = ?",
                new String[]{String.valueOf(substantiv_id)});

    }

    /*
    * Deleting All Substantiven
    */
    public void deleteAllSubstantiven() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SUBSTANTIV, null, null);
    }

    /*
    * * getting Substantiv count
    */
    public int getSubstantivCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SUBSTANTIV;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}
