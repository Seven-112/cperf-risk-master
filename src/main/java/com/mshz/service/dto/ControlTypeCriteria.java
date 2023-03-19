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
 * Criteria class for the {@link com.mshz.domain.ControlType} entity. This class is used
 * in {@link com.mshz.web.rest.ControlTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /control-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ControlTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter type;

    public ControlTypeCriteria() {
    }

    public ControlTypeCriteria(ControlTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
    }

    @Override
    public ControlTypeCriteria copy() {
        return new ControlTypeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ControlTypeCriteria that = (ControlTypeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        type
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ControlTypeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
            "}";
    }

}
