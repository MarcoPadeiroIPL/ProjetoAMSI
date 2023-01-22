package com.projeto.airbender.utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.projeto.airbender.models.Airport;
import com.projeto.airbender.models.BalanceReq;
import com.projeto.airbender.models.Flight;
import com.projeto.airbender.models.Ticket;
import com.projeto.airbender.models.TicketInfo;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "airbender";
    public static final int DB_VERSION = 1;
    private final SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String airports = "CREATE TABLE airports ( " +
                "id INTEGER PRIMARY KEY, " +
                "country VARCHAR NOT NULL," +
                "code VARCHAR NOT NULL, " +
                "city VARCHAR NOT NULL, " +
                "search INT NOT NULL, " +
                "status VARCHAR NOT NULL " +
                ");";
        db.execSQL(airports);
        String flights = "CREATE TABLE flights ( " +
                "id INTEGER PRIMARY KEY, " +
                "departureDate VARCHAR NOT NULL," +
                "duration VARCHAR NOT NULL, " +
                "airplane_id INTEGER NOT NULL, " +
                "airportDeparture_id INTEGER NOT NULL, " +
                "airportArrival_id INTEGER NOT NULL, " +
                "status VARCHAR NOT NULL, " +
                "FOREIGN KEY (airportDeparture_id) REFERENCES airports(id), " +
                "FOREIGN KEY (airportArrival_id) REFERENCES airports(id) " +
                ");";
        db.execSQL(flights);
        String airportsTickets = "CREATE TABLE airportsTickets ( " +
                "id INTEGER PRIMARY KEY, " +
                "country VARCHAR NOT NULL," +
                "code VARCHAR NOT NULL, " +
                "city VARCHAR NOT NULL, " +
                "search INT NOT NULL, " +
                "status VARCHAR NOT NULL " +
                ");";
        db.execSQL(airportsTickets);
        String flightsTickets = "CREATE TABLE flightsTickets ( " +
                "id INTEGER PRIMARY KEY, " +
                "departureDate VARCHAR NOT NULL," +
                "duration VARCHAR NOT NULL, " +
                "airplane_id INTEGER NOT NULL, " +
                "airportDeparture_id INTEGER NOT NULL, " +
                "airportArrival_id INTEGER NOT NULL, " +
                "status VARCHAR NOT NULL, " +
                "FOREIGN KEY (airportDeparture_id) REFERENCES airportsTickets(id), " +
                "FOREIGN KEY (airportArrival_id) REFERENCES airportsTickets(id) " +
                ");";
        db.execSQL(flightsTickets);
        String tickets = "CREATE TABLE tickets ( " +
                "id INTEGER PRIMARY KEY, " +
                "fName TEXT NOT NULL," +
                "surname TEXT NOT NULL, " +
                "age INTEGER NOT NULL, " +
                "gender VARCHAR NOT NULL, " +
                "checkedIn INTEGER, " +
                "client_id INTEGER NOT NULL, " +
                "flight_id INTEGER NOT NULL, " +
                "seatLinha INTEGER NOT NULL, " +
                "seatCol CHAR NOT NULL, " +
                "luggage_1 INTEGER, " +
                "luggage_2 INTEGER, " +
                "receipt_id INTEGER NOT NULL, " +
                "tariff_id INTEGER NOT NULL, " +
                "tariffType VARCHAR NOT NULL, " +
                "type VARCHAR NOT NULL, " +
                "FOREIGN KEY (flight_id) REFERENCES flightsTickets(id) " +
                ");";
        db.execSQL(tickets);
        String ticketsOffline = "CREATE TABLE ticketsOffline ( " +
                "id INTEGER PRIMARY KEY, " +
                "fName TEXT NOT NULL," +
                "surname TEXT NOT NULL, " +
                "age INTEGER NOT NULL, " +
                "gender VARCHAR NOT NULL, " +
                "checkedIn INTEGER, " +
                "client_id INTEGER NOT NULL, " +
                "flight_id INTEGER NOT NULL, " +
                "seatLinha INTEGER NOT NULL, " +
                "seatCol CHAR NOT NULL, " +
                "luggage_1 INTEGER, " +
                "luggage_2 INTEGER, " +
                "receipt_id INTEGER NOT NULL, " +
                "tariff_id INTEGER NOT NULL, " +
                "tariffType VARCHAR NOT NULL, " +
                "FOREIGN KEY (flight_id) REFERENCES flightsTickets(id) " +
                ");";
        db.execSQL(ticketsOffline);
        String balanceRequests = "CREATE TABLE balanceReq (" +
                "id INTEGER PRIMARY KEY, "  +
                "amount DOUBLE NOT NULL,"  +
                "status VARCHAR NOT NULL, " +
                "requestDate VARCHAR NOT NULL, " +
                "decisionDate VARCHAR, " +
                "client_id INTEGER NOT NULL);";
        db.execSQL(balanceRequests);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String flights= "DROP TABLE IF EXISTS flights;";
        db.execSQL(flights);
        String airports= "DROP TABLE IF EXISTS airports;";
        db.execSQL(airports);
        String flightsTickets = "DROP TABLE IF EXISTS flightsTickets;";
        db.execSQL(flightsTickets);
        String airportsTickets = "DROP TABLE IF EXISTS airportsTickets;";
        db.execSQL(airportsTickets);
        String tickets = "DROP TABLE IF EXISTS tickets;";
        db.execSQL(tickets);
        String balanceReq = "DROP TABLE IF EXISTS balanceReq;";
        db.execSQL(balanceReq);
        this.onCreate(db);
    }

    public boolean insertDB(String table, ContentValues values) {
        long id = db.insert(table, null, values);
        return (id >= -1);
    }

    public boolean updateDB(String table, ContentValues values) {
        return db.update(table, values, "id = ?", new String[]{String.valueOf((values.getAsInteger("id")))}) == 1;
    }

    public boolean deleteDB(String table, int id) {
        return db.delete(table, "id = ?", new String[]{String.valueOf(id)}) == 1;
    }

    public void deleteAllDB(String table) {
        db.delete(table, null, null);
    }

    public ArrayList<Flight> getAllFlightsTicketsDB() {
        ArrayList<Flight> flights = new ArrayList<Flight>();
        Cursor cursor = db.query("flightsTickets", new String[]{
                "id",
                "departureDate",
                "duration",
                "airplane_id",
                "airportDeparture_id",
                "airportArrival_id",
                "status"
        }, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Flight flight = new Flight(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(3), cursor.getInt(4), cursor.getString(5));
                flights.add(flight);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return flights;
    }

    public ArrayList<Airport> getAllAirportsDB() {
        ArrayList<Airport> airports = new ArrayList<Airport>();
        Cursor cursor = db.query("airportsTickets", new String[]{
                "id",
                "country",
                "code",
                "city",
                "search",
                "status"
        }, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Airport airport = new Airport(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(3), cursor.getString(4));
                airports.add(airport);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return airports;
    }

    public ArrayList<Airport> getAllAirportsTicketsDB() {
        ArrayList<Airport> airports = new ArrayList<Airport>();
        Cursor cursor = db.query("airportsTickets", new String[]{
                "id",
                "country",
                "code",
                "city",
                "search",
                "status"
        }, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Airport airport = new Airport(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(3), cursor.getString(4));
                airports.add(airport);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return airports;
    }
    public ArrayList<Ticket> getAllTicketsOfflineDB() {
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        Cursor cursor = db.query("ticketsOffline", new String[]{
                "id",
                "fName",
                "surname",
                "gender",
                "age",
                "checkedIn",
                "client_id",
                "flight_id",
                "seatLinha",
                "seatCol",
                "luggage_1",
                "luggage_2",
                "receipt_id",
                "tariff_id",
                "tariffType"
        }, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Ticket ticket = new Ticket(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getString(7), cursor.getInt(8), cursor.getInt(9), cursor.getInt(10), cursor.getInt(11), cursor.getInt(12), cursor.getString(13));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return tickets;
    }
    public ArrayList<Ticket> getAllTicketsDB() {
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        Cursor cursor = db.query("tickets", new String[]{
                "id",
                "fName",
                "surname",
                "gender",
                "age",
                "checkedIn",
                "client_id",
                "flight_id",
                "seatLinha",
                "seatCol",
                "luggage_1",
                "luggage_2",
                "receipt_id",
                "tariff_id",
                "tariffType"
        }, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Ticket ticket = new Ticket(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getString(7), cursor.getInt(8), cursor.getInt(9), cursor.getInt(10), cursor.getInt(11), cursor.getInt(12), cursor.getString(13));
                tickets.add(ticket);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return tickets;
    }

    public ArrayList<BalanceReq> getAllBalanceReqDB() {
        ArrayList<BalanceReq> balanceReqs = new ArrayList<BalanceReq>();
        Cursor cursor = db.query("balanceReq", new String[]{
                "id",
                "amount",
                "requestDate",
                "decisionDate",
                "status",
                "client_id",
        }, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                BalanceReq balanceReq = new BalanceReq(cursor.getInt(0), cursor.getDouble(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5));
                balanceReqs.add(balanceReq);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return balanceReqs;
    }
}
