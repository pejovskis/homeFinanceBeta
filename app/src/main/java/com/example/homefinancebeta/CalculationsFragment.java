package com.example.homefinancebeta;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CalculationsFragment extends Fragment {
    // Constants and parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private TextView tvWeeklyEstimate;

    // Variables to hold the retrieved expenses and salaries
    private List<Expense> retrievedExpenseList = new ArrayList<>();
    private List<Salary> retrievedSalaryList = new ArrayList<>();

    public CalculationsFragment() {
        // Required empty public constructor
    }

    public static CalculationsFragment newInstance(String param1, String param2) {
        CalculationsFragment fragment = new CalculationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculations, container, false);
        initializeUI(view);
        retrieveDbInfo();
        return view;
    }

    private void initializeUI(View view) {
        Button btnBackToPreviousMenu = view.findViewById(R.id.btnBack);
        tvWeeklyEstimate = view.findViewById(R.id.showWeeklyCalculation);
        btnBackToPreviousMenu.setOnClickListener(view1 -> navigateToPreviousMenu());
    }

    private double calculateSum(List<String> amounts) {
        return amounts.stream().filter(amount -> amount != null && !amount.trim().isEmpty())
                .mapToDouble(Double::parseDouble).sum();
    }

    public String weeklyCalculation() {
        double expenseSum = calculateSum(retrievedExpenseList.stream().map(Expense::getPrice).collect(Collectors.toList()));
        double salarySum = calculateSum(retrievedSalaryList.stream().map(Salary::getAmount).collect(Collectors.toList()));
        return "Netto Amount: " + (salarySum - expenseSum);
    }

    private void retrieveDbInfo() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://homefinance-394622-default-rtdb.europe-west1.firebasedatabase.app/");
        retrieveExpenses(database);
        retrieveSalaries(database);
    }

    private void retrieveExpenses(FirebaseDatabase database) {
        DatabaseReference expensesRef = database.getReference("Users").child(getUserId()).child("expenses");
        expensesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()) {
                        Expense expense = expenseSnapshot.getValue(Expense.class);
                        retrievedExpenseList.add(expense);
                    }
                    calculateFinalAmount();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur while retrieving data
            }
        });
    }

    private void retrieveSalaries(FirebaseDatabase database) {
        DatabaseReference salaryRef = database.getReference("Users").child(getUserId()).child("salaries");
        salaryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot salarySnapshot : dataSnapshot.getChildren()) {
                        Salary salary = salarySnapshot.getValue(Salary.class);
                        retrievedSalaryList.add(salary);
                    }
                    calculateFinalAmount();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur while retrieving data
            }
        });
    }

    private void calculateFinalAmount() {
        if (!retrievedExpenseList.isEmpty() && !retrievedSalaryList.isEmpty()) {
            String resultString = weeklyCalculation();
            tvWeeklyEstimate.setText(resultString);
        }
    }

    private String getUserId() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            SecondActivity mainActivity = (SecondActivity) activity;
            return mainActivity.getUserId();
        } else {
            return null;
        }
    }

    public void navigateToPreviousMenu() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigateUp();
    }
}
