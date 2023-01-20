package com.projeto.airbender.models;

public class Flight {
    private int id;
    private String departureDate;
    private String duration;
    private int airplane_id;
    private int airportDeparture_id;
    private int airportArrival_id;
    private String status;


    public Flight(int id, String departureDate, String duration, int airplane_id, int airportDeparture_id, int airportArrival_id, String status) {
        this.id = id;
        this.departureDate = departureDate;
        this.duration = duration;
        this.airplane_id = airplane_id;
        this.airportDeparture_id = airportDeparture_id;
        this.airportArrival_id = airportArrival_id;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getAirplane() {
        return airplane_id;
    }

    public void setAirplane(int airplane_id) {
        this.airplane_id = airplane_id;
    }

    public int getAirportDeparture() {
        return airportDeparture_id;
    }

    public void setAirportDeparture(int airportDeparture_id) {
        this.airportDeparture_id = airportDeparture_id;
    }

    public int getAirportArrival() {
        return airportArrival_id;
    }

    public void setAirportArrival(int airportArrival_id) {
        this.airportArrival_id = airportArrival_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
