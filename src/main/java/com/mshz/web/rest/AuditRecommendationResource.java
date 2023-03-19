package com.mshz.web.rest;

import com.mshz.domain.AuditRecommendation;
import com.mshz.domain.enumeration.AuditStatus;
import com.mshz.domain.enumeration.AuditUserRole;
import com.mshz.service.AuditRecommendationService;
import com.mshz.web.rest.errors.BadRequestAlertException;
import com.mshz.service.dto.AuditRecommendationCriteria;
import com.mshz.service.AuditRecommendationQueryService;

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
 * REST controller for managing {@link com.mshz.domain.AuditRecommendation}.
 */
@RestController
@RequestMapping("/api")
public class AuditRecommendationResource {

    private final Logger log = LoggerFactory.getLogger(AuditRecommendationResource.class);

    private static final String ENTITY_NAME = "microrisqueAuditRecommendation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuditRecommendationService auditRecommendationService;

    private final AuditRecommendationQueryService auditRecommendationQueryService;

    public AuditRecommendationResource(AuditRecommendationService auditRecommendationService, AuditRecommendationQueryService auditRecommendationQueryService) {
        this.auditRecommendationService = auditRecommendationService;
        this.auditRecommendationQueryService = auditRecommendationQueryService;
    }

    /**
     * {@code POST  /audit-recommendations} : Create a new auditRecommendation.
     *
     * @param auditRecommendation the auditRecommendation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new auditRecommendation, or with status {@code 400 (Bad Request)} if the auditRecommendation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/audit-recommendations")
    public ResponseEntity<AuditRecommendation> createAuditRecommendation(@RequestBody AuditRecommendation auditRecommendation) throws URISyntaxException {
        log.debug("REST request to save AuditRecommendation : {}", auditRecommendation);
        if (auditRecommendation.getId() != null) {
            throw new BadRequestAlertException("A new auditRecommendation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuditRecommendation result = auditRecommendationService.save(auditRecommendation);
        return ResponseEntity.created(new URI("/api/audit-recommendations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /audit-recommendations} : Updates an existing auditRecommendation.
     *
     * @param auditRecommendation the auditRecommendation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auditRecommendation,
     * or with status {@code 400 (Bad Request)} if the auditRecommendation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the auditRecommendation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/audit-recommendations")
    public ResponseEntity<AuditRecommendation> updateAuditRecommendation(@RequestBody AuditRecommendation auditRecommendation) throws URISyntaxException {
        log.debug("REST request to update AuditRecommendation : {}", auditRecommendation);
        if (auditRecommendation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AuditRecommendation result = auditRecommendationService.save(auditRecommendation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, auditRecommendation.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /audit-recommendations} : get all the auditRecommendations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of auditRecommendations in body.
     */
    @GetMapping("/audit-recommendations")
    public ResponseEntity<List<AuditRecommendation>> getAllAuditRecommendations(AuditRecommendationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AuditRecommendations by criteria: {}", criteria);
        Page<AuditRecommendation> page = auditRecommendationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        List<AuditRecommendation> sorted = auditRecommendationService.sortRecoms(page.getContent());
        return ResponseEntity.ok().headers(headers).body(sorted);
    }

    /**
     * {@code GET  /audit-recommendations/count} : count all the auditRecommendations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/audit-recommendations/count")
    public ResponseEntity<Long> countAuditRecommendations(AuditRecommendationCriteria criteria) {
        log.debug("REST request to count AuditRecommendations by criteria: {}", criteria);
        return ResponseEntity.ok().body(auditRecommendationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /audit-recommendations/:id} : get the "id" auditRecommendation.
     *
     * @param id the id of the auditRecommendation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the auditRecommendation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/audit-recommendations/{id}")
    public ResponseEntity<AuditRecommendation> getAuditRecommendation(@PathVariable Long id) {
        log.debug("REST request to get AuditRecommendation : {}", id);
        Optional<AuditRecommendation> auditRecommendation = auditRecommendationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(auditRecommendation);
    }

    /**
     * {@code DELETE  /audit-recommendations/:id} : delete the "id" auditRecommendation.
     *
     * @param id the id of the auditRecommendation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/audit-recommendations/{id}")
    public ResponseEntity<Void> deleteAuditRecommendation(@PathVariable Long id) {
        log.debug("REST request to delete AuditRecommendation : {}", id);
        auditRecommendationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("audit-recommendations/findByUserIdAndRoleAndStatus")
    ResponseEntity<List<AuditRecommendation>> findByUserIdAndRoleAndStatus(Long userId, AuditUserRole role, AuditStatus status, Pageable pageable){
        Page<AuditRecommendation> page = auditRecommendationService.findByUserIdAndRoleAndStatus(userId, role, status, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("audit-recommendations/findByUserIdAndStatus")
    ResponseEntity<List<AuditRecommendation>> findByUserIdAndStatus(Long userId, AuditStatus status, Pageable pageable){
        Page<AuditRecommendation> page = auditRecommendationService.findByUserIdAndStatus(userId, status, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("audit-recommendations/findByUserId")
    ResponseEntity<List<AuditRecommendation>> findByUserId(Long userId, Pageable pageable){
        Page<AuditRecommendation> page = auditRecommendationService.findByUserId(userId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
