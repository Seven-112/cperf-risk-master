package com.mshz.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mshz.web.rest.TestUtil;

public class AuditTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Audit.class);
        Audit audit1 = new Audit();
        audit1.setId(1L);
        Audit audit2 = new Audit();
        audit2.setId(audit1.getId());
        assertThat(audit1).isEqualTo(audit2);
        audit2.setId(2L);
        assertThat(audit1).isNotEqualTo(audit2);
        audit1.setId(null);
        assertThat(audit1).isNotEqualTo(audit2);
    }
}
