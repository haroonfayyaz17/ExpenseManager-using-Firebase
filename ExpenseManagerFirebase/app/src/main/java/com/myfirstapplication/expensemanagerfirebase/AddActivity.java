package com.myfirstapplication.expensemanagerfirebase;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {

    DBController dbController;
    TextView txtDate1;
    EditText txtAmount;
    public static Spinner spinner;
    DatePicker datePicker;
    Button button;
    public static ArrayAdapter<String> adapter;
    List itemsID;
    public static List itemsString;
    String category;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        dbController = new DBController(this);
        getIDs();
        LoadSpinnerData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, monthOfYear, dayOfMonth);

                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    date = format.format(calendar.getTime());
                    txtDate1.setText(date);
                }
            });
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int Amount = Integer.parseInt(String.valueOf(txtAmount.getText()));
                //uncomment karna hai
                dbController.getCategoryID(category, new ServerCallBack() {
                    @Override
                    public void onStringArraySuccess(boolean result, ArrayList<String> strings) {

                    }

                    @Override
                    public void onSuccess(boolean result, String path) {
                        if (result == true) {
                            int year = datePicker.getYear();
                            int month = datePicker.getMonth();
                            int day = datePicker.getDayOfMonth();

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, month, day);


                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                            date = format.format(calendar.getTime());
                            dbController.insertExpense(Amount, path, date);
                            showMessage("Data inserted");
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onArraySuccess(boolean result, ArrayList<Expense> expenses) {

                    }
                });
            }
        });

    }

    void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    void LoadSpinnerData() {

        dbController.loadCategories(new ServerCallBack() {
            @Override
            public void onSuccess(boolean result, String path) {

            }

            @Override
            public void onArraySuccess(boolean result, ArrayList<Expense> expenses) {

            }

            @Override
            public void onStringArraySuccess(boolean result, ArrayList<String> strings) {

                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, strings);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        });
    }

    void getIDs() {
        txtAmount = findViewById(R.id.txtAmount);
        spinner = findViewById(R.id.spinnerCategory);
        datePicker = findViewById(R.id.datePickDate);
        button = findViewById(R.id.btnSave1);
        txtDate1 = findViewById(R.id.txtDate1);
    }

    void updateExpenseData(Expense expense) {

    }
}
