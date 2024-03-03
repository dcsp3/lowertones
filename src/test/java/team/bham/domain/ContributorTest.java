package team.bham.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import team.bham.web.rest.TestUtil;

class ContributorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contributor.class);
        Contributor contributor1 = new Contributor();
        contributor1.setId(1L);
        Contributor contributor2 = new Contributor();
        contributor2.setId(contributor1.getId());
        assertThat(contributor1).isEqualTo(contributor2);
        contributor2.setId(2L);
        assertThat(contributor1).isNotEqualTo(contributor2);
        contributor1.setId(null);
        assertThat(contributor1).isNotEqualTo(contributor2);
    }
}
