package com.mshz.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A AuditRecommendationFile.
 */
@Entity
@Table(name = "audit_recommendation_file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AuditRecommendationFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "recommendation_id")
    private Long recommendationId;

    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "file_name")
    private String fileName;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecommendationId() {
        return recommendationId;
    }

    public AuditRecommendationFile recommendationId(Long recommendationId) {
        this.recommendationId = recommendationId;
        return this;
    }

    public void setRecommendationId(Long recommendationId) {
        this.recommendationId = recommendationId;
    }

    public Long getFileId() {
        return fileId;
    }

    public AuditRecommendationFile fileId(Long fileId) {
        this.fileId = fileId;
        return this;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public AuditRecommendationFile fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuditRecommendationFile)) {
            return false;
        }
        return id != null && id.equals(((AuditRecommendationFile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditRecommendationFile{" +
            "id=" + getId() +
            ", recommendationId=" + getRecommendationId() +
            ", fileId=" + getFileId() +
            ", fileName='" + getFileName() + "'" +
            "}";
    }
}
