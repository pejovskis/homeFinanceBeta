package com.example.homefinancebeta;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters

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
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        Button btnAddExpenses = view.findViewById(R.id.btnAddExpenses);
        Button btnShowExpenses = view.findViewById(R.id.btnShowExpenses);
        Button btnAddNewSalary = view.findViewById(R.id.btnAddNewSalary);
        Button btnShowSalaries = view.findViewById(R.id.btnShowSalaries);
        Button btnShowCalculations = view.findViewById(R.id.btnShowCalculations);
        Button btnLogOut = view.findViewById(R.id.btnLogout);

        btnAddExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToAddExpensesFragment();
            }
        });

        btnShowExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToShowExpensesFragment();
            }
        });

        btnAddNewSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToAddNewSalaryFragment();
            }
        });

        btnShowSalaries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToShowSalariesFragment();
            }
        });

        btnShowCalculations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToShowCalculationsFragment();
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogOut();
            }
        });

        return view;
    }

    private void navigateToAddExpensesFragment() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_menuFragment_to_addExpenses);
    }

    private void navigateToShowExpensesFragment() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_menuFragment_to_showExpenses);
    }

    private void navigateToAddNewSalaryFragment() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_menuFragment_to_addNewSalary);
    }

    private void navigateToShowSalariesFragment() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_menuFragment_to_showSalaries);
    }

    private void navigateToShowCalculationsFragment() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_menuFragment_to_calculationsFragment);
    }

    private void userLogOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}