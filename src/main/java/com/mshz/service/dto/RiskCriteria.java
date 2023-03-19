package com.mshz.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mshz.domain.Risk} entity. This class is used
 * in {@link com.mshz.web.rest.RiskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /risks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RiskCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter label;

    private FloatFilter probability;

    private FloatFilter gravity;

    private StringFilter cause;

    private LongFilter controlsId;

    private LongFilter typeId;

    public RiskCriteria() {
    }

    public RiskCriteria(RiskCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.label = other.label == null ? null : other.label.copy();
        this.probability = other.probability == null ? null : other.probability.copy();
        this.gravity = other.gravity == null ? null : other.gravity.copy();
        this.cause = other.cause == null ? null : other.cause.copy();
        this.controlsId = other.controlsId == null ? null : other.controlsId.copy();
        this.typeId = other.typeId == null ? null : other.typeId.copy();
    }

    @Override
    public RiskCriteria copy() {
        return new RiskCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLabel() {
        return label;
    }

    public void setLabel(StringFilter label) {
        this.label = label;
    }

    public FloatFilter getProbability() {
        return probability;
    }

    public void setProbability(FloatFilter probability) {
        this.probability = probability;
    }

    public FloatFilter getGravity() {
        return gravity;
    }

    public void setGravity(FloatFilter gravity) {
        this.gravity = gravity;
    }

    public StringFilter getCause() {
        return cause;
    }

    public void setCause(StringFilter cause) {
        this.cause = cause;
    }

    public LongFilter getControlsId() {
        return controlsId;
    }

    public void setControlsId(LongFilter controlsId) {
        this.controlsId = controlsId;
    }

    public LongFilter getTypeId() {
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RiskCriteria that = (RiskCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(label, that.label) &&
            Objects.equals(probability, that.probability) &&
            Objects.equals(gravity, that.gravity) &&
            Objects.equals(cause, that.cause) &&
            Objects.equals(controlsId, that.controlsId) &&
            Objects.equals(typeId, that.typeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        label,
        probability,
        gravity,
        cause,
        controlsId,
        typeId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RiskCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (label != null ? "label=" + label + ", " : "") +
                (probability != null ? "probability=" + probability + ", " : "") +
                (gravity != null ? "gravity=" + gravity + ", " : "") +
                (cause != null ? "cause=" + cause + ", " : "") +
                (controlsId != null ? "controlsId=" + controlsId + ", " : "") +
                (typeId != null ? "typeId=" + typeId + ", " : "") +
            "}";
    }

}
