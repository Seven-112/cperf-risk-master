package com.mshz.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mshz.web.rest.TestUtil;

public class ControlTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ControlType.class);
        ControlType controlType1 = new ControlType();
        controlType1.setId(1L);
        ControlType controlType2 = new ControlType();
        controlType2.setId(controlType1.getId());
        assertThat(controlType1).isEqualTo(controlType2);
        controlType2.setId(2L);
        assertThat(controlType1).isNotEqualTo(controlType2);
        controlType1.setId(null);
        assertThat(controlType1).isNotEqualTo(controlType2);
    }
}
