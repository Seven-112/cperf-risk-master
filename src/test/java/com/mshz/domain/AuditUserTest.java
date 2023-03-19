package com.mshz.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mshz.web.rest.TestUtil;

public class AuditUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuditUser.class);
        AuditUser auditUser1 = new AuditUser();
        auditUser1.setId(1L);
        AuditUser auditUser2 = new AuditUser();
        auditUser2.setId(auditUser1.getId());
        assertThat(auditUser1).isEqualTo(auditUser2);
        auditUser2.setId(2L);
        assertThat(auditUser1).isNotEqualTo(auditUser2);
        auditUser1.setId(null);
        assertThat(auditUser1).isNotEqualTo(auditUser2);
    }
}
