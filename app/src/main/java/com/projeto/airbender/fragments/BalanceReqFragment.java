package com.projeto.airbender.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.projeto.airbender.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import com.projeto.airbender.adapters.BalanceReqAdapter;
import com.projeto.airbender.listeners.BalanceReqListener;
import com.projeto.airbender.models.BalanceReq;
import com.projeto.airbender.models.SingletonAirbender;

public class BalanceReqFragment extends Fragment implements BalanceReqListener {
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddBalanceReq;

    public BalanceReqFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balance_req, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        SingletonAirbender.getInstance(getContext()).setBalanceReqListener(this);

        SingletonAirbender.getInstance(getContext()).getAllBalanceReqs(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(new BalanceReqAdapter(new ArrayList<BalanceReq>()));

        fabAddBalanceReq = view.findViewById(R.id.fabAddBalanceReq);
        fabAddBalanceReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_ask_balance);

                final EditText etAmount = dialog.findViewById(R.id.etAmount);
                final Button btnOK = dialog.findViewById(R.id.btnOK);

                btnOK.setOnClickListener((v) -> {

                    if (etAmount.getText().toString().isEmpty()) {
                        etAmount.setError("Amount is required");
                        etAmount.requestFocus();
                        return;
                    }

                    int amount = Integer.parseInt(etAmount.getText().toString());

                    SingletonAirbender.getInstance(getContext()).addBalanceReq(amount, getContext());

                    dialog.dismiss();
                });

                dialog.show();
            }
        });

        return view;
    }

    public ArrayList<String> getItemsList() {
        ArrayList<String> items = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            items.add("Item " + i);
        }

        return items;
    }

    @Override
    public void onRefreshBalanceReqList(ArrayList<BalanceReq> balanceReqs) {
        recyclerView.setAdapter(new BalanceReqAdapter(balanceReqs));
    }
}