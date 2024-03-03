package team.bham.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import team.bham.web.rest.TestUtil;

class SongArtistJoinTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SongArtistJoin.class);
        SongArtistJoin songArtistJoin1 = new SongArtistJoin();
        songArtistJoin1.setId(1L);
        SongArtistJoin songArtistJoin2 = new SongArtistJoin();
        songArtistJoin2.setId(songArtistJoin1.getId());
        assertThat(songArtistJoin1).isEqualTo(songArtistJoin2);
        songArtistJoin2.setId(2L);
        assertThat(songArtistJoin1).isNotEqualTo(songArtistJoin2);
        songArtistJoin1.setId(null);
        assertThat(songArtistJoin1).isNotEqualTo(songArtistJoin2);
    }
}
