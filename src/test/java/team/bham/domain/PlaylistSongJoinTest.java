package team.bham.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import team.bham.web.rest.TestUtil;

class PlaylistSongJoinTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlaylistSongJoin.class);
        PlaylistSongJoin playlistSongJoin1 = new PlaylistSongJoin();
        playlistSongJoin1.setId(1L);
        PlaylistSongJoin playlistSongJoin2 = new PlaylistSongJoin();
        playlistSongJoin2.setId(playlistSongJoin1.getId());
        assertThat(playlistSongJoin1).isEqualTo(playlistSongJoin2);
        playlistSongJoin2.setId(2L);
        assertThat(playlistSongJoin1).isNotEqualTo(playlistSongJoin2);
        playlistSongJoin1.setId(null);
        assertThat(playlistSongJoin1).isNotEqualTo(playlistSongJoin2);
    }
}
