package com.projeto.airbender.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.projeto.airbender.R;
import com.projeto.airbender.activities.FlightActivity;
import com.projeto.airbender.listeners.FlightListener;
import com.projeto.airbender.models.Flight;
import com.projeto.airbender.models.FlightInfo;
import com.projeto.airbender.models.SingletonAirbender;

public class ConfirmFlightFragment extends Fragment implements FlightListener {
    private String airportDeparture, airportArrival, departureDate;
    private TextView tvFlightID;
    private FloatingActionButton fabBack;

    public ConfirmFlightFragment(String airportDeparture, String airportArrival, String departureDate) {
        this.airportDeparture = airportDeparture;
        this.airportArrival = airportArrival;
        this.departureDate = departureDate;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_flight, container, false);
        fabBack = view.findViewById(R.id.fabBack);

        SingletonAirbender.getInstance(getContext()).setFlightListener(this);
        SingletonAirbender.getInstance(getContext()).requestFlightAPI(getContext(), airportDeparture, airportArrival, departureDate);

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getActivity()).onBackPressed();
            }
        });

        return view;
    }

    @Override
    public void onRefreshFlight(FlightInfo flight) {
        if(flight != null) {
            Toast.makeText(getContext(), "Flight ID: " + flight.getFlight().getId(), Toast.LENGTH_SHORT).show();
            System.out.println("Flight ID: " + flight.getFlight().getId());
        } else {
            Toast.makeText(getContext(), "No flights found", Toast.LENGTH_SHORT).show();
            ((FlightActivity)getActivity()).onBackPressed();
        }
    }
}