package com.basit;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    public static final String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    DrawerLayout dl;
    NavigationView nv;

    TextView currentMonth;
    TextView monthlyExpense;
    TextView monthlyDebt;

    Button previousMonthButton;
    Button nextMonthButton;

    Button addExpense;
    Button addDebt;

    DatabaseHelper db;

    double totalExpenses = 0.0;
    double totalDebts = 0.0;
    int currentMonthNumber;
    int currentYearNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing views
        dl = (DrawerLayout)findViewById(R.id.drawer_layout);
        currentMonth = findViewById( R.id.currentMonth);
        monthlyDebt = findViewById( R.id.monthlyDebt);
        monthlyExpense = findViewById( R.id.monthlyExpense);
        previousMonthButton = findViewById( R.id.previousMonthButton);
        nextMonthButton = findViewById( R.id.nextMonthButton);
        addExpense = findViewById( R.id.addExpense);
        addDebt = findViewById( R.id.addDebt);


        //initializing toolbar at top
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, dl,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);


        //setting up navigation
        nv = (NavigationView)findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.expenses:
                        Intent a = new Intent(getApplicationContext(), ExpensesActivity.class);
                        startActivity(a);
                        break;
                    case R.id.debts:
                        Intent b = new Intent(getApplicationContext(), DebtsActivity.class);
                        startActivity(b);
                        break;
                    case R.id.settings:
                        Intent c = new Intent(getApplicationContext(), Settings.class);
                        startActivity(c);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        nextMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMonth();
            }
        });
        previousMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousMonth();
            }
        });
        addDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( MainActivity.this, AddDebt.class));
            }
        });
        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( MainActivity.this, AddExpense.class));
            }
        });


        currentYearNumber = Calendar.getInstance().get(Calendar.YEAR);
        currentMonthNumber = Calendar.getInstance().get(Calendar.MONTH) + 1;

        populateViews();
    }

    public void populateViews(){
        currentMonth.setText( months[ currentMonthNumber - 1] + " " + currentYearNumber);

        if( (currentMonthNumber != Calendar.getInstance().get(Calendar.MONTH) + 1) ||
                (currentYearNumber != Calendar.getInstance().get(Calendar.YEAR))){
            addDebt.setVisibility( View.GONE);
            addExpense.setVisibility( View.GONE);
        }
        else{
            addDebt.setVisibility( View.VISIBLE);
            addExpense.setVisibility( View.VISIBLE);
        }


        //asynchronous operation
        db = new DatabaseHelper( MainActivity.this);
        db.getTotalDebt(currentMonthNumber, currentYearNumber, new dbCallBack() {
            @Override
            public void onCallBack(ArrayList debt, double totalDebt, ArrayList expense, double totalExpense, boolean result) {
                monthlyDebt.setText( "" + totalDebt);
            }
        });

        db.getTotalExpenses(currentMonthNumber, currentYearNumber, new dbCallBack() {
            @Override
            public void onCallBack(ArrayList debt, double totalDebt, ArrayList expense, double totalExpense, boolean result) {
                monthlyExpense.setText( "" + totalExpense);
            }
        });
    }

    public void nextMonth(){
        if( currentMonthNumber >= 12){
            currentMonthNumber = 1;
            ++currentYearNumber;
        }
        else{
            currentMonthNumber++;
        }
        populateViews();
    }

    public void previousMonth(){
        if( currentMonthNumber <= 1){
            currentMonthNumber = 12;
            --currentYearNumber;
        }
        else{
            --currentMonthNumber;
        }
        populateViews();
    }

    @Override
    public void onResume(){
        super.onResume();
        try{
            populateViews();
        } catch( Exception e){}
    }
}
