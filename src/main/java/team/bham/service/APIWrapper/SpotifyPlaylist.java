package team.bham.service.APIWrapper;

import java.util.ArrayList;

public class SpotifyPlaylist {

    private String playlistId;
    private String snapshotId;
    private String name;
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
}
