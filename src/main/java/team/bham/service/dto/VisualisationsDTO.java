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

    public int getTopGenre1Percent() {
        return topGenre1Percent;
    }

    public void setTopGenre1Percent(int topGenre1Percent) {
        this.topGenre1Percent = topGenre1Percent;
    }

    public int getTopGenre2Percent() {
        return topGenre2Percent;
    }

    public void setTopGenre2Percent(int topGenre2Percent) {
        this.topGenre2Percent = topGenre2Percent;
    }

    public int getTopGenre3Percent() {
        return topGenre3Percent;
    }

    public void setTopGenre3Percent(int topGenre3Percent) {
        this.topGenre3Percent = topGenre3Percent;
    }

    public int getTopGenre4Percent() {
        return topGenre4Percent;
    }

    public void setTopGenre4Percent(int topGenre4Percent) {
        this.topGenre4Percent = topGenre4Percent;
    }

    public int getTopGenre5Percent() {
        return topGenre5Percent;
    }

    public void setTopGenre5Percent(int topGenre5Percent) {
        this.topGenre5Percent = topGenre5Percent;
    }

    public String getTopGenre1Name() {
        return topGenre1Name;
    }

    public void setTopGenre1Name(String topGenre1Name) {
        this.topGenre1Name = topGenre1Name;
    }

    public String getTopGenre2Name() {
        return topGenre2Name;
    }

    public void setTopGenre2Name(String topGenre2Name) {
        this.topGenre2Name = topGenre2Name;
    }

    public String getTopGenre3Name() {
        return topGenre3Name;
    }

    public void setTopGenre3Name(String topGenre3Name) {
        this.topGenre3Name = topGenre3Name;
    }

    public String getTopGenre4Name() {
        return topGenre4Name;
    }

    public void setTopGenre4Name(String topGenre4Name) {
        this.topGenre4Name = topGenre4Name;
    }

    public String getTopGenre5Name() {
        return topGenre5Name;
    }

    public void setTopGenre5Name(String topGenre5Name) {
        this.topGenre5Name = topGenre5Name;
    }

    public float getAvgpopularity() {
        return Avgpopularity;
    }

    public void setAvgpopularity(float avgpopularity) {
        Avgpopularity = avgpopularity;
    }

    public float getAvgDanceability() {
        return AvgDanceability;
    }

    public void setAvgDanceability(float avgDanceability) {
        AvgDanceability = avgDanceability;
    }

    public float getAvgEnergy() {
        return AvgEnergy;
    }

    public void setAvgEnergy(float avgEnergy) {
        AvgEnergy = avgEnergy;
    }

    public float getAvgAcousticness() {
        return AvgAcousticness;
    }

    public void setAvgAcousticness(float avgAcousticness) {
        AvgAcousticness = avgAcousticness;
    }

    public float getAvgTempo() {
        return AvgTempo;
    }

    public void setAvgTempo(float avgTempo) {
        AvgTempo = avgTempo;
    }

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
