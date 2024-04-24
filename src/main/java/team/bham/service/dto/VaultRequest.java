package team.bham.service.dto;

import java.time.LocalDate;

public class VaultRequest {

    private String playlistName;
    private String sourcePlaylistId;
    private String frequency;

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getSourcePlaylistId() {
        return sourcePlaylistId;
    }

    public void setSourcePlaylistId(String sourcePlaylistId) {
        this.sourcePlaylistId = sourcePlaylistId;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
