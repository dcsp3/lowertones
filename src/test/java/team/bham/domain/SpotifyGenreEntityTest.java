package team.bham.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import team.bham.web.rest.TestUtil;

class SpotifyGenreEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpotifyGenreEntity.class);
        SpotifyGenreEntity spotifyGenreEntity1 = new SpotifyGenreEntity();
        spotifyGenreEntity1.setId(1L);
        SpotifyGenreEntity spotifyGenreEntity2 = new SpotifyGenreEntity();
        spotifyGenreEntity2.setId(spotifyGenreEntity1.getId());
        assertThat(spotifyGenreEntity1).isEqualTo(spotifyGenreEntity2);
        spotifyGenreEntity2.setId(2L);
        assertThat(spotifyGenreEntity1).isNotEqualTo(spotifyGenreEntity2);
        spotifyGenreEntity1.setId(null);
        assertThat(spotifyGenreEntity1).isNotEqualTo(spotifyGenreEntity2);
    }
}
