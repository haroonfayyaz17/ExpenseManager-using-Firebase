package com.myfirstapplication.expensemanagerfirebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddExpenseActivity extends AppCompatActivity {

    private static final int SELECT_PHOTO = 1;
    DBController dbController;
    EditText editText;

    private Uri filePath;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        dbController = new DBController(this);
        imageView = findViewById(R.id.imgPicChoose);
        editText = findViewById(R.id.txtExpense);
        Button button = findViewById(R.id.btnAdd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] bytes = imageToByte();
                dbController.insertCategory(String.valueOf(editText.getText()), filePath, new ServerCallBack() {
                    @Override
                    public void onSuccess(boolean result, String path) {
                        if (result) {
                            showMessage("Inserted");
                            editText.setText("");
                            imageView.setImageResource(R.drawable.blank_person);
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
        });

        Button btnChoose = findViewById(R.id.btnChoose);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_PHOTO);
            }
        });


    }

    void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                try {
                    filePath = data.getData();
                    InputStream inputStream = getContentResolver().openInputStream(filePath);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    byte[] imageToByte() {
        ImageView imageView = findViewById(R.id.imgPicChoose);
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        return data;
    }

}
