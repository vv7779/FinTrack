package com.example.fintrack.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fintrack.R;
import com.example.fintrack.databinding.RowTransactionBinding;
import com.example.fintrack.models.Category;
import com.example.fintrack.models.Transaction;
import com.example.fintrack.utils.Constants;
import com.example.fintrack.utils.Helper;
import com.example.fintrack.views.activities.MainActivity;

import java.util.ArrayList;

import io.realm.RealmResults;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {

    Context context;
    RealmResults<Transaction> transactions;

    public TransactionsAdapter(Context context, RealmResults<Transaction> transactions) {

        this.context=context;
        this.transactions=transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transaction, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {

        Transaction transaction = transactions.get(position);
        holder.binding.transactionAmount.setText(String.valueOf(transaction.getAmount()));
        holder.binding.transactiondate.setText(Helper.formatDate(transaction.getDate()));
        holder.binding.accountLbl.setText(transaction.getAccount());
        holder.binding.transactionCategory.setText(transaction.getCategory());

        Category transactioncategory = Constants.getCategoryDetails(transaction.getCategory());
        holder.binding.categoryIcon.setImageResource(transactioncategory.getCategoryImage());
        holder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(transactioncategory.getCategoryColor()));

        holder.binding.accountLbl.setBackgroundTintList(context.getColorStateList(Constants.getAccountColor(transaction.getAccount())));

        if(transaction.getType().equals(Constants.INCOME)){
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.greenColor));
        }else if(transaction.getType().equals(Constants.EXPENSE)){
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.redColor));
        }

        holder.itemView.setOnClickListener(view -> {

            AlertDialog noteDialog = new AlertDialog.Builder(context).create();
            noteDialog.setTitle("Note");
            noteDialog.setMessage(transaction.getNote());
            noteDialog.show();
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
                deleteDialog.setTitle("Delete  Transaction!");
                deleteDialog.setMessage("Are you sure you want to Delete this Transaction!");
                deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE,"Yes", (dialogInterface, i) -> {

                    ((MainActivity)context).viewModel.deleteTransaction(transaction);
                });

                deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"No", (dialogInterface, i) -> {
                deleteDialog.dismiss();
                });

                deleteDialog.show();

                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public  class TransactionViewHolder extends RecyclerView.ViewHolder{

        RowTransactionBinding binding;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowTransactionBinding.bind(itemView);
        }
    }
}
