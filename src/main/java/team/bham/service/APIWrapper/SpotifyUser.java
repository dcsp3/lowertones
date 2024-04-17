package team.bham.service.APIWrapper;

import java.util.ArrayList;
import team.bham.service.APIWrapper.SpotifyImage;

public class SpotifyUser {

    private String displayName;
    private String email;
    private String spotifyId;
    private ArrayList<SpotifyImage> images;

    public SpotifyUser() {
        images = new ArrayList<>();
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public void addImage(SpotifyImage image) {
        images.add(image);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public ArrayList<SpotifyImage> getImages() {
        return images;
    }
}
