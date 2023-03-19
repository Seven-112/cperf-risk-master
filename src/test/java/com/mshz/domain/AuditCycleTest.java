package com.mshz.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mshz.web.rest.TestUtil;

public class AuditCycleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuditCycle.class);
        AuditCycle auditCycle1 = new AuditCycle();
        auditCycle1.setId(1L);
        AuditCycle auditCycle2 = new AuditCycle();
        auditCycle2.setId(auditCycle1.getId());
        assertThat(auditCycle1).isEqualTo(auditCycle2);
        auditCycle2.setId(2L);
        assertThat(auditCycle1).isNotEqualTo(auditCycle2);
        auditCycle1.setId(null);
        assertThat(auditCycle1).isNotEqualTo(auditCycle2);
    }
}
