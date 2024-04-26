package team.bham.service.dto;

import java.util.ArrayList;

public class PlaylistExportDTO {

    private String name;
    private ArrayList<String> songSpotifyIds;

    public void setName(String name) {
        this.name = name;
    }

    public void setSongSpotifyIds(ArrayList<String> songSpotifyIds) {
        this.songSpotifyIds = songSpotifyIds;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getSongSpotifyIds() {
        return songSpotifyIds;
    }
}
