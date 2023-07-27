package com.example.homefinancebeta.oldApp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import com.example.homefinancebeta.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnAddExpenses = view.findViewById(R.id.btnAddExpenses);
        btnCancel.setOnClickListener(view2 -> Navigation.findNavController(view).navigateUp());
        btnAddExpenses.setOnClickListener(view3 -> addExpense(view));


        return view;
    }

    public void addExpense(View view) {

        TextView consoleOutput = view.findViewById(R.id.consoleOutput);

        Expense expense = getNewExpense(view);
        consoleOutput.setText("\nWhere: " + expense.getWhere() +
                "\nCategory: " + expense.getCategory() +
                "\nEssentials: " + expense.getEssentials() +
                "\nDate: " + expense.getDate() +
                "\nPrice: " + expense.getPrice());

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference dbAdd = database.getReference(expense.getId());
        DatabaseReference expensesRef = database.getReference("expenses");

        DatabaseReference newExpenseRef = expensesRef.push();

        newExpenseRef.setValue(expense);

//        expensesRef.setValue("Id: " + expense.getId() +
//        "\nWhere: " + expense.getWhere() +
//                "\nCategory: " + expense.getCategory() +
//                "\nEssentials: " + expense.getEssentials() +
//                "\nDate: " + expense.getDate() +
//                "\nPrice: " + expense.getPrice());

    }

    public Expense getNewExpense(View view) {

        EditText inWhere = view.findViewById(R.id.inWhere);
        EditText inCategory = view.findViewById(R.id.inCategory);
        EditText inPrice = view.findViewById(R.id.inPrice);

        // Radio Btn
        RadioGroup inRadioGroup = view.findViewById(R.id.radioGroupOptions);
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

        Expense newExpense = new Expense(whereText, categoryText, essentialsText, dateText, priceText);

        return newExpense;

    }

}