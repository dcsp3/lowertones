package team.bham.service.dto;

// This class is a Data Transfer Object (DTO) for the VisualisationsService class
// It's mostly for returning all the data at once to minimise the number of requests

public class VisualisationsDTO {

    private int numOfSongs;

    // variables for the top 5 artists
    private int topArtist1Count;
    private int topArtist2Count;
    private int topArtist3Count;
    private int topArtist4Count;
    private int topArtist5Count;

    private String topArtist1Name;
    private String topArtist2Name;
    private String topArtist3Name;
    private String topArtist4Name;
    private String topArtist5Name;

    // variables for the top 5 genres
    private int topGenre1Percent;
    private int topGenre2Percent;
    private int topGenre3Percent;
    private int topGenre4Percent;
    private int topGenre5Percent;

    private String topGenre1Name;
    private String topGenre2Name;
    private String topGenre3Name;
    private String topGenre4Name;
    private String topGenre5Name;

    // variables for the features of the users liked songs
    private float Avgpopularity;
    private float AvgDanceability;
    private float AvgEnergy;
    private float AvgAcousticness;
    private float AvgTempo;

    // Constructor
    public VisualisationsDTO() {}

    // Getters and Setters of the above variables
    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public float getTopArtist1Count() {
        return topArtist1Count;
    }

    public void setTopArtist1Count(int topArtist1Count) {
        this.topArtist1Count = topArtist1Count;
    }

    public float getTopArtist2Count() {
        return topArtist2Count;
    }

    public void setTopArtist2Count(int topArtist2Count) {
        this.topArtist2Count = topArtist2Count;
    }

    public float getTopArtist3Count() {
        return topArtist3Count;
    }

    public void setTopArtist3Count(int topArtist3Count) {
        this.topArtist3Count = topArtist3Count;
    }

    public float getTopArtist4Count() {
        return topArtist4Count;
    }

    public void setTopArtist4Count(int topArtist4Count) {
        this.topArtist4Count = topArtist4Count;
    }

    public float getTopArtist5Count() {
        return topArtist5Count;
    }

    public void setTopArtist5Count(int topArtist5Count) {
        this.topArtist5Count = topArtist5Count;
    }

    public String getTopArtist1Name() {
        return topArtist1Name;
    }

    public void setTopArtist1Name(String topArtist1Name) {
        this.topArtist1Name = topArtist1Name;
    }

    public String getTopArtist2Name() {
        return topArtist2Name;
    }

    public void setTopArtist2Name(String topArtist2Name) {
        this.topArtist2Name = topArtist2Name;
    }

    public String getTopArtist3Name() {
        return topArtist3Name;
    }

    public void setTopArtist3Name(String topArtist3Name) {
        this.topArtist3Name = topArtist3Name;
    }

    public String getTopArtist4Name() {
        return topArtist4Name;
    }

    public void setTopArtist4Name(String topArtist4Name) {
        this.topArtist4Name = topArtist4Name;
    }

    public String getTopArtist5Name() {
        return topArtist5Name;
    }

    public void setTopArtist5Name(String topArtist5Name) {
        this.topArtist5Name = topArtist5Name;
    }
}
