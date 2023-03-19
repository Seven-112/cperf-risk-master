package com.mshz.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mshz.web.rest.TestUtil;

public class AuditStatusTrakingTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuditStatusTraking.class);
        AuditStatusTraking auditStatusTraking1 = new AuditStatusTraking();
        auditStatusTraking1.setId(1L);
        AuditStatusTraking auditStatusTraking2 = new AuditStatusTraking();
        auditStatusTraking2.setId(auditStatusTraking1.getId());
        assertThat(auditStatusTraking1).isEqualTo(auditStatusTraking2);
        auditStatusTraking2.setId(2L);
        assertThat(auditStatusTraking1).isNotEqualTo(auditStatusTraking2);
        auditStatusTraking1.setId(null);
        assertThat(auditStatusTraking1).isNotEqualTo(auditStatusTraking2);
    }
}
