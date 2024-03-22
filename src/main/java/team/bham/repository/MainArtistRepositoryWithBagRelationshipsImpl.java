package team.bham.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import team.bham.domain.MainArtist;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class MainArtistRepositoryWithBagRelationshipsImpl implements MainArtistRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<MainArtist> fetchBagRelationships(Optional<MainArtist> mainArtist) {
        return mainArtist.map(this::fetchAlbums);
    }

    @Override
    public Page<MainArtist> fetchBagRelationships(Page<MainArtist> mainArtists) {
        return new PageImpl<>(fetchBagRelationships(mainArtists.getContent()), mainArtists.getPageable(), mainArtists.getTotalElements());
    }

    @Override
    public List<MainArtist> fetchBagRelationships(List<MainArtist> mainArtists) {
        return Optional.of(mainArtists).map(this::fetchAlbums).orElse(Collections.emptyList());
    }

    MainArtist fetchAlbums(MainArtist result) {
        return entityManager
            .createQuery(
                "select mainArtist from MainArtist mainArtist left join fetch mainArtist.albums where mainArtist is :mainArtist",
                MainArtist.class
            )
            .setParameter("mainArtist", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<MainArtist> fetchAlbums(List<MainArtist> mainArtists) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, mainArtists.size()).forEach(index -> order.put(mainArtists.get(index).getId(), index));
        List<MainArtist> result = entityManager
            .createQuery(
                "select distinct mainArtist from MainArtist mainArtist left join fetch mainArtist.albums where mainArtist in :mainArtists",
                MainArtist.class
            )
            .setParameter("mainArtists", mainArtists)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
