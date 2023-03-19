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
 * Criteria class for the {@link com.mshz.domain.AuditStatusTrakingFile} entity. This class is used
 * in {@link com.mshz.web.rest.AuditStatusTrakingFileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /audit-status-traking-files?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AuditStatusTrakingFileCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter trackId;

    private LongFilter fileId;

    private StringFilter fileName;

    public AuditStatusTrakingFileCriteria() {
    }

    public AuditStatusTrakingFileCriteria(AuditStatusTrakingFileCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.trackId = other.trackId == null ? null : other.trackId.copy();
        this.fileId = other.fileId == null ? null : other.fileId.copy();
        this.fileName = other.fileName == null ? null : other.fileName.copy();
    }

    @Override
    public AuditStatusTrakingFileCriteria copy() {
        return new AuditStatusTrakingFileCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getTrackId() {
        return trackId;
    }

    public void setTrackId(LongFilter trackId) {
        this.trackId = trackId;
    }

    public LongFilter getFileId() {
        return fileId;
    }

    public void setFileId(LongFilter fileId) {
        this.fileId = fileId;
    }

    public StringFilter getFileName() {
        return fileName;
    }

    public void setFileName(StringFilter fileName) {
        this.fileName = fileName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AuditStatusTrakingFileCriteria that = (AuditStatusTrakingFileCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(trackId, that.trackId) &&
            Objects.equals(fileId, that.fileId) &&
            Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        trackId,
        fileId,
        fileName
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditStatusTrakingFileCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (trackId != null ? "trackId=" + trackId + ", " : "") +
                (fileId != null ? "fileId=" + fileId + ", " : "") +
                (fileName != null ? "fileName=" + fileName + ", " : "") +
            "}";
    }

}
