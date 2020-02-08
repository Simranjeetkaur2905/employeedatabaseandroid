package com.example.databasedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    //using contraints for column names

    //another way to persist data using build in function instead of using queries

    private static final String DATABASE_NAME = "EmployeeDatabase";
    private static final int DATABASE_VERSION =1;
    private static final String TABLE_NAME = "employees";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DEPT = "department";
    private static final String COLUMN_JOIN_DATE = "joiningdate";
    private static final String COLUMN_SALARY = "salary";

    public DatabaseHelper(@Nullable Context context) {
        //cursor factory is when you are using your own custom cursor

        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID +" INTEGER NOT NULL CONSTRAINT employee_pk PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " varchar(200) NOT NULL," +
                COLUMN_DEPT + " varchar(200) NOT NULL," +
                COLUMN_JOIN_DATE + " varchar(200) NOT NULL," +
                COLUMN_SALARY + " double NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // WE ARE JUST DROPPING THE TABLE AND RECREATE IT

        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(sql);
        onCreate(db);


    }

    boolean addEmployee(String name,String dept ,String joiningdate , double salary){

        //INORDER OT INSERT ITEM INTO DATABAASE
        //WE NEED A WRITABLE DATABASE
        //THIS METHOS RETURN A SQL DATABASE
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        //WE NEED TO DEFINE A CONTENT INSTANCE

        ContentValues cv = new ContentValues();

        //THE FIRST ARGUMENT OF THE PUT METHOD IS THE COLUMN NAME AND THE SECOND VALUE IS THE SHOWN AS BELOW

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_DEPT, dept);
        cv.put(COLUMN_JOIN_DATE, joiningdate);
        cv.put(COLUMN_SALARY, String.valueOf(salary));

        //insert method returns row number if the inseriton is successfully and -1 if the unsuccessfull

      return   sqLiteDatabase.insert(TABLE_NAME, null, cv) != -1;


    }

    Cursor getAllEmployees(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    boolean updateEmployee(int id,String name,String dept,double salary){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_DEPT, dept);
        cv.put(COLUMN_SALARY, String.valueOf(salary));

        //this method returns the number of rows effected

        return sqLiteDatabase.update(TABLE_NAME, cv, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) > 0;

    }

    boolean deleteEmployee(int id){
        SQLiteDatabase sqLiteDatabase  = getWritableDatabase();

        //the delete method returns the  number of rows effected
        return sqLiteDatabase.delete(TABLE_NAME, COLUMN_ID +"=?", new String[]{String.valueOf(id)}) > 0;
    }
}
