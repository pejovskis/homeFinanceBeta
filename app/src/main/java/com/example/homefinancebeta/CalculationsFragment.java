package com.example.homefinancebeta;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalculationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalculationsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView tvWeeklyEstimate;
    private static List<Expense> expenseList;
    private static List<Salary> salaryList;
    private String expenseFinal = "";
    private String salaryFinal = "";

    public CalculationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalculationsFragment.
     */
    // TODO: Rename and change types and number of parameters
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculations, container, false);

        Button btnBackToPreviousMenu = view.findViewById(R.id.btnBack);
        tvWeeklyEstimate = view.findViewById(R.id.showWeeklyCalculation);
        TextView tvMontlyEstimate = view.findViewById(R.id.showMonthlyCalculation);
        TextView tvYearEstimate = view.findViewById(R.id.showYearlyCalculation);

        retrieveDbInfo(view);

        tvWeeklyEstimate.setText(weeklyCalculation());


        btnBackToPreviousMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToPreviousMenu();
            }
        });


        return view;
    }

    public String weeklyCalculation() {

        double expenseSum = 0;
        double salarySum = 0;

        List<Expense> expenses = expenseList;
        List<Salary> salaries = salaryList;

        for (Expense expense : expenses) {
            String price = expense.getPrice();
            if (price != null && !price.trim().isEmpty()) { // Check if price is not null and not empty
                double getExpenseAmount = Double.parseDouble(price);
                expenseSum += getExpenseAmount;
            } else {
                // Optionally log or handle the case where the price is null or empty
            }
        }

        for (Salary salary : salaries) {
            String amount = salary.getAmount();
            if (amount != null && !amount.trim().isEmpty()) { // Check if price is not null and not empty
                double getSalaryAmount = Double.parseDouble(amount);
                salarySum += getSalaryAmount;
            } else {
                // Optionally log or handle the case where the price is null or empty
            }
        }

        Log.d("Salary", "Amount: " + salarySum);

        String weeklyEstimate = "Netto Amount: " + (salarySum - expenseSum);

        return weeklyEstimate;
    }

    private void retrieveDbInfo(View view) {
        String userId = getUserId();

        expenseList = new ArrayList<>();
        salaryList = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://homefinance-394622-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference expensesRef = database.getReference("Users").child(userId).child("expenses");
        DatabaseReference salaryRef = database.getReference("Users").child(userId).child("salaries");

        expensesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()) {
                        Expense expense = expenseSnapshot.getValue(Expense.class);
                        expenseList.add(expense);
                    }
                    expenseFinal = weeklyCalculation();
                    calculateFinalAmount();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur while retrieving data
            }
        });

        salaryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot salarySnapshot : dataSnapshot.getChildren()) {
                        Salary salary = salarySnapshot.getValue(Salary.class);
                        salaryList.add(salary);
                    }
                    salaryFinal = weeklyCalculation();
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
        if (!expenseFinal.isEmpty() && !salaryFinal.isEmpty()) {
            double calculation = (Double.parseDouble(salaryFinal.replace("Netto Amount: ", "")) - Double.parseDouble(expenseFinal.replace("Netto Amount: ", "")));
            String resultString = "Amount: " + calculation;
            tvWeeklyEstimate.setText(resultString);
        }
    }

    private String getUserId() {
        // Get the activity that is currently hosting the fragment
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            SecondActivity mainActivity = (SecondActivity) activity;
            String userId = mainActivity.getUserId();
            return userId;
        } else {
            return null;
        }
    }

    public void navigateToPreviousMenu() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigateUp();
    }
}