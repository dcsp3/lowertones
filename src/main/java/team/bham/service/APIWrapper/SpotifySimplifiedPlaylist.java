package team.bham.service.APIWrapper;

import java.util.ArrayList;

public class SpotifySimplifiedPlaylist {

    private String name;
    private String spotifyId;
    private String snapshotId;
    private ArrayList<SpotifyImage> images;

    public SpotifySimplifiedPlaylist() {
        this.images = new ArrayList<>();
    }

    public SpotifySimplifiedPlaylist(String name, String spotifyId, String snapshotId) {
        this.name = name;
        this.spotifyId = spotifyId;
        this.snapshotId = snapshotId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public void addImage(SpotifyImage image) {
        this.images.add(image);
    }

    public String getName() {
        return name;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public ArrayList<SpotifyImage> getImages() {
        return images;
    }
}
