package com.myfirstapplication.expensemanagerfirebase;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    DBController dbController;
    Intent intent;

    Button btnAdd, btnShow, btnAddExpense;
    ListView listOdd, listEven;

    public static String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent1 = getIntent();
        UserID = intent1.getStringExtra("UserID");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        getIDs();
        dbController = new DBController(this);
        loadData();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, ShowAllActivity.class);
                startActivity(intent);
            }
        });
        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, AddExpenseActivity.class);
                startActivity(intent);
            }
        });

    }

    void loadData() {
        dbController.getPercentages(new ServerCallBack() {
            @Override
            public void onSuccess(boolean result, String path) {

            }

            @Override
            public void onArraySuccess(boolean result, ArrayList<Expense> expenses) {

                if (result) {

                    ArrayList<Expense> evenList = new ArrayList<>();
                    ArrayList<Expense> oddList = new ArrayList<>();

                    for (int i = 0; i < expenses.size(); i++) {
                        if (i % 2 == 0)
                            evenList.add(expenses.get(i));
                        else
                            oddList.add(expenses.get(i));
                    }

                    CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), R.layout.xml_display_percentage, oddList);
                    ListView listView = findViewById(R.id.lstListOdd);
                    listView.setAdapter(customAdapter);

                    CustomAdapter customAdapter1 = new CustomAdapter(getApplicationContext(), R.layout.xml_display_percentage, evenList);
                    ListView listView1 = findViewById(R.id.lstListEven);
                    listView1.setAdapter(customAdapter1);
                }
            }

            @Override
            public void onStringArraySuccess(boolean result, ArrayList<String> strings) {

            }
        });


    }

    void getIDs() {
        btnAdd = findViewById(R.id.btnAdd);
        btnShow = findViewById(R.id.btnShowAll);
        listEven = findViewById(R.id.lstListEven);
        listOdd = findViewById(R.id.lstListOdd);
        btnAddExpense = findViewById(R.id.btnAddExpense);
    }

}


