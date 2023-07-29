package com.example.homefinancebeta;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddExpenses#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddExpenses extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddExpenses() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddExpenses.
     */
    // TODO: Rename and change types and number of parameters
    public static AddExpenses newInstance(String param1, String param2) {
        AddExpenses fragment = new AddExpenses();
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
        View view = inflater.inflate(R.layout.fragment_add_expenses, container, false);

        Button btnBack = view.findViewById(R.id.btnBack);
        Button btnAddNewExpense = view.findViewById(R.id.btnAddNewExpense);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToPreviousMenu();
            }
        });

        btnAddNewExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewExpense(view);
            }
        });


        return view;
    }

    private void navigateToPreviousMenu() {

        NavController navController = NavHostFragment.findNavController(this);

        navController.navigateUp();

    }

        public void addNewExpense(View view) {
        Expense expense = getNewExpense(view);

        if (expense != null) {
            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference expensesRef = database.getReference("expenses");

            DatabaseReference newExpenseRef = expensesRef.push();

            newExpenseRef.setValue(expense)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Data successfully added to the database
                                // Do something if needed
                            } else {
                                // Handle the case where data upload failed
                                // Show an error message or log the error
                            }
                        }
                    });

            Toast.makeText(getActivity(), "Expense added successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Failed to add expense. Please check the form.", Toast.LENGTH_SHORT).show();
        }
    }



    public Expense getNewExpense(View view) {

        EditText inWhere = view.findViewById(R.id.inWhere);
        EditText inCategory = view.findViewById(R.id.inCategory);
        EditText inPrice = view.findViewById(R.id.inPrice);

        RadioGroup inRadioGroup = view.findViewById(R.id.radioGroupOptions);
        if (inRadioGroup == null) {
            // Handle the case where the view is not found
            // For example, show a toast message or log an error
            return null;
        }

        // Radio Btn
        int selectedRadioBtnId = inRadioGroup.getCheckedRadioButtonId();
        String inEssentials = "not provided";
        if (selectedRadioBtnId != -1) {
            RadioButton selectedRadioBtn = view.findViewById(selectedRadioBtnId);
            if (selectedRadioBtn != null) {
                inEssentials = selectedRadioBtn.getText().toString();
            }
        }

        // Date Picker
        DatePicker datePicker = view.findViewById(R.id.inDate);
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int dayOfMonth = datePicker.getDayOfMonth();
        String dateIn = dayOfMonth + "." + (month + 1) + "." + year; // Month is zero-based, so add 1
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        Date selectedTime = calendar.getTime();

        String whereText = inWhere.getText().toString();
        String categoryText = inCategory.getText().toString();
        String dateText = dateIn;
        String essentialsText = inEssentials;
        String priceText = inPrice.getText().toString();

        Expense newExpense = new Expense();
        newExpense.setWhere(whereText);
        newExpense.setCategory(categoryText);
        newExpense.setEssentials(essentialsText);
        newExpense.setDate(dateText);
        newExpense.setPrice(priceText);

        // Get the activity that is currently hosting the fragment (MainActivity)
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            String userId = mainActivity.getUserId();
            newExpense.setId(userId);
            // Use the userId as needed
            return newExpense;
        } else {
            // Handle the case when the activity is not available or not MainActivity
            return null;
        }
    }


}