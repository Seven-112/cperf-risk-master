package com.mshz.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mshz.web.rest.TestUtil;

public class RiskTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Risk.class);
        Risk risk1 = new Risk();
        risk1.setId(1L);
        Risk risk2 = new Risk();
        risk2.setId(risk1.getId());
        assertThat(risk1).isEqualTo(risk2);
        risk2.setId(2L);
        assertThat(risk1).isNotEqualTo(risk2);
        risk1.setId(null);
        assertThat(risk1).isNotEqualTo(risk2);
    }
}
