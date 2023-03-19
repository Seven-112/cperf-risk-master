package com.mshz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

import com.mshz.domain.enumeration.AuditRiskLevel;

import com.mshz.domain.enumeration.AuditType;

import com.mshz.domain.enumeration.AuditStatus;

/**
 * A Audit.
 */
@Entity
@Table(name = "audit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Audit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "executed_at")
    private Instant executedAt;

    @Column(name = "process_id")
    private Long processId;

    @Column(name = "process_name")
    private String processName;

    @Column(name = "process_category_id")
    private Long processCategoryId;

    @Column(name = "process_category_name")
    private String processCategoryName;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level")
    private AuditRiskLevel riskLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AuditType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AuditStatus status;

    @Column(name = "risk_id")
    private Long riskId;

    @Column(name = "risk_name")
    private String riskName;

    @ManyToOne
    @JsonIgnoreProperties(value = "audits", allowSetters = true)
    private AuditCycle cycle;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Audit title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Audit startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public Audit endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Instant getExecutedAt() {
        return executedAt;
    }

    public Audit executedAt(Instant executedAt) {
        this.executedAt = executedAt;
        return this;
    }

    public void setExecutedAt(Instant executedAt) {
        this.executedAt = executedAt;
    }

    public Long getProcessId() {
        return processId;
    }

    public Audit processId(Long processId) {
        this.processId = processId;
        return this;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getProcessName() {
        return processName;
    }

    public Audit processName(String processName) {
        this.processName = processName;
        return this;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Long getProcessCategoryId() {
        return processCategoryId;
    }

    public Audit processCategoryId(Long processCategoryId) {
        this.processCategoryId = processCategoryId;
        return this;
    }

    public void setProcessCategoryId(Long processCategoryId) {
        this.processCategoryId = processCategoryId;
    }

    public String getProcessCategoryName() {
        return processCategoryName;
    }

    public Audit processCategoryName(String processCategoryName) {
        this.processCategoryName = processCategoryName;
        return this;
    }

    public void setProcessCategoryName(String processCategoryName) {
        this.processCategoryName = processCategoryName;
    }

    public AuditRiskLevel getRiskLevel() {
        return riskLevel;
    }

    public Audit riskLevel(AuditRiskLevel riskLevel) {
        this.riskLevel = riskLevel;
        return this;
    }

    public void setRiskLevel(AuditRiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public AuditType getType() {
        return type;
    }

    public Audit type(AuditType type) {
        this.type = type;
        return this;
    }

    public void setType(AuditType type) {
        this.type = type;
    }

    public AuditStatus getStatus() {
        return status;
    }

    public Audit status(AuditStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(AuditStatus status) {
        this.status = status;
    }

    public Long getRiskId() {
        return riskId;
    }

    public Audit riskId(Long riskId) {
        this.riskId = riskId;
        return this;
    }

    public void setRiskId(Long riskId) {
        this.riskId = riskId;
    }

    public String getRiskName() {
        return riskName;
    }

    public Audit riskName(String riskName) {
        this.riskName = riskName;
        return this;
    }

    public void setRiskName(String riskName) {
        this.riskName = riskName;
    }

    public AuditCycle getCycle() {
        return cycle;
    }

    public Audit cycle(AuditCycle auditCycle) {
        this.cycle = auditCycle;
        return this;
    }

    public void setCycle(AuditCycle auditCycle) {
        this.cycle = auditCycle;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Audit)) {
            return false;
        }
        return id != null && id.equals(((Audit) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Audit{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", executedAt='" + getExecutedAt() + "'" +
            ", processId=" + getProcessId() +
            ", processName='" + getProcessName() + "'" +
            ", processCategoryId=" + getProcessCategoryId() +
            ", processCategoryName='" + getProcessCategoryName() + "'" +
            ", riskLevel='" + getRiskLevel() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            ", riskId=" + getRiskId() +
            ", riskName='" + getRiskName() + "'" +
            "}";
    }
}
