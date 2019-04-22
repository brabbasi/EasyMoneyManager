package com.basit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    //public database helper functions for all activities

    public static final String DATABASE_NAME = "easymoney.db";

    public static final String EXPENSE_TABLE = "expenses";
    public static final String EXPENSE_ID = "expenseid";
    public static final String EXPENSE_AMOUNT = "expenseamount";
    public static final String EXPENSE_TITLE = "expensetitle";
    public static final String EXPENSE_DESCRIPTION = "expensedesc";
    public static final String EXPENSE_MONTH = "expensemonth";
    public static final String EXPENSE_YEAR = "expenseyear";
    public static final String EXPENSE_DAY = "expensedate";
    public static final String EXPENSE_TIME = "timestamp";

    public static final String DEBT_TABLE = "debt";
    public static final String DEBT_ID = "debtid";
    public static final String DEBT_AMOUNT = "debtamount";
    public static final String DEBT_TO = "debtto";
    public static final String DEBT_DESCRIPTION = "debtdesc";
    public static final String DEBT_MONTH = "debtmonth";
    public static final String DEBT_YEAR = "debtyear";
    public static final String DEBT_DAY = "debtdate";
    public static final String DEBT_TIME = "timestamp";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + EXPENSE_TABLE + "( " + EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EXPENSE_AMOUNT +
                " STRING, " + EXPENSE_TITLE + " STRING, " + EXPENSE_DESCRIPTION + " STRING," + EXPENSE_MONTH + " INTEGER, " + EXPENSE_YEAR + " INTEGER, " +
                EXPENSE_DAY + ", INTEGER, " + EXPENSE_TIME + " DATE)");
        db.execSQL("create table " + DEBT_TABLE + "( " + DEBT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DEBT_AMOUNT +
                " STRING, " + DEBT_TO + " STRING, " + DEBT_DESCRIPTION + " STRING," + DEBT_MONTH + " INTEGER, " + DEBT_YEAR + " INTEGER, " +
                DEBT_DAY + ", INTEGER, " + DEBT_TIME + " DATE)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", EXPENSE_TABLE));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", DEBT_TABLE));
        onCreate(db);

    }

    public void addExpense( String amount, String title, String description, int month, int year, int date, dbCallBack cb){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( EXPENSE_AMOUNT, amount);
        contentValues.put( EXPENSE_TITLE, title);
        contentValues.put( EXPENSE_DESCRIPTION, description);
        contentValues.put( EXPENSE_MONTH, month);
        contentValues.put( EXPENSE_YEAR, year);
        contentValues.put( EXPENSE_DAY, date);
        contentValues.put( EXPENSE_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        long result = db.insert( EXPENSE_TABLE,null, contentValues);
        if (result == -1)
            cb.onCallBack( null, 0, null, 0, false);
        else
            cb.onCallBack( null, 0, null, 0, true);
    }

    public void addDebt( String amount, String to, String description, int month, int year, int date, dbCallBack cb){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( DEBT_AMOUNT, amount);
        contentValues.put( DEBT_TO, to);
        contentValues.put( DEBT_DESCRIPTION, description);
        contentValues.put( DEBT_MONTH, month);
        contentValues.put( DEBT_YEAR, year);
        contentValues.put( DEBT_DAY, date);
        contentValues.put( DEBT_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        long result = db.insert( DEBT_TABLE,null, contentValues);
        if (result == -1)
            cb.onCallBack( null, 0, null, 0, false);
        else
            cb.onCallBack( null, 0, null, 0, true);
    }

    public void getExpenses( int month, int year, dbCallBack cb) {

        ArrayList<Expense> expenses = new ArrayList<>();

        //retrieving list of expenses from the database
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + EXPENSE_TABLE + " WHERE " + EXPENSE_MONTH + " = " + month + " AND " +
                                    EXPENSE_YEAR + " = " + year + " ORDER BY " + EXPENSE_TIME + " DESC", null);

        if(cursor.moveToFirst()) {
            do {
                //new expense, saves attributes to it, and saves that expense to arraylist
                Expense temp = new Expense();
                temp.id = cursor.getInt(cursor.getColumnIndex(EXPENSE_ID));
                temp.amount = Double.parseDouble( cursor.getString(cursor.getColumnIndex(EXPENSE_AMOUNT)));
                temp.title = cursor.getString(cursor.getColumnIndex(EXPENSE_TITLE));
                temp.description = cursor.getString(cursor.getColumnIndex(EXPENSE_DESCRIPTION));
                temp.month = cursor.getInt(cursor.getColumnIndex(EXPENSE_MONTH));
                temp.year = cursor.getInt(cursor.getColumnIndex(EXPENSE_YEAR));
                temp.date = cursor.getInt(cursor.getColumnIndex(EXPENSE_DAY));
                expenses.add( temp);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        cb.onCallBack( null, 0, expenses, 0, true);
    }

    public void getDebt( int month, int year, dbCallBack cb) {

        ArrayList<Debt> debts = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DEBT_TABLE + " WHERE " + DEBT_MONTH + " = " + month + " AND " +
                DEBT_YEAR + " = " + year + " ORDER BY " + DEBT_TIME + " DESC", null);

        if(cursor.moveToFirst()) {
//            cursor.moveToNext();
            do {
                Debt temp = new Debt();
                temp.id = cursor.getInt(cursor.getColumnIndex(DEBT_ID));
                temp.amount = Double.parseDouble( cursor.getString(cursor.getColumnIndex(DEBT_AMOUNT)));
                temp.to = cursor.getString(cursor.getColumnIndex(DEBT_TO));
                temp.description = cursor.getString(cursor.getColumnIndex(DEBT_DESCRIPTION));
                temp.month = cursor.getInt(cursor.getColumnIndex(DEBT_MONTH));
                temp.year = cursor.getInt(cursor.getColumnIndex(DEBT_YEAR));
                temp.date = cursor.getInt(cursor.getColumnIndex(DEBT_DAY));
                debts.add( temp);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        cb.onCallBack( debts, 0, null, 0, true);
    }

    public void getTotalDebt( int month, int year, dbCallBack cb) {

        double totalDebt = 0.0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DEBT_TABLE + " WHERE " + DEBT_MONTH + " = " + month + " AND " +
                DEBT_YEAR + " = " + year , null);

        if(cursor.moveToFirst()) {
            do {
                //add to total debt
                totalDebt = totalDebt + Double.parseDouble( cursor.getString(cursor.getColumnIndex(DEBT_AMOUNT)));
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        cb.onCallBack( null, totalDebt, null, 0, true);
    }

    public void getTotalExpenses( int month, int year, dbCallBack cb) {

        double totalExpense = 0.0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + EXPENSE_TABLE + " WHERE " + EXPENSE_MONTH + " = " + month + " AND " +
                EXPENSE_YEAR + " = " + year , null);

        if(cursor.moveToFirst()) {
//            cursor.moveToNext();
            do {
                totalExpense = totalExpense + Double.parseDouble( cursor.getString(cursor.getColumnIndex(EXPENSE_AMOUNT)));
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        cb.onCallBack( null, 0, null, totalExpense, true);
    }

    public void deleteExpense( String id, dbCallBack cb){
        SQLiteDatabase db = this.getReadableDatabase();
        if( db.delete( EXPENSE_TABLE, EXPENSE_ID + "=" + id, null) > 0){
            cb.onCallBack( null, 0, null, 0, true);
        }
        else{
            cb.onCallBack( null, 0, null, 0, false);
        }
    }

    public void deleteDebt( String id, dbCallBack cb){
        SQLiteDatabase db = this.getReadableDatabase();
        if( db.delete( DEBT_TABLE, DEBT_ID + "=" + id, null) > 0){
            cb.onCallBack( null, 0, null, 0, true);
        }
        else{
            cb.onCallBack( null, 0, null, 0, false);
        }
    }
}
