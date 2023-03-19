package com.mshz.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.mshz.domain.enumeration.AuditRiskLevel;
import com.mshz.domain.enumeration.AuditType;
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
 * Criteria class for the {@link com.mshz.domain.Audit} entity. This class is used
 * in {@link com.mshz.web.rest.AuditResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /audits?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AuditCriteria implements Serializable, Criteria {
    /**
     * Class for filtering AuditRiskLevel
     */
    public static class AuditRiskLevelFilter extends Filter<AuditRiskLevel> {

        public AuditRiskLevelFilter() {
        }

        public AuditRiskLevelFilter(AuditRiskLevelFilter filter) {
            super(filter);
        }

        @Override
        public AuditRiskLevelFilter copy() {
            return new AuditRiskLevelFilter(this);
        }

    }
    /**
     * Class for filtering AuditType
     */
    public static class AuditTypeFilter extends Filter<AuditType> {

        public AuditTypeFilter() {
        }

        public AuditTypeFilter(AuditTypeFilter filter) {
            super(filter);
        }

        @Override
        public AuditTypeFilter copy() {
            return new AuditTypeFilter(this);
        }

    }
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

    private StringFilter title;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private InstantFilter executedAt;

    private LongFilter processId;

    private StringFilter processName;

    private LongFilter processCategoryId;

    private StringFilter processCategoryName;

    private AuditRiskLevelFilter riskLevel;

    private AuditTypeFilter type;

    private AuditStatusFilter status;

    private LongFilter riskId;

    private StringFilter riskName;

    private LongFilter cycleId;

    public AuditCriteria() {
    }

    public AuditCriteria(AuditCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.executedAt = other.executedAt == null ? null : other.executedAt.copy();
        this.processId = other.processId == null ? null : other.processId.copy();
        this.processName = other.processName == null ? null : other.processName.copy();
        this.processCategoryId = other.processCategoryId == null ? null : other.processCategoryId.copy();
        this.processCategoryName = other.processCategoryName == null ? null : other.processCategoryName.copy();
        this.riskLevel = other.riskLevel == null ? null : other.riskLevel.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.riskId = other.riskId == null ? null : other.riskId.copy();
        this.riskName = other.riskName == null ? null : other.riskName.copy();
        this.cycleId = other.cycleId == null ? null : other.cycleId.copy();
    }

    @Override
    public AuditCriteria copy() {
        return new AuditCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public InstantFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getEndDate() {
        return endDate;
    }

    public void setEndDate(InstantFilter endDate) {
        this.endDate = endDate;
    }

    public InstantFilter getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(InstantFilter executedAt) {
        this.executedAt = executedAt;
    }

    public LongFilter getProcessId() {
        return processId;
    }

    public void setProcessId(LongFilter processId) {
        this.processId = processId;
    }

    public StringFilter getProcessName() {
        return processName;
    }

    public void setProcessName(StringFilter processName) {
        this.processName = processName;
    }

    public LongFilter getProcessCategoryId() {
        return processCategoryId;
    }

    public void setProcessCategoryId(LongFilter processCategoryId) {
        this.processCategoryId = processCategoryId;
    }

    public StringFilter getProcessCategoryName() {
        return processCategoryName;
    }

    public void setProcessCategoryName(StringFilter processCategoryName) {
        this.processCategoryName = processCategoryName;
    }

    public AuditRiskLevelFilter getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(AuditRiskLevelFilter riskLevel) {
        this.riskLevel = riskLevel;
    }

    public AuditTypeFilter getType() {
        return type;
    }

    public void setType(AuditTypeFilter type) {
        this.type = type;
    }

    public AuditStatusFilter getStatus() {
        return status;
    }

    public void setStatus(AuditStatusFilter status) {
        this.status = status;
    }

    public LongFilter getRiskId() {
        return riskId;
    }

    public void setRiskId(LongFilter riskId) {
        this.riskId = riskId;
    }

    public StringFilter getRiskName() {
        return riskName;
    }

    public void setRiskName(StringFilter riskName) {
        this.riskName = riskName;
    }

    public LongFilter getCycleId() {
        return cycleId;
    }

    public void setCycleId(LongFilter cycleId) {
        this.cycleId = cycleId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AuditCriteria that = (AuditCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(executedAt, that.executedAt) &&
            Objects.equals(processId, that.processId) &&
            Objects.equals(processName, that.processName) &&
            Objects.equals(processCategoryId, that.processCategoryId) &&
            Objects.equals(processCategoryName, that.processCategoryName) &&
            Objects.equals(riskLevel, that.riskLevel) &&
            Objects.equals(type, that.type) &&
            Objects.equals(status, that.status) &&
            Objects.equals(riskId, that.riskId) &&
            Objects.equals(riskName, that.riskName) &&
            Objects.equals(cycleId, that.cycleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        startDate,
        endDate,
        executedAt,
        processId,
        processName,
        processCategoryId,
        processCategoryName,
        riskLevel,
        type,
        status,
        riskId,
        riskName,
        cycleId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (endDate != null ? "endDate=" + endDate + ", " : "") +
                (executedAt != null ? "executedAt=" + executedAt + ", " : "") +
                (processId != null ? "processId=" + processId + ", " : "") +
                (processName != null ? "processName=" + processName + ", " : "") +
                (processCategoryId != null ? "processCategoryId=" + processCategoryId + ", " : "") +
                (processCategoryName != null ? "processCategoryName=" + processCategoryName + ", " : "") +
                (riskLevel != null ? "riskLevel=" + riskLevel + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (riskId != null ? "riskId=" + riskId + ", " : "") +
                (riskName != null ? "riskName=" + riskName + ", " : "") +
                (cycleId != null ? "cycleId=" + cycleId + ", " : "") +
            "}";
    }

}
