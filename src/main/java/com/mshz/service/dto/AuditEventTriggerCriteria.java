package com.mshz.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.mshz.domain.enumeration.AuditEventRecurrence;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.mshz.domain.AuditEventTrigger} entity. This class is used
 * in {@link com.mshz.web.rest.AuditEventTriggerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /audit-event-triggers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AuditEventTriggerCriteria implements Serializable, Criteria {
    /**
     * Class for filtering AuditEventRecurrence
     */
    public static class AuditEventRecurrenceFilter extends Filter<AuditEventRecurrence> {

        public AuditEventRecurrenceFilter() {
        }

        public AuditEventRecurrenceFilter(AuditEventRecurrenceFilter filter) {
            super(filter);
        }

        @Override
        public AuditEventRecurrenceFilter copy() {
            return new AuditEventRecurrenceFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter editorId;

    private InstantFilter createdAt;

    private StringFilter name;

    private AuditEventRecurrenceFilter recurrence;

    private BooleanFilter disabled;

    private StringFilter editorName;

    private InstantFilter firstStartedAt;

    private InstantFilter nextStartAt;

    private IntegerFilter startCount;

    private LongFilter auditId;

    public AuditEventTriggerCriteria() {
    }

    public AuditEventTriggerCriteria(AuditEventTriggerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.editorId = other.editorId == null ? null : other.editorId.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.recurrence = other.recurrence == null ? null : other.recurrence.copy();
        this.disabled = other.disabled == null ? null : other.disabled.copy();
        this.editorName = other.editorName == null ? null : other.editorName.copy();
        this.firstStartedAt = other.firstStartedAt == null ? null : other.firstStartedAt.copy();
        this.nextStartAt = other.nextStartAt == null ? null : other.nextStartAt.copy();
        this.startCount = other.startCount == null ? null : other.startCount.copy();
        this.auditId = other.auditId == null ? null : other.auditId.copy();
    }

    @Override
    public AuditEventTriggerCriteria copy() {
        return new AuditEventTriggerCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getEditorId() {
        return editorId;
    }

    public void setEditorId(LongFilter editorId) {
        this.editorId = editorId;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public AuditEventRecurrenceFilter getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(AuditEventRecurrenceFilter recurrence) {
        this.recurrence = recurrence;
    }

    public BooleanFilter getDisabled() {
        return disabled;
    }

    public void setDisabled(BooleanFilter disabled) {
        this.disabled = disabled;
    }

    public StringFilter getEditorName() {
        return editorName;
    }

    public void setEditorName(StringFilter editorName) {
        this.editorName = editorName;
    }

    public InstantFilter getFirstStartedAt() {
        return firstStartedAt;
    }

    public void setFirstStartedAt(InstantFilter firstStartedAt) {
        this.firstStartedAt = firstStartedAt;
    }

    public InstantFilter getNextStartAt() {
        return nextStartAt;
    }

    public void setNextStartAt(InstantFilter nextStartAt) {
        this.nextStartAt = nextStartAt;
    }

    public IntegerFilter getStartCount() {
        return startCount;
    }

    public void setStartCount(IntegerFilter startCount) {
        this.startCount = startCount;
    }

    public LongFilter getAuditId() {
        return auditId;
    }

    public void setAuditId(LongFilter auditId) {
        this.auditId = auditId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AuditEventTriggerCriteria that = (AuditEventTriggerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(editorId, that.editorId) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(name, that.name) &&
            Objects.equals(recurrence, that.recurrence) &&
            Objects.equals(disabled, that.disabled) &&
            Objects.equals(editorName, that.editorName) &&
            Objects.equals(firstStartedAt, that.firstStartedAt) &&
            Objects.equals(nextStartAt, that.nextStartAt) &&
            Objects.equals(startCount, that.startCount) &&
            Objects.equals(auditId, that.auditId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        editorId,
        createdAt,
        name,
        recurrence,
        disabled,
        editorName,
        firstStartedAt,
        nextStartAt,
        startCount,
        auditId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditEventTriggerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (editorId != null ? "editorId=" + editorId + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (recurrence != null ? "recurrence=" + recurrence + ", " : "") +
                (disabled != null ? "disabled=" + disabled + ", " : "") +
                (editorName != null ? "editorName=" + editorName + ", " : "") +
                (firstStartedAt != null ? "firstStartedAt=" + firstStartedAt + ", " : "") +
                (nextStartAt != null ? "nextStartAt=" + nextStartAt + ", " : "") +
                (startCount != null ? "startCount=" + startCount + ", " : "") +
                (auditId != null ? "auditId=" + auditId + ", " : "") +
            "}";
    }

}
