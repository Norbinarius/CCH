package ru.artsec.cch.model;

import java.util.ArrayList;

import ru.artsec.cch.model.SaleTicket;
import ru.artsec.cch.model.Ticket;

/**
 * Created by Norbinarius on 31.01.2018.
 */

public class PairTicketProps {

    private ArrayList<Ticket> ticketValues = new ArrayList<Ticket>();
    private ArrayList<SaleTicket> ticketSales = new ArrayList<SaleTicket>();

    public ArrayList<Ticket> getTicketValues() {
        return ticketValues;
    }

    public void setTicketValues(ArrayList<Ticket> ticketValues) {
        this.ticketValues = ticketValues;
    }

    public ArrayList<SaleTicket> getTicketSales() {
        return ticketSales;
    }

    public void setTicketSales(ArrayList<SaleTicket> ticketSales) {
        this.ticketSales = ticketSales;
    }

}
