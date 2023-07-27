package com.example.homefinancebeta.oldApp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.homefinancebeta.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Menu extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Menu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Menu.
     */

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
        Button btnAddSalary = view.findViewById(R.id.btnAddSalary);
        Button btnViewExpenses = view.findViewById(R.id.btnViewExpenses);
        Button btnViewCalculations = view.findViewById(R.id.btnCalculations);
        Button btnHelp = view.findViewById(R.id.btnHelp);
        Button btnAbout = view.findViewById(R.id.btnAbout);
        btnAddExpenses.setOnClickListener(view1 -> Navigation.findNavController(view1).navigate(R.id.action_menu2_to_addExpenses));

        btnAddSalary.setOnClickListener(view12 -> Navigation.findNavController(view12).navigate(R.id.action_menu2_to_addSalary));

        btnViewExpenses.setOnClickListener(view13 -> Navigation.findNavController(view13).navigate(R.id.action_menu2_to_showExpensesTable));

        btnViewCalculations.setOnClickListener(view14 -> Navigation.findNavController(view14).navigate(R.id.action_menu2_to_viewCalculations));

        btnHelp.setOnClickListener(view15 -> Navigation.findNavController(view15).navigate(R.id.action_menu2_to_help));

        btnAbout.setOnClickListener(view16 -> Navigation.findNavController(view16).navigate(R.id.action_menu2_to_about));

        return view;
    }
}