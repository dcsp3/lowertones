package team.bham.repository;

import java.time.LocalDate;
import team.bham.domain.Song;

public interface SongWithArtistName {
    Song getSong();
    String getSongSpotifyId();
    String getSongTitle();
    Integer getSongDuration();
    Boolean getSongExplicit();
    Integer getSongPopularity();
    Float getSongAcousticness();
    Float getSongDanceability();
    Float getSongEnergy();
    Float getSongInstrumentalness();
    Float getSongLiveness();
    Float getSongLoudness();
    Float getSongSpeechiness();
    Float getSongTempo();
    Float getSongValence();
    LocalDate getAlbumReleaseDate();
    String getArtistName();
}
