package team.bham.service.APIWrapper;

import java.util.ArrayList;
import team.bham.service.APIWrapper.SpotifyImage;

public class SpotifyArtist {

    private String name;
    private String spotifyId;
    private int popularity;
    private ArrayList<SpotifyImage> images;

    //todo: more shit for this (img, etc.)

    public SpotifyArtist() {
        images = new ArrayList<SpotifyImage>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void addImage(SpotifyImage image) {
        images.add(image);
    }

    public String getName() {
        return name;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public int getPopularity() {
        return popularity;
    }

    public ArrayList<SpotifyImage> getImages() {
        return images;
    }
}
