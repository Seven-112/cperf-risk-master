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
 * Criteria class for the {@link com.mshz.domain.AuditStatusTraking} entity. This class is used
 * in {@link com.mshz.web.rest.AuditStatusTrakingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /audit-status-trakings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AuditStatusTrakingCriteria implements Serializable, Criteria {
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

    private LongFilter auditId;

    private AuditStatusFilter status;

    private InstantFilter tracingAt;

    private LongFilter userId;

    private BooleanFilter editable;

    private BooleanFilter recom;

    public AuditStatusTrakingCriteria() {
    }

    public AuditStatusTrakingCriteria(AuditStatusTrakingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.auditId = other.auditId == null ? null : other.auditId.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.tracingAt = other.tracingAt == null ? null : other.tracingAt.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.editable = other.editable == null ? null : other.editable.copy();
        this.recom = other.recom == null ? null : other.recom.copy();
    }

    @Override
    public AuditStatusTrakingCriteria copy() {
        return new AuditStatusTrakingCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
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

    public InstantFilter getTracingAt() {
        return tracingAt;
    }

    public void setTracingAt(InstantFilter tracingAt) {
        this.tracingAt = tracingAt;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public BooleanFilter getEditable() {
        return editable;
    }

    public void setEditable(BooleanFilter editable) {
        this.editable = editable;
    }

    public BooleanFilter getRecom() {
        return recom;
    }

    public void setRecom(BooleanFilter recom) {
        this.recom = recom;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AuditStatusTrakingCriteria that = (AuditStatusTrakingCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(auditId, that.auditId) &&
            Objects.equals(status, that.status) &&
            Objects.equals(tracingAt, that.tracingAt) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(editable, that.editable) &&
            Objects.equals(recom, that.recom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        auditId,
        status,
        tracingAt,
        userId,
        editable,
        recom
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditStatusTrakingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (auditId != null ? "auditId=" + auditId + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (tracingAt != null ? "tracingAt=" + tracingAt + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (editable != null ? "editable=" + editable + ", " : "") +
                (recom != null ? "recom=" + recom + ", " : "") +
            "}";
    }

}
