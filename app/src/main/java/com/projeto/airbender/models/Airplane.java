package com.projeto.airbender.models;

public class Airplane {
    private int id;
    private int luggageCappacity;
    private int minLinha;
    private char minCol;
    private int maxLinha;
    private char maxCol;
    private char economicStart;
    private char economicStop;
    private char normalStart;
    private char normalStop;
    private char luxuryStart;
    private char luxuryStop;
    private String status;

    public int getId() {
        return id;
    }

    public int getLuggageCappacity() {
        return luggageCappacity;
    }

    public void setLuggageCappacity(int luggageCappacity) {
        this.luggageCappacity = luggageCappacity;
    }

    public int getMinLinha() {
        return minLinha;
    }

    public void setMinLinha(int minLinha) {
        this.minLinha = minLinha;
    }

    public char getMinCol() {
        return minCol;
    }

    public void setMinCol(char minCol) {
        this.minCol = minCol;
    }

    public int getMaxLinha() {
        return maxLinha;
    }

    public void setMaxLinha(int maxLinha) {
        this.maxLinha = maxLinha;
    }

    public char getMaxCol() {
        return maxCol;
    }

    public void setMaxCol(char maxCol) {
        this.maxCol = maxCol;
    }

    public char getEconomicStart() {
        return economicStart;
    }

    public void setEconomicStart(char economicStart) {
        this.economicStart = economicStart;
    }

    public char getEconomicStop() {
        return economicStop;
    }

    public void setEconomicStop(char economicStop) {
        this.economicStop = economicStop;
    }

    public char getNormalStart() {
        return normalStart;
    }

    public void setNormalStart(char normalStart) {
        this.normalStart = normalStart;
    }

    public char getNormalStop() {
        return normalStop;
    }

    public void setNormalStop(char normalStop) {
        this.normalStop = normalStop;
    }

    public char getLuxuryStart() {
        return luxuryStart;
    }

    public void setLuxuryStart(char luxuryStart) {
        this.luxuryStart = luxuryStart;
    }

    public char getLuxuryStop() {
        return luxuryStop;
    }

    public void setLuxuryStop(char luxuryStop) {
        this.luxuryStop = luxuryStop;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Airplane(int luggageCappacity, int minLinha, char minCol, int maxLinha, char maxCol, char economicStart, char economicStop, char normalStart, char normalStop, char luxuryStart, char luxuryStop, String status) {
        this.luggageCappacity = luggageCappacity;
        this.minLinha = minLinha;
        this.minCol = minCol;
        this.maxLinha = maxLinha;
        this.maxCol = maxCol;
        this.economicStart = economicStart;
        this.economicStop = economicStop;
        this.normalStart = normalStart;
        this.normalStop = normalStop;
        this.luxuryStart = luxuryStart;
        this.luxuryStop = luxuryStop;
        this.status = status;
    }



}
