package com.mshz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Control.
 */
@Entity
@Table(name = "control")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Control implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "validation_required")
    private Boolean validationRequired;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "controls", allowSetters = true)
    private ControlType type;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "controls", allowSetters = true)
    private ControlMaturity maturity;

    @ManyToOne
    @JsonIgnoreProperties(value = "controls", allowSetters = true)
    private Risk risk;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Control description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isValidationRequired() {
        return validationRequired;
    }

    public Control validationRequired(Boolean validationRequired) {
        this.validationRequired = validationRequired;
        return this;
    }

    public void setValidationRequired(Boolean validationRequired) {
        this.validationRequired = validationRequired;
    }

    public ControlType getType() {
        return type;
    }

    public Control type(ControlType controlType) {
        this.type = controlType;
        return this;
    }

    public void setType(ControlType controlType) {
        this.type = controlType;
    }

    public ControlMaturity getMaturity() {
        return maturity;
    }

    public Control maturity(ControlMaturity controlMaturity) {
        this.maturity = controlMaturity;
        return this;
    }

    public void setMaturity(ControlMaturity controlMaturity) {
        this.maturity = controlMaturity;
    }

    public Risk getRisk() {
        return risk;
    }

    public Control risk(Risk risk) {
        this.risk = risk;
        return this;
    }

    public void setRisk(Risk risk) {
        this.risk = risk;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Control)) {
            return false;
        }
        return id != null && id.equals(((Control) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Control{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", validationRequired='" + isValidationRequired() + "'" +
            "}";
    }
}
