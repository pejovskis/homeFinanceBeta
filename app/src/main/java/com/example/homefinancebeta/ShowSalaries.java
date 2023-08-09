package com.example.homefinancebeta;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

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
 * Use the {@link ShowSalaries#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowSalaries extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static List<Salary> salaryList = new ArrayList<>();

    public ShowSalaries() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowSalaries.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowSalaries newInstance(String param1, String param2) {
        ShowSalaries fragment = new ShowSalaries();
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
        View view = inflater.inflate(R.layout.fragment_show_salaries, container, false);

        Button btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateBackToPreviousMenu();
            }
        });

        retrieveAndDisplaySalaries(view);

        return view;
    }

    private void retrieveAndDisplaySalaries(View view) {

        String userId = getUserId();

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://homefinance-394622-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference expensesRef = database.getReference("Users").child(userId).child("salaries");
        expensesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear any previous data from the table
                TableLayout tableLayoutSalaries = view.findViewById(R.id.tableLayoutSalaries);

                addTableHeaders(tableLayoutSalaries);

                if (dataSnapshot.exists()) {
                    for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()) {
                        Salary salary = expenseSnapshot.getValue(Salary.class);

                        addSalaryToTable(tableLayoutSalaries, salary);
                        salaryList.add(salary);
                    }
                } else {
                    addNoDataMessage(tableLayoutSalaries);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur while retrieving data
            }
        });
    }

    private void addTableHeaders(TableLayout tableLayout) {
        TableRow row = new TableRow(requireContext());

        int backgroundColor = Color.LTGRAY;
        int textSize = 18;
        int textStyle = Typeface.BOLD;
        int position = Gravity.CENTER;

        row.addView(createHeaderTextView("Where", backgroundColor, textSize, textStyle, position));
        row.addView(createHeaderTextView("Category", backgroundColor, textSize, textStyle, position));
        row.addView(createHeaderTextView("Date", backgroundColor, textSize, textStyle, position));
        row.addView(createHeaderTextView("Amount", backgroundColor, textSize, textStyle, position));

        tableLayout.addView(row);
    }

    private void addNoDataMessage(TableLayout tableLayout) {
        TableRow row = new TableRow(requireContext());

        TextView tvNoData = createTextView("No Salaries found.", 20, Gravity.CENTER);
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

    private void addSalaryToTable(TableLayout tableLayout, Salary salary) {
        // Create a new TableRow
        TableRow row = new TableRow(requireContext());

        int textSize = 16;
        int position = Gravity.CENTER;

        // Create TextViews to display each attribute of the expense
        TextView tvEmployer = createTextView(salary.getEmployer(), textSize, position);
        TextView tvCategory = createTextView(salary.getCategory(), textSize, position);
        TextView tvDate = createTextView(salary.getDate(), textSize, position);
        TextView tvAmount = createTextView(salary.getAmount(), textSize, position);

        // Add TextViews to the TableRow
        row.addView(tvEmployer);
        row.addView(tvCategory);
        row.addView(tvDate);
        row.addView(tvAmount);

        // Add the TableRow to the TableLayout
        tableLayout.addView(row);
    }

    private View createHeaderTextView(String text, int backgroundColor, int textSize, int textStyle, int position) {
        TextView textView = createTextView(text, textSize, position);
        textView.setBackgroundColor(backgroundColor);
        textView.setTypeface(null, textStyle);
        return textView;
    }

    private TextView createTextView(String text, int textSize, int position) {
        TextView textView = new TextView(requireContext());

        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setGravity(position);

        return textView;
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

    private void navigateBackToPreviousMenu() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigateUp();
    }
}