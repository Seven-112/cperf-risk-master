package com.mshz.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A AuditStatusTrakingFile.
 */
@Entity
@Table(name = "audit_status_traking_file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AuditStatusTrakingFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "track_id")
    private Long trackId;

    @NotNull
    @Column(name = "file_id", nullable = false)
    private Long fileId;

    @NotNull
    @Column(name = "file_name", nullable = false)
    private String fileName;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTrackId() {
        return trackId;
    }

    public AuditStatusTrakingFile trackId(Long trackId) {
        this.trackId = trackId;
        return this;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public Long getFileId() {
        return fileId;
    }

    public AuditStatusTrakingFile fileId(Long fileId) {
        this.fileId = fileId;
        return this;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public AuditStatusTrakingFile fileName(String fileName) {
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
        if (!(o instanceof AuditStatusTrakingFile)) {
            return false;
        }
        return id != null && id.equals(((AuditStatusTrakingFile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditStatusTrakingFile{" +
            "id=" + getId() +
            ", trackId=" + getTrackId() +
            ", fileId=" + getFileId() +
            ", fileName='" + getFileName() + "'" +
            "}";
    }
}
