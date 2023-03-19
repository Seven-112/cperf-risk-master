package com.mshz.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.mshz.domain.enumeration.AuditStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.mshz.domain.AuditRecommendation} entity. This class is used
 * in {@link com.mshz.web.rest.AuditRecommendationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /audit-recommendations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AuditRecommendationCriteria implements Serializable, Criteria {
    /**
     * Class for filtering AuditStatus
     */
    public static class AuditStatusFilter extends Filter<AuditStatus> {

        public AuditStatusFilter() {
        }

        public AuditStatusFilter(AuditStatusFilter filter) {
            super(filter);
        }

        @Override
        public AuditStatusFilter copy() {
            return new AuditStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter auditorId;

    private StringFilter auditorName;

    private StringFilter auditorEmail;

    private LongFilter auditId;

    private AuditStatusFilter status;

    private LongFilter responsableId;

    private StringFilter responsableName;

    private StringFilter responsableEmail;

    private InstantFilter dateLimit;

    private InstantFilter editAt;

    private InstantFilter executedAt;

    private LongFilter entityId;

    private StringFilter entiyName;

    public AuditRecommendationCriteria() {
    }

    public AuditRecommendationCriteria(AuditRecommendationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.auditorId = other.auditorId == null ? null : other.auditorId.copy();
        this.auditorName = other.auditorName == null ? null : other.auditorName.copy();
        this.auditorEmail = other.auditorEmail == null ? null : other.auditorEmail.copy();
        this.auditId = other.auditId == null ? null : other.auditId.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.responsableId = other.responsableId == null ? null : other.responsableId.copy();
        this.responsableName = other.responsableName == null ? null : other.responsableName.copy();
        this.responsableEmail = other.responsableEmail == null ? null : other.responsableEmail.copy();
        this.dateLimit = other.dateLimit == null ? null : other.dateLimit.copy();
        this.editAt = other.editAt == null ? null : other.editAt.copy();
        this.executedAt = other.executedAt == null ? null : other.executedAt.copy();
        this.entityId = other.entityId == null ? null : other.entityId.copy();
        this.entiyName = other.entiyName == null ? null : other.entiyName.copy();
    }

    @Override
    public AuditRecommendationCriteria copy() {
        return new AuditRecommendationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(LongFilter auditorId) {
        this.auditorId = auditorId;
    }

    public StringFilter getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(StringFilter auditorName) {
        this.auditorName = auditorName;
    }

    public StringFilter getAuditorEmail() {
        return auditorEmail;
    }

    public void setAuditorEmail(StringFilter auditorEmail) {
        this.auditorEmail = auditorEmail;
    }

    public LongFilter getAuditId() {
        return auditId;
    }

    public void setAuditId(LongFilter auditId) {
        this.auditId = auditId;
    }

    public AuditStatusFilter getStatus() {
        return status;
    }

    public void setStatus(AuditStatusFilter status) {
        this.status = status;
    }

    public LongFilter getResponsableId() {
        return responsableId;
    }

    public void setResponsableId(LongFilter responsableId) {
        this.responsableId = responsableId;
    }

    public StringFilter getResponsableName() {
        return responsableName;
    }

    public void setResponsableName(StringFilter responsableName) {
        this.responsableName = responsableName;
    }

    public StringFilter getResponsableEmail() {
        return responsableEmail;
    }

    public void setResponsableEmail(StringFilter responsableEmail) {
        this.responsableEmail = responsableEmail;
    }

    public InstantFilter getDateLimit() {
        return dateLimit;
    }

    public void setDateLimit(InstantFilter dateLimit) {
        this.dateLimit = dateLimit;
    }

    public InstantFilter getEditAt() {
        return editAt;
    }

    public void setEditAt(InstantFilter editAt) {
        this.editAt = editAt;
    }

    public InstantFilter getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(InstantFilter executedAt) {
        this.executedAt = executedAt;
    }

    public LongFilter getEntityId() {
        return entityId;
    }

    public void setEntityId(LongFilter entityId) {
        this.entityId = entityId;
    }

    public StringFilter getEntiyName() {
        return entiyName;
    }

    public void setEntiyName(StringFilter entiyName) {
        this.entiyName = entiyName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AuditRecommendationCriteria that = (AuditRecommendationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(auditorId, that.auditorId) &&
            Objects.equals(auditorName, that.auditorName) &&
            Objects.equals(auditorEmail, that.auditorEmail) &&
            Objects.equals(auditId, that.auditId) &&
            Objects.equals(status, that.status) &&
            Objects.equals(responsableId, that.responsableId) &&
            Objects.equals(responsableName, that.responsableName) &&
            Objects.equals(responsableEmail, that.responsableEmail) &&
            Objects.equals(dateLimit, that.dateLimit) &&
            Objects.equals(editAt, that.editAt) &&
            Objects.equals(executedAt, that.executedAt) &&
            Objects.equals(entityId, that.entityId) &&
            Objects.equals(entiyName, that.entiyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        auditorId,
        auditorName,
        auditorEmail,
        auditId,
        status,
        responsableId,
        responsableName,
        responsableEmail,
        dateLimit,
        editAt,
        executedAt,
        entityId,
        entiyName
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditRecommendationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (auditorId != null ? "auditorId=" + auditorId + ", " : "") +
                (auditorName != null ? "auditorName=" + auditorName + ", " : "") +
                (auditorEmail != null ? "auditorEmail=" + auditorEmail + ", " : "") +
                (auditId != null ? "auditId=" + auditId + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (responsableId != null ? "responsableId=" + responsableId + ", " : "") +
                (responsableName != null ? "responsableName=" + responsableName + ", " : "") +
                (responsableEmail != null ? "responsableEmail=" + responsableEmail + ", " : "") +
                (dateLimit != null ? "dateLimit=" + dateLimit + ", " : "") +
                (editAt != null ? "editAt=" + editAt + ", " : "") +
                (executedAt != null ? "executedAt=" + executedAt + ", " : "") +
                (entityId != null ? "entityId=" + entityId + ", " : "") +
                (entiyName != null ? "entiyName=" + entiyName + ", " : "") +
            "}";
    }

}
