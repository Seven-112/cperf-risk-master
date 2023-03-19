package com.mshz.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mshz.web.rest.TestUtil;

public class AuditRecommendationFileTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuditRecommendationFile.class);
        AuditRecommendationFile auditRecommendationFile1 = new AuditRecommendationFile();
        auditRecommendationFile1.setId(1L);
        AuditRecommendationFile auditRecommendationFile2 = new AuditRecommendationFile();
        auditRecommendationFile2.setId(auditRecommendationFile1.getId());
        assertThat(auditRecommendationFile1).isEqualTo(auditRecommendationFile2);
        auditRecommendationFile2.setId(2L);
        assertThat(auditRecommendationFile1).isNotEqualTo(auditRecommendationFile2);
        auditRecommendationFile1.setId(null);
        assertThat(auditRecommendationFile1).isNotEqualTo(auditRecommendationFile2);
    }
}
