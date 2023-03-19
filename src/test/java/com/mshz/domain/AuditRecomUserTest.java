package com.mshz.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mshz.web.rest.TestUtil;

public class AuditRecomUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuditRecomUser.class);
        AuditRecomUser auditRecomUser1 = new AuditRecomUser();
        auditRecomUser1.setId(1L);
        AuditRecomUser auditRecomUser2 = new AuditRecomUser();
        auditRecomUser2.setId(auditRecomUser1.getId());
        assertThat(auditRecomUser1).isEqualTo(auditRecomUser2);
        auditRecomUser2.setId(2L);
        assertThat(auditRecomUser1).isNotEqualTo(auditRecomUser2);
        auditRecomUser1.setId(null);
        assertThat(auditRecomUser1).isNotEqualTo(auditRecomUser2);
    }
}
