package team.bham.service.APIWrapper;

public class SpotifyTrackAudioFeatures {

    private float acousticness;
    private float danceability;
    private float energy;
    private float instrumentalness;
    private int key;
    private float liveness;
    private float loudness;
    private float mode;
    private float speechiness;
    private float tempo;
    private int timeSignature;
    private float valence;

    public void setAcousticness(float acousticness) {
        this.acousticness = acousticness;
    }

    public void setDanceability(float danceability) {
        this.danceability = danceability;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public void setInstrumentalness(float instrumentalness) {
        this.instrumentalness = instrumentalness;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setLiveness(float liveness) {
        this.liveness = liveness;
    }

    public void setLoudness(float loudness) {
        this.loudness = loudness;
    }

    public void setMode(float mode) {
        this.mode = mode;
    }

    public void setSpeechiness(float speechiness) {
        this.speechiness = speechiness;
    }

    public void setTempo(float tempo) {
        this.tempo = tempo;
    }

    public void setTimeSignature(int timeSignature) {
        this.timeSignature = timeSignature;
    }

    public void setValence(float valence) {
        this.valence = valence;
    }

    public float getAcousticness() {
        return acousticness;
    }

    public float getDanceability() {
        return danceability;
    }

    public float getEnergy() {
        return energy;
    }

    public float getInstrumentalness() {
        return instrumentalness;
    }

    public int getKey() {
        return key;
    }

    public float getLiveness() {
        return liveness;
    }

    public float getLoudness() {
        return loudness;
    }

    public float getMode() {
        return mode;
    }

    public float getSpeechiness() {
        return speechiness;
    }

    public float getTempo() {
        return tempo;
    }

    public int getTimeSignature() {
        return timeSignature;
    }

    public float getValence() {
        return valence;
    }
}
