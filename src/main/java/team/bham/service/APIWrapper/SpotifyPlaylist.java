package team.bham.service.APIWrapper;

import java.util.ArrayList;

public class SpotifyPlaylist {

    private String playlistId;
    private String snapshotId;
    private String name;
    private String playlistImageSmall;
    private String playlistImageMedium;
    private String playlistImageLarge;
    private ArrayList<SpotifyTrack> tracks;

    public SpotifyPlaylist() {
        tracks = new ArrayList<SpotifyTrack>();
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTrack(SpotifyTrack track) {
        tracks.add(track);
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public String getName() {
        return name;
    }

    public ArrayList<SpotifyTrack> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<SpotifyTrack> tracks) {
        this.tracks = tracks;
    }

    public String getPlaylistImageSmall() {
        return playlistImageSmall;
    }

    public void setPlaylistImageSmall(String playlistImageSmall) {
        this.playlistImageSmall = playlistImageSmall;
    }

    public String getPlaylistImageMedium() {
        return playlistImageMedium;
    }

    public void setPlaylistImageMedium(String playlistImageMedium) {
        this.playlistImageMedium = playlistImageMedium;
    }

    public String getPlaylistImageLarge() {
        return playlistImageLarge;
    }

    public void setPlaylistImageLarge(String playlistImageLarge) {
        this.playlistImageLarge = playlistImageLarge;
    }
}
