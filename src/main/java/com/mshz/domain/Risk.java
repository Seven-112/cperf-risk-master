package com.mshz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Risk.
 */
@Entity
@Table(name = "risk")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Risk implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "probability")
    private Float probability;

    @Column(name = "gravity")
    private Float gravity;

    @Column(name = "cause")
    private String cause;

    @OneToMany(mappedBy = "risk")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Control> controls = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "risks", allowSetters = true)
    private RiskType type;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public Risk label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Float getProbability() {
        return probability;
    }

    public Risk probability(Float probability) {
        this.probability = probability;
        return this;
    }

    public void setProbability(Float probability) {
        this.probability = probability;
    }

    public Float getGravity() {
        return gravity;
    }

    public Risk gravity(Float gravity) {
        this.gravity = gravity;
        return this;
    }

    public void setGravity(Float gravity) {
        this.gravity = gravity;
    }

    public String getCause() {
        return cause;
    }

    public Risk cause(String cause) {
        this.cause = cause;
        return this;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Set<Control> getControls() {
        return controls;
    }

    public Risk controls(Set<Control> controls) {
        this.controls = controls;
        return this;
    }

    public Risk addControls(Control control) {
        this.controls.add(control);
        control.setRisk(this);
        return this;
    }

    public Risk removeControls(Control control) {
        this.controls.remove(control);
        control.setRisk(null);
        return this;
    }

    public void setControls(Set<Control> controls) {
        this.controls = controls;
    }

    public RiskType getType() {
        return type;
    }

    public Risk type(RiskType riskType) {
        this.type = riskType;
        return this;
    }

    public void setType(RiskType riskType) {
        this.type = riskType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Risk)) {
            return false;
        }
        return id != null && id.equals(((Risk) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Risk{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", probability=" + getProbability() +
            ", gravity=" + getGravity() +
            ", cause='" + getCause() + "'" +
            "}";
    }
}
