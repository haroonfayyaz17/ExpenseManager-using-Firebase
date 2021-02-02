package com.myfirstapplication.expensemanagerfirebase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class ShowAllActivity extends AppCompatActivity {


    private static final int LAUNCH_SECOND_ACTIVITY = 1;
    String result = "";
    private static ShowAllActivity instance;
    ArrayList<Expense> expensesArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);
        instance = this;

        DBController dbController = new DBController(this);

        dbController.loadExpenseData(new ServerCallBack() {
            @Override
            public void onStringArraySuccess(boolean result, ArrayList<String> strings) {

            }

            @Override
            public void onSuccess(boolean result, String path) {

            }

            @Override
            public void onArraySuccess(boolean result, ArrayList<Expense> expenses) {
                expensesArray = expenses;
                CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), R.layout.xml_show_data, expensesArray);
                ListView listView = findViewById(R.id.lstShow);
                listView.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();
            }
        });
    }

    public void editData(String id) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("id", id);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
    }

    public static ShowAllActivity getInstance() {
        return instance;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                result = data.getStringExtra("result");
                if (result.equals("updated"))
                    recreate();
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }
}
