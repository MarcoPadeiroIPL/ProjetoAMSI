package com.projeto.airbender.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.projeto.airbender.R;
import com.projeto.airbender.adapters.BalanceReqAdapter;
import com.projeto.airbender.adapters.TicketAdapter;
import com.projeto.airbender.listeners.TicketListener;
import com.projeto.airbender.models.BalanceReq;
import com.projeto.airbender.models.SingletonAirbender;
import com.projeto.airbender.models.Ticket;
import com.projeto.airbender.models.TicketInfo;

import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;

// Instances of this class are fragments representing a single
// object in our collection.
public class TicketsViewFragment extends Fragment implements TicketListener {
    public static final String ARG_OBJECT = "object";
    private RecyclerView recyclerView;
    private TextView tvTitle;
    private SwipeRefreshLayout pullToRefresh;
    private TicketAdapter ticketAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tickets_view_fragment, container, false);

        recyclerView = view.findViewById(R.id.rvTickets);
        tvTitle = view.findViewById(R.id.tvTitle);
        pullToRefresh = view.findViewById(R.id.pullToRefresh);

        tvTitle.setText(getArguments().getInt(ARG_OBJECT) == 0 ? "Upcoming" : getArguments().getInt(ARG_OBJECT) == 1 ? "Pending" : "Past");

        ticketAdapter = new TicketAdapter(new ArrayList<TicketInfo>(),this);
        recyclerView.setAdapter(ticketAdapter);

        switch (getArguments().getInt(ARG_OBJECT)) {
            case 0:
                SingletonAirbender.getInstance(getContext()).setTicketUpcomingListener(this);
                break;
            case 1:
                SingletonAirbender.getInstance(getContext()).setTicketPendingListener(this);
                break;
            case 2:
                SingletonAirbender.getInstance(getContext()).setTicketPastListener(this);
                break;
        }
        if(getArguments().getInt(ARG_OBJECT) == 0)
            SingletonAirbender.getInstance(getContext()).getTicketsFromDB(getArguments().getInt(ARG_OBJECT));
        else
            SingletonAirbender.getInstance(getContext()).requestTicketsAPI(getContext(), getArguments().getInt(ARG_OBJECT));

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SingletonAirbender.getInstance(getContext()).requestTicketsAPI(getContext(), getArguments().getInt(ARG_OBJECT));
                pullToRefresh.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onRefreshTicketList(ArrayList<TicketInfo> tickets) {
        ticketAdapter = new TicketAdapter(tickets, this);
        recyclerView.setAdapter(ticketAdapter);
    }

    @Override
    public void onItemClicked(TicketInfo ticket) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
        );

        fragmentTransaction.replace(R.id.frameLayout, new TicketDetailFragment(ticket, getArguments().getInt(ARG_OBJECT)));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


}
