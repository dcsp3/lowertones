package team.bham.repository;

import java.time.LocalDate;
import team.bham.domain.Song;

public interface SongWithCollaborators {
    Song getSong();
    String getSongSpotifyId();
    String getContributorName();
    String getContributorRole();
    String getContributorInstrument();
}
