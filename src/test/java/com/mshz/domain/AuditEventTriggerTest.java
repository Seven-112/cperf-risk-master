package com.mshz.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mshz.web.rest.TestUtil;

public class AuditEventTriggerTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuditEventTrigger.class);
        AuditEventTrigger auditEventTrigger1 = new AuditEventTrigger();
        auditEventTrigger1.setId(1L);
        AuditEventTrigger auditEventTrigger2 = new AuditEventTrigger();
        auditEventTrigger2.setId(auditEventTrigger1.getId());
        assertThat(auditEventTrigger1).isEqualTo(auditEventTrigger2);
        auditEventTrigger2.setId(2L);
        assertThat(auditEventTrigger1).isNotEqualTo(auditEventTrigger2);
        auditEventTrigger1.setId(null);
        assertThat(auditEventTrigger1).isNotEqualTo(auditEventTrigger2);
    }
}
