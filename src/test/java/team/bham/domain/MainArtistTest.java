package team.bham.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import team.bham.web.rest.TestUtil;

class MainArtistTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MainArtist.class);
        MainArtist mainArtist1 = new MainArtist();
        mainArtist1.setId(1L);
        MainArtist mainArtist2 = new MainArtist();
        mainArtist2.setId(mainArtist1.getId());
        assertThat(mainArtist1).isEqualTo(mainArtist2);
        mainArtist2.setId(2L);
        assertThat(mainArtist1).isNotEqualTo(mainArtist2);
        mainArtist1.setId(null);
        assertThat(mainArtist1).isNotEqualTo(mainArtist2);
    }
}
