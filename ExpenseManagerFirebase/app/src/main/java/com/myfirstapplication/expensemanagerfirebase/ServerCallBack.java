package com.myfirstapplication.expensemanagerfirebase;

import java.util.ArrayList;

public interface ServerCallBack {

    void onSuccess(boolean result, String path);

    void onArraySuccess(boolean result, ArrayList<Expense> expenses);

    void onStringArraySuccess(boolean result, ArrayList<String> strings);
}
