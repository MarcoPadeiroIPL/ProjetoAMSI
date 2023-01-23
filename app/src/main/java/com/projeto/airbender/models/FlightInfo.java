package com.projeto.airbender.models;

public class FlightInfo {
    private Flight flight;
    private Tariff tariff;
    private Airplane airplane;

    public FlightInfo(Flight flight, Tariff tariff, Airplane airplane) {
        this.flight = flight;
        this.tariff = tariff;
        this.airplane = airplane;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    public Airplane getAirplane() {
        return airplane;
    }

    public void setAirplane(Airplane airplane) {
        this.airplane = airplane;
    }
}
