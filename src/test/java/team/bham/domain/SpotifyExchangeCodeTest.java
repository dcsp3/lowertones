package team.bham.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import team.bham.web.rest.TestUtil;

class SpotifyExchangeCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpotifyExchangeCode.class);
        SpotifyExchangeCode spotifyExchangeCode1 = new SpotifyExchangeCode();
        spotifyExchangeCode1.setId(1L);
        SpotifyExchangeCode spotifyExchangeCode2 = new SpotifyExchangeCode();
        spotifyExchangeCode2.setId(spotifyExchangeCode1.getId());
        assertThat(spotifyExchangeCode1).isEqualTo(spotifyExchangeCode2);
        spotifyExchangeCode2.setId(2L);
        assertThat(spotifyExchangeCode1).isNotEqualTo(spotifyExchangeCode2);
        spotifyExchangeCode1.setId(null);
        assertThat(spotifyExchangeCode1).isNotEqualTo(spotifyExchangeCode2);
    }
}
