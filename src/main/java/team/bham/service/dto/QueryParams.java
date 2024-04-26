package team.bham.service.dto;

import java.util.Arrays;

public class QueryParams {

    private String searchQuery;
    private Double minDuration;
    private Double maxDuration;
    private String selectedExplicitness;
    private Integer minPopularity;
    private Integer maxPopularity;
    private String[] artistChips;
    private String[] contributorChips;
    private Double minAcousticness;
    private Double maxAcousticness;
    private Double minDanceability;
    private Double maxDanceability;
    private Double minInstrumentalness;
    private Double maxInstrumentalness;
    private Double minEnergy;
    private Double maxEnergy;
    private Double minLiveness;
    private Double maxLiveness;
    private Double minLoudness;
    private Double maxLoudness;
    private Double minSpeechiness;
    private Double maxSpeechiness;
    private Double minValence;
    private Double maxValence;

    // Getters
    public String getSearchQuery() {
        return searchQuery;
    }

    public Double getMinDuration() {
        return minDuration;
    }

    public Double getMaxDuration() {
        return maxDuration;
    }

    public String getSelectedExplicitness() {
        return selectedExplicitness;
    }

    public Integer getMinPopularity() {
        return minPopularity;
    }

    public Integer getMaxPopularity() {
        return maxPopularity;
    }

    public String[] getArtistChips() {
        return artistChips;
    }

    public String[] getContributorChips() {
        return contributorChips;
    }

    public Double getMinAcousticness() {
        return minAcousticness;
    }

    public Double getMaxAcousticness() {
        return maxAcousticness;
    }

    public Double getMinDanceability() {
        return minDanceability;
    }

    public Double getMaxDanceability() {
        return maxDanceability;
    }

    public Double getMinInstrumentalness() {
        return minInstrumentalness;
    }

    public Double getMaxInstrumentalness() {
        return maxInstrumentalness;
    }

    public Double getMinEnergy() {
        return minEnergy;
    }

    public Double getMaxEnergy() {
        return maxEnergy;
    }

    public Double getMinLiveness() {
        return minLiveness;
    }

    public Double getMaxLiveness() {
        return maxLiveness;
    }

    public Double getMinLoudness() {
        return minLoudness;
    }

    public Double getMaxLoudness() {
        return maxLoudness;
    }

    public Double getMinSpeechiness() {
        return minSpeechiness;
    }

    public Double getMaxSpeechiness() {
        return maxSpeechiness;
    }

    public Double getMinValence() {
        return minValence;
    }

    public Double getMaxValence() {
        return maxValence;
    }

    // Setters
    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public void setMinDuration(Double minDuration) {
        this.minDuration = minDuration;
    }

    public void setMaxDuration(Double maxDuration) {
        this.maxDuration = maxDuration;
    }

    public void setSelectedExplicitness(String selectedExplicitness) {
        this.selectedExplicitness = selectedExplicitness;
    }

    public void setMinPopularity(Integer minPopularity) {
        this.minPopularity = minPopularity;
    }

    public void setMaxPopularity(Integer maxPopularity) {
        this.maxPopularity = maxPopularity;
    }

    public void setArtistChips(String[] artistChips) {
        this.artistChips = artistChips;
    }

    public void setContributorChips(String[] contributorChips) {
        this.contributorChips = contributorChips;
    }

    public void setMinAcousticness(Double minAcousticness) {
        this.minAcousticness = minAcousticness;
    }

    public void setMaxAcousticness(Double maxAcousticness) {
        this.maxAcousticness = maxAcousticness;
    }

    public void setMinDanceability(Double minDanceability) {
        this.minDanceability = minDanceability;
    }

    public void setMaxDanceability(Double maxDanceability) {
        this.maxDanceability = maxDanceability;
    }

    public void setMinInstrumentalness(Double minInstrumentalness) {
        this.minInstrumentalness = minInstrumentalness;
    }

    public void setMaxInstrumentalness(Double maxInstrumentalness) {
        this.maxInstrumentalness = maxInstrumentalness;
    }

    public void setMinEnergy(Double minEnergy) {
        this.minEnergy = minEnergy;
    }

    public void setMaxEnergy(Double maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    public void setMinLiveness(Double minLiveness) {
        this.minLiveness = minLiveness;
    }

    public void setMaxLiveness(Double maxLiveness) {
        this.maxLiveness = maxLiveness;
    }

    public void setMinLoudness(Double minLoudness) {
        this.minLoudness = minLoudness;
    }

    public void setMaxLoudness(Double maxLoudness) {
        this.maxLoudness = maxLoudness;
    }

    public void setMinSpeechiness(Double minSpeechiness) {
        this.minSpeechiness = minSpeechiness;
    }

    public void setMaxSpeechiness(Double maxSpeechiness) {
        this.maxSpeechiness = maxSpeechiness;
    }

    public void setMinValence(Double minValence) {
        this.minValence = minValence;
    }

    public void setMaxValence(Double maxValence) {
        this.maxValence = maxValence;
    }

    @Override
    public String toString() {
        return (
            "QueryParams{" +
            "searchQuery='" +
            searchQuery +
            '\'' +
            ", minDuration=" +
            minDuration +
            ", maxDuration=" +
            maxDuration +
            ", selectedExplicitness='" +
            selectedExplicitness +
            '\'' +
            ", minPopularity=" +
            minPopularity +
            ", maxPopularity=" +
            maxPopularity +
            ", artistChips=" +
            Arrays.toString(artistChips) +
            ", contributorChips=" +
            Arrays.toString(contributorChips) +
            ", minAcousticness=" +
            minAcousticness +
            ", maxAcousticness=" +
            maxAcousticness +
            ", minDanceability=" +
            minDanceability +
            ", maxDanceability=" +
            maxDanceability +
            ", minInstrumentalness=" +
            minInstrumentalness +
            ", maxInstrumentalness=" +
            maxInstrumentalness +
            ", minEnergy=" +
            minEnergy +
            ", maxEnergy=" +
            maxEnergy +
            ", minLiveness=" +
            minLiveness +
            ", maxLiveness=" +
            maxLiveness +
            ", minLoudness=" +
            minLoudness +
            ", maxLoudness=" +
            maxLoudness +
            ", minSpeechiness=" +
            minSpeechiness +
            ", maxSpeechiness=" +
            maxSpeechiness +
            ", minValence=" +
            minValence +
            ", maxValence=" +
            maxValence +
            '}'
        );
    }
}
