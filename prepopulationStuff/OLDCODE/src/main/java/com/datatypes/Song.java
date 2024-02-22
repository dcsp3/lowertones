package com.datatypes;

import java.time.LocalDate;

public class Song {
    private int songID;
    private String songTitle;
    private String spotifyURI;
    private String geniusID;

    private String[] artistsName;
    private String[] artistsURI;
    private String[] artistsID;
    private String[] producers;
    private String[] producerIDs;
    private String[] writers;

    private String albumTitle;
    private String albumURI;
    private LocalDate releaseDate;

    private int popularity;
    private boolean explicit;
    private int duration;

    public Song(String songTitle, String spotifyURI, String[] artistsName, String[] artistsURI, String albumTitle, String albumURI, LocalDate releaseDate, int popularity, boolean explicit, int duration) {
        this.songTitle = songTitle;
        this.spotifyURI = spotifyURI;
        this.artistsName = artistsName;
        this.artistsURI = artistsURI;
        this.albumTitle = albumTitle;
        this.albumURI = albumURI;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.explicit = explicit;
        this.duration = duration;
    }

    public int getSongID() { return songID; }
    public void setSongID(int songID) { this.songID = songID; }
    public String getSongTitle() {
        return songTitle;
    }
    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }
    public String getSpotifyURI() {
        return spotifyURI;
    }
    public void setSpotifyURI(String spotifyURI) {
        this.spotifyURI = spotifyURI;
    }
    public String getGeniusID() {
        return geniusID;
    }
    public void setGeniusID(String geniusID) {
        this.geniusID = geniusID;
    }
    public String[] getArtistsName() { return artistsName; }
    public void setArtistsName(String[] artistsName) { this.artistsName = artistsName; }
    public String[] getArtistsURI() { return artistsURI; }
    public void setArtistsURI(String[] artistsURI) { this.artistsURI = artistsURI; }
    public String[] getArtistsID() { return artistsID; }
    public void setArtistsID(String[] artistsID) { this.artistsID = artistsID; }
    public String[] getProducers() {
        return producers;
    }
    public void setProducers(String[] producers) {
        this.producers = producers;
    }
    public String[] getProducerIDs() {
        return producerIDs;
    }
    public void setProducerIDs(String[] producerIDs) {
        this.producerIDs = producerIDs;
    }
    public String[] getWriters() {
        return writers;
    }
    public void setWriters(String[] writers) {
        this.writers = writers;
    }
    public String getAlbumTitle() {
        return albumTitle;
    }
    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }
    public String getAlbumURI() {
        return albumURI;
    }
    public void setAlbumURI(String albumURI) {
        this.albumURI = albumURI;
    }
    public LocalDate getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
    public int getPopularity() {
        return popularity;
    }
    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }
    public boolean isExplicit() {
        return explicit;
    }
    public void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
}
