package com.myfirstapplication.expensemanagerfirebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.core.content.ContextCompat;

public class CustomAdapter extends ArrayAdapter<Expense> {
    private List<Expense> Lst;
    public int resource;
    DBController dbController = new DBController(getContext());


    public CustomAdapter(Context context, int resource, List<Expense> items) {
        super(context, resource, items);
        this.resource = resource;
        Lst = items;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            v = vi.inflate(resource, null);
        }
        if (resource == R.layout.xml_show_data) {
            final Expense p = getItem(position);
            if (p != null) {
                LinearLayout linearLayout = v.findViewById(R.id.linearShow);
                if (position % 2 == 0) {
                    linearLayout.setBackgroundResource(R.drawable.custombordereven);
                    //linearLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
                } else {
                    linearLayout.setBackgroundResource(R.drawable.customborderodd);
                    //linearLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBlue));
                }
                TextView txtAmo = v.findViewById(R.id.txtAmo);
                TextView txtDate = v.findViewById(R.id.txtDate);
                TextView txtType = v.findViewById(R.id.txtType);
                if (txtAmo != null) {
                    txtAmo.setText(p.getAmount());
                }
                if (txtDate != null) {
                    txtDate.setText(p.getDate());
                }
                if (txtType != null) {
                    txtType.setText(p.getType());
                }

                Button btnEdit = v.findViewById(R.id.btnListEdit);
                Button btnDelete = v.findViewById(R.id.btnListDelete);
                if (btnEdit != null) {
                    btnEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ShowAllActivity.getInstance().editData(p.getID());
                        }
                    });
                }
                if (btnDelete != null) {
                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dbController.deleteExpense(p.getID(), new ServerCallBack() {
                                @Override
                                public void onSuccess(boolean result, String path) {
                                    if (result) {
                                        CustomAdapter.super.remove(p);
                                        //ShowAllActivity.getInstance().recreate();
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
                }
            }
        } else if (resource == R.layout.xml_display_percentage) {
            Expense p = getItem(position);
            if (p != null) {
                TextView txtID = v.findViewById(R.id.txtCategory);
                TextView txtAmo = v.findViewById(R.id.txtPercentage);
                ImageView imageView = v.findViewById(R.id.imageLayout);
                if (txtID != null) {
                    txtID.setText(p.getID());
                }
                if (txtAmo != null) {
                    txtAmo.setText(p.getAmount() + " %");
                }
                if (imageView != null) {
                    byte[] bytes = p.getBytes();
                    Bitmap bmp;
                    if (bytes != null) {
                        bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageView.setImageBitmap(bmp);
                    } else
                        imageView.setImageResource(R.drawable.ic_error_img);
                }

                int[] androidColors = getContext().getResources().getIntArray(R.array.androidcolors);
                int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
                //v.setBackgroundColor(randomAndroidColor);

                GradientDrawable bgShape = (GradientDrawable) v.getBackground();
                bgShape.setColor(randomAndroidColor);
            }
        }
        return v;

    }


}
