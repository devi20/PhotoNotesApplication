package edu.csulb.android.photonotesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by devi on 3/7/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "PhotoNotes";

    private static final String Photos_Table = "Photos";


    private static final String P_Id = "Account_Id";
    private static final String P_Location = "Account_Location";
    private static final String P_Caption = "Account_Caption";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ACCOUNT_TABLE = "CREATE TABLE " + Photos_Table + "("
                + P_Id + " INTEGER PRIMARY KEY AUTOINCREMENT," + P_Location + " TEXT,"
                + P_Caption + " TEXT" + ")";
        db.execSQL(CREATE_ACCOUNT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Photos_Table);
        onCreate(db);
    }

    public boolean addtoList(Data dataset) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        ContentValues values = new ContentValues();
        values.put(P_Id, dataset.id);
        values.put(P_Caption, dataset.caption);
        values.put(P_Location, dataset.location);

        long store_id = db.insert(Photos_Table, null, values);
        db.close(); // Closing database connection

        if(store_id>0)
            return true;

        return false;

    }

    public ArrayList<Data> getList() {
        ArrayList<Data> list = new ArrayList<Data>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String query = "select * from " + Photos_Table + " where 1";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Data dgs = new Data();
                dgs.id = cursor.getString(0);
                dgs.caption = cursor.getString(2);
                dgs.location = cursor.getString(1);
                list.add(dgs);
            }
        } catch (Exception e) {
            System.out.print(e.toString());
        }
        return list;
    }

    public Data getPhoto(String id) {
        Data dgs = new Data();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String query = "select * from " + Photos_Table + " where `" + P_Id + "` = '" + id + "'";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                dgs.id = cursor.getString(0);
                dgs.caption = cursor.getString(2);
                dgs.location = cursor.getString(1);
            }
        } catch (Exception e) {
            System.out.print(e.toString());
        }
        return dgs;
    }


    public boolean deletePhoto(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String query = "delete from " + Photos_Table + " where `" + P_Id + "` = '" + id + "'";
            db.execSQL(query);
            return true;
        } catch (Exception e) {
            System.out.print(e.toString());
        }
        return false;
    }
}
