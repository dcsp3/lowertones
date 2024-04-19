package team.bham.service.APIWrapper;

import java.time.LocalDate;
import team.bham.service.APIWrapper.Enums.SpotifyReleasePrecision;

public class SpotifyAlbum {

    private String albumType;
    private int numTracks;
    private String spotifyId;
    private String name;
    private LocalDate releaseDate;
    private SpotifyReleasePrecision releasePrecision;
    private String coverArtURL;

    //extended info - e.g. artist info, tracks ???
    //tracks only seem accessible from explicit GET album calls - i.e. not from querying a track or playlist etc.

    public void setAlbumType(String albumType) {
        this.albumType = albumType;
    }

    public void setNumTracks(int numTracks) {
        this.numTracks = numTracks;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setReleasePrecision(SpotifyReleasePrecision releasePrecision) {
        this.releasePrecision = releasePrecision;
    }

    public void setCoverArtURL(String coverArtURL) {
        this.coverArtURL = coverArtURL;
    }

    public String getAlbumType() {
        return albumType;
    }

    public int getNumTracks() {
        return numTracks;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public String getName() {
        return name;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public SpotifyReleasePrecision getReleasePrecision() {
        return releasePrecision;
    }

    public String getCoverArtURL() {
        return coverArtURL;
    }
}
