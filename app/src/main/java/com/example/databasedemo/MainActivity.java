package com.example.databasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //in order to use database you should give a chnace a name to your database

    public static final String DATABASE_NAME= "myDatabase";
    SQLiteDatabase mDataBase;

    EditText edittextname , edittextsalary;
    Spinner spinnerDept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edittextname = findViewById(R.id.edittextname);
        edittextsalary = findViewById(R.id.edittextsalary);

        spinnerDept = findViewById(R.id.spinnerdepartment);
        findViewById(R.id.btnaddemployee).setOnClickListener(this);
        findViewById(R.id.tvviewemployee).setOnClickListener(this);

        // in order to open or create a database we use the following code

        mDataBase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        createTable();



    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS employees (" +
                "id INTEGER NOT NULL CONSTRAINT employee_pk PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(200) NOT NULL," +
               "department VARCHAR(200) NOT NULL, "+
                "joiningdate  DATETIME NOT NULL," +
                "salary DOUBLE NOT NULL);";
        mDataBase.execSQL(sql);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnaddemployee:
                addEmployee();
                break;
            case R.id.tvviewemployee:
                //start activity to another activity to use the list of employees
                Intent intent = new Intent(MainActivity.this,EmployeeActivity.class);
                startActivity(intent);

                break;

        }

    }

    private void addEmployee() {
        String name = edittextname.getText().toString().trim();
        String salary = edittextsalary.getText().toString().trim();
        String dept = spinnerDept.getSelectedItem().toString();

        //using the calender object to get the current time

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String joiningdate = sdf.format(calendar.getTime());

        if (name.isEmpty()){
            edittextname.setError("name field is empty");
            edittextname.requestFocus();
            return;
        }
        if (salary.isEmpty()){
            edittextsalary.setError("salary field is empty");
            edittextsalary.requestFocus();
            return;
        }
        String sql = "INSERT INTO employees(name, department, joiningdate , salary)" +
                "VALUES (?,?,?,?)";
        mDataBase.execSQL(sql ,new String[]{name,dept,joiningdate,salary});
        Toast.makeText(this, "Employee added", Toast.LENGTH_SHORT).show();

    }
}
