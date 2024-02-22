package com.datatypes;

public class Playlist {
    private String playlistName;
    private Song[] playlistContents;
    private String playlistOwnerName;
    private String playlistOwnerID;
    private int numOfSongs;

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public Song[] getPlaylistContents() {
        return playlistContents;
    }

    public void setPlaylistContents(Song[] playlistContents) {
        this.playlistContents = playlistContents;
    }

    public String getPlaylistOwnerName() {
        return playlistOwnerName;
    }

    public void setPlaylistOwnerName(String playlistOwnerName) {
        this.playlistOwnerName = playlistOwnerName;
    }

    public String getPlaylistOwnerID() {
        return playlistOwnerID;
    }

    public void setPlaylistOwnerID(String playlistOwnerID) {
        this.playlistOwnerID = playlistOwnerID;
    }

    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }
}
