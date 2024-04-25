package team.bham.service.dto;

public class RecappedRequest {

    private String timeframe;
    private MusicianType musicianType;
    private String scanType;

    public enum MusicianType {
        PRODUCER,
        MIXING_ENGINEER,
        GUITARIST,
        DRUMMER,
        BASSIST,
        SINGER,
        SAXOPHONIST,
        PIANIST,
        TRUMPETER,
        VIOLINIST,
        DJ,
        COMPOSER,
    }

    public RecappedRequest() {}

    public RecappedRequest(String timeframe, MusicianType musicianType, String scanType) {
        this.timeframe = timeframe;
        this.musicianType = musicianType;
        this.scanType = scanType;
    }

    public MusicianType getMusicianType() {
        return musicianType;
    }

    public void setMusicianType(MusicianType musicianType) {
        this.musicianType = musicianType;
    }

    public String getScanType() {
        return scanType;
    }

    public void setScanType(String scanType) {
        this.scanType = scanType;
    }

    public String getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(String timeframe) {
        this.timeframe = timeframe;
    }
}
