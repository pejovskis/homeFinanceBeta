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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNewSalary#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class AddNewSalary extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatePicker inSalaryDate;
    private EditText inEmployer;
    private EditText inSalaryCategory;
    private EditText inSalaryAmount;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewSalary.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewSalary newInstance(String param1, String param2) {
        AddNewSalary fragment = new AddNewSalary();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AddNewSalary() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_add_new_salary, container, false);

        // Input Fields
        inEmployer = view.findViewById(R.id.inEmployer);
        inSalaryCategory = view.findViewById(R.id.inSalaryCategory);
        inSalaryDate = view.findViewById(R.id.inSalaryDate);
        inSalaryAmount = view.findViewById(R.id.inSalaryAmount);

        // Buttons
        Button btnBackToPreviousMenu = view.findViewById(R.id.btnBack);

        btnBackToPreviousMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToPreviousMenu();
            }
        });

        Button btnAddNewSalary = view.findViewById(R.id.btnAddNewSalary);

        btnAddNewSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewSalary();
            }
        });

        return view;
    }

    private void addNewSalary() {
        Salary salary = getNewSalary();

        if (salary != null) {
            String userId = getUserIdFromFireBase();

            if (userId != null) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://homefinance-394622-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference salaryRef = firebaseDatabase.getReference("Users").child(userId).child("salaries");

                DatabaseReference newDatabaseRef = salaryRef.push();
                newDatabaseRef.setValue(salary);

                Toast.makeText(getActivity(), "Salary Added Successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Failed to add new Salary. User Id is not available", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "Failed to add new Salary. Unexpected error occurred.", Toast.LENGTH_SHORT).show();
        }
    }

    private Salary getNewSalary() {

        int year = inSalaryDate.getYear();
        int month = inSalaryDate.getMonth();
        int day = inSalaryDate.getDayOfMonth();

        String employer = inEmployer.getText().toString();
        String category = inSalaryCategory.getText().toString();
        String dateIn = day + "." + (month + 1) + "." + year;
        String amount = inSalaryAmount.getText().toString();

        Salary newSalary = new Salary(employer, category, dateIn, amount);
        return newSalary;
    }

    private String getUserIdFromFireBase() {
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

    private void navigateToPreviousMenu() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigateUp();
    }
}