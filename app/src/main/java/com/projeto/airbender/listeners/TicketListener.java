package com.projeto.airbender.listeners;

import com.projeto.airbender.models.BalanceReq;
import com.projeto.airbender.models.Ticket;

import java.util.ArrayList;

public interface TicketListener {
    void onRefreshTicketList(ArrayList<Ticket> tickets);
}
