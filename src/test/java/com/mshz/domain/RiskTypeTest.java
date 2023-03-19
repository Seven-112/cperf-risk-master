package com.mshz.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mshz.web.rest.TestUtil;

public class RiskTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RiskType.class);
        RiskType riskType1 = new RiskType();
        riskType1.setId(1L);
        RiskType riskType2 = new RiskType();
        riskType2.setId(riskType1.getId());
        assertThat(riskType1).isEqualTo(riskType2);
        riskType2.setId(2L);
        assertThat(riskType1).isNotEqualTo(riskType2);
        riskType1.setId(null);
        assertThat(riskType1).isNotEqualTo(riskType2);
    }
}
