package team.bham.service.APIWrapper;

public class SpotifySimplifiedPlaylist {

    private String name;
    private String spotifyId;

    public SpotifySimplifiedPlaylist(String name, String spotifyId) {
        this.name = name;
        this.spotifyId = spotifyId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getName() {
        return name;
    }

    public String getSpotifyId() {
        return spotifyId;
    }
}
