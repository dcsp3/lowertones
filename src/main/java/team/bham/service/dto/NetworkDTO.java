package team.bham.service.dto;

import java.util.List;

public class NetworkDTO {

    private Long playlistId;
    private String playlistName;
    private List<SongDetails> songDetails;

    public NetworkDTO(Long playlistId, String playlistName) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
    }

    public Long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public List<SongDetails> getSongDetails() {
        return songDetails;
    }

    public void setSongDetails(List<SongDetails> songDetails) {
        this.songDetails = songDetails;
    }

    public static class SongDetails {

        private Long id;
        private String title;
        private Integer popularity;

        public SongDetails(Long id, String title, Integer popularity) {
            this.id = id;
            this.title = title;
            this.popularity = popularity;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getPopularity() {
            return popularity;
        }

        public void setPopularity(Integer popularity) {
            this.popularity = popularity;
        }
    }

    public static class ArtistDetails {

        private Long id;
        private String name;
        private List<String> genres;
        private String imageUrl;
        private double distance;

        public ArtistDetails(Long id, String name, List<String> genres, String imageUrl, double distance) {
            this.id = id;
            this.name = name;
            this.genres = genres;
            this.imageUrl = imageUrl;
            this.distance = distance;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getGenres() {
            return genres;
        }

        public void setGenres(List<String> genres) {
            this.genres = genres;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }
    }
}
