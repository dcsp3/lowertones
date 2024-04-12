package team.bham.service.APIWrapper;

public class SpotifyArtist {

    private String name;
    private String spotifyId;
    private int popularity;

    //todo: more shit for this (img, etc.)

    public void setName(String name) {
        this.name = name;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
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
}
