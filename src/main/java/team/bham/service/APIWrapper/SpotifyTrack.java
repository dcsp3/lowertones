package team.bham.service.APIWrapper;

public class SpotifyTrack {

    private SpotifyAlbum album;
    private SpotifyArtist mainArtist; //<--extend to arraylist
    private SpotifyTrackAudioFeatures audioFeatures;
    private String id;
    private String name;
    private Boolean explicit;
    private int duration;
    private int popularity;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExplicit(Boolean explicit) {
        this.explicit = explicit;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void setAlbum(SpotifyAlbum album) {
        this.album = album;
    }

    public void setMainArtist(SpotifyArtist artist) {
        this.mainArtist = artist;
    }

    public void setAudioFeatures(SpotifyTrackAudioFeatures audioFeatures) {
        this.audioFeatures = audioFeatures;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getExplicit() {
        return explicit;
    }

    public int getDuration() {
        return duration;
    }

    public int getPopularity() {
        return popularity;
    }

    public SpotifyAlbum getAlbum() {
        return album;
    }

    public SpotifyArtist getArtist() {
        return mainArtist;
    }

    public SpotifyTrackAudioFeatures getAudioFeatures() {
        return audioFeatures;
    }
}
