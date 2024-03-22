package team.bham.service.dto;

import java.time.LocalDate;

public class RecappedRequest {

    private LocalDate startDate;
    private LocalDate endDate;
    private String dateRange;
    private MusicianType musicianType;
    private boolean scanEntireLibrary;
    private boolean scanTopSongs;
    private boolean scanSpecificPlaylist;
    private String playlistId; // Assuming playlist ID is a string, adjust as necessary for your application

    public enum MusicianType {
        PRODUCER,
        MIXING_ENGINEER,
        GUITARIST,
        DRUMMER,
        BASSIST,
        VOCALIST,
    }

    public RecappedRequest() {}

    public RecappedRequest(
        LocalDate startDate,
        LocalDate endDate,
        String dateRange,
        MusicianType musicianType,
        boolean scanEntireLibrary,
        boolean scanTopSongs,
        boolean scanSpecificPlaylist,
        String playlistId
    ) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.dateRange = dateRange;
        this.musicianType = musicianType;
        this.scanEntireLibrary = scanEntireLibrary;
        this.scanTopSongs = scanTopSongs;
        this.scanSpecificPlaylist = scanSpecificPlaylist;
        this.playlistId = playlistId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public MusicianType getMusicianType() {
        return musicianType;
    }

    public void setMusicianType(MusicianType musicianType) {
        this.musicianType = musicianType;
    }

    public boolean isScanEntireLibrary() {
        return scanEntireLibrary;
    }

    public void setScanEntireLibrary(boolean scanEntireLibrary) {
        this.scanEntireLibrary = scanEntireLibrary;
    }

    public boolean isScanTopSongs() {
        return scanTopSongs;
    }

    public void setScanTopSongs(boolean scanTopSongs) {
        this.scanTopSongs = scanTopSongs;
    }

    public boolean isScanSpecificPlaylist() {
        return scanSpecificPlaylist;
    }

    public void setScanSpecificPlaylist(boolean scanSpecificPlaylist) {
        this.scanSpecificPlaylist = scanSpecificPlaylist;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }
}
