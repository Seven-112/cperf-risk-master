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
 * Criteria class for the {@link com.mshz.domain.ControlMaturity} entity. This class is used
 * in {@link com.mshz.web.rest.ControlMaturityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /control-maturities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ControlMaturityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter label;

    public ControlMaturityCriteria() {
    }

    public ControlMaturityCriteria(ControlMaturityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.label = other.label == null ? null : other.label.copy();
    }

    @Override
    public ControlMaturityCriteria copy() {
        return new ControlMaturityCriteria(this);
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ControlMaturityCriteria that = (ControlMaturityCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        label
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ControlMaturityCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (label != null ? "label=" + label + ", " : "") +
            "}";
    }

}
