package com.basit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import static com.basit.MainActivity.months;

public class ExpensesActivity extends AppCompatActivity {

    Button backButton, nextMonth, prevMonth;
    TextView monthName;
    RecyclerView content;
    ExpensesAdapter adapter;

    DatabaseHelper db;
    ArrayList<Expense> expenses = new ArrayList<>();
    int currentMonth;
    int currentYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        //views initialized
        content = findViewById( R.id.content);
        backButton = findViewById( R.id.backButton);
        nextMonth = findViewById( R.id.nextMonth);
        prevMonth = findViewById( R.id.prevMonth);
        monthName = findViewById( R.id.currentMonth);

        //init current vairables
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        db = new DatabaseHelper( ExpensesActivity.this);

        //initializing recycler views
        adapter = new ExpensesAdapter( expenses, ExpensesActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        content.setLayoutManager(mLayoutManager);
        content.setItemAnimator(new DefaultItemAnimator());
        content.setAdapter( adapter);


        updateViews();
        updateList();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMonth();
            }
        });
        prevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousMonth();
            }
        });
    }

    public void updateList(){
        db.getExpenses(currentMonth, currentYear, new dbCallBack() {
            @Override
            public void onCallBack(ArrayList debt, double totalDebt, ArrayList expense, double totalExpense, boolean result) {
                expenses = expense;
                updateViews();
            }
        });
    }

    public void updateViews(){
        monthName.setText( months[ currentMonth - 1] + " " + currentYear);


        adapter = new ExpensesAdapter( expenses, ExpensesActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        content.setLayoutManager(mLayoutManager);
        content.setItemAnimator(new DefaultItemAnimator());
        content.setAdapter( adapter);
    }

    public void nextMonth(){
        if( currentMonth >= 12){
            currentMonth = 1;
            ++currentYear;
        }
        else{
            currentMonth++;
        }
        updateList();
    }

    public void previousMonth(){
        if( currentMonth <= 1){
            currentMonth = 12;
            --currentYear;
        }
        else{
            --currentMonth;
        }
        updateList();
    }
}
