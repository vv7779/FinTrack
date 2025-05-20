package com.example.fintrack.views.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.fintrack.R;
import com.example.fintrack.adapters.TransactionsAdapter;
import com.example.fintrack.databinding.FragmentAddTransactionBinding;
import com.example.fintrack.databinding.FragmentTransactionsBinding;
import com.example.fintrack.models.Transaction;
import com.example.fintrack.utils.Constants;
import com.example.fintrack.utils.Helper;
import com.example.fintrack.viewmodels.MainViewModel;
import com.example.fintrack.views.activities.MainActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

import io.realm.RealmResults;


public class TransactionsFragment extends Fragment {


    public TransactionsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentTransactionsBinding binding;

    public MainViewModel viewModel;
    /*
    0 = Daily
    1 = Monthly
    2 = Calender
    3 = Summary
    4 = Note
     */

    Calendar calendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransactionsBinding.inflate(inflater);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        calendar = Calendar.getInstance();
        updateDate();

        binding.currentDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    //Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    calendar.set(Calendar.MONTH, datePicker.getMonth());
                    calendar.set(Calendar.YEAR, datePicker.getYear());

//                    String dateToshow = Helper.formatDate(calendar.getTime());
//                    binding.currentDate.setText(dateToshow);
                    updateDate();

//                    transaction.setDate(calendar.getTime());
//                    transaction.setId(calendar.getTime().getTime());
                }
            });
            datePickerDialog.show();
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getText().equals("Daily")){
                    Constants.SELECTED_TAB = 0;
                    updateDate();
                } else  if (tab.getText().equals("Monthly")){
                    Constants.SELECTED_TAB = 1;
                    updateDate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.floatingActionButton.setOnClickListener(v->{
            new AddTransactionFragment().show(getParentFragmentManager(), null);
        });

        binding.nextDatebtn.setOnClickListener(view -> {

            if (Constants.SELECTED_TAB == Constants.DAILY){
                calendar.add(Calendar.DATE,1);
            } else if(Constants.SELECTED_TAB == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH,1);

            }
            updateDate();
        });

        binding.previousDatebtn.setOnClickListener(view -> {

            if (Constants.SELECTED_TAB == Constants.DAILY){
                calendar.add(Calendar.DATE,-1);

            }
            else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH,-1);

            }
            updateDate();
        });

        binding.transactionsList.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.transactions.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {

                TransactionsAdapter transactionsAdapter = new TransactionsAdapter(getActivity(), transactions);
                binding.transactionsList.setAdapter(transactionsAdapter);

                if (transactions.size()>0){
                    binding.emptyState.setVisibility(View.GONE);

                } else {
                    binding.emptyState.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.totalIncome.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.incomeLbl.setText(String.valueOf(aDouble));
            }
        });

        viewModel.totalExpense.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expenseLbl.setText(String.valueOf(aDouble));
            }
        });

        viewModel.totalAmount.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.totalLbl.setText(String.valueOf(aDouble));
            }
        });

        viewModel.getTransactions(calendar);


        return binding.getRoot();
    }

    void  updateDate(){

        if (Constants.SELECTED_TAB == Constants.DAILY){

            binding.currentDate.setText(Helper.formatDate(calendar.getTime()));

        } else if (Constants.SELECTED_TAB == Constants.MONTHLY){

            binding.currentDate.setText(Helper.formatDateByMonth(calendar.getTime()));

        }
        viewModel.getTransactions(calendar);
    }
}