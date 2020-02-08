package com.example.databasedemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import javax.microedition.khronos.egl.EGLDisplay;

public class EmployeeAdapter extends ArrayAdapter {

    Context mContext;
    int layoutRes;
    List<Employee> employees;
    //SQLiteDatabase mDatabase;

    DatabaseHelper mDatabase;

    public EmployeeAdapter( Context mContext, int layoutRes, List<Employee> employees, DatabaseHelper mDatabase) {
        super(mContext, layoutRes,employees);
        this.mContext = mContext;
        this.layoutRes = layoutRes;
        this.employees = employees;
        this.mDatabase = mDatabase;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(layoutRes, null);
        TextView tvName = v.findViewById(R.id.tv_name);
        TextView tvSalary = v.findViewById(R.id.tv_salary);
        TextView tvDept = v.findViewById(R.id.tv_department);
        TextView tvJoiningDate = v.findViewById(R.id.tv_joiningdate);

       final Employee employee = employees.get(position);
        tvName.setText(employee.getName());
        tvSalary.setText(String.valueOf(employee.getSalary()));
        tvDept.setText(employee.getDept());
        tvJoiningDate.setText(employee.getJoiningdate());


        v.findViewById(R.id.btn_edit_employee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmployee(employee);
            }
        });

        v.findViewById(R.id.btn_delete_employee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEmployee(employee);

            }
        });
        return v;
    }

    private void deleteEmployee(final Employee employee) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*
                String sql = "DELETE FROM employees WHERE id = ?";
                mDatabase.execSQL(sql,new Integer[]{employee.getId()});

                 */

                if(mDatabase.deleteEmployee(employee.getId()))
                loadEmployees();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    private void updateEmployee(final Employee employee) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.dialog_layout_update_employee, null);
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


        final EditText etname = v.findViewById(R.id.edittextname);
        final EditText etslary = v.findViewById(R.id.edittextsalary);
        final Spinner spinner = v.findViewById(R.id.spinnerdepartment);

        String[] deptarray = mContext.getResources().getStringArray(R.array.departments);
        int position = Arrays.asList(deptarray).indexOf(employee.getDept());

        etname.setText(employee.getName());
        etslary.setText(String.valueOf(employee.getSalary()));
        spinner.setSelection(position);

        v.findViewById(R.id.btn_update_employee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etname.getText().toString().trim();
                String salary = etslary.getText().toString().trim();

                String dept = spinner.getSelectedItem().toString();

                if(name.isEmpty()){
                    etname.setError("name field is empty");
                    etname.requestFocus();
                    return;
                }

                if(salary.isEmpty()){
                    etslary.setError("name field is empty");
                    etslary.requestFocus();
                    return;
                }
/*
                String sql = " UPDATE employees SET name =?,salary =?,department=? WHERE id = ?";
              mDatabase.execSQL(sql,new String[]{ name,salary,dept, String.valueOf(employee.getId())});
                Toast.makeText(mContext, "employee update", Toast.LENGTH_SHORT).show();

 */
if(mDatabase.updateEmployee(employee.getId(), name, dept, Double.parseDouble(salary))){
                    Toast.makeText(mContext, "employee update", Toast.LENGTH_SHORT).show();
                    loadEmployees();
                }
else
    Toast.makeText(mContext, "employee not update", Toast.LENGTH_SHORT).show();

                // loadEmployees();
                alertDialog.dismiss();

            }
        });


    }
    private void loadEmployees() {

/*
        String sql = "SELECT * FROM employees";
        Cursor cursor = mDatabase.rawQuery(sql, null);

 */
        Cursor cursor = mDatabase.getAllEmployees();

        if(cursor.moveToFirst()){
            employees.clear();
            do{
                employees.add(new Employee(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4)
                ));
            }while (cursor.moveToNext());

            cursor.close();
        }
        notifyDataSetChanged();



    }


}
