package team.bham.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import team.bham.web.rest.TestUtil;

class MusicBrainzSongAttributionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MusicBrainzSongAttribution.class);
        MusicBrainzSongAttribution musicBrainzSongAttribution1 = new MusicBrainzSongAttribution();
        musicBrainzSongAttribution1.setId(1L);
        MusicBrainzSongAttribution musicBrainzSongAttribution2 = new MusicBrainzSongAttribution();
        musicBrainzSongAttribution2.setId(musicBrainzSongAttribution1.getId());
        assertThat(musicBrainzSongAttribution1).isEqualTo(musicBrainzSongAttribution2);
        musicBrainzSongAttribution2.setId(2L);
        assertThat(musicBrainzSongAttribution1).isNotEqualTo(musicBrainzSongAttribution2);
        musicBrainzSongAttribution1.setId(null);
        assertThat(musicBrainzSongAttribution1).isNotEqualTo(musicBrainzSongAttribution2);
    }
}
