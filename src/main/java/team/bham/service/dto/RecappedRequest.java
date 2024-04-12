package team.bham.service.dto;

public class RecappedRequest {

    private String dateRange;
    private MusicianType musicianType;
    private boolean scanEntireLibrary;
    private boolean scanTopSongs;
    private boolean scanSpecificPlaylist;
    private String playlistId;

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
        String dateRange,
        MusicianType musicianType,
        boolean scanEntireLibrary,
        boolean scanTopSongs,
        boolean scanSpecificPlaylist,
        String playlistId
    ) {
        this.dateRange = dateRange;
        this.musicianType = musicianType;
        this.scanEntireLibrary = scanEntireLibrary;
        this.scanTopSongs = scanTopSongs;
        this.scanSpecificPlaylist = scanSpecificPlaylist;
        this.playlistId = playlistId;
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
