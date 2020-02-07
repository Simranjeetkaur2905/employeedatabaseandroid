package com.example.databasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EmployeeActivity extends AppCompatActivity {

    SQLiteDatabase mDataBase;
    List<Employee> employeeList;
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        listView = findViewById(R.id.lvemployees);
        employeeList = new ArrayList<>();

        mDataBase = openOrCreateDatabase(MainActivity.DATABASE_NAME,MODE_PRIVATE,null);
        loadEmployees();

    }

    private void loadEmployees() {
        String sql = "SELECT * FROM employees";
        Cursor cursor = mDataBase.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do {
                employeeList.add(new Employee(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4)
                ));


            }while (cursor.moveToNext());
            cursor.close();
            //show item in a listView
            //we use a custom adapter to show employees

EmployeeAdapter employeeAdapter = new EmployeeAdapter(this, R.layout.list_layout_employee, employeeList, mDataBase);
listView.setAdapter(employeeAdapter);

        }
    }
}
