package team.bham.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import team.bham.web.rest.TestUtil;

class RelatedArtistsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RelatedArtists.class);
        RelatedArtists relatedArtists1 = new RelatedArtists();
        relatedArtists1.setId(1L);
        RelatedArtists relatedArtists2 = new RelatedArtists();
        relatedArtists2.setId(relatedArtists1.getId());
        assertThat(relatedArtists1).isEqualTo(relatedArtists2);
        relatedArtists2.setId(2L);
        assertThat(relatedArtists1).isNotEqualTo(relatedArtists2);
        relatedArtists1.setId(null);
        assertThat(relatedArtists1).isNotEqualTo(relatedArtists2);
    }
}
