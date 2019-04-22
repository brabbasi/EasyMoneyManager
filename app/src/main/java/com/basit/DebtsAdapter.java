package com.basit;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DebtsAdapter extends RecyclerView.Adapter<DebtsAdapter.MyViewHolder> {

    private ArrayList<Debt> debtList;
    private Context context;
    private DebtsAdapter adapter = this;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, month, title, description, amount;
        public View itemView;

        public MyViewHolder(View view) {
            super(view);
            itemView = view;
            date = (TextView) view.findViewById(R.id.date);
            month = (TextView) view.findViewById(R.id.month);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            amount = (TextView) view.findViewById(R.id.amount);
        }
    }


    public DebtsAdapter(ArrayList<Debt> debtList, Context context) {
        this.context = context;
        this.debtList = debtList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_expense, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Debt debt = debtList.get(position);
        holder.date.setText("" + debt.date);
        holder.month.setText( "" + MainActivity.months[ debt.month]);
        holder.title.setText( "" + debt.to);
        holder.description.setText( "" + debt.description);
        holder.amount.setText( "" + debt.amount);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder( context)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new DatabaseHelper( context).deleteDebt("" + debt.id, new dbCallBack() {
                                    @Override
                                    public void onCallBack(ArrayList debt, double totalDebt, ArrayList expense, double totalExpense, boolean result) {
                                        if( result){
                                            debtList.remove( position);
                                            adapter.notifyItemRemoved( position);

                                        }
                                        else{
                                            Toast.makeText( context, "Error deleting record", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return debtList.size();
    }
}