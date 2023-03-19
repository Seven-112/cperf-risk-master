package com.mshz.web.rest;

import com.mshz.domain.AuditRecommendationFile;
import com.mshz.service.AuditRecommendationFileService;
import com.mshz.web.rest.errors.BadRequestAlertException;
import com.mshz.service.dto.AuditRecommendationFileCriteria;
import com.mshz.service.AuditRecommendationFileQueryService;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mshz.domain.AuditRecommendationFile}.
 */
@RestController
@RequestMapping("/api")
public class AuditRecommendationFileResource {

    private final Logger log = LoggerFactory.getLogger(AuditRecommendationFileResource.class);

    private static final String ENTITY_NAME = "microrisqueAuditRecommendationFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuditRecommendationFileService auditRecommendationFileService;

    private final AuditRecommendationFileQueryService auditRecommendationFileQueryService;

    public AuditRecommendationFileResource(AuditRecommendationFileService auditRecommendationFileService, AuditRecommendationFileQueryService auditRecommendationFileQueryService) {
        this.auditRecommendationFileService = auditRecommendationFileService;
        this.auditRecommendationFileQueryService = auditRecommendationFileQueryService;
    }

    /**
     * {@code POST  /audit-recommendation-files} : Create a new auditRecommendationFile.
     *
     * @param auditRecommendationFile the auditRecommendationFile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new auditRecommendationFile, or with status {@code 400 (Bad Request)} if the auditRecommendationFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/audit-recommendation-files")
    public ResponseEntity<AuditRecommendationFile> createAuditRecommendationFile(@RequestBody AuditRecommendationFile auditRecommendationFile) throws URISyntaxException {
        log.debug("REST request to save AuditRecommendationFile : {}", auditRecommendationFile);
        if (auditRecommendationFile.getId() != null) {
            throw new BadRequestAlertException("A new auditRecommendationFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuditRecommendationFile result = auditRecommendationFileService.save(auditRecommendationFile);
        return ResponseEntity.created(new URI("/api/audit-recommendation-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /audit-recommendation-files} : Updates an existing auditRecommendationFile.
     *
     * @param auditRecommendationFile the auditRecommendationFile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auditRecommendationFile,
     * or with status {@code 400 (Bad Request)} if the auditRecommendationFile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the auditRecommendationFile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/audit-recommendation-files")
    public ResponseEntity<AuditRecommendationFile> updateAuditRecommendationFile(@RequestBody AuditRecommendationFile auditRecommendationFile) throws URISyntaxException {
        log.debug("REST request to update AuditRecommendationFile : {}", auditRecommendationFile);
        if (auditRecommendationFile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AuditRecommendationFile result = auditRecommendationFileService.save(auditRecommendationFile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, auditRecommendationFile.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /audit-recommendation-files} : get all the auditRecommendationFiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of auditRecommendationFiles in body.
     */
    @GetMapping("/audit-recommendation-files")
    public ResponseEntity<List<AuditRecommendationFile>> getAllAuditRecommendationFiles(AuditRecommendationFileCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AuditRecommendationFiles by criteria: {}", criteria);
        Page<AuditRecommendationFile> page = auditRecommendationFileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /audit-recommendation-files/count} : count all the auditRecommendationFiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/audit-recommendation-files/count")
    public ResponseEntity<Long> countAuditRecommendationFiles(AuditRecommendationFileCriteria criteria) {
        log.debug("REST request to count AuditRecommendationFiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(auditRecommendationFileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /audit-recommendation-files/:id} : get the "id" auditRecommendationFile.
     *
     * @param id the id of the auditRecommendationFile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the auditRecommendationFile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/audit-recommendation-files/{id}")
    public ResponseEntity<AuditRecommendationFile> getAuditRecommendationFile(@PathVariable Long id) {
        log.debug("REST request to get AuditRecommendationFile : {}", id);
        Optional<AuditRecommendationFile> auditRecommendationFile = auditRecommendationFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(auditRecommendationFile);
    }

    /**
     * {@code DELETE  /audit-recommendation-files/:id} : delete the "id" auditRecommendationFile.
     *
     * @param id the id of the auditRecommendationFile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/audit-recommendation-files/{id}")
    public ResponseEntity<Void> deleteAuditRecommendationFile(@PathVariable Long id) {
        log.debug("REST request to delete AuditRecommendationFile : {}", id);
        auditRecommendationFileService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
