package com.example.ferdilarahmi.crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ferdila Rahmi on 5/18/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "mahasiswa";
    public static final String TABLE_NAME = "mahasiswa_tabel";
    public static final String COL_1 = "_id";
    public static final String COL_2 = "nim";
    public static final String COL_3 = "name";
    public static final String COL_4 = "foto";

    private SQLiteDatabase db = this.getWritableDatabase();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ( " +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " TEXT, " +
                COL_3 + " TEXT, " +
                COL_4 + " TEXT );";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String nim, String nama){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, nim);
        contentValues.put(COL_3, nama);
        contentValues.put(COL_4, "unknown.jpg");
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getAllData(){
//        Cursor res = db.query(TABLE_NAME, new String[] {COL_1+" as _id", COL_2, COL_3, COL_4}, null, null, null, null, null);
        Cursor res = db.rawQuery("select _id, nim, name from " + TABLE_NAME, null);
        if(res!=null){
            res.moveToFirst();
        }
        return res;
    }

    public void deleteAll()
    {
       /* TRUNCATE SQLITE*/
        db.execSQL("DELETE FROM "+ TABLE_NAME);
//        db.execSQL("VACUUM");
        db.execSQL("delete from sqlite_sequence where name='"+ TABLE_NAME +"'");
    }

    public void deleteById(long id){
        String strFilter = "_id=" + id;
        db.delete(TABLE_NAME, strFilter, null);
    }
    public boolean updateById(long id, String nim, String nama){
        String strFilter = "_id=" + id;
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, nim);
        contentValues.put(COL_3, nama);

        long result = db.update(TABLE_NAME, contentValues, strFilter, null);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
}
