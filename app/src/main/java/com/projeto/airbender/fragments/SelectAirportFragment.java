package com.projeto.airbender.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.projeto.airbender.R;
import com.projeto.airbender.activities.FlightActivity;
import com.projeto.airbender.activities.MainActivity;
import com.projeto.airbender.adapters.AirportAdapter;
import com.projeto.airbender.adapters.BalanceReqAdapter;
import com.projeto.airbender.listeners.AirportListener;
import com.projeto.airbender.models.Airport;
import com.projeto.airbender.models.BalanceReq;
import com.projeto.airbender.models.SingletonAirbender;

import java.util.ArrayList;
import java.util.Calendar;

public class SelectAirportFragment extends Fragment implements AirportListener {
    private ArrayList<String> airports;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView autoCompleteTextViewDeparture;
    private AutoCompleteTextView autoCompleteTextViewArrival;
    private AutoCompleteTextView autoCompleteTextViewDate;
    private FloatingActionButton fabBack;
    private Button btnSearch;

    public SelectAirportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_airport, container, false);
        autoCompleteTextViewDeparture = view.findViewById(R.id.autoCompleteTextViewDeparture);
        autoCompleteTextViewArrival = view.findViewById(R.id.autoCompleteTextViewArrival);
        autoCompleteTextViewDate = view.findViewById(R.id.autoCompleteTextViewDate);
        fabBack = view.findViewById(R.id.fabBack);
        btnSearch = view.findViewById(R.id.btnSearch);

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getActivity()).onBackPressed();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                );

                fragmentTransaction.replace(R.id.frameLayout, new ConfirmFlightFragment(autoCompleteTextViewDeparture.getText().toString(), autoCompleteTextViewArrival.getText().toString(), autoCompleteTextViewDate.getText().toString()));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        SingletonAirbender.getInstance(getContext()).setAirportListener(this);
        SingletonAirbender.getInstance(getContext()).requestAirportsAPI(getContext());


        autoCompleteTextViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                autoCompleteTextViewDate.setText(i + "-" + (i1 + 1) + "-" + i2);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        autoCompleteTextViewDeparture.setAdapter(adapter);
        autoCompleteTextViewArrival.setAdapter(adapter);
    }

    @Override
    public void onRefreshAirports(ArrayList<Airport> temp) {
        airports = new ArrayList<String>();
        for (Airport airport : temp) {
            airports.add(airport.getCity());
        }

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, airports);
        autoCompleteTextViewArrival.setAdapter(adapter);
        autoCompleteTextViewDeparture.setAdapter(adapter);
    }
}