package com.myfirstapplication.expensemanagerfirebase;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import androidx.annotation.NonNull;

public class DBController {


    private FirebaseStorage storage;
    private StorageReference storageReference;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Context context;
    public static String value;

    DBController(Context context) {
        this.context = context;
        FirebaseApp.initializeApp(context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Users").child(MainActivity.UserID);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child(MainActivity.UserID);
    }

    void insertCategory(String Category, Uri uri, ServerCallBack callBack) {


        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Uploading....");
        progressDialog.show();

        String path = UUID.randomUUID().toString();
        StorageReference reference = storageReference.child("images/" + path);

        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_LONG).show();
            }
        })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                        progressDialog.setMessage("Uploaded " + (int) progress);
                    }
                });
        String ID = databaseReference.child("Categories").push().getKey();
        DatabaseReference reference1 = databaseReference.child("Categories").push();
        reference1.child("Name").setValue(Category);
        reference1.child("Image").setValue(path);
        callBack.onSuccess(true, "");

    }

    void loadCategories(final ServerCallBack serverCallBack) {

        DatabaseReference databaseReference1 = databaseReference.child("Categories");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<String> strings = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("Name").getValue() == null)
                        break;
                    strings.add(snapshot.child("Name").getValue().toString());
                }
                serverCallBack.onStringArraySuccess(true, strings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

    }

    void insertExpense(int Amount, String category, String date) {

        DatabaseReference reference1 = databaseReference.child("Expenses").push();
        reference1.child("Amount").setValue(Amount);
        reference1.child("Date").setValue(date);
        reference1.child("CatID").setValue(category);

    }

    void getCategoryID(final String category, final ServerCallBack serverCallBack) {

        DatabaseReference databaseReference1 = databaseReference.child("Categories");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren())

                    if (snapshot.child("Name").getValue().toString().equals(category)) {
                        value = snapshot.getKey().toString();
                        serverCallBack.onSuccess(true, value);
                        break;
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void loadExpenseData(final ServerCallBack serverCallBack) {

        final DatabaseReference databaseReference1 = databaseReference.child("Expenses");
        final DatabaseReference categoryReference = databaseReference.child("Categories");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<Expense> expenses = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final String id = snapshot.getKey().toString();
                    final String amount = snapshot.child("Amount").getValue().toString();
                    final String Date = snapshot.child("Date").getValue().toString();
                    final String[] TypeID = {snapshot.child("CatID").getValue().toString()};
                    categoryReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {

                                if (TypeID[0].equals(snapshot1.getKey().toString())) {
                                    TypeID[0] = snapshot1.child("Name").getValue().toString();
                                    expenses.add(new Expense(id, amount, Date, TypeID[0]));
                                }
                            }
                            serverCallBack.onArraySuccess(true, expenses);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

    }

    void deleteExpense(String id, ServerCallBack serverCallBack) {
        databaseReference.child("Expenses").child(id).removeValue();
        serverCallBack.onSuccess(true, "");
    }

    void getExpenseData(final String id, final ServerCallBack serverCallBack) {


        final DatabaseReference databaseReference1 = databaseReference.child("Expenses");
        final DatabaseReference categoryReference = databaseReference.child("Categories");
        databaseReference1.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<Expense> expenses = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (id.equals(snapshot.getKey().toString())) {
                        final String amount = snapshot.child("Amount").getValue().toString();
                        final String Date = snapshot.child("Date").getValue().toString();
                        final String[] TypeID = {snapshot.child("CatID").getValue().toString()};
                        categoryReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {

                                    if (TypeID[0].equals(snapshot1.getKey().toString())) {
                                        TypeID[0] = snapshot1.child("Name").getValue().toString();
                                        expenses.add(new Expense(id, amount, Date, TypeID[0]));
                                    }
                                }

                                serverCallBack.onArraySuccess(true, expenses);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

    }

    void updateExpenseData(final Expense expense, ServerCallBack serverCallBack) {

        DatabaseReference reference1 = databaseReference.child("Expenses").child(expense.getID());
        reference1.child("Amount").setValue(expense.getAmount());
        reference1.child("Date").setValue(expense.getDate());
        reference1.child("CatID").setValue(expense.getType());
        serverCallBack.onSuccess(true, "");

    }


    void getPercentages(final ServerCallBack serverCallBack) {

        //ArrayList<Expense> percentages = new ArrayList<>();

        DatabaseReference databaseReference1 = databaseReference.child("Expenses");

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<String> strings = new ArrayList<>();
                final ArrayList<Integer> sum = new ArrayList<>();
                final float[] Total = {0};
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("CatID").getValue() == null)
                        break;
                    String ID = snapshot.child("CatID").getValue().toString();
                    int i = strings.indexOf(ID);
                    int value = Integer.parseInt(snapshot.child("Amount").getValue().toString());
                    if (i == -1) {
                        strings.add(ID);
                        sum.add(value);
                    } else {
                        sum.set(i, sum.get(i) + value);
                    }
                    Total[0] += value;
                }

                final ArrayList<Expense> expenses = new ArrayList<>();

                for (int i = 0; i < strings.size(); i++) {
                    DatabaseReference categoriesReference = databaseReference.child("Categories").child(strings.get(i));
                    final String[] Name = new String[1];
                    final String[] imagePath = new String[1];
                    final int finalI = i;
                    if (categoriesReference != null) {
                        categoriesReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child("Name").getValue() == null) {
                                    Name[0] = "";
                                    imagePath[0] = "abc";
                                } else {
                                    Name[0] = dataSnapshot.child("Name").getValue().toString();
                                    imagePath[0] = dataSnapshot.child("Image").getValue().toString();
                                }
                                float percentage = (sum.get(finalI) / Total[0]) * 100;

                                DecimalFormat value = new DecimalFormat("#.#");
                                final String format = value.format(percentage);

                                StorageReference reference = storageReference.child("images/" + imagePath[0]);
                                reference.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        expenses.add(new Expense(Name[0], format, bytes));
                                        serverCallBack.onArraySuccess(true, expenses);
                                    }

                                });
                                reference.getBytes(1024 * 1024).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        expenses.add(new Expense(Name[0], format, null));
                                        serverCallBack.onArraySuccess(true, expenses);
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
