package team.bham.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import team.bham.web.rest.TestUtil;

class MusicbrainzGenreEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MusicbrainzGenreEntity.class);
        MusicbrainzGenreEntity musicbrainzGenreEntity1 = new MusicbrainzGenreEntity();
        musicbrainzGenreEntity1.setId(1L);
        MusicbrainzGenreEntity musicbrainzGenreEntity2 = new MusicbrainzGenreEntity();
        musicbrainzGenreEntity2.setId(musicbrainzGenreEntity1.getId());
        assertThat(musicbrainzGenreEntity1).isEqualTo(musicbrainzGenreEntity2);
        musicbrainzGenreEntity2.setId(2L);
        assertThat(musicbrainzGenreEntity1).isNotEqualTo(musicbrainzGenreEntity2);
        musicbrainzGenreEntity1.setId(null);
        assertThat(musicbrainzGenreEntity1).isNotEqualTo(musicbrainzGenreEntity2);
    }
}
