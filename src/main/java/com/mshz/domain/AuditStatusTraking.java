package com.mshz.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import com.mshz.domain.enumeration.AuditStatus;

/**
 * A AuditStatusTraking.
 */
@Entity
@Table(name = "audit_status_traking")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AuditStatusTraking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "audit_id", nullable = false)
    private Long auditId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AuditStatus status;

    @Column(name = "tracing_at")
    private Instant tracingAt;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "justification")
    private String justification;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "editable")
    private Boolean editable;

    @Column(name = "recom")
    private Boolean recom;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuditId() {
        return auditId;
    }

    public AuditStatusTraking auditId(Long auditId) {
        this.auditId = auditId;
        return this;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public AuditStatus getStatus() {
        return status;
    }

    public AuditStatusTraking status(AuditStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(AuditStatus status) {
        this.status = status;
    }

    public Instant getTracingAt() {
        return tracingAt;
    }

    public AuditStatusTraking tracingAt(Instant tracingAt) {
        this.tracingAt = tracingAt;
        return this;
    }

    public void setTracingAt(Instant tracingAt) {
        this.tracingAt = tracingAt;
    }

    public String getJustification() {
        return justification;
    }

    public AuditStatusTraking justification(String justification) {
        this.justification = justification;
        return this;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public Long getUserId() {
        return userId;
    }

    public AuditStatusTraking userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean isEditable() {
        return editable;
    }

    public AuditStatusTraking editable(Boolean editable) {
        this.editable = editable;
        return this;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean isRecom() {
        return recom;
    }

    public AuditStatusTraking recom(Boolean recom) {
        this.recom = recom;
        return this;
    }

    public void setRecom(Boolean recom) {
        this.recom = recom;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuditStatusTraking)) {
            return false;
        }
        return id != null && id.equals(((AuditStatusTraking) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditStatusTraking{" +
            "id=" + getId() +
            ", auditId=" + getAuditId() +
            ", status='" + getStatus() + "'" +
            ", tracingAt='" + getTracingAt() + "'" +
            ", justification='" + getJustification() + "'" +
            ", userId=" + getUserId() +
            ", editable='" + isEditable() + "'" +
            ", recom='" + isRecom() + "'" +
            "}";
    }
}
