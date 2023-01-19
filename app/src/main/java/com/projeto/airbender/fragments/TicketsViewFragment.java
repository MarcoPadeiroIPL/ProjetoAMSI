package com.projeto.airbender.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projeto.airbender.R;
import com.projeto.airbender.adapters.BalanceReqAdapter;
import com.projeto.airbender.adapters.TicketAdapter;
import com.projeto.airbender.listeners.TicketListener;
import com.projeto.airbender.models.BalanceReq;
import com.projeto.airbender.models.SingletonAirbender;
import com.projeto.airbender.models.Ticket;

import java.util.ArrayList;

// Instances of this class are fragments representing a single
// object in our collection.
public class TicketsViewFragment extends Fragment implements TicketListener {
    public static final String ARG_OBJECT = "object";
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tickets_view_fragment, container, false);

        recyclerView = view.findViewById(R.id.rvTickets);

        SingletonAirbender.getInstance(getContext()).setTicketListener(this);

        recyclerView.setAdapter(new TicketAdapter(new ArrayList<Ticket>()));

        SingletonAirbender.getInstance(getContext()).getTickets(getContext(), getArguments().getInt(ARG_OBJECT));

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onRefreshTicketList(ArrayList<Ticket> tickets) {
        recyclerView.setAdapter(new TicketAdapter(tickets));
    }
}
