package com.example.fintrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fintrack.R;
import com.example.fintrack.databinding.RowAccountBinding;
import com.example.fintrack.models.Account;

import java.util.ArrayList;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder>{

    Context context;
    ArrayList<Account> accountArrayList;

    public interface AccountsClickListener{
        void onAccountSelected(Account account);
    }

    AccountsClickListener accountsClickListener;

    public AccountsAdapter(Context context,ArrayList<Account> accountArrayList,AccountsClickListener accountsClickListener){

        this.context=context;
        this.accountArrayList=accountArrayList;
        this.accountsClickListener=accountsClickListener;

    }
    @NonNull
    @Override
    public AccountsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountsViewHolder(LayoutInflater.from(context).inflate(R.layout.row_account, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AccountsViewHolder holder, int position) {

        holder.binding.accountName.setText(accountArrayList.get(position).getAccountName());
        holder.itemView.setOnClickListener(view -> {
            accountsClickListener.onAccountSelected(accountArrayList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return accountArrayList.size();
    }

    public class AccountsViewHolder extends RecyclerView.ViewHolder{

        RowAccountBinding binding;

        public AccountsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowAccountBinding.bind(itemView);
        }
    }
}
