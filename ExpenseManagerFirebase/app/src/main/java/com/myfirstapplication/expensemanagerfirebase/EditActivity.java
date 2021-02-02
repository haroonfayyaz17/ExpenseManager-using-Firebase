package com.myfirstapplication.expensemanagerfirebase;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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

public class EditActivity extends AppCompatActivity {

    EditText txtAmount;
    Spinner spinner;
    DatePicker datePicker;
    TextView txtDate;
    DBController dbController;
    ArrayAdapter<String> adapter;
    List itemsID;
    List itemsString;
    String date, category, id;
    Button btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getIDs();
        dbController = new DBController(this);
        LoadSpinnerData();

        final Intent intent = getIntent();
        id = intent.getStringExtra("id");
        loadIDData(id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, monthOfYear, dayOfMonth);

                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    date = format.format(calendar.getTime());
                    txtDate.setText(date);
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

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id == "") {
                    return;
                }
                String amount = String.valueOf(txtAmount.getText());
                if (amount.equals("")) {
                    showMessage("Value of Amount is empty");
                    return;
                }
                final int Amount = Integer.parseInt(String.valueOf(txtAmount.getText()));
                dbController.getCategoryID(category, new ServerCallBack() {
                    @Override
                    public void onSuccess(boolean result, String path) {
                        if (result) {
                            int year = datePicker.getYear();
                            int month = datePicker.getMonth();
                            int day = datePicker.getDayOfMonth();

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, month, day);

                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                            date = format.format(calendar.getTime());
                            Expense expense = new Expense(id, String.valueOf(Amount), date, path);
                            dbController.updateExpenseData(expense, new ServerCallBack() {
                                @Override
                                public void onSuccess(boolean result, String path) {
                                    if (result) {

                                        intent.putExtra("result", "updated");
                                        setResult(Activity.RESULT_OK, intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onArraySuccess(boolean result, ArrayList<Expense> expenses) {

                                }

                                @Override
                                public void onStringArraySuccess(boolean result, ArrayList<String> strings) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onStringArraySuccess(boolean result, ArrayList<String> strings) {

                    }

                    @Override
                    public void onArraySuccess(boolean result, ArrayList<Expense> expenses) {

                    }
                });
            }
        });


    }

    void getIDs() {
        txtAmount = findViewById(R.id.txtAmount);
        spinner = findViewById(R.id.spinnerCategory);
        datePicker = findViewById(R.id.dateDate);
        txtDate = findViewById(R.id.txtDate1);
        btnEdit = findViewById(R.id.btnSave);
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
                if (result) {
                    adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, strings);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }
            }
        });

    }

    void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    void loadIDData(String id) {
        if (!id.equals("")) {
            dbController.getExpenseData(id, new ServerCallBack() {
                @Override
                public void onSuccess(boolean result, String path) {

                }

                @Override
                public void onArraySuccess(boolean result, ArrayList<Expense> expenses) {
                    if (result) {
                        ArrayList<Expense> expenseArrayAdapter = expenses;
                        if (!expenseArrayAdapter.isEmpty()) {
                            txtAmount.setText(expenseArrayAdapter.get(0).getAmount());
                            String date = expenseArrayAdapter.get(0).getDate();
                            String category = expenseArrayAdapter.get(0).getType();
                            txtDate.setText(date);
                            spinner.setSelection(adapter.getPosition(category));
                        } else {
                            txtAmount.setText("");
                            String date = category = "";
                            txtDate.setText(date);
                            spinner.setSelection(adapter.getPosition(category));
                        }
                    }
                }

                @Override
                public void onStringArraySuccess(boolean result, ArrayList<String> strings) {

                }
            });


        }
    }

}
