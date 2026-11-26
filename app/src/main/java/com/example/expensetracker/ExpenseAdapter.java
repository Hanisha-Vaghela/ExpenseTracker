package com.example.expensetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    Context context;
    String type;
    ArrayList<ExpenseList> arrayList;
    public ExpenseAdapter(Context context, ArrayList<ExpenseList> arrayList,String type) {
        this.context = context;
        this.arrayList = arrayList;
        this.type = type;
    }

    @NonNull
    @Override
    public ExpenseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction,parent,false);
        return new ExpenseAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView categoryName, amount;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            icon = itemView.findViewById(R.id.imgCategory);
            categoryName = itemView.findViewById(R.id.item_transaction_txtCategory);
            amount = itemView.findViewById(R.id.item_transaction_txtAmount);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ViewHolder holder, int position) {
        holder.icon.setImageResource(arrayList.get(position).getCategoryIcon());
        holder.categoryName.setText(arrayList.get(position).getCategoryName());

        if(type.equals("Income")){
            holder.amount.setTextColor(0xFF16835B);
        } else {
            holder.amount.setTextColor(0xFFC65B4B);
        }

        holder.amount.setText(String.valueOf(arrayList.get(position).getAmount()));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
