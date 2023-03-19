package com.mshz.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mshz.web.rest.TestUtil;

public class AuditRecommendationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuditRecommendation.class);
        AuditRecommendation auditRecommendation1 = new AuditRecommendation();
        auditRecommendation1.setId(1L);
        AuditRecommendation auditRecommendation2 = new AuditRecommendation();
        auditRecommendation2.setId(auditRecommendation1.getId());
        assertThat(auditRecommendation1).isEqualTo(auditRecommendation2);
        auditRecommendation2.setId(2L);
        assertThat(auditRecommendation1).isNotEqualTo(auditRecommendation2);
        auditRecommendation1.setId(null);
        assertThat(auditRecommendation1).isNotEqualTo(auditRecommendation2);
    }
}
