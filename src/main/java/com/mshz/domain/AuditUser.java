package com.mshz.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

import com.mshz.domain.enumeration.AuditUserRole;

/**
 * A AuditUser.
 */
@Entity
@Table(name = "audit_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AuditUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "audit_id")
    private Long auditId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_full_name")
    private String userFullName;

    @Column(name = "user_email")
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private AuditUserRole role;

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

    public AuditUser auditId(Long auditId) {
        this.auditId = auditId;
        return this;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public Long getUserId() {
        return userId;
    }

    public AuditUser userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public AuditUser userFullName(String userFullName) {
        this.userFullName = userFullName;
        return this;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public AuditUser userEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public AuditUserRole getRole() {
        return role;
    }

    public AuditUser role(AuditUserRole role) {
        this.role = role;
        return this;
    }

    public void setRole(AuditUserRole role) {
        this.role = role;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuditUser)) {
            return false;
        }
        return id != null && id.equals(((AuditUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditUser{" +
            "id=" + getId() +
            ", auditId=" + getAuditId() +
            ", userId=" + getUserId() +
            ", userFullName='" + getUserFullName() + "'" +
            ", userEmail='" + getUserEmail() + "'" +
            ", role='" + getRole() + "'" +
            "}";
    }
}
