package com.mshz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import com.mshz.domain.enumeration.AuditEventRecurrence;

/**
 * A AuditEventTrigger.
 */
@Entity
@Table(name = "audit_event_trigger")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AuditEventTrigger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "editor_id", nullable = false)
    private Long editorId;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence")
    private AuditEventRecurrence recurrence;

    @Column(name = "disabled")
    private Boolean disabled;

    @Column(name = "editor_name")
    private String editorName;

    @Column(name = "first_started_at")
    private Instant firstStartedAt;

    @Column(name = "next_start_at")
    private Instant nextStartAt;

    @Column(name = "start_count")
    private Integer startCount;

    @ManyToOne
    @JsonIgnoreProperties(value = "auditEventTriggers", allowSetters = true)
    private Audit audit;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEditorId() {
        return editorId;
    }

    public AuditEventTrigger editorId(Long editorId) {
        this.editorId = editorId;
        return this;
    }

    public void setEditorId(Long editorId) {
        this.editorId = editorId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public AuditEventTrigger createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public AuditEventTrigger name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AuditEventRecurrence getRecurrence() {
        return recurrence;
    }

    public AuditEventTrigger recurrence(AuditEventRecurrence recurrence) {
        this.recurrence = recurrence;
        return this;
    }

    public void setRecurrence(AuditEventRecurrence recurrence) {
        this.recurrence = recurrence;
    }

    public Boolean isDisabled() {
        return disabled;
    }

    public AuditEventTrigger disabled(Boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getEditorName() {
        return editorName;
    }

    public AuditEventTrigger editorName(String editorName) {
        this.editorName = editorName;
        return this;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    public Instant getFirstStartedAt() {
        return firstStartedAt;
    }

    public AuditEventTrigger firstStartedAt(Instant firstStartedAt) {
        this.firstStartedAt = firstStartedAt;
        return this;
    }

    public void setFirstStartedAt(Instant firstStartedAt) {
        this.firstStartedAt = firstStartedAt;
    }

    public Instant getNextStartAt() {
        return nextStartAt;
    }

    public AuditEventTrigger nextStartAt(Instant nextStartAt) {
        this.nextStartAt = nextStartAt;
        return this;
    }

    public void setNextStartAt(Instant nextStartAt) {
        this.nextStartAt = nextStartAt;
    }

    public Integer getStartCount() {
        return startCount;
    }

    public AuditEventTrigger startCount(Integer startCount) {
        this.startCount = startCount;
        return this;
    }

    public void setStartCount(Integer startCount) {
        this.startCount = startCount;
    }

    public Audit getAudit() {
        return audit;
    }

    public AuditEventTrigger audit(Audit audit) {
        this.audit = audit;
        return this;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuditEventTrigger)) {
            return false;
        }
        return id != null && id.equals(((AuditEventTrigger) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditEventTrigger{" +
            "id=" + getId() +
            ", editorId=" + getEditorId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", name='" + getName() + "'" +
            ", recurrence='" + getRecurrence() + "'" +
            ", disabled='" + isDisabled() + "'" +
            ", editorName='" + getEditorName() + "'" +
            ", firstStartedAt='" + getFirstStartedAt() + "'" +
            ", nextStartAt='" + getNextStartAt() + "'" +
            ", startCount=" + getStartCount() +
            "}";
    }
}
