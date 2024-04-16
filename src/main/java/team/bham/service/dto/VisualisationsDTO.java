package team.bham.service.dto;

// This class is a Data Transfer Object (DTO) for the VisualisationsService class
// It's mostly for returning all the data at once to minimise the number of requests

public class VisualisationsDTO {

    private float topArtist1Percent;
    private float topArtist2Percent;
    private float topArtist3Percent;
    private float topArtist4Percent;
    private float topArtist5Percent;

    private String topArtist1Name;
    private String topArtist2Name;
    private String topArtist3Name;
    private String topArtist4Name;
    private String topArtist5Name;

    public VisualisationsDTO() {}

    public float getTopArtist1Percent() {
        return topArtist1Percent;
    }

    public void setTopArtist1Percent(float topArtist1Percent) {
        this.topArtist1Percent = topArtist1Percent;
    }

    public float getTopArtist2Percent() {
        return topArtist2Percent;
    }

    public void setTopArtist2Percent(float topArtist2Percent) {
        this.topArtist2Percent = topArtist2Percent;
    }

    public float getTopArtist3Percent() {
        return topArtist3Percent;
    }

    public void setTopArtist3Percent(float topArtist3Percent) {
        this.topArtist3Percent = topArtist3Percent;
    }

    public float getTopArtist4Percent() {
        return topArtist4Percent;
    }

    public void setTopArtist4Percent(float topArtist4Percent) {
        this.topArtist4Percent = topArtist4Percent;
    }

    public float getTopArtist5Percent() {
        return topArtist5Percent;
    }

    public void setTopArtist5Percent(float topArtist5Percent) {
        this.topArtist5Percent = topArtist5Percent;
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
