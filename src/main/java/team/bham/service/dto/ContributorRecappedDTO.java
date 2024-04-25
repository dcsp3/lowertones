package team.bham.service.dto;

public class ContributorRecappedDTO {

    private String name;
    private long numberOfSongs;

    public ContributorRecappedDTO(String name, long numberOfSongs) {
        this.name = name;
        this.numberOfSongs = numberOfSongs;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNumberOfSongs() {
        return numberOfSongs;
    }

    public void setNumberOfSongs(long numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }
}
