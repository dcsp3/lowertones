package team.bham.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import team.bham.web.rest.TestUtil;

class VaultTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vault.class);
        Vault vault1 = new Vault();
        vault1.setId(1L);
        Vault vault2 = new Vault();
        vault2.setId(vault1.getId());
        assertThat(vault1).isEqualTo(vault2);
        vault2.setId(2L);
        assertThat(vault1).isNotEqualTo(vault2);
        vault1.setId(null);
        assertThat(vault1).isNotEqualTo(vault2);
    }
}
