package team.bham.service.APIWrapper;

import java.util.ArrayList;

public class SpotifyPlaylist {

    private String playlistId;
    private String snapshotId;
    private ArrayList<SpotifyTrack> tracks;

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
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

    public ArrayList<SpotifyTrack> getTracks() {
        return tracks;
    }
}
