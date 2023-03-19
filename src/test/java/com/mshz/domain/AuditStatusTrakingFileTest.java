package com.mshz.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mshz.web.rest.TestUtil;

public class AuditStatusTrakingFileTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuditStatusTrakingFile.class);
        AuditStatusTrakingFile auditStatusTrakingFile1 = new AuditStatusTrakingFile();
        auditStatusTrakingFile1.setId(1L);
        AuditStatusTrakingFile auditStatusTrakingFile2 = new AuditStatusTrakingFile();
        auditStatusTrakingFile2.setId(auditStatusTrakingFile1.getId());
        assertThat(auditStatusTrakingFile1).isEqualTo(auditStatusTrakingFile2);
        auditStatusTrakingFile2.setId(2L);
        assertThat(auditStatusTrakingFile1).isNotEqualTo(auditStatusTrakingFile2);
        auditStatusTrakingFile1.setId(null);
        assertThat(auditStatusTrakingFile1).isNotEqualTo(auditStatusTrakingFile2);
    }
}
