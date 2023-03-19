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
 * Criteria class for the {@link com.mshz.domain.Control} entity. This class is used
 * in {@link com.mshz.web.rest.ControlResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /controls?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ControlCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private BooleanFilter validationRequired;

    private LongFilter typeId;

    private LongFilter maturityId;

    private LongFilter riskId;

    public ControlCriteria() {
    }

    public ControlCriteria(ControlCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.validationRequired = other.validationRequired == null ? null : other.validationRequired.copy();
        this.typeId = other.typeId == null ? null : other.typeId.copy();
        this.maturityId = other.maturityId == null ? null : other.maturityId.copy();
        this.riskId = other.riskId == null ? null : other.riskId.copy();
    }

    @Override
    public ControlCriteria copy() {
        return new ControlCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BooleanFilter getValidationRequired() {
        return validationRequired;
    }

    public void setValidationRequired(BooleanFilter validationRequired) {
        this.validationRequired = validationRequired;
    }

    public LongFilter getTypeId() {
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
    }

    public LongFilter getMaturityId() {
        return maturityId;
    }

    public void setMaturityId(LongFilter maturityId) {
        this.maturityId = maturityId;
    }

    public LongFilter getRiskId() {
        return riskId;
    }

    public void setRiskId(LongFilter riskId) {
        this.riskId = riskId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ControlCriteria that = (ControlCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(validationRequired, that.validationRequired) &&
            Objects.equals(typeId, that.typeId) &&
            Objects.equals(maturityId, that.maturityId) &&
            Objects.equals(riskId, that.riskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        description,
        validationRequired,
        typeId,
        maturityId,
        riskId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ControlCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (validationRequired != null ? "validationRequired=" + validationRequired + ", " : "") +
                (typeId != null ? "typeId=" + typeId + ", " : "") +
                (maturityId != null ? "maturityId=" + maturityId + ", " : "") +
                (riskId != null ? "riskId=" + riskId + ", " : "") +
            "}";
    }

}
