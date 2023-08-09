package com.example.homefinancebeta;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowExpenses#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowExpenses extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShowExpenses() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowExpenses.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowExpenses newInstance(String param1, String param2) {
        ShowExpenses fragment = new ShowExpenses();
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
        View view = inflater.inflate(R.layout.fragment_show_expenses, container, false);

        Button btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToPreviousMenu();
            }
        });

        // Retrieve and display the expenses
        retrieveAndDisplayExpenses(view);

        return view;
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

    private void retrieveAndDisplayExpenses(View view) {

        String userId = getUserId();

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://homefinance-394622-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference expensesRef = database.getReference("Users").child(userId).child("expenses");
        expensesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear any previous data from the table
                TableLayout tableLayoutExpenses = view.findViewById(R.id.tableLayoutExpenses);

                addTableHeaders(tableLayoutExpenses);

                if (dataSnapshot.exists()) {
                    for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()) {
                        Expense expense = expenseSnapshot.getValue(Expense.class);

                        addExpenseToTable(tableLayoutExpenses, expense);
                    }
                } else {
                    addNoDataMessage(tableLayoutExpenses);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur while retrieving data
            }
        });
    }

    private void addNoDataMessage(TableLayout tableLayout) {
        TableRow row = new TableRow(requireContext());

        TextView tvNoData = createTextView("No expenses found.", 20, Gravity.CENTER);
        tvNoData.setGravity(Gravity.CENTER);
        tvNoData.setTextColor(Color.RED);
        tvNoData.setTextSize(25);

        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        layoutParams.span = 6; // Span the message across all columns

        row.addView(tvNoData, layoutParams);

        // Add the TableRow to the TableLayout
        tableLayout.addView(row);
    }

    private void addTableHeaders(TableLayout tableLayout) {
        TableRow row = new TableRow(requireContext());

        int backgroundColor = Color.LTGRAY;
        int textSize = 18;
        int textStyle = Typeface.BOLD;
        int position = Gravity.CENTER;

        row.addView(createHeaderTextView("Where", backgroundColor, textSize, textStyle, position));
        row.addView(createHeaderTextView("Essentials", backgroundColor, textSize, textStyle, position));
        row.addView(createHeaderTextView("Category", backgroundColor, textSize, textStyle, position));
        row.addView(createHeaderTextView("Date", backgroundColor, textSize, textStyle, position));
        row.addView(createHeaderTextView("Price", backgroundColor, textSize, textStyle, position));

        tableLayout.addView(row);
    }

    private View createHeaderTextView(String text, int backgroundColor, int textSize, int textStyle, int position) {
        TextView textView = createTextView(text, textSize, position);
        textView.setBackgroundColor(backgroundColor);
        textView.setTypeface(null, textStyle);
        return textView;
    }

    private void addExpenseToTable(TableLayout tableLayout, Expense expense) {
        // Create a new TableRow
        TableRow row = new TableRow(requireContext());

        int textSize = 16;
        int position = Gravity.CENTER;

        // Create TextViews to display each attribute of the expense
        TextView tvWhere = createTextView(expense.getWhere(), textSize, position);
        TextView tvEssentials = createTextView(expense.getEssentials(), textSize, position);
        TextView tvCategory = createTextView(expense.getCategory(), textSize, position);
        TextView tvDate = createTextView(expense.getDate(), textSize, position);
        TextView tvPrice = createTextView(expense.getPrice(), textSize, position);

        // Add TextViews to the TableRow
        row.addView(tvWhere);
        row.addView(tvEssentials);
        row.addView(tvCategory);
        row.addView(tvDate);
        row.addView(tvPrice);

        // Add the TableRow to the TableLayout
        tableLayout.addView(row);
    }

    private TextView createTextView(String text, int textSize, int position) {
        TextView textView = new TextView(requireContext());

        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setGravity(position);

        return textView;
    }

    // Back Btn
    private void navigateToPreviousMenu() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigateUp();
    }

}