package team.bham.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import team.bham.domain.MainArtist;

public interface MainArtistRepositoryWithBagRelationships {
    Optional<MainArtist> fetchBagRelationships(Optional<MainArtist> mainArtist);

    List<MainArtist> fetchBagRelationships(List<MainArtist> mainArtists);

    Page<MainArtist> fetchBagRelationships(Page<MainArtist> mainArtists);
}
