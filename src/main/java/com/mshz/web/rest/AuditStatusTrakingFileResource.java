package com.mshz.web.rest;

import com.mshz.domain.AuditStatusTrakingFile;
import com.mshz.service.AuditStatusTrakingFileService;
import com.mshz.web.rest.errors.BadRequestAlertException;
import com.mshz.service.dto.AuditStatusTrakingFileCriteria;
import com.mshz.service.AuditStatusTrakingFileQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mshz.domain.AuditStatusTrakingFile}.
 */
@RestController
@RequestMapping("/api")
public class AuditStatusTrakingFileResource {

    private final Logger log = LoggerFactory.getLogger(AuditStatusTrakingFileResource.class);

    private static final String ENTITY_NAME = "microrisqueAuditStatusTrakingFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuditStatusTrakingFileService auditStatusTrakingFileService;

    private final AuditStatusTrakingFileQueryService auditStatusTrakingFileQueryService;

    public AuditStatusTrakingFileResource(AuditStatusTrakingFileService auditStatusTrakingFileService, AuditStatusTrakingFileQueryService auditStatusTrakingFileQueryService) {
        this.auditStatusTrakingFileService = auditStatusTrakingFileService;
        this.auditStatusTrakingFileQueryService = auditStatusTrakingFileQueryService;
    }

    /**
     * {@code POST  /audit-status-traking-files} : Create a new auditStatusTrakingFile.
     *
     * @param auditStatusTrakingFile the auditStatusTrakingFile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new auditStatusTrakingFile, or with status {@code 400 (Bad Request)} if the auditStatusTrakingFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/audit-status-traking-files")
    public ResponseEntity<AuditStatusTrakingFile> createAuditStatusTrakingFile(@Valid @RequestBody AuditStatusTrakingFile auditStatusTrakingFile) throws URISyntaxException {
        log.debug("REST request to save AuditStatusTrakingFile : {}", auditStatusTrakingFile);
        if (auditStatusTrakingFile.getId() != null) {
            throw new BadRequestAlertException("A new auditStatusTrakingFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuditStatusTrakingFile result = auditStatusTrakingFileService.save(auditStatusTrakingFile);
        return ResponseEntity.created(new URI("/api/audit-status-traking-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /audit-status-traking-files} : Updates an existing auditStatusTrakingFile.
     *
     * @param auditStatusTrakingFile the auditStatusTrakingFile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auditStatusTrakingFile,
     * or with status {@code 400 (Bad Request)} if the auditStatusTrakingFile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the auditStatusTrakingFile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/audit-status-traking-files")
    public ResponseEntity<AuditStatusTrakingFile> updateAuditStatusTrakingFile(@Valid @RequestBody AuditStatusTrakingFile auditStatusTrakingFile) throws URISyntaxException {
        log.debug("REST request to update AuditStatusTrakingFile : {}", auditStatusTrakingFile);
        if (auditStatusTrakingFile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AuditStatusTrakingFile result = auditStatusTrakingFileService.save(auditStatusTrakingFile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, auditStatusTrakingFile.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /audit-status-traking-files} : get all the auditStatusTrakingFiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of auditStatusTrakingFiles in body.
     */
    @GetMapping("/audit-status-traking-files")
    public ResponseEntity<List<AuditStatusTrakingFile>> getAllAuditStatusTrakingFiles(AuditStatusTrakingFileCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AuditStatusTrakingFiles by criteria: {}", criteria);
        Page<AuditStatusTrakingFile> page = auditStatusTrakingFileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /audit-status-traking-files/count} : count all the auditStatusTrakingFiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/audit-status-traking-files/count")
    public ResponseEntity<Long> countAuditStatusTrakingFiles(AuditStatusTrakingFileCriteria criteria) {
        log.debug("REST request to count AuditStatusTrakingFiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(auditStatusTrakingFileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /audit-status-traking-files/:id} : get the "id" auditStatusTrakingFile.
     *
     * @param id the id of the auditStatusTrakingFile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the auditStatusTrakingFile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/audit-status-traking-files/{id}")
    public ResponseEntity<AuditStatusTrakingFile> getAuditStatusTrakingFile(@PathVariable Long id) {
        log.debug("REST request to get AuditStatusTrakingFile : {}", id);
        Optional<AuditStatusTrakingFile> auditStatusTrakingFile = auditStatusTrakingFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(auditStatusTrakingFile);
    }

    /**
     * {@code DELETE  /audit-status-traking-files/:id} : delete the "id" auditStatusTrakingFile.
     *
     * @param id the id of the auditStatusTrakingFile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/audit-status-traking-files/{id}")
    public ResponseEntity<Void> deleteAuditStatusTrakingFile(@PathVariable Long id) {
        log.debug("REST request to delete AuditStatusTrakingFile : {}", id);
        auditStatusTrakingFileService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
