package com.datatypes;

public class GeniusInfo {
    private int geniusID;
    private String[] producersName;
    private String[] writersName;

    public GeniusInfo(int geniusID, String[] producersName, String[] writersName) {
        this.geniusID = geniusID;
        this.producersName = producersName;
        this.writersName = writersName;
    }

    public GeniusInfo() {
        geniusID = 0;
        producersName = new String[]{};
        writersName = new String[]{};
    }

    public int getGeniusID() {
        return geniusID;
    }

    public void setGeniusID(int geniusID) {
        this.geniusID = geniusID;
    }

    public String[] getProducersName() {
        return producersName;
    }

    public void setProducersName(String[] producersName) {
        this.producersName = producersName;
    }

    public String[] getWritersName() {
        return writersName;
    }

    public void setWritersName(String[] writersName) {
        this.writersName = writersName;
    }
}
