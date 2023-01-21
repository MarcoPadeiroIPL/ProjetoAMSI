package com.projeto.airbender.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.projeto.airbender.R;
import com.projeto.airbender.models.BalanceReq;
import com.projeto.airbender.models.Ticket;
import com.projeto.airbender.models.TicketInfo;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {

    private ArrayList<TicketInfo> localDataSet;
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView airportDeparture, airportArrival, date;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            airportDeparture = (TextView) view.findViewById(R.id.tvAirportDeparture);
            airportArrival = (TextView) view.findViewById(R.id.tvAirportArrival);
            date = (TextView) view.findViewById(R.id.tvDate);
        }

        public TextView getAirportDeparture() {
            return airportDeparture;
        }

        public TextView getAirportArrival() {
            return airportArrival;
        }

        public TextView getDate() {
            return date;
        }

    }

    public TicketAdapter(ArrayList<TicketInfo> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ticket_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getAirportDeparture().setText(localDataSet.get(position).getAirportDeparture().getCity());
        viewHolder.getAirportArrival().setText(localDataSet.get(position).getAirportArrival().getCity());
        viewHolder.getDate().setText(localDataSet.get(position).getFlight().getDepartureDate());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

