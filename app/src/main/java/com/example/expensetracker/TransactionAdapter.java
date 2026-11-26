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

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    Context context;
    ArrayList<TransactionList> arrayList;
    public TransactionAdapter(Context context, ArrayList<TransactionList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction,parent,false);
        return new TransactionAdapter.ViewHolder(view);
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
    public void onBindViewHolder(@NonNull TransactionAdapter.ViewHolder holder, int position) {

        TransactionList list = arrayList.get(position);

        holder.icon.setImageResource(arrayList.get(position).getCategoryIcon());
        holder.categoryName.setText(arrayList.get(position).getCategoryName());
        holder.amount.setText(String.valueOf(arrayList.get(position).getAmount()));

        if(list.type.equals("Income")){
            holder.amount.setTextColor(0xFF16835B);
        } else {
            holder.amount.setTextColor(0xFFC65B4B);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
