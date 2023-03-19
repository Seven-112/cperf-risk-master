package com.mshz.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mshz.web.rest.TestUtil;

public class ControlMaturityTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ControlMaturity.class);
        ControlMaturity controlMaturity1 = new ControlMaturity();
        controlMaturity1.setId(1L);
        ControlMaturity controlMaturity2 = new ControlMaturity();
        controlMaturity2.setId(controlMaturity1.getId());
        assertThat(controlMaturity1).isEqualTo(controlMaturity2);
        controlMaturity2.setId(2L);
        assertThat(controlMaturity1).isNotEqualTo(controlMaturity2);
        controlMaturity1.setId(null);
        assertThat(controlMaturity1).isNotEqualTo(controlMaturity2);
    }
}
