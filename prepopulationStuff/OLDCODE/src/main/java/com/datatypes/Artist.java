package com.datatypes;

public class Artist {
    private String artistName;
    private String artistURI;
    private String artistID;
    private int artistType; //0 for undefined, 1 for artist, 2 for producer, 3 for both

    private Song[] listOfSongs;
    private int[] roleInSongs; //0 for undefined, 1 for artist, 2 for producer, 3 for both

    private String[] songTitles;
    private String[] songIDs;
    private String[] songSpotifyURIs;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistURI() {
        return artistURI;
    }

    public void setArtistURI(String artistURI) {
        this.artistURI = artistURI;
    }

    public String getArtistID() {
        return artistID;
    }

    public void setArtistID(String artistID) {
        this.artistID = artistID;
    }

    public int getArtistType() {
        return artistType;
    }

    public void setArtistType(int artistType) {
        this.artistType = artistType;
    }

    public Song[] getListOfSongs() {
        return listOfSongs;
    }

    public void setListOfSongs(Song[] listOfSongs) {
        this.listOfSongs = listOfSongs;
    }

    public int[] getRoleInSongs() {
        return roleInSongs;
    }

    public void setRoleInSongs(int[] roleInSongs) {
        this.roleInSongs = roleInSongs;
    }

    public String[] getSongTitles() {
        return songTitles;
    }

    public void setSongTitles(String[] songTitles) {
        this.songTitles = songTitles;
    }

    public String[] getSongIDs() {
        return songIDs;
    }

    public void setSongIDs(String[] songIDs) {
        this.songIDs = songIDs;
    }

    public String[] getSongSpotifyURIs() {
        return songSpotifyURIs;
    }

    public void setSongSpotifyURIs(String[] songSpotifyURIs) {
        this.songSpotifyURIs = songSpotifyURIs;
    }
}