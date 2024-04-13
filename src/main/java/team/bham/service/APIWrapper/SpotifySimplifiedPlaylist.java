package team.bham.service.APIWrapper;

public class SpotifySimplifiedPlaylist {

    private String name;
    private String spotifyId;
    private String snapshotId;

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

    public String getName() {
        return name;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public String getSnapshotId() {
        return snapshotId;
    }
}
