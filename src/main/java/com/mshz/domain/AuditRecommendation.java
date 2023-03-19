package com.mshz.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

import com.mshz.domain.enumeration.AuditStatus;

/**
 * A AuditRecommendation.
 */
@Entity
@Table(name = "audit_recommendation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AuditRecommendation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "auditor_id")
    private Long auditorId;

    @Column(name = "auditor_name")
    private String auditorName;

    @Column(name = "auditor_email")
    private String auditorEmail;

    @Column(name = "audit_id")
    private Long auditId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AuditStatus status;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "content")
    private String content;

    @Column(name = "responsable_id")
    private Long responsableId;

    @Column(name = "responsable_name")
    private String responsableName;

    @Column(name = "responsable_email")
    private String responsableEmail;

    @Column(name = "date_limit")
    private Instant dateLimit;

    @Column(name = "edit_at")
    private Instant editAt;

    @Column(name = "executed_at")
    private Instant executedAt;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "entiy_name")
    private String entiyName;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuditorId() {
        return auditorId;
    }

    public AuditRecommendation auditorId(Long auditorId) {
        this.auditorId = auditorId;
        return this;
    }

    public void setAuditorId(Long auditorId) {
        this.auditorId = auditorId;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public AuditRecommendation auditorName(String auditorName) {
        this.auditorName = auditorName;
        return this;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public String getAuditorEmail() {
        return auditorEmail;
    }

    public AuditRecommendation auditorEmail(String auditorEmail) {
        this.auditorEmail = auditorEmail;
        return this;
    }

    public void setAuditorEmail(String auditorEmail) {
        this.auditorEmail = auditorEmail;
    }

    public Long getAuditId() {
        return auditId;
    }

    public AuditRecommendation auditId(Long auditId) {
        this.auditId = auditId;
        return this;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public AuditStatus getStatus() {
        return status;
    }

    public AuditRecommendation status(AuditStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(AuditStatus status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public AuditRecommendation content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getResponsableId() {
        return responsableId;
    }

    public AuditRecommendation responsableId(Long responsableId) {
        this.responsableId = responsableId;
        return this;
    }

    public void setResponsableId(Long responsableId) {
        this.responsableId = responsableId;
    }

    public String getResponsableName() {
        return responsableName;
    }

    public AuditRecommendation responsableName(String responsableName) {
        this.responsableName = responsableName;
        return this;
    }

    public void setResponsableName(String responsableName) {
        this.responsableName = responsableName;
    }

    public String getResponsableEmail() {
        return responsableEmail;
    }

    public AuditRecommendation responsableEmail(String responsableEmail) {
        this.responsableEmail = responsableEmail;
        return this;
    }

    public void setResponsableEmail(String responsableEmail) {
        this.responsableEmail = responsableEmail;
    }

    public Instant getDateLimit() {
        return dateLimit;
    }

    public AuditRecommendation dateLimit(Instant dateLimit) {
        this.dateLimit = dateLimit;
        return this;
    }

    public void setDateLimit(Instant dateLimit) {
        this.dateLimit = dateLimit;
    }

    public Instant getEditAt() {
        return editAt;
    }

    public AuditRecommendation editAt(Instant editAt) {
        this.editAt = editAt;
        return this;
    }

    public void setEditAt(Instant editAt) {
        this.editAt = editAt;
    }

    public Instant getExecutedAt() {
        return executedAt;
    }

    public AuditRecommendation executedAt(Instant executedAt) {
        this.executedAt = executedAt;
        return this;
    }

    public void setExecutedAt(Instant executedAt) {
        this.executedAt = executedAt;
    }

    public Long getEntityId() {
        return entityId;
    }

    public AuditRecommendation entityId(Long entityId) {
        this.entityId = entityId;
        return this;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntiyName() {
        return entiyName;
    }

    public AuditRecommendation entiyName(String entiyName) {
        this.entiyName = entiyName;
        return this;
    }

    public void setEntiyName(String entiyName) {
        this.entiyName = entiyName;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuditRecommendation)) {
            return false;
        }
        return id != null && id.equals(((AuditRecommendation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditRecommendation{" +
            "id=" + getId() +
            ", auditorId=" + getAuditorId() +
            ", auditorName='" + getAuditorName() + "'" +
            ", auditorEmail='" + getAuditorEmail() + "'" +
            ", auditId=" + getAuditId() +
            ", status='" + getStatus() + "'" +
            ", content='" + getContent() + "'" +
            ", responsableId=" + getResponsableId() +
            ", responsableName='" + getResponsableName() + "'" +
            ", responsableEmail='" + getResponsableEmail() + "'" +
            ", dateLimit='" + getDateLimit() + "'" +
            ", editAt='" + getEditAt() + "'" +
            ", executedAt='" + getExecutedAt() + "'" +
            ", entityId=" + getEntityId() +
            ", entiyName='" + getEntiyName() + "'" +
            "}";
    }
}
