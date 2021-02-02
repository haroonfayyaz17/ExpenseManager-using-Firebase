package com.myfirstapplication.expensemanagerfirebase;

public class Expense {

    String ID, Amount, Date, Type;
    byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Expense(String ID, String amount, String date, String type) {
        this.ID = ID;
        Amount = amount;
        Date = date;
        Type = type;
    }

    public Expense(String ID, String amount, byte[] bytes) {
        this.ID = ID;
        Amount = amount;
        this.bytes = bytes;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
